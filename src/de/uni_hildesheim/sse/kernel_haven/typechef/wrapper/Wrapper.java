package de.uni_hildesheim.sse.kernel_haven.typechef.wrapper;

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

import de.uni_hildesheim.sse.kernel_haven.code_model.SourceFile;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.TypeChefBlock;
import de.uni_hildesheim.sse.kernel_haven.typechef.util.OutputVoider;
import de.uni_hildesheim.sse.kernel_haven.util.ExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.Logger;

/**
 * A wrapper that provides a clean interface around the execution of Typechef in another process.
 * 
 * @author Adam
 */
public class Wrapper {

    private static final Logger LOGGER = Logger.get();
    
    /**
     * Whether to launch a separate JVM or not. Set to <code>true</code> only
     * for debug purposes.
     */
    private static final boolean CALL_IN_SAME_VM = false;
    
    /**
     * Whether the TypeChef parameters should be logged. Useful for debugging.
     */
    private static final boolean LOG_CALL_PARAMS = false;
    
    /**
     * Whether the child JVM process should have same stdout and stderr as the parent one.
     * Useful for debugging. If set to false, then all output is discarded.
     */
    private static final boolean INHERIT_OUTPUT = false;
    
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
        
        private ServerSocket serSock;
        
        private Configuration config;
        
        private List<String> params;
        
        private TypeChefBlock result;
        
        private List<String> lexerErrors;
        
        private IOException commException;
        
        private ExtractorException extractorException;
        
        public CommThread(ServerSocket serSock, Configuration config, List<String> params) {
            super("Comm of " + Thread.currentThread().getName());
            
            this.serSock = serSock;
            this.config = config;
            this.params = params;
            
            lexerErrors = new ArrayList<>(0);
        }
        
        public TypeChefBlock getResult() {
            return result;
        }
        
        public List<String> getLexerErrors() {
            return lexerErrors;
        }
        
        public IOException getCommException() {
            return commException;
        }
        
        public ExtractorException getExtractorException() {
            return extractorException;
        }
        
        @SuppressWarnings("unchecked")
        public void run() {
            try {
                Socket socket = serSock.accept();
                
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                
                out.writeBoolean(config.isParseToAst());
                out.writeObject(params);
                
                Object result = in.readObject();
                
                if (result instanceof ExtractorException) {
                    this.extractorException = (ExtractorException) result;
                    
                } else if (result instanceof TypeChefBlock) {
                    this.result = (TypeChefBlock) result;
                    
                } else {
                    throw new IOException("Invalid result type: " + result.getClass().getName());
                }
                
                Object lexerErrorList = in.readObject();
                if (lexerErrorList != null) {
                    lexerErrors.addAll((List<String>) lexerErrorList);
                }
                
            } catch (EOFException e) {
                commException = new IOException("TypeChefRunner exited without sending a result");
            } catch (ClassNotFoundException | ClassCastException e) {
                commException = new IOException(e);
            } catch (IOException e) {
                commException = e;
            } finally {
                try {
                    serSock.close();
                } catch (IOException e) {
                }
            }
        }
        
    }
    
    /**
     * Runs Typechef on the given source file inside the source tree specified in the configuration.
     * 
     * @param file The file to run on. Relative to the source_tree in the configuration passed to
     *          the constructor of this wrapper.
     * @return A {@link TypeChefBlock} representing the whole file (as an AST).
     * 
     * @throws IOException If the TypeChef execution or communication with the sub-process throws an IOException.
     * @throws ExtractorException If Typechef or the output conversion fails.
     */
    private TypeChefBlock runTypeChef(File file) throws IOException, ExtractorException {
        List<String> params = config.buildParameters(file);
        
        if (LOG_CALL_PARAMS) {
            LOGGER.logDebug(params.toArray(new String[0]));
        }
        
        ServerSocket serSock = new ServerSocket(0);
        
        CommThread comm = new CommThread(serSock, config, params);
        comm.start();
        
        if (CALL_IN_SAME_VM) {
            LOGGER.logWarning("Starting TypeChef in same JVM");
            try {
                Runner.main(new String[] {String.valueOf(serSock.getLocalPort())});
            } catch (Exception e) {
                LOGGER.logException("Exception in TypeChefRunner", e);
            }
            
        } else {
            ProcessBuilder builder = new ProcessBuilder("java",
                    "-Xmx" + config.getProcessRam(),
                    "-cp", getClassPath(),
                    Runner.class.getName(),
                    String.valueOf(serSock.getLocalPort()));
            
            LOGGER.logDebug("Starting typechef process", builder.command().toString());
            
            builder.redirectErrorStream(true);
            if (INHERIT_OUTPUT) {
                builder.redirectOutput(Redirect.INHERIT);
            } else {
                builder.redirectOutput(Redirect.PIPE);
            }
            
            Process process = builder.start();
            
            if (!INHERIT_OUTPUT) {
                new OutputVoider(process.getInputStream()).start();
            }
            
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                LOGGER.logException("Exception while waiting", e);
            }
        }
        
        // if the process finished, then close sersocket
        // otherwise, if the process failed before the connection was established, we wait here forever
        try {
            serSock.close();
        } catch (IOException e) {
        }
        
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
            throw comm.getExtractorException();
        }
        
        if (comm.getCommException() != null) {
            throw comm.getCommException();
        }
        
        if (comm.getResult() == null) {
            throw new ExtractorException("Runner didn't return result or exception");
        }
        
        return comm.getResult();
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
        
        TypeChefBlock block = runTypeChef(file);
        result.addBlock(block);
        
        return result;
    }
    
}
