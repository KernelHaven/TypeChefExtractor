package net.ssehub.kernel_haven.typechef.ast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fosd.typechef.error.WithPosition;
import net.ssehub.kernel_haven.code_model.Block;
import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.logic.Conjunction;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.parser.ExpressionFormatException;
import net.ssehub.kernel_haven.util.logic.parser.Parser;

/**
 * An element of an AST.
 * 
 * @author Adam
 */
public class TypeChefBlock extends Block implements Serializable {
    
    private static final long serialVersionUID = 3122358323396424111L;
    
    /**
     * A position in the source code, specified by file and line.
     */
    public static final class Position implements Serializable {
        
        private static final long serialVersionUID = 6178380565831457716L;

        private File file;
        
        private int line;
        
        /**
         * Creates a new position.
         * 
         * @param file The source file.
         * @param line The line in the file.
         */
        public Position(File file, int line) {
            this.file = file;
            this.line = line;
        }
        
        /**
         * Creates a position from the scala object.
         * 
         * @param position The scala object to get the position from.
         */
        private Position(de.fosd.typechef.error.Position position) {
            String file = position.getFile();
            this.file = new File(file != null ? file : "<unkown>");
            this.line = position.getLine();
        }
        
        /**
         * The source file.
         * 
         * @return The source file.
         */
        public File getFile() {
            return file;
        }
        
        /**
         * The line in the source file.
         * 
         * @return The line in the source file.
         */
        public int getLine() {
            return line;
        }
        
        /**
         * Sets the source file.
         * 
         * @param file The source file.
         */
        public void setFile(File file) {
            this.file = file;
        }
        
        /**
         * Sets the line.
         *  
         * @param line The line.
         */
        public void setLine(int line) {
            this.line = line;
        }
        
    }

    private List<TypeChefBlock> childreen;
    
    private TypeChefBlock parent;
    
    private Formula condition;
    
    private Formula presencCondition;
    
    private ISyntaxElement type;
    
    private String relation;
    
    private Position pos;
    
    /**
     * Creates a new TypechefBlock. This automatically adds itself to the children list of the parent block. Do not use
     * {@link #addChild(Block)} for this!
     * 
     * @param parent The parent of this new block. May be null.
     * @param condition The condition of this block. Must not be null.
     * @param type The text type of this block. Must not be null.
     * @param relation The relation of this block to its parent. Must not be null.
     */
    public TypeChefBlock(TypeChefBlock parent, Formula condition, ISyntaxElement type, String relation) {
        this.condition = condition;
        this.type = type;
        this.relation = relation;

        if (parent != null) {
            parent.addChild(this);
        } else {
            this.presencCondition = this.condition;
        }
        this.childreen = new LinkedList<>();
    }
    
    /**
     * Creates a new TypechefBlock. This automatically adds itself to the children list of the parent block. Do not use
     * {@link #addChild(Block)} for this!
     * 
     * @param parent The parent of this new block. May be null.
     * @param condition The condition of this block. Must not be null.
     * @param type The text type of this block. Must not be null.
     * @param relation The relation of this block to its parent. Must not be null.
     * @param position The position of this element in the source code. Must not be null.
     */
    TypeChefBlock(TypeChefBlock parent, Formula condition, ISyntaxElement type, String relation,
            WithPosition position) {
        this(parent, condition, type, relation, new Position(position.getPositionFrom()));
    }
    
    /**
     * Creates a new TypechefBlock. This automatically adds itself to the children list of the parent block. Do not use
     * {@link #addChild(Block)} for this!
     * 
     * @param parent The parent of this new block. May be null.
     * @param condition The condition of this block. Must not be null.
     * @param type The text type of this block. Must not be null.
     * @param relation The relation of this block to its parent. Must not be null.
     * @param pos The position. May be null.
     */
    public TypeChefBlock(TypeChefBlock parent, Formula condition, ISyntaxElement type, String relation, Position pos) {
        this(parent, condition, type, relation);
        this.pos = pos;
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
        return pos != null ? pos.getLine() : -1;
    }

