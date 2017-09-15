package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A communication between parent and sub-process that utilizes Java serialization to send the AST. 
 * 
 * @author Adam
 */
class JavaSerializationSending implements IResultReceiver, IResultSender {

    @Override
    public void sendResult(ObjectOutputStream out, TypeChefBlock result) throws IOException {
        out.writeUnshared(result);
    }

    @Override
    public TypeChefBlock receiveResult(ObjectInputStream in) throws IOException {
        try {
            return (TypeChefBlock) in.readUnshared();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

}
