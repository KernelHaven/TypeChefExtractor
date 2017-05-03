package de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements;

import java.util.ArrayList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions.Expression;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;

public class IfStatement extends Statement {

    private static final long serialVersionUID = 9196081063027939308L;

    private List<Conditional<Expression>> conditions;
    
    private List<Statement> thenBranches;

    private List<IfStatement> elifs;
    
    private List<Statement> elseBranches;
    
    public IfStatement(Formula pc) {
        super(pc);
        this.conditions = new ArrayList<>(1);
        this.thenBranches = new ArrayList<>(1);
        this.elseBranches = new ArrayList<>(1);
        this.elifs = new ArrayList<>(0);
    }
    
    public void addCondition(Conditional<Expression> condition) {
        conditions.add(condition);
    }
    
    public void addThenBranch(Statement branch) {
        thenBranches.add(branch);
    }
    
    public void addElseBranch(Statement branch) {
        elseBranches.add(branch);
    }
    
    public void addElif(IfStatement elif) {
        elifs.add(elif);
    }
    
    @Override
    public String toString(String indentation) {
        StringBuffer result = new StringBuffer();
        
        result.append(indentation).append("[if ").append(getPc()).append("] ").append("If:\n");
        indentation += "\t";
        for (Conditional<Expression> condition : conditions) {
            result.append(indentation).append("[if ").append(condition.getPc()).append("]\n")
                    .append(condition.getElem().toString(indentation + "\t"));
        }
        
        for (Statement thenBranch : thenBranches) {
            result.append(indentation).append("[if ").append(thenBranch.getPc()).append("] ").append("Then:\n");
            result.append(thenBranch.toString(indentation + "\t"));
        }
        
        for (IfStatement elif : elifs) {
            result.append(indentation).append("[if ").append(elif.getPc()).append("] ").append("Else-If:\n");
            
            String tmpIndentation = indentation + "\t";
            for (Conditional<Expression> condition : elif.conditions) {
                result.append(tmpIndentation).append("[if ").append(condition.getPc()).append("]\n")
                        .append(condition.getElem().toString(tmpIndentation + "\t"));
            }
            
            for (Statement thenBranch : elif.thenBranches) {
                result.append(tmpIndentation).append("[if ").append(thenBranch.getPc()).append("] ").append("Then:\n");
                result.append(thenBranch.toString(tmpIndentation + "\t"));
            }
            
        }
        
        for (Statement elseBranch : elseBranches) {
            result.append(indentation).append("[if ").append(elseBranch.getPc()).append("] ").append("Else:\n");
            result.append(elseBranch.toString(indentation + "\t"));
        }
        
        return result.toString();
    }

}