    @Override
    public int getLineEnd() {
        return pos != null ? pos.getLine() : -1;
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
        
        String text;
        if (type instanceof LiteralSyntaxElement) {
            // TODO: remove escaping once the cache properly handles it
            text = "Literal: " + ((LiteralSyntaxElement) type).getContent().replace("\\", "\\\\").replace(";", "\\,");
        } else if (type instanceof ErrorSyntaxElement) {
            // TODO: remove escaping once the cache properly handles it
            text = "Error: " + ((ErrorSyntaxElement) type).getMessage().replace("\\", "\\\\").replace(";", "\\,");
        } else if (type instanceof SyntaxElements) {
            text = type.toString();
        } else {
            // TODO: error handling
            text = "Error: Unkown type found in AST";
        }
        
        result.add(text);
        result.add(relation);
        result.add(condition.toString());
        
        if (pos != null) {
            result.add(pos.getFile().toString());
            result.add(pos.getLine() + "");
        } else {
            result.add("null");
            result.add("null");
        }
        
        return result;
    }

    @Override
    public void addChild(Block block) {
        TypeChefBlock typechefBlock = (TypeChefBlock) block;
        
        typechefBlock.parent = this;
        typechefBlock.presencCondition = new Conjunction(this.presencCondition, typechefBlock.condition);
        
        childreen.add(typechefBlock);
    }
    
    /**
     * Returns the type of this block.
     * 
     * @return The type. Never null.
     */
    public ISyntaxElement getType() {
        return type;
    }
    
    /**
     * Returns the relation of this block to its parent.
     * 
     * @return The relation description. Never null.
     */
    public String getRelation() {
        return relation;
    }
    
    /**
     * Returns the parent of this block.
     * 
     * @return The parent block. May be null.
     */
    public TypeChefBlock getParent() {
        return parent;
    }
    
    /**
     * Returns the position of this block.
     * 
     * @return The position of this block. May be null.
     */
    public Position getPosition() {
        return pos;
    }
    
    /**
     * Changes the position of this block.
     * 
     * @param pos The new position of this block.
     */
    public void setPosition(Position pos) {
        this.pos = pos;
    }
    
    /**
     * Returns the first child with the given relation string.
     * 
     * @param relation The relation of the child to this parent block.
     * 
     * @return The child with the given relation; <code>null</code> if none found.
     */
    public TypeChefBlock getChild(String relation) {
        TypeChefBlock result = null;
        for (TypeChefBlock child : childreen) {
            if (child.getRelation().equals(relation)) {
                result = child;
                break;
            }
        }
        return result;
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
        if (csv.length != 5) {
            throw new FormatException("Wrong number of CSV fields, expected 5 but got "
                    + csv.length + ": " + Arrays.toString(csv));
        }
        
        ISyntaxElement type;
        String text = csv[0];
        if (text.startsWith("Literal: ")) {
            text = text.substring("Literal: ".length());
            // TODO: remove escaping once the cache properly handles it
            text = text.replaceAll("[^\\\\]\\\\,", ";").replace("\\\\", "\\");
            type = new LiteralSyntaxElement(text);
        } else if (text.startsWith("Error: ")) {
            text = text.substring("Error: ".length());
            // TODO: remove escaping once the cache properly handles it
            text = text.replaceAll("[^\\\\]\\\\,", ";").replace("\\\\", "\\");
            type = new ErrorSyntaxElement(text);
        } else {
            type = SyntaxElements.getByName(text);
            if (type == null) {
                throw new FormatException("Unkown SyntaxElement type: " + text);
            }
        }
        
        String relation = csv[1];
        
        Formula condition;
        try {
            condition = parser.parse(csv[2]);
        } catch (ExpressionFormatException e) {
            throw new FormatException(e);
        }
        
        Position pos = null;
        
        if (!csv[3].equals("null") && !csv[4].equals("null")) {
            try {
                pos = new Position(new File(csv[3]), Integer.parseInt(csv[4]));
            } catch (NumberFormatException e) {
                throw new FormatException(e);
            }
        }
        
        
        return new TypeChefBlock(null, condition, type, relation, pos);
    }
    
    @Override
    public String toString() {
        return toString("");
    }
    
    /**
     * Turns this block into a string with the given indentation. Recursively walks through its children with increased
     * indentation.
     * 
     * @param indentation The indentation. Contains only tabs. Never null.
     * 
     * @return This block as a string. Never null.
     */
    private String toString(String indentation) {
        StringBuilder result = new StringBuilder();
        
        String conditionStr = condition.toString();
        if (conditionStr.length() > 16) {
            conditionStr = "...";
        }
        
        result.append(indentation).append(relation).append(" [").append(conditionStr).append("] ");
        
        if (pos != null) {
            result.append('[').append(pos.getFile().getName()).append(':').append(pos.getLine()).append("] ");
        }
        
        result.append(type.toString()).append('\n');
        
        indentation += '\t';
        
        for (TypeChefBlock block : childreen) {
            result.append(block.toString(indentation));
        }
        
        return result.toString();
    }

}
