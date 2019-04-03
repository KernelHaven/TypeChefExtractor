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

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for voiding (reading until stream is closed) an InputStream.
 * This is useful for keeping buffers of sub-process output from running full. 
 * 
 * @author Adam
 */
public class OutputVoider extends Thread {

    private InputStream in;
    
    /**
     * Creates a new OutputVoider for the given stream.
     * 
     * @param in The stream to read from until its end.
     */
    public OutputVoider(InputStream in) {
        super("OutputVoider");
        
        this.in = in;
    }

    @Override
    public void run() {
        try {
            // loop until end of stream
            int read = 0;
            while (read != -1) {
                read = in.read();
            }
            
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
        
    
}
