package de.uni_hildesheim.sse.kernel_haven.typechef;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_hildesheim.sse.kernel_haven.SetUpException;
import de.uni_hildesheim.sse.kernel_haven.TestConfiguration;
import de.uni_hildesheim.sse.kernel_haven.code_model.SourceFile;
import de.uni_hildesheim.sse.kernel_haven.typechef.wrapper.Configuration;
import de.uni_hildesheim.sse.kernel_haven.typechef.wrapper.Wrapper;
import de.uni_hildesheim.sse.kernel_haven.util.ExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.Logger;
import de.uni_hildesheim.sse.kernel_haven.util.Util;

public class TypeChefExtractorTest {
    
    private static final File resDir = new File("testdata/res");
    
    @BeforeClass
    public static void initLogger() {
        Logger.init();
    }
    
    @Before
    public void createResDir() {
        assertThat(resDir.exists(), is(false));
        assertThat(resDir.getParentFile().isDirectory(), is(true));
        
        resDir.mkdir();
        
        assertThat(resDir.isDirectory(), is(true));
    }
    
    @After
    public void deleteResDir() throws IOException {
        Util.deleteFolder(resDir);
        assertThat(resDir.exists(), is(false));
    }

    @Test
    public void temp() throws SetUpException, ExtractorException, IOException {
        try {
            File sourceFile = new File("test.c");
            
            Properties props = new Properties();
            props.setProperty("source_tree", "testdata");
            props.setProperty("resource_dir", resDir.getPath());
            props.setProperty("code.extractor.skip_default_include_dirs", "true");
            props.setProperty("code.extractor.parse_to_ast", "true");
            TestConfiguration testConfig = new TestConfiguration(props);
            
            Wrapper wrapper = new Wrapper(new Configuration(testConfig.getCodeConfiguration()));
            
            SourceFile result = wrapper.runOnFile(sourceFile);
            System.out.println(result.iterator().next().toString());
        } catch (ExtractorException e) {
            Logger.get().logException("Exception passed to unit test", e);
            throw e;
        }
    }
    
    @Test
    public void linuxTest() throws Exception {
        File sourceFile = new File("kernel/kexec.c");
//        File sourceFile = new File("drivers/memstick/core/memstick.c");
        
        Properties props = new Properties();
        props.setProperty("source_tree", "/home/adam/tmp/linux-4.4");
        props.setProperty("arch", "x86");
        props.setProperty("resource_dir", resDir.getPath());
        
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
