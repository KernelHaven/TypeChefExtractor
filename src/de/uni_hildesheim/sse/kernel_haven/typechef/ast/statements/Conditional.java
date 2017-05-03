package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import java.io.Serializable;

import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class Conditional<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -7164008837279907741L;

    private Formula pc;
    
    private T elem;
    
    public Conditional(Formula pc, T elem) {
        this.pc = pc;
        this.elem = elem;
    }
    
    public Formula getPc() {
        return pc;
    }
    
    public T getElem() {
        return elem;
    }
    
}
