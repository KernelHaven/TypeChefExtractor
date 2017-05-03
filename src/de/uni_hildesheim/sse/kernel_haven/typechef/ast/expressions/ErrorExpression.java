package de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions;

public class ErrorExpression extends Expression {

    private static final long serialVersionUID = 687726758912979819L;
    
    private String message;
    
    public ErrorExpression(String message) {
        this.message = message;
    }
    
    @Override
    public String toString(String indentation) {
        return indentation + "Expression (" + message + ")\n";
    }

}
