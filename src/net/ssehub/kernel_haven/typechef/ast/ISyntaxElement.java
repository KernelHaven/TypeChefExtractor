package net.ssehub.kernel_haven.typechef.ast;

import java.io.Serializable;

/**
 * A syntax element in the AST.
 * 
 * @author Adam
 */
public interface ISyntaxElement extends Serializable {

    /**
     * Represents this syntax element as a string. For "normal" elements, this returns a string representation of this
     * type.
     * 
     * @return This syntax element as a string.
     */
    public String toString();
    
}
