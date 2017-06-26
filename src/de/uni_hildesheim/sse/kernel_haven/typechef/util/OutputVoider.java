package de.uni_hildesheim.sse.kernel_haven.typechef.util;

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
