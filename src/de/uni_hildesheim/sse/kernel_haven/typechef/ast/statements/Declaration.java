package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import java.util.ArrayList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class Declaration extends Statement {

    private static final long serialVersionUID = 7942375221523692340L;

    private List<Conditional<String>> names;
    
    public Declaration(Formula pc) {
        super(pc);
        this.names = new ArrayList<>(1);
    }
    
    public void addName(Conditional<String> name) {
        names.add(name);
    }

    @Override
    public String toString(String indentation) {
        StringBuilder result = new StringBuilder();
        
        result.append(indentation).append("[if ").append(getPc().toString()).append("] Declaration\n");
        
        indentation += "\t";
        for (Conditional<String> name : names) {
            result.append(indentation).append("[if ").append(name.getPc()).append("] name = ")
                    .append(name.getElem()).append("\n");
        }
        
        
        return result.toString();
    }

}
