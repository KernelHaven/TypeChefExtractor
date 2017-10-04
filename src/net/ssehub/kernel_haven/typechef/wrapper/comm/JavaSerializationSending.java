package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.code_model.SyntaxElement;
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
