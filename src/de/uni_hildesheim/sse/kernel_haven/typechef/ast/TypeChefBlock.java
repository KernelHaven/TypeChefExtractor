package de.uni_hildesheim.sse.kernel_haven.typechef.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.code_model.Block;
import de.uni_hildesheim.sse.kernel_haven.util.FormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Conjunction;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.ExpressionFormatException;
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
        this.condition = condition;
        this.text = text;
        this.relation = relation;

        if (parent != null) {
            parent.addChild(this);
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
        List<String> result = new ArrayList<>(3);
        
        // TODO: remove escaping once the cache properly handles it
        String escapedText = text.replace("\\", "\\\\").replace(";", "\\,");
        
        result.add(escapedText);
        result.add(relation);
        result.add(condition.toString());
        
        return result;
    }

    @Override
    public void addChild(Block block) {
        TypeChefBlock typechefBlock = (TypeChefBlock) block;
        
        typechefBlock.parent = this;
        typechefBlock.presencCondition = new Conjunction(this.presencCondition, typechefBlock.condition);
        
        childreen.add(typechefBlock);
    }
    
    public String getText() {
        return text;
    }
    
    public String getRelation() {
        return relation;
    }
    
    public TypeChefBlock getParent() {
        return parent;
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
        if (csv.length != 3) {
            throw new FormatException("Wrong number of CSV fields, expected 3 but got " + csv.length + ": " + Arrays.toString(csv));
        }
        
        // TODO: remove escaping once the cache properly handles it
        String text = csv[0].replaceAll("[^\\\\]\\\\,", ";").replace("\\\\", "\\");
        String relation = csv[1];
        
        Formula condition;
        try {
            condition = parser.parse(csv[2]);
        } catch (ExpressionFormatException e) {
            throw new FormatException(e);
        }
        
        
        return new TypeChefBlock(null, condition, text, relation);
    }
    
    @Override
    public String toString() {
        return toString("");
    }
    
    public String toString(String indentation) {
        StringBuilder result = new StringBuilder();
        
        String conditionStr = condition.toString();
        if (conditionStr.length() > 16) {
            conditionStr = "...";
        }
        
        result.append(indentation).append(relation).append(" [").append(conditionStr).append("] ")
                .append(text).append("\n");
        
        indentation += "\t";
        
        for (TypeChefBlock block : childreen) {
            result.append(block.toString(indentation));
        }
        
        return result.toString();
    }

}
