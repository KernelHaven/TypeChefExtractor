package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.code_model.SyntaxElement;

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
