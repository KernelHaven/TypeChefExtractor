package de.uni_hildesheim.sse.kernel_haven.typechef.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.code_model.Block;
import de.uni_hildesheim.sse.kernel_haven.util.FormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Conjunction;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.Parser;

public class TypeChefBlock extends Block implements Serializable {
    
    private static final long serialVersionUID = 3122358323396424111L;

    private List<TypeChefBlock> childreen;
    
    private TypeChefBlock parent;
    
    private Formula condition;
    
    private Formula presencCondition;
    
    private String text;
    
    private String relation;
    
    public TypeChefBlock(TypeChefBlock parent, Formula condition, String text, String relation) {
        this.parent = parent;
        this.condition = condition;
        this.text = text;
        this.relation = relation;

        if (this.parent != null) {
            this.presencCondition = new Conjunction(this.parent.getPresenceCondition(), this.condition);
            this.parent.addChild(this);
        } else {
            this.presencCondition = this.condition;
        }
        this.childreen = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<Block> iterator() {
        return (Iterator<Block>) (Iterator<?>) childreen.iterator();
    }

    @Override
    public int getNestedBlockCount() {
        return childreen.size();
    }

    @Override
    public int getLineStart() {
        return 0;
    }

    @Override
    public int getLineEnd() {
        return 0;
    }

    @Override
    public Formula getCondition() {
        return condition;
    }

    @Override
    public Formula getPresenceCondition() {
        return presencCondition;
    }

    @Override
    public List<String> serializeCsv() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    @Override
    public void addChild(Block block) {
        childreen.add((TypeChefBlock) block);
    }
    
    public String getText() {
        return text;
    }
    
    public String getRelation() {
        return relation;
    }
    
    /**
     * Deserializes the given CSV into a block.
     * 
     * @param csv The csv.
     * @param parser The parser to parse boolean formulas.
     * @return The deserialized block.
     * 
     * @throws FormatException If the CSV is malformed.
     */
    public static TypeChefBlock createFromCsv(String[] csv, Parser<Formula> parser) throws FormatException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String toString() {
        return toString("");
    }
    
    public String toString(String indentation) {
        StringBuilder result = new StringBuilder();
        
        result.append(indentation).append(relation).append(" [").append(condition.toString()).append("] ")
                .append(text).append("\n");
        
        indentation += "\t";
        
        for (TypeChefBlock block : childreen) {
            result.append(block.toString(indentation));
        }
        
        return result.toString();
    }

}
