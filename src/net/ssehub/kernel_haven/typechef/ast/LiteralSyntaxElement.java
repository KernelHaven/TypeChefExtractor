package net.ssehub.kernel_haven.typechef.ast;

/**
 * A syntax element that represents a literal value.
 * 
 * @author Adam
 */
public class LiteralSyntaxElement implements ISyntaxElement {

    private static final long serialVersionUID = -8346450175208270413L;
    
    private String content;
    
    /**
     * Creates a new literal syntax element.
     * 
     * @param content The content of this literal.
     */
    public LiteralSyntaxElement(String content) {
        this.content = content;
    }
    
    /**
     * Retrieves the content of this literal.
     * 
     * @return The content of this literal.
     */
    public String getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return "Literal: " + content;
    }

}
