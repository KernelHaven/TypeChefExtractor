package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A communication between parent and sub-process that sends the AST as CSV (string arrays) over the Java
 * serialization stream. 
 * 
 * @author Adam
 */
class CsvOverTcpSending extends AbstractCsvSending implements IResultReceiver, IResultSender {

    @Override
    public TypeChefBlock receiveResult(ObjectInputStream in) throws IOException {
        return read(in);
    }
    
    @Override
    public void sendResult(ObjectOutputStream out, TypeChefBlock result) throws IOException {
        write(result, out);
    }
    
}
