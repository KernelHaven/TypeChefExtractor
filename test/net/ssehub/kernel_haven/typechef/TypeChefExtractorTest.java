package net.ssehub.kernel_haven.typechef;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.test_utils.TestConfiguration;
import net.ssehub.kernel_haven.typechef.wrapper.TypeChefSettings.ParseType;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.Util;
import net.ssehub.kernel_haven.util.Util.OSType;
import net.ssehub.kernel_haven.util.logic.Conjunction;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.Negation;
import net.ssehub.kernel_haven.util.logic.True;
import net.ssehub.kernel_haven.util.logic.Variable;

/**
 * Tests for the TypeChef extractor.
 * 
 * @author Adam
 */
public class TypeChefExtractorTest {
    
    private static final File RES_DIR = new File(AllTests.TESTDATA, "res");
    
    /**
     * Creates the resource directory for each test.
     */
    @Before
    public void createResDir() {
        assertThat(RES_DIR.exists(), is(false));
        assertThat(RES_DIR.getParentFile().isDirectory(), is(true));
        
        RES_DIR.mkdir();
        
        assertThat(RES_DIR.isDirectory(), is(true));
    }
    
    /**
     * Clears the resource directory after each test.
     * 
     * @throws IOException unwanted.
     */
    @After
    public void deleteResDir() throws IOException {
        Util.deleteFolder(RES_DIR);
        assertThat(RES_DIR.exists(), is(false));
    }

    /**
     * Tests whether a TypeChef instance running in a separate process returns correct results.
     * 
     */
    @Test
    public void testSeparateProcess() {
        SourceFile<SyntaxElement> result = parseFile(new File("test.c"), "src1", false, ParseType.FULL_AST);
        
        String actual = result.iterator().next().toString();
        assertAST(actual, "testdata/src1/ast.txt");
    }
    
    /**
     * Tests whether a TypeChef instance running in our process returns correct results.
     * This is not a production scenario, but gives a hint to the code coverage in the sub-process classes.
     * 
     */
    @Test
    public void testSameProcess() {
        SourceFile<SyntaxElement> result = parseFile(new File("test.c"), "src1", true, ParseType.FULL_AST);
        
        String actual = result.iterator().next().toString();
        assertAST(actual, "testdata/src1/ast.txt");
    }
    
    /**
     * Tests that the ASTs contain the expected presence conditions.
     */
    @Test
    @Ignore("Failure caused by a bug in TypeChef, see comment at the end of the test method for details")
    public void testPCsOfAST() {
        SourceFile<SyntaxElement> result = parseFile(new File("calc.c"), "varAST", false, ParseType.FULL_AST);
        
        // Collect all elements, whose presence condition is not TRUE
        SyntaxElement unit = result.iterator().next();
        List<SyntaxElement> varAstParts = new ArrayList<>();
        collectVariableElements(unit, varAstParts);
        
        // Allowed PCs
        Formula varAdd = new Variable("CONFIG_ADDITION");
        Formula varSub = new Variable("CONFIG_SUBTRACTION");
        Formula elseIfCondition = new Conjunction(new Negation(varAdd), varSub);
        Formula elseCondition = new Conjunction(new Negation(varAdd), new Negation(varSub));
        List<Formula> allowedPCs = new ArrayList<>();
        allowedPCs.add(varAdd);
        allowedPCs.add(elseIfCondition);
        /*
        /* printf depends on if-Block, TypeChef computes for this reason also a presence condition for
         * the not existing else block
         */
        allowedPCs.add(elseCondition);
        
        // Check for valid PCs
        assertConditions(unit, varAstParts, allowedPCs);
        
        /*
         * This fails due to an error in TypeChef.
         * 
         * TypeChef translates the C source file into an AST with the roughly the following form:
         * (Presence conditions in [], comments/explanations in ())
         * 
         * Function (calc(int, int))
         *     CompoundStatement (function body)
         *     
         *         (two declarations in #if defined(CONFIG_ADDITION))
         *         [CONFIG_ADDITION] Declaration (result)
         *             IntSpecifier (result is declared as an int)
         *         [CONFIG_ADDITION] Declaration (op)
         *             [CONFIG_ADDITION] CharSpecifier (op is declared as a char)
         *         
         *         [!CONFIG_ADDITION && !CONFIG_SUBTRACTION] ExprStatement (the call to printf; TypeChef adds this here
         *                                                                  so the type checker can later find, that the
         *                                                                  variables it uses are not defined for all
         *                                                                  cases)
         *         
         *         (two declarations in #elif defined(CONFIG_SUBTRACTION))
         *         [!CONFIG_ADDITION && CONFIG_SUBTRACTION] Declaration (result)
         *             [CONFIG_ADDITION] CharSpecifier (this is the error; this shouldn't be here)
         *             IntSpecifier (result is declared as an int)
         *         [!CONFIG_ADDITION && CONFIG_SUBTRACTION] Declaration (op)
         *             CharSpecifier (op is declared as a char)
         *             
         *         (the call to printf)
         *         [CONFIG_ADDITION || CONFIG_SUBTRACTION] ExprStatement
         * 
         * The error is in the declaration of "result" in the #elif block. It contains a CharSpecifier element
         * with the condition "CONFIG_ADDITION", which results in the overall PC of that element to be
         * "!CONFIG_ADDITION && CONFIG_SUBTRACTION && CONFIG_ADDITION". Note that this is exactly the same CharSpecifier
         * element as for the variable "op" in the #if block. Also note, that these are the only two specifiers that
         * have conditions; all other specifiers (IntSpecifiers for "result" variables, CharSpecifier for second "op"
         * variable) all have "true" as their immediate condition.
         * 
         * This superfluous CharSpecifier element should not be there; there seems to be a bug in TypeChef that copies
         * the CharSpecifier to the next declaration for some reason. Note that this superfluous element is not
         * accidentally added during _our_ translation; I verified that this comes directly from TypeChef.
         */
    }
    
