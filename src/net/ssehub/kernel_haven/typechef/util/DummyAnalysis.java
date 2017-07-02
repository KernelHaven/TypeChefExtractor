package net.ssehub.kernel_haven.typechef.util;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.analysis.AbstractAnalysis;
import net.ssehub.kernel_haven.code_model.Block;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.util.CodeExtractorException;

/**
 * A dummy analysis for debugging purposes. Simply dumps the AST result to the logs.
 *  
 * @author Adam
 */
public class DummyAnalysis extends AbstractAnalysis {

    /**
     * Creates a new dummy analysis.
     * 
     * @param config The configuration.
     */
    public DummyAnalysis(Configuration config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            vmProvider.start();
            bmProvider.start();
            cmProvider.start();
            
            SourceFile file;
            while ((file = cmProvider.getNextResult()) != null) {
                for (Block block : file) {
                    String[] lines = block.toString().split("\n");
                    LOGGER.logInfo(lines);
                }
            }
            
            CodeExtractorException exception;
            while ((exception = (CodeExtractorException) cmProvider.getNextException()) != null) {
                if (exception.getCause() != null) {
                    LOGGER.logException("Couldn't parse file " + exception.getCausingFile().getPath(),
                            exception.getCause());
                } else {
                    LOGGER.logException("Couldn't parse file " + exception.getCausingFile().getPath(),
                            exception);
                }
            }
            
        } catch (SetUpException e) {
            LOGGER.logException("Exception in analysis", e);
        }
    }

}