package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A communication strategy for the result AST of a TypeChef execution.
 *  
 * @author Adam
 */
public interface IComm {
   
    /**
     * Receives the result.
     * 
     * @return The read result.
     * 
     * @throws IOException If reading the result fails.
     */
    public TypeChefBlock receiveResult() throws IOException;
    
    /**
     * Sends the result.
     * 
     * @param result The result to send.
     * 
     * @throws IOException If sending the result fails.
     */
    public void sendResult(TypeChefBlock result) throws IOException;

}