    /**
     * Tests that the (unused) included header macros (includes at the beginning of a file) can optionally be
     * omitted during AST generation.
     * @see #testFullAst()
     */
    @Test
    public void testCAst() {
        File testFile = new File("main.c");
        SourceFile<SyntaxElement> result = parseFile(testFile, "includeHeader", false, ParseType.ONLY_C_AST);
        
        // Collect all elements, whose presence condition is not TRUE
        SyntaxElement unit = result.iterator().next();
        
        /*
         * Test file contains 1 header element (which should be skipped) and 2 C elements
         */
        int nElements = unit.getNestedElementCount();
        Assert.assertEquals(2, nElements);
        for (int i = 0; i < nElements; i++) {
            SyntaxElement astElement = unit.getNestedElement(i);
            Assert.assertEquals(testFile, astElement.getSourceFile());
        }
    }
    
    /**
     * Tests that the (unused) included header macros (includes at the beginning of a file) can optionally be
     * kept during AST generation.
     * @see #testCAst()
     */
    @Test
    public void testFullAst() {
        File testFile = new File("main.c");
        SourceFile<SyntaxElement> result = parseFile(testFile, "includeHeader", false, ParseType.FULL_AST);
        
        // Collect all elements, whose presence condition is not TRUE
        SyntaxElement unit = result.iterator().next();
        
        /*
         * Test file contains 1 header element (which should be skipped) and 2 C elements
         */
        int nElements = unit.getNestedElementCount();
        Assert.assertEquals(3, nElements);
        // First element is struct definition from header
        SyntaxElement headerElement = unit.getNestedElement(0);
        Assert.assertEquals(new File("logging.h"), headerElement.getSourceFile());
        for (int i = 1; i < nElements; i++) {
            SyntaxElement astElement = unit.getNestedElement(i);
            Assert.assertEquals(testFile, astElement.getSourceFile());
        }
    }

    /**
     * Checks that the specified AST elements contain only allowed presence conditions.
     * @param unit The complete AST, required for printing debug messages in case of a detected error.
     * @param varAstParts The AST elements to test (should be a subset of the <tt>unit</tt>).
     * @param allowedPCs The expected presence conditions.
     */
    private void assertConditions(SyntaxElement unit, List<SyntaxElement> varAstParts, List<Formula> allowedPCs) {
        for (SyntaxElement astElem : varAstParts) {
            Formula pc = astElem.getPresenceCondition();
            boolean contained = false;
            for (int i = 0; i < allowedPCs.size() && !contained; i++) {
                contained = allowedPCs.get(i).equals(pc);
            }
            if (!contained) {
                String elem = astElem.getType().toString();
                if (astElem.getNestedElementCount() > 0) {
                    elem += " " + astElem.getRelation(0);
                }
                elem += " in Line " + astElem.getLineStart();
                String msg = "AST element \"" + elem + "\" containes an invalid presence condtion: "
                    + pc + "\nComplete AST:\n" + unit;
                System.err.println(msg);
                Assert.fail(msg);
            }
        }
    }
    
