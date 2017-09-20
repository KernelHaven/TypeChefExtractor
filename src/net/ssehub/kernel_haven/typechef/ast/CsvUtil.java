package net.ssehub.kernel_haven.typechef.ast;

import java.util.Arrays;
import java.util.Map;

import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.parser.ExpressionFormatException;
import net.ssehub.kernel_haven.util.logic.parser.Parser;

/**
 * Utility functions for the CSV format of TypechefBlocks.
 *
 * @author Adam
 */
public class CsvUtil {

    /**
     * Don't allow any instances.
     */
    private CsvUtil() {
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
    public static TypeChefBlock csvToBlock(String[] csv, Parser<Formula> parser) throws FormatException {
        return csvToBlock(csv, parser, null, null);
    }
    
    /**
     * Deserializes the given CSV into a block.
     * 
     * @param csv The csv.
     * @param parser The parser to parse boolean formulas.
     * @param formulaCache A cache for re-using already seen formulas.
     * @param filenameCache A cache for filename strings, that ensures that the same string objects are re-used.
     * @return The deserialized block.
     * 
     * @throws FormatException If the CSV is malformed.
     */
    public static TypeChefBlock csvToBlock(String[] csv, Parser<Formula> parser, Map<String, Formula> formulaCache,
            Map<String, String> filenameCache) throws FormatException {
        if (csv.length != 5) {
            throw new FormatException("Wrong number of CSV fields, expected 5 but got "
                    + csv.length + ": " + Arrays.toString(csv));
        }
        
        ISyntaxElement type;
        String text = csv[0];
        if (text.startsWith("Literal: ")) {
            text = text.substring("Literal: ".length());
            text = unescape(text);
            type = new LiteralSyntaxElement(text);
        } else if (text.startsWith("Error: ")) {
            text = text.substring("Error: ".length());
            text = unescape(text);
            type = new ErrorSyntaxElement(text);
        } else {
            type = SyntaxElements.getByName(text);
            if (type == null) {
                throw new FormatException("Unkown SyntaxElement type: " + text);
            }
        }
        
        String relation = csv[1];
        
        Formula condition = null;
        if (formulaCache != null) {
            condition = formulaCache.get(csv[2]);
        }
        if (condition == null) {
            try {
                condition = parser.parse(csv[2]);
            } catch (ExpressionFormatException e) {
                throw new FormatException(e);
            }
            if (formulaCache != null) {
                formulaCache.put(csv[2], condition);
            }
        }
        
        TypeChefBlock result = new TypeChefBlock(null, condition, type, relation);
        
        if (!csv[3].equals("null") && !csv[4].equals("null")) {
            try {
                if (filenameCache != null) {
                    filenameCache.putIfAbsent(csv[3], csv[3]);
                    result.setFile(filenameCache.get(csv[3]));
                } else {
                    result.setFile(csv[3]);
                }
                result.setLine(Integer.parseInt(csv[4]));
            } catch (NumberFormatException e) {
                throw new FormatException(e);
            }
        }
        
        return result;
    }
    
    /**
     * Converts a block into CSV.
     * 
     * @param block The block to convert.
     * @return The CSV; length is 5.
     */
    public static String[] blockToCsv(TypeChefBlock block) {
        String[] result = new String[5];
        
        String text;
        ISyntaxElement type = block.getType();
        if (type instanceof LiteralSyntaxElement) {
            text = "Literal: " + escape(((LiteralSyntaxElement) type).getContent());
        } else if (type instanceof ErrorSyntaxElement) {
            text = "Error: " + escape(((ErrorSyntaxElement) type).getMessage());
        } else if (type instanceof SyntaxElements) {
            text = type.toString();
        } else {
            // TODO: error handling
            text = "Error: Unkown type found in AST";
        }
        
        result[0] = text;
        result[1] = block.getRelation();
        result[2] = block.getCondition().toString();
        
        if (block.getFile() != null) {
            result[3] = block.getFile();
            result[4] = block.getLine() + "";
        } else {
            result[3] = "null";
            result[4] = "null";
        }
        
        return result;
    }
    
    /**
     * Escapes all ';' with '\,' and all '\' with '\\' so the string doesn't break CSV.
     *  
     * @param text The text to escape.
     * @return The escaped text.
     */
    private static String escape(String text) {
        // TODO: remove escaping once the cache properly handles it
        return text.replace("\\", "\\\\").replace(";", "\\,");
    }
    
    /**
     * Un-escapes all '\,' back to ';' and all '\\' back to '\'. Equivalent to escape(String).
     *  
     * @param text The text to un-escape.
     * @return The un-escaped text.
     */
    private static String unescape(String text) {
        // TODO: remove escaping once the cache properly handles it
        return text.replaceAll("[^\\\\]\\\\,", ";").replace("\\\\", "\\");
    }
    
}
