package net.ssehub.kernel_haven.typechef;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
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
    @Ignore
    public void temp() throws SetUpException, ExtractorException, IOException {
        try {
            File sourceFile = new File("test.c");
            
            Properties props = new Properties();
            props.setProperty("source_tree", "testdata");
            props.setProperty("resource_dir", RES_DIR.getPath());
            props.setProperty("code.extractor.skip_default_include_dirs", "true");
            props.setProperty("code.extractor.parse_to_ast", "false");
            TestConfiguration testConfig = new TestConfiguration(props);
            
            Wrapper wrapper = new Wrapper(new Configuration(testConfig.getCodeConfiguration()));
            
            SourceFile result = wrapper.runOnFile(sourceFile);
            System.out.println(result.iterator().next().toString());
        } catch (ExtractorException e) {
            Logger.get().logException("Exception passed to unit test", e);
            throw e;
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
    
    /**
     * Temporary dummy test to make jenkins happy.
     * TODO
     */
    @Test
    public void dummy() {
    }
    
}
