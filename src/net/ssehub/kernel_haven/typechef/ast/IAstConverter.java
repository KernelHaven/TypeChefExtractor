package net.ssehub.kernel_haven.typechef.ast;

import java.io.File;

import de.fosd.typechef.parser.c.TranslationUnit;
import net.ssehub.kernel_haven.code_model.SyntaxElement;

/**
 * A converter to transform the TypeChef output into {@link SyntaxElement}s. 
 * 
 * @author Adam
 */
public interface IAstConverter {

    /**
     * Converts the given Typechef AST to our own format. Not thread safe.
     * 
     * @param unit The AST to convert.
     * @param sourceTree The path to the source tree for relativizing filenames.
     * 
     * @return The result of the conversion.
     */
    public SyntaxElement convertToFile(TranslationUnit unit, File sourceTree);
    
}
