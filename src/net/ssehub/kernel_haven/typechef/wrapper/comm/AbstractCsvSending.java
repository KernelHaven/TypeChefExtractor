package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import net.ssehub.kernel_haven.code_model.SyntaxElement;
import net.ssehub.kernel_haven.code_model.SyntaxElementCsvUtil;
import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.parser.CStyleBooleanGrammar;
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
    protected SyntaxElement read(ObjectInputStream in) throws IOException {
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
    protected void write(SyntaxElement result, ObjectOutputStream out) throws IOException {
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
    private void sendSingleBlock(ObjectOutputStream out, SyntaxElement block, int nesting) throws IOException {
        out.writeUnshared(nesting);
        out.writeUnshared(block.serializeCsv().toArray(new String[0]));
        
        for (SyntaxElement child : block.iterateNestedSyntaxElements()) {
            sendSingleBlock(out, (SyntaxElement) child, nesting + 1);
        }
    }
    
    /**
     * Reads the CSV that the sub-process sends us.
     * 
     * @param in The stream to read from;
     * @return The root node.
     * @throws IOException If reading the stream fails.
     */
    private SyntaxElement readCsvListResult(ObjectInputStream in) throws IOException {
        VariableCache cache = new VariableCache();
        Parser<Formula> parser = new Parser<>(new CStyleBooleanGrammar(cache));
        
        Map<String, Formula> formulaCache = new HashMap<>(15000);
        Map<String, File> filenameCache = new HashMap<>(1000);
        
        try {
            SyntaxElement root = SyntaxElement.createFromCsv((String[]) in.readUnshared(), parser);
            Stack<SyntaxElement> nesting = new Stack<>();
            nesting.push(root);
            
            Object read = in.readUnshared();
            while (read instanceof Integer) {
                
                int level = (Integer) read;
                SyntaxElement block = SyntaxElementCsvUtil.csvToElement((String[]) in.readUnshared(), parser,
                        formulaCache, filenameCache);
                
                while (level < nesting.size()) {
                    nesting.pop();
                }
                nesting.peek().addNestedElement(block);
                
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
    
}
