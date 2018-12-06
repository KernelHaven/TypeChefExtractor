package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.typechef.wrapper.Wrapper;

/**
 * A communication between parent and sub-process that creates a Memory Mapped File (MMF) for a Java serialization of
 * the AST represented in CSV. The filename is send to the parent process, which de-serializes the CSV and parses it
 * back into the AST.
 * @see <a href="https://www.javacodegeeks.com/2013/05/power-of-java-memorymapped-file.html">
 * https://www.javacodegeeks.com/2013/05/power-of-java-memorymapped-file.html</a>
 * 
 * @author Sascha
 * @author Adam
 */
class CsvOverMMFSending extends AbstractCsvSending implements IComm {
    
    /**
     * 150 KB buffer (probably to much, since we write/read always only a single {@link SyntaxElement}).
     */
    private static final long BUFFER_SIZE = 150 * 1024;

    private ObjectInputStream in;
    
    private ObjectOutputStream out;
    
    /**
     * Creates this communication strategy.
     * 
     * @param in The input stream to read data from the other side.
     * @param out The output stream to write data to the other side.
     */
    public CsvOverMMFSending(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    @Override
    public void sendResult(SyntaxElement result) throws IOException {
        File file = File.createTempFile("typechef_result", ".java_serialization");
        file.deleteOnExit();
        
        @SuppressWarnings("resource") // TODO
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, BUFFER_SIZE);
        
        
        try (ObjectOutputStream out = new ObjectOutputStream(new ByteBufferBackedOutputStream(mem))) {
            write(result, out);
        }
        
        out.writeUnshared(file);
        
        try {
            // read a single object, that indicates that the other side finished with reading
            Wrapper.CommThread.readObject(in);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
        
        fc.close();
    }

    @Override
    public SyntaxElement receiveResult() throws IOException {
        try {
            File file = Wrapper.CommThread.readObject(in);
            @SuppressWarnings("resource") // TODO
            FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
            MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_ONLY, 0, BUFFER_SIZE);
            
            SyntaxElement result;
            
            try (ObjectInputStream fileIn = new ObjectInputStream(new ByteBufferBackedInputStream(mem))) {
                result = read(fileIn);
            }
            
            out.writeUnshared(""); // indicate that we are done
            file.delete();
            return result;
            
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

}
