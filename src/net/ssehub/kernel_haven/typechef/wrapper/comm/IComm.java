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
package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;

import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;

/**
 * A communication strategy for the result AST of a TypeChef execution.
 *  
 * @author Adam
 */
public interface IComm {
   
    /**
     * Receives the result.
     * 
     * @return The read result.
     * 
     * @throws IOException If reading the result fails.
     */
    public SyntaxElement receiveResult() throws IOException;
    
    /**
     * Sends the result.
     * 
     * @param result The result to send.
     * 
     * @throws IOException If sending the result fails.
     */
    public void sendResult(SyntaxElement result) throws IOException;

}
