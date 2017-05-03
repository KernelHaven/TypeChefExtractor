package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class ErrorStatement extends Statement {

    private static final long serialVersionUID = 3544720874521822221L;
    
    private String message;
    
    public ErrorStatement(Formula pc, String message) {
        super(pc);
        this.message = message;
    }

    @Override
    public String toString(String indentation) {
        return indentation + "[if " + getPc() + "] "  + message + "\n";
    }
    
}
