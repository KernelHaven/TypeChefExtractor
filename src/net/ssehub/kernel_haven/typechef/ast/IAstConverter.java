/*
 * Copyright 2017-2018 University of Hildesheim, Software Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ssehub.kernel_haven.typechef.ast;

import java.io.File;

import de.fosd.typechef.parser.c.TranslationUnit;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;

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
