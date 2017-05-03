package de.uni_hildesheim.sse.kernel_haven.typechef;

import de.uni_hildesheim.sse.kernel_haven.SetUpException;
import de.uni_hildesheim.sse.kernel_haven.analysis.AbstractAnalysis;
import de.uni_hildesheim.sse.kernel_haven.config.Configuration;

public class DummyAnalysis extends AbstractAnalysis {

    public DummyAnalysis(Configuration config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            cmProvider.start(config.getCodeConfiguration());
        } catch (SetUpException e) {
            LOGGER.logException("", e);
        }
    }

}
