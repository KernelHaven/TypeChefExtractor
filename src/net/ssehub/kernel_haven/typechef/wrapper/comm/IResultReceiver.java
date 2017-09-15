package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A receiver for the result AST of a TypeChef execution.
 *  
 * @author Adam
 */
public interface IResultReceiver {
   
    /**
     * Receives the result from the given input stream. The input stream is connected to the sub-process.
     * 
     * @param in The input stream to read from.
     * @return The result read from the input stream.
     * 
     * @throws IOException If reading the result fails.
     */
    public TypeChefBlock receiveResult(ObjectInputStream in) throws IOException;

}
