package net.ssehub.kernel_haven.typechef.ast;

/**
 * An element in the syntax tree that indicates an error.
 * 
 * @author Adam
 */
public class ErrorSyntaxElement implements ISyntaxElement {

    private static final long serialVersionUID = 3895790404074576397L;

    private String message;
    
    /**
     * Creates a new error syntax element.
     * 
     * @param message The message to be displayed.
     */
    public ErrorSyntaxElement(String message) {
        this.message = message;
    }
    
    /**
     * Retrieves the message for this error.
     * 
     * @return This error message.
     */
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "Error: " + message;
    }
    
}
