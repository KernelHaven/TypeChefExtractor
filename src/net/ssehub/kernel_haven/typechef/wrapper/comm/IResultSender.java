package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A sender for the result AST of a TypeChef execution.
 *  
 * @author Adam
 */
public interface IResultSender {

    /**
     * Sends the result.
     * 
     * @param out The stream to the parent process.
     * @param result The result to send.
     * 
     * @throws IOException If sending the result fails.
     */
    public void sendResult(ObjectOutputStream out, TypeChefBlock result) throws IOException;
    
}
