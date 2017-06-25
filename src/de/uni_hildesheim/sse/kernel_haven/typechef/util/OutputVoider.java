package de.uni_hildesheim.sse.kernel_haven.typechef.util;

import java.io.IOException;
import java.io.InputStream;

public class OutputVoider extends Thread {

    private InputStream in;
    
    public OutputVoider(InputStream in) {
        super("OutputVoider");
        
        this.in= in;
    }

    public void run() {
        try {
            while (in.read() != -1) {
                // loop until end of stream
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
