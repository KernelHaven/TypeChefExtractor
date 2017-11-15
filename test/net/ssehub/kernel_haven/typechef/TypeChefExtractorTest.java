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
import org.junit.BeforeClass;
import org.junit.Test;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.code_model.CodeElement;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.code_model.SyntaxElement;
import net.ssehub.kernel_haven.test_utils.TestConfiguration;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.Util;
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
     * Inits the logger.
     */
    @BeforeClass
    public static void initLogger() {
        Logger.init();
    }
    
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
        SourceFile result = parseFile(new File("test.c"), "src1", false);
        
        String actual = result.iterator().next().toString();
        String expected = "";
        try {
            InputStream in = new FileInputStream("testdata/src1/ast.txt");
            expected = Util.readStream(in);
            expected.replace("\r", "");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Can't load file with expected ASt");
        }
        
        try {
            assertThat(actual, is(expected));
        } catch (AssertionError e) {
            printDifference(actual, expected);
            throw e;
        }
    }
    
    /**
     * Tests whether a TypeChef instance running in our process returns correct results.
     * This is not a production scenario, but gives a hint to the code coverage in the sub-process classes.
     * 
     */
    @Test
    public void testSameProcess() {
        SourceFile result = parseFile(new File("test.c"), "src1", true);
        
        String actual = result.iterator().next().toString();
        String expected = "";
        try {
            InputStream in = new FileInputStream("testdata/src1/ast.txt");
            expected = Util.readStream(in);
            expected.replace("\r", "");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Can't load file with expected ASt");
        }
        
        System.out.println(actual);
        try {
            assertThat(actual, is(expected));
        } catch (AssertionError e) {
            printDifference(actual, expected);
            throw e;
        }
    }
    
    /**
     * Tests that the aSTs contain the expected presence conditions.
     */
    @Test
    public void testPCsOfAST() {
        SourceFile result = parseFile(new File("calc.c"), "varAST", true);
        
        // Collect all elements, whose presence condition is not TRUE
        CodeElement unit = result.iterator().next();
        List<CodeElement> varAstParts = new ArrayList<>();
        collectVariableElements(unit, varAstParts);
        
        // Allowed PCs
        Formula varAdd = new Variable("CONFIG_ADDITION");
        Formula varSub = new Variable("CONFIG_SUBTRACTION");
        Formula elseCondition = new Conjunction(new Negation(varAdd), varSub);
        List<Formula> allowedPCs = new ArrayList<>();
        allowedPCs.add(varAdd);
        allowedPCs.add(elseCondition);
        
        // Check for valid PCs
        for (CodeElement astElem : varAstParts) {
            Formula pc = astElem.getPresenceCondition();
            boolean contained = false;
            for (int i = 0; i < allowedPCs.size() && !contained; i++) {
                contained = allowedPCs.get(i).equals(pc);
            }
            if (!contained) {
                SyntaxElement syntaxElement = (SyntaxElement) astElem;
                String elem = syntaxElement.getType() + " " + syntaxElement.getRelation(0) 
                    + " in Line " + syntaxElement.getLineStart();
                Assert.fail("AST element \"" + elem + "\" containes an invalid presence condtion: " + pc);
            }
        }
        
    }
    
    /**
     * Collect all (nested) AST elements, whose presence condition is not {@link True}.
     * @param astElement The parent element to start, will also look into all nested elements.
     * @param varAstParts An empty list, which will be filled as side-effect.
     */
    private void collectVariableElements(CodeElement astElement, List<CodeElement> varAstParts) {
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
     *     in a separate process.
     * @return The parsed file, won't be <tt>null</tt>.
     */
    private SourceFile parseFile(File sourceFile, String srcDir, boolean inSameVm) {
        
        // Configure extractor
        Properties props = new Properties();
        props.setProperty("source_tree", "testdata/" + srcDir);
        props.setProperty("resource_dir", RES_DIR.getPath());
        props.setProperty("code.extractor.skip_default_include_dirs", "true");
        props.setProperty("code.extractor.parse_to_ast", "true");
        
        props.setProperty("code.extractor.process_ram", "200m");
        props.setProperty("code.extractor.ignore_other_models", "true");
        props.setProperty("code.extractor.debug.inherit_output", "false");
        props.setProperty("code.extractor.debug.call_in_same_vm", Boolean.toString(inSameVm));
        
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            props.setProperty("code.extractor.platform_header", "testdata/win/platform.h");
        }
        
        // Start extraction
        SourceFile result = null;
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
    
}
