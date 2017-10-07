package net.ssehub.kernel_haven.typechef;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.test_utils.TestConfiguration;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.Util;

/**
 * Tests for the TypeChef extractor.
 * 
 * @author Adam
 */
public class TypeChefExtractorTest {
    
    private static final File RES_DIR = new File("testdata/res");
    
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
     * @throws SetUpException Unwanted.
     * @throws ExtractorException Unwanted.
     * @throws IOException Unwanted.
     */
    @Test
    public void testSeparateProcess() throws SetUpException, ExtractorException, IOException {
        try {
            File sourceFile = new File("test.c");
            
            Properties props = new Properties();
            props.setProperty("source_tree", "testdata/src1");
            props.setProperty("resource_dir", RES_DIR.getPath());
            props.setProperty("code.extractor.skip_default_include_dirs", "true");
            props.setProperty("code.extractor.parse_to_ast", "true");
            
            props.setProperty("code.extractor.process_ram", "200m");
            props.setProperty("code.extractor.ignore_other_models", "true");
            props.setProperty("code.extractor.debug.inherit_output", "false");
            
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                props.setProperty("code.extractor.platform_header", "testdata/win/platform.h");
            }
            
            TestConfiguration testConfig = new TestConfiguration(props);
            
            TypeChefExtractor extractor = new TypeChefExtractor();
            extractor.init(testConfig.getCodeConfiguration());
            SourceFile result = extractor.runOnFile(sourceFile);
            
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
            
        } catch (ExtractorException e) {
            Logger.get().logException("Exception passed to unit test", e);
            throw e;
        }
    }
    
    /**
     * Tests whether a TypeChef instance running in our process returns correct results.
     * This is not a production scenario, but gives a hint to the code coverage in the sub-process classes.
     * 
     * @throws SetUpException Unwanted.
     * @throws ExtractorException Unwanted.
     * @throws IOException Unwanted.
     */
    @Test
    public void testSameProcess() throws SetUpException, ExtractorException, IOException {
        try {
            File sourceFile = new File("test.c");
            
            Properties props = new Properties();
            props.setProperty("source_tree", "testdata/src1");
            props.setProperty("resource_dir", RES_DIR.getPath());
            props.setProperty("code.extractor.skip_default_include_dirs", "true");
            props.setProperty("code.extractor.parse_to_ast", "true");
            
            props.setProperty("code.extractor.process_ram", "200m");
            props.setProperty("code.extractor.ignore_other_models", "true");
            props.setProperty("code.extractor.debug.inherit_output", "false");
            props.setProperty("code.extractor.debug.call_in_same_vm", "true");
            
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                props.setProperty("code.extractor.platform_header", "testdata/win/platform.h");
            }
            
            TestConfiguration testConfig = new TestConfiguration(props);
            
            TypeChefExtractor extractor = new TypeChefExtractor();
            extractor.init(testConfig.getCodeConfiguration());
            SourceFile result = extractor.runOnFile(sourceFile);
            
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
            
        } catch (ExtractorException e) {
            Logger.get().logException("Exception passed to unit test", e);
            throw e;
        }
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
