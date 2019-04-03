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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A factory for creating communication strategies ({@link IComm}).
 * 
 * @author Adam
 */
public class CommFactory {

    /**
     * Creates the communication object.
     * 
     * @param in The input stream, to read data from the other side.
     * @param out The output stream, to write data to the other side.
     * 
     * @return The communication object.
     */
    public static IComm createComm(ObjectInputStream in, ObjectOutputStream out) {
//        return new CsvOverFileSending(in, out);
//        return new CsvOverMMFSending(in, out);
        return new CsvOverTcpSending(in, out);
//        return new JavaSerializationSending(in, out);
    }
    
}
