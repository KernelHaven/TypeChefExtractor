package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions.Expression;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class ExpressionStatement extends Statement {

    private static final long serialVersionUID = 3613850322734930351L;
    
    private Expression expr;
    
    public ExpressionStatement(Formula pc, Expression expr) {
        super(pc);
        this.expr = expr;
    }

    public Expression getExpression() {
        return expr;
    }
    
    @Override
    public String toString(String indentation) {
        StringBuffer result = new StringBuffer();
        
        result.append(indentation).append("[if ").append(getPc().toString()).append("] ").append("Expression:\n");
        
        result.append(expr.toString(indentation + "\t"));
        
        return result.toString();
    }

}