    /**
     * Collect all (nested) AST elements, whose presence condition is not {@link True}.
     * @param astElement The parent element to start, will also look into all nested elements.
     * @param varAstParts An empty list, which will be filled as side-effect.
     */
    private void collectVariableElements(SyntaxElement astElement, List<SyntaxElement> varAstParts) {
        if (astElement.getPresenceCondition() != True.INSTANCE) {
            varAstParts.add(astElement);
        }
        for (int i = 0; i < astElement.getNestedElementCount(); i++) {
            collectVariableElements(astElement.getNestedElement(i), varAstParts);
        }
    }

    /**
     * Calls the {@link TypeChefExtractor} to parse a single file.
     * @param sourceFile The source file to parse, should be placed somewhere in testdata.
     * @param srcDir The source directory relative to testdata.
     * @param inSameVm <tt>true</tt> Calls {@link TypeChefExtractor} in the same Java VM <tt>false</tt> calls TypeChef
     *     in a separate process. Only one test case may set this to true.
     * @param parseType The type of result that should be produced.
     *     
     * @return The parsed file, won't be <tt>null</tt>.
     */
    private SourceFile<SyntaxElement> parseFile(File sourceFile, String srcDir, boolean inSameVm, ParseType parseType) {
        
        // Configure extractor
        Properties props = new Properties();
        props.setProperty("source_tree", "testdata/" + srcDir);
        props.setProperty("resource_dir", RES_DIR.getPath());
        props.setProperty("code.extractor.skip_default_include_dirs", "true");
        props.setProperty("code.extractor.parse_type", parseType.name());
        
        props.setProperty("code.extractor.process_ram", "200m");
        props.setProperty("code.extractor.ignore_other_models", "true");
        props.setProperty("code.extractor.debug.inherit_output", "false");
        props.setProperty("code.extractor.debug.call_in_same_vm", Boolean.toString(inSameVm));
        
        OSType os = Util.determineOS();
        if (os == OSType.WIN32 || os == OSType.WIN64) {
            props.setProperty("code.extractor.platform_header", "testdata/win/platform.h");
        }
        
        // Start extraction
        SourceFile<SyntaxElement> result = null;
        try {
            TestConfiguration testConfig = new TestConfiguration(props);
            
            TypeChefExtractor extractor = new TypeChefExtractor();
            extractor.init(testConfig);
            result = extractor.runOnFile(sourceFile);
        } catch (SetUpException setupExc) {
            Assert.fail("Configuration of TypeChefExtractor failed: " + setupExc.getMessage());
        } catch (ExtractorException extExc) {
            Logger.get().logException("Exception passed to unit test", extExc);
            Assert.fail("Extraction with TypeChefExtractor failed: " + extExc.getMessage());
        }
        
        // Check and return result
        Assert.assertNotNull(srcDir + "/" + sourceFile.getName() + " could not be parsed, result is null.", result);
        return result;
    }
    
    /**
     * Helper method that prints a hint to the differences in the two given strings to console.
     * 
     * @param actual The first string.
     * @param expected The second string.
     */
    private static void printDifference(String actual, String expected) {
        for (int i = 0; i < Math.min(actual.length(), expected.length()); i++) {
            if (actual.charAt(i) != expected.charAt(i)) {
                System.out.println("Actual:");
                System.out.println(actual.substring(Math.max(0, i - 50), Math.min(actual.length(), i + 50)));
                System.out.println("Expected:");
                System.out.println(expected.substring(Math.max(0, i - 50), Math.min(expected.length(), i + 50)));
                break;
            }
        }
    }
    
    /**
     * Compares (and asserts) the actual AST with an expectedAST.
     * @param actual The parsed AST to test.
     * @param expectedAstPath A relative location of an AST to load (relative to this project).
     * @throws AssertionError If the actual AST is not equal to the expected one.
     */
    private void assertAST(String actual, String expectedAstPath) throws AssertionError {
        actual.replace("\r", "");
        String expected = "";
        try {
            InputStream in = new FileInputStream(expectedAstPath);
            expected = Util.readStream(in);
            expected.replace("\r", "");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Can't load file with expected AST");
        }
        
        System.out.println(actual);
        try {
            Assert.assertEquals(expected, actual);
        } catch (AssertionError e) {
            printDifference(actual, expected);
            throw e;
        }
    }
}
