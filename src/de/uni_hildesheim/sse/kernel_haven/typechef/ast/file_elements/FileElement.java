package de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements;

import java.io.Serializable;

import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public abstract class FileElement implements Serializable {

    private static final long serialVersionUID = 6629626460452375034L;

    private Formula pc;
    
    public FileElement(Formula pc) {
        this.pc = pc;
    }

    public Formula getPc() {
        return pc;
    }
    
    @Override
    public String toString() {
        return toString("");
    }
    
    public abstract String toString(String indentation);
    
}
