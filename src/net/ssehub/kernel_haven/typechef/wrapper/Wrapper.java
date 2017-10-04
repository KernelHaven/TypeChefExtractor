package net.ssehub.kernel_haven.typechef.wrapper;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.code_model.SyntaxElement;
import net.ssehub.kernel_haven.typechef.util.OutputVoider;
import net.ssehub.kernel_haven.typechef.wrapper.comm.CommFactory;
import net.ssehub.kernel_haven.typechef.wrapper.comm.IComm;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.util.Util;

/**
 * A wrapper that provides a clean interface around the execution of Typechef in another process.
 * 
 * @author Adam
 */
public class Wrapper {

    private static final Logger LOGGER = Logger.get();
    
    private Configuration config;
    
    /**
     * Creates a Wrapper for Typechef with the given configuration.
     * 
     * @param config The configuration for Typechef.
     */
    public Wrapper(Configuration config) {
        this.config = config;
    }
    
    /**
     * Calculates the class path of the current JVM. This even works, if
     * additional jars are loaded at runtime, thus it is more reliable than
     * simply using <code>System.getProperty("java.class.path")</code>.
     * 
     * @return The class path of the current JVM.
     */
    private static String getClassPath() {
        // TODO: maybe move this to a Util class?
        
        // we can't simply do this:
        // return System.getProperty("java.class.path");
        // this is because we load our plugin jars after the VM already started,
        // while java.class.path has the class path passed via the command line
        
        StringBuilder cp = new StringBuilder();
        
        URL[] urls = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();
        
        for (URL url : urls) {
            cp.append(url.getFile()).append(File.pathSeparatorChar);
        }
        
        // remove last path separator
        if (cp.length() > 0) {
            cp.delete(cp.length() - 1, cp.length());
        }
        
        return cp.toString();
    }
    
    /**
     * Thread for communicating with the process that executes Typechef.
     * 
     * @author Adam
     */
    private static class CommThread extends Thread {
        
        private static int numRecieving = 0;
        
        private static Object numReceivingLock = new Object();
        
        private ServerSocket serSock;
        
        private Socket socket;
        
        private Configuration config;
        
        private List<String> params;
        
        private SyntaxElement result;
        
        private List<String> lexerErrors;
        
        private IOException commException;
        
        private ExtractorException extractorException;
        
        /**
         * Creates a communication thread.
         * 
         * @param serSock The server socket to listen at.
         * @param config The configuration.
         * @param params The parameters to send to the other process.
         */
        public CommThread(ServerSocket serSock, Configuration config, List<String> params) {
            super("Comm of " + Thread.currentThread().getName());
            
            this.serSock = serSock;
            this.config = config;
            this.params = params;
            
            lexerErrors = new ArrayList<>(0);
        }
        
        /**
         * Returns the result that was sent to us. If this is null, then the extractor failed.
         * 
         * @return The result, may be null.
         */
        public SyntaxElement getResult() {
            return result;
        }
        
        /**
         * Returns the list of lexer errors that was sent to us.
         * 
         * @return The list of lexer errors, never null.
         */
        public List<String> getLexerErrors() {
            return lexerErrors;
        }
        
        /**
         * Returns an exception if communication failed.
         * 
         * @return The exception, null if no exception occured.
         */
        public IOException getCommException() {
            return commException;
        }
        
        /**
         * Returns the exception that was sent to us. If this is not null then the extractor failed.
         * 
         * @return The exception, may be null.
         */
        public ExtractorException getExtractorException() {
            return extractorException;
        }
        
        /**
         * Closes all sockets that are open.
         */
        public void close() {
            if (serSock != null) {
                try {
                    serSock.close();
                } catch (IOException e) {
                }
            }
        }
        
