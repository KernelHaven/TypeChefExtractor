package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions.Expression;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class ReturnStatement extends Statement {

    private static final long serialVersionUID = -628720570186696856L;

    private Expression returnValue;
    
    public ReturnStatement(Formula pc) {
        super(pc);
    }
    
    public void setReturnValue(Expression returnValue) {
        this.returnValue = returnValue;
    }
    
    @Override
    public String toString(String indentation) {
        StringBuffer result = new StringBuffer();
        
        result.append(indentation).append("[if ").append(getPc()).append("] ").append("return\n");
        
        if (returnValue != null) {
            result.append(returnValue.toString(indentation + "\t"));
        }
        
        return result.toString();
    }

}
