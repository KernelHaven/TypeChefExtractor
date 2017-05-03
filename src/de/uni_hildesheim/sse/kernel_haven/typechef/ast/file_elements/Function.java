package de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements;

import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.Sequence;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.True;

public class Function extends FileElement {
    
    private static final long serialVersionUID = 503070325399275705L;

    private String name;
    
    private Sequence body;
    
    public Function(String name, Formula pc) {
        super(pc);
        this.name = name;
        body = new Sequence(new True());
    }
    
    public String getName() {
        return name;
    }
    
    public Sequence getBody() {
        return body;
    }

    public String toString(String indentation) {
        StringBuffer result = new StringBuffer();
        
        result.append(indentation).append("[if ").append(getPc().toString()).append("] ");
        result.append("Function: ").append(name).append("\n");
        result.append(body.toString(indentation + "\t"));
        
        return result.toString();
    }

}
