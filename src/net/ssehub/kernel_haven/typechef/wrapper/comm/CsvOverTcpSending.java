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

/**
 * A communication between parent and sub-process that sends the AST as CSV (string arrays) over the Java
 * serialization stream. 
 * 
 * @author Adam
 */
class CsvOverTcpSending extends AbstractCsvSending implements IComm {

    private ObjectInputStream in;
    
    private ObjectOutputStream out;
    
    /**
     * Creates this communcation strategy.
     * 
     * @param in The input stream to read data from the other side.
     * @param out The output stream to write data to the other side.
     */
    public CsvOverTcpSending(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    @Override
    public SyntaxElement receiveResult() throws IOException {
        return read(in);
    }
    
    @Override
    public void sendResult(SyntaxElement result) throws IOException {
        write(result, out);
    }
    
}
