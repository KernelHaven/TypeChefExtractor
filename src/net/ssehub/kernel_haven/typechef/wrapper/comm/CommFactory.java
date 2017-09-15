package net.ssehub.kernel_haven.typechef.wrapper.comm;

/**
 * A factory for creating matching pairs of {@link IResultReceiver} and {@link IResultSender}.
 * 
 * @author Adam
 */
public class CommFactory {

    /**
     * Creates the receiver object.
     * 
     * @return The receiver object.
     */
    public static IResultReceiver createReceiver() {
        return new CsvOverFileSending();
//        return new CsvOverTcpSending();
//        return new JavaSerializationSending();
    }
    
    /**
     * Creates the sender object.
     * 
     * @return The sencer object.
     */
    public static IResultSender createSender() {
        return new CsvOverFileSending();
//        return new CsvOverTcpSending();
//        return new JavaSerializationSending();
    }
    
}
