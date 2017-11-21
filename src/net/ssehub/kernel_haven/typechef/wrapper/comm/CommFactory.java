package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A factory for creating communication strategies ({@link IComm}).
 * 
 * @author Adam
 */
public class CommFactory {

    /**
     * Creates the communication object.
     * 
     * @param in The input stream, to read data from the other side.
     * @param out The output stream, to write data to the other side.
     * 
     * @return The communication object.
     */
    public static IComm createComm(ObjectInputStream in, ObjectOutputStream out) {
//        return new CsvOverFileSending(in, out);
//        return new CsvOverMMFSending(in, out);
        return new CsvOverTcpSending(in, out);
//        return new JavaSerializationSending(in, out);
    }
    
}
