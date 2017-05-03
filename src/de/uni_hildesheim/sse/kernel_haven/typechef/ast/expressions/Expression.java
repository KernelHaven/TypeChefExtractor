package de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions;

import java.io.Serializable;

public abstract class Expression implements Serializable {

    private static final long serialVersionUID = 4885341030255256753L;

    @Override
    public String toString() {
        return toString("");
    }
    
    public abstract String toString(String indentation);
    
}
