package de.uni_hildesheim.sse.kernel_haven.typechef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.SetUpException;
import de.uni_hildesheim.sse.kernel_haven.code_model.CodeModelProvider;
import de.uni_hildesheim.sse.kernel_haven.code_model.ICodeModelExtractor;
import de.uni_hildesheim.sse.kernel_haven.code_model.SourceFile;
import de.uni_hildesheim.sse.kernel_haven.config.CodeExtractorConfiguration;
import de.uni_hildesheim.sse.kernel_haven.util.BlockingQueue;
import de.uni_hildesheim.sse.kernel_haven.util.CodeExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.FormatException;
import de.uni_hildesheim.sse.kernel_haven.util.Logger;

/**
 * Wrapper to run typechef on c source files.
 * 
 * @author Adam
 */
public class TypeChefExtractor implements ICodeModelExtractor, Runnable {
    
    private static final Logger LOGGER = Logger.get();

    private File linuxSourceTree;

    /**
     * The provider to notify about results.
     */
    private CodeModelProvider provider;

    /**
     * The directory where this extractor can store its resources. Not null.
     */
//    private File resourceDir;

    private boolean stopRequested;

    private int numberOfThreads;
    
    private BlockingQueue<File> filesToParse;
    
    /**
     * Creates a new Typechef extractor.
     * 
     * @param config
     *            The configuration. Must not be null.
     * 
     * @throws SetUpException
     *             If the configuration is not valid.
     */
    public TypeChefExtractor(CodeExtractorConfiguration config) throws SetUpException {
        linuxSourceTree = config.getSourceTree();
        if (linuxSourceTree == null) {
            throw new SetUpException("Config does not contain source_tree setting");
        }

//        resourceDir = config.getExtractorResourceDir(getClass());
        numberOfThreads = config.getThreads();
        
        if (numberOfThreads <= 0) {
            throw new SetUpException("Number of threads is " + numberOfThreads + "; This extractor needs"
                    + " at least one thread");
        }
    }

    @Override
    public void setProvider(CodeModelProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void stop() {
        synchronized (this) {
            stopRequested = true;
        }
    }
    
    /**
     * Checks if the provider requested that we stop our extraction.
     * 
     * @return Whether stop is requested.
     */
    private synchronized boolean isStopRequested() {
        return stopRequested;
    }
    
    @Override
    public void start(BlockingQueue<File> filesToParse) {
        this.filesToParse = filesToParse;
        
        Thread th = new Thread(this);
        th.setName("TypechefExtractor");
        th.start();
    }
    
    @Override
    public void run() {
        LOGGER.logInfo("Starting " + numberOfThreads + " execution threads");
        

        List<Thread> threads = new ArrayList<>(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Thread th = new Thread(() -> {
                File file;
                while ((file = filesToParse.get()) != null) {
                    
                    if (isStopRequested()) {
                        break;
                    }
                    
                    LOGGER.logDebug("Starting extraction for file " + file.getPath());
                    try {
                        runOnFile(file);
                    } catch (IOException | FormatException e) {
                        if (!isStopRequested()) {
                            provider.addException(new CodeExtractorException(file, e));
                        }
                    }
                }
            });
            th.setName("TypechefThread-" + i);
            th.start();
            threads.add(th);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
        
        LOGGER.logInfo("All threads done");
        provider.addResult(null);
    }
    
    /**
     * Runs typechef on a single file.
     * 
     * @param file
     *            The source file in the source code tree to run on.
     * @throws IOException
     *             If running the process fails.
     * @throws FormatException
     *             If the output of the process is invalid.
     */
    private void runOnFile(File file) throws IOException, FormatException {
        LOGGER.logDebug("Creating typechef wrapper...");
        
        TypeChefWrapper wrapper = new TypeChefWrapper();
        
        wrapper.setSourceDir(linuxSourceTree);
        wrapper.setPlatformHeader(new File("platform.h"));
        wrapper.setSystemRoot(new File("/"));
        wrapper.addDefaultPostIncludeDirs();
        
        wrapper.addPreprocessorDefine("_GCC_LIMITS_H_");
        
        LOGGER.logDebug("Calling typechef wrapper...");
        
        SourceFile result = wrapper.runOnFile(file);
        
        if (!isStopRequested()) {
            provider.addResult(result);
        }
    }

}
