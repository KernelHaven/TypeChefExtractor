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
import org.junit.Ignore;
import org.junit.Test;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.TestConfiguration;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.typechef.wrapper.Configuration;
import net.ssehub.kernel_haven.typechef.wrapper.Wrapper;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.Util;

/**
 * Tests for the typechef extractor.
 * 
 * TODO: currently this are just temporary methods for debugging purposes.
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
     * Temporary method for starting typechef on testdata/test.c.
     * 
     * @throws SetUpException Unwanted.
     * @throws ExtractorException Unwanted.
     * @throws IOException Unwanted.
     */
    @Test
    public void temp() throws SetUpException, ExtractorException, IOException {
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
            actual.replaceAll("", "");
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
     * Prints a hint to the differences in the two given strings to console.
     * 
     * @param t1 The first string.
     * @param t2 The second string.
     */
    private static void printDifference(String t1, String t2) {
        for (int i = 0; i < Math.min(t1.length(), t2.length()); i++) {
            if (t1.charAt(i) != t2.charAt(i)) {
                System.out.println("Actual:");
                System.out.println(t1.substring(Math.max(0, i - 50), Math.min(t1.length(), i + 50)));
                System.out.println("Expected:");
                System.out.println(t2.substring(Math.max(0, i - 50), Math.min(t2.length(), i + 50)));
                break;
            }
        }
    }
    
    /**
     * Temporary method for starting typechef on a linux file.
     * 
     * @throws SetUpException Unwanted.
     * @throws ExtractorException Unwanted.
     * @throws IOException Unwanted.
     */
    @Test
    @Ignore
    public void linuxTest() throws SetUpException, ExtractorException, IOException {
        File sourceFile = new File("kernel/kexec.c");
//        File sourceFile = new File("drivers/memstick/core/memstick.c");
        
        Properties props = new Properties();
        props.setProperty("source_tree", "/home/adam/tmp/linux-4.4");
        props.setProperty("arch", "x86");
        props.setProperty("resource_dir", RES_DIR.getPath());
        
        props.setProperty("code.extractor.add_linux_source_include_dirs", "true");
        
        props.setProperty("code.extractor.kbuildparam_file", "testdata/linux/kbuildparam.sh");
        
        props.setProperty("code.extractor.static_include.0", "testdata/linux/x86.completed.h");
        props.setProperty("code.extractor.static_include.1", "testdata/linux/partial_conf.h");
        props.setProperty("code.extractor.static_include.2", "testdata/linux/kconfig.h");
        
        props.setProperty("code.extractor.preprocessor_define.0", "__KERNEL__");
        props.setProperty("code.extractor.preprocessor_define.1", "CONFIG_AS_CFI=1");
        props.setProperty("code.extractor.preprocessor_define.2", "CONFIG_AS_CFI_SIGNAL_FRAME=1");
        props.setProperty("code.extractor.preprocessor_define.3", "KBUILD_BASENAME=\"base\"");
        props.setProperty("code.extractor.preprocessor_define.4", "KBUILD_MODNAME=\"base\"");
        
        props.setProperty("code.extractor.parse_to_ast", "false");
        TestConfiguration testConfig = new TestConfiguration(props);
        
        Wrapper wrapper = new Wrapper(new Configuration(testConfig.getCodeConfiguration()));
        
        SourceFile result = wrapper.runOnFile(sourceFile);
        System.out.println(result.iterator().next().toString());
    }
    
}
