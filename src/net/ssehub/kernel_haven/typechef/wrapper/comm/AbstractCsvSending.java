package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import net.ssehub.kernel_haven.code_model.Block;
import net.ssehub.kernel_haven.typechef.ast.ErrorSyntaxElement;
import net.ssehub.kernel_haven.typechef.ast.ISyntaxElement;
import net.ssehub.kernel_haven.typechef.ast.LiteralSyntaxElement;
import net.ssehub.kernel_haven.typechef.ast.SyntaxElements;
import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;
import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.parser.CStyleBooleanGrammar;
import net.ssehub.kernel_haven.util.logic.parser.ExpressionFormatException;
import net.ssehub.kernel_haven.util.logic.parser.Parser;
import net.ssehub.kernel_haven.util.logic.parser.VariableCache;

/**
 * Common methods for serializing the AST as CSV and sending the string arrays over a Java serilization stream.
 *  
 * @author Adam
 */
public class AbstractCsvSending {

    /**
     * Reads the AST from the given stream.
     * 
     * @param in The stream to read from.
     * @return The AST read from the stream.
     * 
     * @throws IOException If reading fails.
     */
    protected TypeChefBlock read(ObjectInputStream in) throws IOException {
        try {
            in.readUnshared(); // first integer, always 0
            
            return readCsvListResult(in);
            
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } 
    }
    
    /**
     * Writes the given AST to the given stream.
     * 
     * @param result The AST to write.
     * @param out The stream to write to.
     * 
     * @throws IOException If writing the AST fails.
     */
    protected void write(TypeChefBlock result, ObjectOutputStream out) throws IOException {
        sendSingleBlock(out, result, 0);
        
        // signal end of hierarchy
        out.writeUnshared(new Byte((byte) 0));
    }
    
    /**
     * Recursively sends this block and its children.
     * 
     * @param out The stream to send the block over.
     * @param block The block to send.
     * @param nesting The nesting level of this block.
     * @throws IOException If writing the block or its children throws an IOException.
     */
    private void sendSingleBlock(ObjectOutputStream out, TypeChefBlock block, int nesting) throws IOException {
        out.writeUnshared(nesting);
        out.writeUnshared(block.serializeCsv().toArray(new String[0]));
        
        for (Block child : block) {
            sendSingleBlock(out, (TypeChefBlock) child, nesting + 1);
        }
    }
    
    /**
     * Reads the CSV that the sub-process sends us.
     * 
     * @param in The stream to read from;
     * @return The root node.
     * @throws IOException If reading the stream fails.
     */
    private TypeChefBlock readCsvListResult(ObjectInputStream in) throws IOException {
        VariableCache cache = new VariableCache();
        Parser<Formula> parser = new Parser<>(new CStyleBooleanGrammar(cache));
        
        Map<String, Formula> formulaCache = new HashMap<>(15000);
        Map<String, String> filenameCache = new HashMap<>(1000);
        
        try {
            TypeChefBlock root = TypeChefBlock.createFromCsv((String[]) in.readUnshared(), parser);
            Stack<TypeChefBlock> nesting = new Stack<>();
            nesting.push(root);
            
            Object read = in.readUnshared();
            while (read instanceof Integer) {
                
                int level = (Integer) read;
                TypeChefBlock block = createFromCsv((String[]) in.readUnshared(), parser, formulaCache, filenameCache);
                
                while (level < nesting.size()) {
                    nesting.pop();
                }
                nesting.peek().addChild(block);
                
                nesting.push(block);
                
                read = in.readUnshared();
            }

            Logger.get().logDebug("Final formulaCache size: " + formulaCache.size(),
                    "Final filenameCache size: " + filenameCache.size());
            
            return root;
        } catch (ClassNotFoundException | FormatException e) {
            throw new IOException("Recieved invalid data from TypeChef sub-process", e);
        }
        
    }
    
    /**
     * Deserializes the given CSV into a block. This is a copy from the {@link TypeChefBlock} class, modified to use
     * a formula cache.
     * 
     * @param csv The csv.
     * @param parser The parser to parse boolean formulas.
     * @param formulaCache A cache for re-using already seen formulas.
     * @param filenameCache A cache for filename strings, that ensures that the same string objects are re-used.
     * @return The deserialized block.
     * 
     * @throws FormatException If the CSV is malformed.
     */
    private static TypeChefBlock createFromCsv(String[] csv, Parser<Formula> parser, Map<String, Formula> formulaCache,
            Map<String, String> filenameCache)
            throws FormatException {
        
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
        
        Formula condition = formulaCache.get(csv[2]);
        if (condition == null) {
            try {
                condition = parser.parse(csv[2]);
            } catch (ExpressionFormatException e) {
                throw new FormatException(e);
            }
            formulaCache.put(csv[2], condition);
        }
        
        TypeChefBlock result = new TypeChefBlock(null, condition, type, relation);
        
        if (!csv[3].equals("null") && !csv[4].equals("null")) {
            try {
                filenameCache.putIfAbsent(csv[3], csv[3]);
                result.setFile(filenameCache.get(csv[3]));
                result.setLine(Integer.parseInt(csv[4]));
            } catch (NumberFormatException e) {
                throw new FormatException(e);
            }
        }
        
        return result;
    }
    
}
