package net.ssehub.kernel_haven.typechef;

import java.io.File;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All tests for TypeChefExtractor.
 * 
 * @author Adam
 */
@RunWith(Suite.class)
@SuiteClasses({
    TypeChefExtractorTest.class
    })
public class AllTests {
    
    public static final File TESTDATA = new File("testdata");

}
