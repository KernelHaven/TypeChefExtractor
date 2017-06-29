package net.ssehub.kernel_haven.typechef;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.code_model.ICodeExtractorFactory;
import net.ssehub.kernel_haven.code_model.ICodeModelExtractor;
import net.ssehub.kernel_haven.config.CodeExtractorConfiguration;

/**
 * Factory for the Typechef extractor.
 * 
 * @author Adam
 */
public class TypeChefExtractorFactory implements ICodeExtractorFactory {

    @Override
    public ICodeModelExtractor create(CodeExtractorConfiguration config) throws SetUpException {
        return new TypeChefExtractor(config);
    }

}
