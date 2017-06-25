package de.uni_hildesheim.sse.kernel_haven.typechef.util;

import de.uni_hildesheim.sse.kernel_haven.analysis.AbstractAnalysis;
import de.uni_hildesheim.sse.kernel_haven.code_model.Block;
import de.uni_hildesheim.sse.kernel_haven.code_model.SourceFile;
import de.uni_hildesheim.sse.kernel_haven.config.Configuration;
import de.uni_hildesheim.sse.kernel_haven.util.CodeExtractorException;

public class DummyAnalysis extends AbstractAnalysis {

    public DummyAnalysis(Configuration config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            vmProvider.start(config.getVariabilityConfiguration());
            bmProvider.start(config.getBuildConfiguration());
            cmProvider.start(config.getCodeConfiguration());
            
            SourceFile file;
            while ((file = cmProvider.getNext()) != null) {
                for (Block block : file) {
                    String[] lines = block.toString().split("\n");
                    LOGGER.logInfo(lines);
                }
            }
            
            CodeExtractorException exception;
            while ((exception = cmProvider.getNextException()) != null) {
                LOGGER.logException("Couldn't parse file " + exception.getCausingFile().getPath(), exception.getCause());
            }
            
        } catch (Exception e) {
            LOGGER.logException("Exception in analysis", e);
        }
    }

}