package net.ssehub.kernel_haven.typechef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import net.ssehub.kernel_haven.config.CodeExtractorConfiguration;
import net.ssehub.kernel_haven.typechef.wrapper.Configuration;
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
    
    private CodeExtractorConfiguration config;
    
    private boolean ignoreOtherModels;
    
    private boolean readFromOtherExtractors;
    
    private boolean prepared;
    
    private BuildModel buildModel;
    
    private SatSolver satSolver;
    
    private IFormulaToCnfConverter cnfConverter;
    
    @Override
    protected void init(CodeExtractorConfiguration config) throws SetUpException {
        this.config = config;
        ignoreOtherModels = Boolean.parseBoolean(config.getProperty("code.extractor.ignore_other_models"));
        typechefConfig = new Configuration(config);
        readFromOtherExtractors = false;
        prepared = false;
    }

    @Override
    protected SourceFile runOnFile(File target) throws ExtractorException {
        if (!ignoreOtherModels) {
            readFromOtherExtractors();
        }
//        try {
//            prepare();
//        } catch (IOException e) {
//            throw new CodeExtractorException(target, e);
//        }
        
        if (!ignoreOtherModels && !shouldParse(target)) {
            throw new CodeExtractorException(target, "File presence condition not satisfiable");
        }
        
        try {
            Wrapper wrapper = new Wrapper(typechefConfig);
            SourceFile result = wrapper.runOnFile(target);
            return result;
            
        } catch (IOException e) {
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
        typechefConfig.setVariables(varModel.getVariables());
        
        buildModel = PipelineConfigurator.instance().getBmProvider().getResult();
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
    
    
    /**
     * A non boolean operation on a variability variable.
     * E.g. <code>>= 3</code>.
     */
    private static final class NonBooleanOperation {
        
        private String operator;
        
        private int value;

        /**
         * .
         * @param operator .
         * @param value .
         */
        public NonBooleanOperation(String operator, int value) {
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        public int hashCode() {
            return operator.hashCode() + Integer.hashCode(value);
        }
        
        @Override
        public boolean equals(Object obj) {
            boolean equal = false;
            if (obj instanceof NonBooleanOperation) {
                NonBooleanOperation other = (NonBooleanOperation) obj;
                equal = this.operator.equals(other.operator) && this.value == other.value;
            }
            return equal;
        }
        
    }
    
    /**
     * Prepare the source tree.
     * 
     * @throws IOException IF reading files fails.
     */
    @SuppressWarnings("unused")
    private synchronized void prepare() throws IOException {
        if (prepared) {
            return;
        }
        prepared = true;
        
        Map<String, Set<NonBooleanOperation>> nonBooleanVariables = new HashMap<>();
        
        // walk through every source file and collect non-boolean variability variables
        try {
            Files.walk(config.getSourceTree().toPath(), FileVisitOption.FOLLOW_LINKS).forEach((file) -> {
                try {
                    collectNonBoolean(file.toFile(), nonBooleanVariables);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
        
        for (Map.Entry<String, Set<NonBooleanOperation>> entry : nonBooleanVariables.entrySet()) {
            System.out.println(entry.getKey() + " ");
            for (NonBooleanOperation op : entry.getValue()) {
                System.out.println("\t" + op.operator + " " + op.value);
            }
        }
    }
    
    /**
     * Finds all non-boolean variability variables used in the given file.
     * 
     * @param file The file to search in.
     * @param result Where to add the found non-boolean variables to.
     * 
     * @throws IOException If reading the file fails.
     */
    private void collectNonBoolean(File file, Map<String, Set<NonBooleanOperation>> result) throws IOException {
        if (!file.isFile() || (!file.getName().endsWith(".c") && !file.getName().endsWith(".h"))) {
            return;
        }
        
        Pattern regex = Pattern.compile("(CONFIG_[A-Za-z0-9_]+)\\s*(==|!=|<|>|<=|>=)\\s*(-?[0-9]+)\\s*");
        
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.charAt(0) != '#') {
                    continue;
                }
                
                while (line.charAt(line.length() - 1) == '\\') {
                    line += in.readLine();
                }
                
                Matcher m = regex.matcher(line);
                
                while (m.find()) {
                    String variable = m.group(1);
                    String operator = m.group(2);
                    int value = Integer.parseInt(m.group(3));
                    
//                    if (operator.equals(">=")) {
//                        operator = ">";
//                        value--;
//                    } else if (operator.equals("<=")) {
//                        operator = "<";
//                        value++;
//                    }
                    
                    Set<NonBooleanOperation> l = result.get(variable);
                    if (l == null) {
                        l = new HashSet<>();
                        result.put(variable, l);
                    }
                    l.add(new NonBooleanOperation(operator, value));
                }
            }
            
        }
    }

}
