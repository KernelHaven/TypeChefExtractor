package net.ssehub.kernel_haven.typechef;

import java.io.File;
import java.io.IOException;

import net.ssehub.kernel_haven.PipelineConfigurator;
import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.build_model.BuildModel;
import net.ssehub.kernel_haven.cnf.ConverterException;
import net.ssehub.kernel_haven.cnf.FormulaToCnfConverterFactory;
import net.ssehub.kernel_haven.cnf.IFormulaToCnfConverter;
import net.ssehub.kernel_haven.cnf.SatSolver;
import net.ssehub.kernel_haven.cnf.SolverException;
import net.ssehub.kernel_haven.cnf.VmToCnfConverter;
import net.ssehub.kernel_haven.code_model.AbstractCodeModelExtractor;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.typechef.wrapper.Configuration;
import net.ssehub.kernel_haven.typechef.wrapper.TypeChefSettings;
import net.ssehub.kernel_haven.typechef.wrapper.Wrapper;
import net.ssehub.kernel_haven.util.CodeExtractorException;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.variability_model.VariabilityModel;

/**
 * Extractor to run Typechef on C source files.
 * 
 * @author Adam
 */
public class TypeChefExtractor extends AbstractCodeModelExtractor {
    
    private static final Logger LOGGER = Logger.get();

    private Configuration typechefConfig;
    
    private boolean ignoreOtherModels;
    
    private boolean readFromOtherExtractors;
    
    private BuildModel buildModel;
    
    private SatSolver satSolver;
    
    private IFormulaToCnfConverter cnfConverter;
    
    @Override
    protected void init(net.ssehub.kernel_haven.config.Configuration config) throws SetUpException {
        TypeChefSettings.registerAllSettings(config);
        ignoreOtherModels = config.getValue(TypeChefSettings.IGNORE_OTHER_MODELS);
        typechefConfig = new Configuration(config);
        readFromOtherExtractors = false;
    }

    @Override
    protected SourceFile runOnFile(File target) throws ExtractorException {
        if (!ignoreOtherModels) {
            readFromOtherExtractors();
        }
        
        if (!ignoreOtherModels && !shouldParse(target)) {
            throw new CodeExtractorException(target, "File presence condition not satisfiable");
        }
        
        try {
            Wrapper wrapper = new Wrapper(typechefConfig);
            SourceFile result = wrapper.runOnFile(target);
            return result;
            
        } catch (IOException | ExtractorException e) {
            throw new CodeExtractorException(target, e);
        }
    }

    @Override
    protected String getName() {
        return "TypechefExtractor";
    }
    
    /**
     * Gets the necessary data from the Build and VariabilityModelProviders, if they are not already available.
     * 
     * @throws ExtractorException If the other pipelines were not successful.
     */
    private synchronized void readFromOtherExtractors() throws ExtractorException {
        if (readFromOtherExtractors) {
            return;
        }
        readFromOtherExtractors = true;
    
        VariabilityModel varModel = PipelineConfigurator.instance().getVmProvider().getResult();
        if (varModel == null) {
            // null if the VM provider threw an exception instead
            throw new ExtractorException("VariabilityModel is null, but Typechef is configured to use it "
                    + "(set code.extractor.ignore_other_models=true to ignore file presence conditions)");
        }
        
        typechefConfig.setVariables(varModel.getVariables());
        
        // set the DIMACS model, so that the parser can throw away some invalid configurations
        typechefConfig.setDimacsModel(varModel.getConstraintModel());
        
        buildModel = PipelineConfigurator.instance().getBmProvider().getResult();
        if (buildModel == null) {
            // null if the BM provider threw an exception instead
            throw new ExtractorException("BuildModel is null, but Typechef is configured to use it "
                    + "(set code.extractor.ignore_other_models=true to ignore file presence conditions)");
        }
        cnfConverter = FormulaToCnfConverterFactory.create();
        try {
            satSolver = new SatSolver(new VmToCnfConverter().convertVmToCnf(varModel));
        } catch (FormatException e) {
            throw new ExtractorException(e);
        }
    }
    
    /**
     * Checks the presence condition in the build model for the given file. If the PC is not defined or not satisfiable
     * then this method returns false.
     * 
     * @param file The file to check the PC for.
     * 
     * @return Whether to parse this file or not.
     */
    private boolean shouldParse(File file) {
        boolean shouldParse = true;
        
        Formula pc = buildModel.getPc(file);
        
        if (pc != null) {
            try {
                if (!satSolver.isSatisfiable(cnfConverter.convert(pc))) {
                    shouldParse = false;
                    LOGGER.logInfo("Presence condition for file " + file.getPath()
                            + " is not satisfiable; skipping file");
                }
            } catch (SolverException | ConverterException e) {
                LOGGER.logException("Can't check satisfiability of presence condition for file " + file.getPath()
                        + "; parsing file anyway", e);
            }
            
        } else {
            shouldParse = false;
            LOGGER.logInfo("No presence condition for file " + file.getPath() + "; skipping");
        }
        
        return shouldParse;
    }

}
