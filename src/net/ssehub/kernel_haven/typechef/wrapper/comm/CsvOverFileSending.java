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
class CsvOverFileSending extends AbstractCsvSending implements IResultReceiver, IResultSender {

    @Override
    public void sendResult(ObjectOutputStream out, TypeChefBlock result) throws IOException {
        File file = File.createTempFile("typechef_result", ".java_serialization");
        
        try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(file))) {
            write(result, fileOut);
        }
        
        out.writeUnshared(file);
    }

    @Override
    public TypeChefBlock receiveResult(ObjectInputStream in) throws IOException {
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
