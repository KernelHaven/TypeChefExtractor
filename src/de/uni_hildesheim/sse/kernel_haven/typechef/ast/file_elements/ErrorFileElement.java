package de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements;

import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class ErrorFileElement extends FileElement {

    private static final long serialVersionUID = -827314788803163737L;
    
    private String message;
    
    public ErrorFileElement(String message, Formula pc) {
        super(pc);
        this.message = message;
    }
    
    @Override
    public String toString(String indentation) {
        return indentation + "[if " + getPc() + "] "  + message + "\n";
    }
    
}
