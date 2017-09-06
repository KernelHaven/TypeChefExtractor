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

import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.typechef.ast.TypeChefBlock;
import net.ssehub.kernel_haven.typechef.util.OutputVoider;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;

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
        
        private ServerSocket serSock;
        
        private Configuration config;
        
        private List<String> params;
        
        private TypeChefBlock result;
        
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
        public TypeChefBlock getResult() {
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
        
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            try {
                Socket socket = serSock.accept();
                
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                
                out.writeBoolean(config.isParseToAst());
                out.writeUnshared(params);
                
                Runtime rt = Runtime.getRuntime();
                long usedMemoryBefore = rt.totalMemory() - rt.freeMemory();
                
                Object result = in.readUnshared();
                
                long usedMemoryAfter = rt.totalMemory() - rt.freeMemory();
                
                LOGGER.logDebug("Memory usage before waiting for result: " + usedMemoryBefore,
                        "Memory usage after we got result object: " + usedMemoryAfter);
                
                if (result instanceof ExtractorException) {
                    this.extractorException = (ExtractorException) result;
                    
                } else if (result instanceof TypeChefBlock) {
                    this.result = (TypeChefBlock) result;
                    
                } else {
                    throw new IOException("Invalid result type: " + result.getClass().getName());
                }
                
                Object lexerErrorList = in.readUnshared();
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
     * Runs the Typechef process. This method blocks until the process exited.
     * 
     * @param port The port to pass to the process for communication.
     * 
     * @throws IOException If running the process fails.
     */
    private void runTypeChefProcess(int port) throws IOException {
        if (config.callInSameVm()) {
            LOGGER.logWarning("Starting TypeChef in same JVM");
            try {
                Runner.main(new String[] {String.valueOf(port)});
            } catch (IOException e) {
                LOGGER.logException("Exception in TypeChefRunner", e);
            }
            
        } else {
            ProcessBuilder builder = new ProcessBuilder("java",
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
            
            Process process = builder.start();
            
            if (!config.inheritOutput()) {
                new OutputVoider(process.getInputStream()).start();
            }
            
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                LOGGER.logException("Exception while waiting", e);
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
        
        if (config.logCallParams()) {
            LOGGER.logDebug(params.toArray(new String[0]));
        }
        
        ServerSocket serSock = new ServerSocket(0);
        
        CommThread comm = new CommThread(serSock, config, params);
        comm.start();
        
        runTypeChefProcess(serSock.getLocalPort());
        
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
        
        TypeChefBlock result = comm.getResult();
        
        if (result == null) {
            throw new ExtractorException("Runner didn't return result or exception");
        }
        
        // manually set the location of the result, since Typechef creates a temporary command-line input so we
        // don't get proper filenames for the top-block
        result.setPosition(new TypeChefBlock.Position(file, 1));
        
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
        
        long start = System.currentTimeMillis();
        
        TypeChefBlock block = runTypeChef(file);
        result.addBlock(block);
        
        long duration = System.currentTimeMillis() - start;
        LOGGER.logDebug("TypeChef took " + (duration / 1000) + "s for " + file.getPath());
        
        return result;
    }
    
}
