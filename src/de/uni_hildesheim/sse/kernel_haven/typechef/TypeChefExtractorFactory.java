package de.uni_hildesheim.sse.kernel_haven.typechef;

import de.uni_hildesheim.sse.kernel_haven.SetUpException;
import de.uni_hildesheim.sse.kernel_haven.code_model.ICodeExtractorFactory;
import de.uni_hildesheim.sse.kernel_haven.code_model.ICodeModelExtractor;
import de.uni_hildesheim.sse.kernel_haven.config.CodeExtractorConfiguration;

/**
 * Factory for the typechef extractor.
 * 
 * @author Adam
 */
public class TypeChefExtractorFactory implements ICodeExtractorFactory {

    @Override
    public ICodeModelExtractor create(CodeExtractorConfiguration config) throws SetUpException {
        return new TypeChefExtractor(config);
    }

}
