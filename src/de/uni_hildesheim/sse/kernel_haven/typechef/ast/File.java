package de.uni_hildesheim.sse.kernel_haven.typechef.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements.FileElement;

public class File implements Serializable {
    
    private static final long serialVersionUID = 8466438812607108023L;
    
    private String name;
    
    private List<FileElement> elements;
    
    public File(String name) {
        this.name = name;
        elements = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public List<FileElement> getElements() {
        return elements;
    }
    
    @Override
    public String toString() {
        return toString("");
    }
    
    public String toString(String indentation) {
        StringBuffer result = new StringBuffer();
        result.append(indentation).append("File: ").append(name).append("\n");

        for (FileElement element : elements) {
            result.append(element.toString(indentation + "\t"));
        }
        
        return result.toString();
    }

}
