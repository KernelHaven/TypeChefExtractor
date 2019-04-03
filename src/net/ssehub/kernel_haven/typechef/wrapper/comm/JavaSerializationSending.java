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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.typechef.wrapper.Wrapper;

/**
 * A communication between parent and sub-process that utilizes Java serialization to send the AST. 
 * 
 * @author Adam
 */
class JavaSerializationSending implements IComm {

    private ObjectInputStream in;
    
    private ObjectOutputStream out;
    
    /**
     * Creates this communcation strategy.
     * 
     * @param in The input stream to read data from the other side.
     * @param out The output stream to write data to the other side.
     */
    public JavaSerializationSending(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    
    @Override
    public void sendResult(SyntaxElement result) throws IOException {
        out.writeUnshared(result);
    }

    @Override
    public SyntaxElement receiveResult() throws IOException {
        try {
            return Wrapper.CommThread.readObject(in);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

}
