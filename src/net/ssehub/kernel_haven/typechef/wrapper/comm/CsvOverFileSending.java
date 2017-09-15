package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;

/**
 * A communication between parent and sub-process that creates an file for a Java serialization of the AST represented
 * in CSV. The filename is send to the parent process, which de-serializes the CSV and parses it back into the AST.
 * 
 * @author Adam
 */
class CsvOverFileSending extends AbstractCsvSending implements IComm {

    private ObjectInputStream in;
    
    private ObjectOutputStream out;
    
    /**
     * Creates this communcation strategy.
     * 
     * @param in The input stream to read data from the other side.
     * @param out The output stream to write data to the other side.
     */
    public CsvOverFileSending(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    @Override
    public void sendResult(TypeChefBlock result) throws IOException {
        File file = File.createTempFile("typechef_result", ".java_serialization");
        
        try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(file))) {
            write(result, fileOut);
        }
        
        out.writeUnshared(file);
    }

    @Override
    public TypeChefBlock receiveResult() throws IOException {
        try {
            File file = (File) in.readUnshared();
            
            TypeChefBlock result;
            
            try (ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(file))) {
                result = read(fileIn);
            }
            
            file.delete();
            return result;
            
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

}
