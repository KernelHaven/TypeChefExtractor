package de.uni_hildesheim.sse.kernel_haven.typechef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.PipelineConfigurator;
import de.uni_hildesheim.sse.kernel_haven.SetUpException;
import de.uni_hildesheim.sse.kernel_haven.build_model.BuildModel;
import de.uni_hildesheim.sse.kernel_haven.cnf.ConverterException;
import de.uni_hildesheim.sse.kernel_haven.cnf.FormulaToCnfConverterFactory;
import de.uni_hildesheim.sse.kernel_haven.cnf.IFormulaToCnfConverter;
import de.uni_hildesheim.sse.kernel_haven.cnf.SatSolver;
import de.uni_hildesheim.sse.kernel_haven.cnf.SolverException;
import de.uni_hildesheim.sse.kernel_haven.cnf.VmToCnfConverter;
import de.uni_hildesheim.sse.kernel_haven.code_model.CodeModelProvider;
import de.uni_hildesheim.sse.kernel_haven.code_model.ICodeModelExtractor;
import de.uni_hildesheim.sse.kernel_haven.code_model.SourceFile;
import de.uni_hildesheim.sse.kernel_haven.config.CodeExtractorConfiguration;
import de.uni_hildesheim.sse.kernel_haven.typechef.wrapper.Configuration;
import de.uni_hildesheim.sse.kernel_haven.typechef.wrapper.Wrapper;
import de.uni_hildesheim.sse.kernel_haven.util.BlockingQueue;
import de.uni_hildesheim.sse.kernel_haven.util.CodeExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.ExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.FormatException;
import de.uni_hildesheim.sse.kernel_haven.util.Logger;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.variability_model.VariabilityModel;

/**
 * Extractor to run Typechef on C source files.
 * 
 * @author Adam
 */
public class TypeChefExtractor implements ICodeModelExtractor, Runnable {
    
    private static final Logger LOGGER = Logger.get();

    /**
     * The provider to notify about results.
     */
    private CodeModelProvider provider;

    private boolean stopRequested;

    private int numberOfThreads;
    
    private BlockingQueue<File> filesToParse;
    
    private Configuration configuration;
    
    private BuildModel buildModel;
    
    private SatSolver satSolver;
    
    private IFormulaToCnfConverter cnfConverter;
    
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
        configuration = new Configuration(config);
        
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
        
        try {
            readFromOtherExtractors();
        } catch (ExtractorException e) {
            if (!isStopRequested()) {
                provider.addException(new CodeExtractorException(new File("<all>"), e));
                provider.addResult(null);
            }
        }

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
                    } catch (IOException | ExtractorException e) {
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
     * Gets the necessary data from the Build and VariabilityModelProviders.
     * 
     * @throws ExtractorException If the other pipelines were not successful.
     */
    private void readFromOtherExtractors() throws ExtractorException {
        buildModel = PipelineConfigurator.instance().getBmProvider().getResult();
        
        VariabilityModel varModel = PipelineConfigurator.instance().getVmProvider().getResult();
        cnfConverter = FormulaToCnfConverterFactory.create();
        
        try {
            satSolver = new SatSolver(new VmToCnfConverter().convertVmToCnf(varModel));
        } catch (FormatException e) {
            throw new ExtractorException(e);
        }
            
        configuration.setVariables(varModel.getVariables());
            
    }
    
    /**
     * Runs Typechef on a single file. This first checks, whether the file presence condition is
     * satisfiable (and skips the unsatisfiable ones).
     * 
     * @param file
     *            The source file in the source code tree to run on.
     * 
     * @throws IOException If running Typechef throws an IOException.
     * @throws ExtractorException If the Typechfe execution or reuslt conversion was not successful.
     */
    private void runOnFile(File file) throws IOException, ExtractorException {
        LOGGER.logDebug("Checking if file PC is satisfiable");
        
        Formula pc = buildModel.getPc(file);
        if (pc == null) {
            LOGGER.logInfo("No presence condition for file " + file.getPath() + "; skipping");
            return;
        }
        
        try {
            if (!satSolver.isSatisfiable(cnfConverter.convert(pc))) {
                LOGGER.logInfo("Presence condition for file " + file.getPath() + " is not satisfiable; skipping file");
                return;
            }
        } catch (SolverException | ConverterException e) {
            LOGGER.logException("Can't check satisfiability of presence condition for file " + file.getPath()
                    + "; parsing file anyway", e);
        }
        
        LOGGER.logDebug("Creating typechef wrapper...");
        
        Wrapper wrapper = new Wrapper(configuration);
        
        LOGGER.logDebug("Calling typechef wrapper...");
        
        SourceFile result = wrapper.runOnFile(file);
        
        if (!isStopRequested()) {
            provider.addResult(result);
        }
    }

}
