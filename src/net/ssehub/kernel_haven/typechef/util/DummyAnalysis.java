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
package net.ssehub.kernel_haven.typechef.util;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.analysis.AbstractAnalysis;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.util.CodeExtractorException;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

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
    public DummyAnalysis(@NonNull Configuration config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            vmProvider.start();
            bmProvider.start();
            cmProvider.start();
            
            SourceFile<SyntaxElement> file;
            while ((file = cmProvider.getNextResult().castTo(SyntaxElement.class)) != null) {
                for (SyntaxElement element : file) {
                    String[] lines = element.toString().split("\n");
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