        /**
         * Checks whether we can receive data, without violating the receiving limit. If we can't receive now, wait
         * until we can. Also increases numReceiving.
         */
        private void checkAndWaitReceivingLimit() {
            synchronized (numReceivingLock) {
                while (config.getMaxReceivingThreads() > 0 && numRecieving >= config.getMaxReceivingThreads()) {
                    // other threads notify numReceivingLock, if they decrement numReceiving
                    LOGGER.logDebug(numRecieving + " threads are currently recieving data; waiting until this is"
                            + " lower than " + config.getMaxReceivingThreads());
                    try {
                        numReceivingLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                LOGGER.logDebug("Start receiving data");
                numRecieving++;
            }
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            boolean numReceivingIncreased = false;
            try {
                socket = serSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeBoolean(config.isParseToAst());
                out.writeUnshared(config.getSourceDir());
                out.writeUnshared(params);
                Message type;
                while ((type = (Message) in.readUnshared()) != Message.END) {
                    LOGGER.logDebug("Got message: " + type);
                    switch (type) {
                    case RESULT: {
                        checkAndWaitReceivingLimit();
                        numReceivingIncreased = true;
                        
                        Runtime rt = Runtime.getRuntime();
                        long usedMemoryBefore = rt.totalMemory() - rt.freeMemory();
                        IComm receiver = CommFactory.createComm(in, out);
                        this.result = receiver.receiveResult();
                        
                        long usedMemoryAfter = rt.totalMemory() - rt.freeMemory();
                        LOGGER.logDebug("Memory usage before we got the result: " + Util.formatBytes(usedMemoryBefore),
                                "Memory usage after we got the result: " + Util.formatBytes(usedMemoryAfter));
                        break;
                    }
                    case LEXER_ERRORS: {
                        lexerErrors = (List<String>) in.readUnshared();
                        break;
                    }
                    case EXCEPTION: {
                        this.extractorException = (ExtractorException) in.readUnshared();
                        break;
                    }
                    case TIMINGS: {
                        Map<String, Long> times = (Map<String, Long>) in.readUnshared();
                        LOGGER.logDebug("Timing reported by runner (in ms):", times.toString());
                        break;
                    }
                    case CRASH: {
                        Throwable th = (Throwable) in.readUnshared();
                        LOGGER.logException("TypeChef runner crashed", th);
                        break;
                    }
                    default:
                        throw new IOException("Invalid message type: " + type);
                    }
                }
                
            } catch (EOFException e) {
                commException = new IOException("TypeChefRunner exited abruptly;"
                        + " the sub-process probably crashed", e);
            } catch (ClassNotFoundException | ClassCastException e) {
                commException = new IOException(e);
            } catch (IOException e) {
                commException = e;
            } finally {
                try {
                    serSock.close();
                } catch (IOException e) {
                }
                if (numReceivingIncreased) {
                    synchronized (numReceivingLock) {
                        numRecieving--;
                        numReceivingLock.notify();
                    }
                }
            }
        }
        
    }
    
    /**
     * Runs the Typechef process. This method blocks until the process exited.
     * 
     * @param port The port to pass to the process for communication.
     * @return Whether the execution was successful, or not.
     * 
     * @throws IOException If running the process fails.
     */
    private boolean runTypeChefProcess(int port) throws IOException {
        boolean success = false;
        
        if (config.callInSameVm()) {
            LOGGER.logWarning("Starting TypeChef in same JVM");
            try {
                Runner.main(new String[] {String.valueOf(port)});
                success = true;
            } catch (IOException e) {
                LOGGER.logException("Exception in TypeChefRunner", e);
            }
            
        } else {
            ProcessBuilder builder = new ProcessBuilder("java",
                    "-DKH_Parent=" + Thread.currentThread().getName(),
                    "-Xms" + config.getProcessRam(),
                    "-Xmx" + config.getProcessRam(),
                    "-cp", getClassPath(),
                    Runner.class.getName(),
                    String.valueOf(port));
            
            LOGGER.logDebug("Starting Typechef process", builder.command().toString());
            
            builder.redirectErrorStream(true);
            if (config.inheritOutput()) {
                builder.redirectOutput(Redirect.INHERIT);
            } else {
                builder.redirectOutput(Redirect.PIPE);
            }
            
            long start = System.currentTimeMillis();
            
            Process process = builder.start();
            
            if (!config.inheritOutput()) {
                new OutputVoider(process.getInputStream()).start();
            }
            
            int returnValue = -1;
            try {
                returnValue = process.waitFor();
            } catch (InterruptedException e) {
                LOGGER.logException("Exception while waiting", e);
            }

            long duration = System.currentTimeMillis() - start;
            LOGGER.logDebug("TypeChef runner took " + (duration / 1000) + "s");
            
            success = returnValue == 0;
        }
        
        return success;
    }
    
    /**
     * Runs Typechef on the given source file inside the source tree specified in the configuration.
     * 
     * @param file The file to run on. Relative to the source_tree in the configuration passed to
     *          the constructor of this wrapper.
     * @return A {@link SyntaxElement} representing the whole file (as an AST).
     * 
     * @throws IOException If the TypeChef execution or communication with the sub-process throws an IOException.
     * @throws ExtractorException If Typechef or the output conversion fails.
     */
    private SyntaxElement runTypeChef(File file) throws IOException, ExtractorException {
        List<String> params = config.buildParameters(file);
        
        if (config.logCallParams()) {
            LOGGER.logDebug(params.toArray(new String[0]));
        }
        
        ServerSocket serSock = new ServerSocket(0);
        
        CommThread comm = new CommThread(serSock, config, params);
        comm.start();
        
        boolean success = runTypeChefProcess(serSock.getLocalPort());
        if (!success) {
            LOGGER.logWarning("TypeChef runner process returned non-zero exit status",
                    "It will probably not have sent correct data to us");
        }
        
        // if the process finished, then close the server socket
        // otherwise, if the process failed before the connection was established, we wait here forever
        comm.close();
        
        try {
            comm.join();
        } catch (InterruptedException e) {
            LOGGER.logException("Exception while waiting", e);
        }
        
        if (!comm.getLexerErrors().isEmpty()) {
            String[] errorStr = new String[comm.getLexerErrors().size() + 1];
            errorStr[0] = "Lexer errors:";
            for (int i = 1; i < errorStr.length; i++) {
                errorStr[i] = comm.getLexerErrors().get(i - 1);
            }
            LOGGER.logInfo(errorStr);
        }
        
        if (comm.getExtractorException() != null) {
            LOGGER.logExceptionDebug("Got extractor exception", comm.getExtractorException());
            throw comm.getExtractorException();
        }
        
        if (comm.getCommException() != null) {
            LOGGER.logExceptionDebug("Got comm exception", comm.getCommException());
            throw comm.getCommException();
        }
        
        SyntaxElement result = comm.getResult();
        
        if (result == null) {
            LOGGER.logDebug("Got neither exception nor result");
            throw new ExtractorException("Runner didn't return result or exception");
        }
        
        // manually set the location of the result, since Typechef creates a temporary command-line input so we
        // don't get proper filenames for the top-element
        result.setSourceFile(file);
        result.setLineStart(1);
//        result.setLineEnd(-1); // TODO?
        
        LOGGER.logDebug("Got result");
        
        return result;
    }
    
    /**
    * Runs Typechef on the given source file inside the source tree specified in the configuration.
    * 
    * @param file The file to run on. Relative to the source_tree in the configuration passed to
    *          the constructor of this wrapper.
    * @return A {@link SourceFile} representing the file.
    * 
    * @throws IOException If the TypeChef execution or communication with the sub-process throws an IOException.
    * @throws ExtractorException If Typechef or the output conversion fails.
    */
    public SourceFile runOnFile(File file) throws IOException, ExtractorException {
        SourceFile result = new SourceFile(file);
        
        SyntaxElement element = runTypeChef(file);
        result.addElement(element);
        
        return result;
    }
    
}
