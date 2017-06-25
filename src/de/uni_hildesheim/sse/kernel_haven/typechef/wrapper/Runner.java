package de.uni_hildesheim.sse.kernel_haven.typechef.wrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import de.fosd.typechef.LexerToken;
import de.fosd.typechef.VALexer;
import de.fosd.typechef.conditional.Conditional;
import de.fosd.typechef.conditional.One;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.FeatureModel;
import de.fosd.typechef.lexer.LexerException;
import de.fosd.typechef.lexer.LexerFrontend;
import de.fosd.typechef.lexer.LexerFrontend.LexerError;
import de.fosd.typechef.lexer.LexerFrontend.LexerResult;
import de.fosd.typechef.lexer.LexerFrontend.LexerSuccess;
import de.fosd.typechef.options.FrontendOptionsWithConfigFiles;
import de.fosd.typechef.options.OptionException;
import de.fosd.typechef.parser.TokenReader;
import de.fosd.typechef.parser.c.CLexerAdapter;
import de.fosd.typechef.parser.c.CParser;
import de.fosd.typechef.parser.c.CTypeContext;
import de.fosd.typechef.parser.c.ParserMain;
import de.fosd.typechef.parser.c.TranslationUnit;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.AstConverter;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.TypeChefBlock;
import de.uni_hildesheim.sse.kernel_haven.typechef.util.TypeChefPresenceConditionGrammar;
import de.uni_hildesheim.sse.kernel_haven.util.ExtractorException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.True;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.ExpressionFormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.Parser;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.VariableCache;
import scala.Tuple2;

/**
 * Runs Typechef. This class will run in a separate process and communicate to the main process
 * via ObjectStreams (see CommThread in {@link Wrapper}).
 * 
 * @author Adam
 */
public class Runner {

    private Socket socket;
    
    private ObjectOutputStream out;
    
    private ObjectInputStream in;
    
    private boolean parseToAst;
    
    private FrontendOptionsWithConfigFiles config;
    
    private List<String> lexerErrors;
    
    /**
     * Creates a new runner.
     * 
     * @param port The TCP port on which to open a connection to the main process.
     * 
     * @throws IOException If creating the comm streams fails.
     */
    public Runner(int port) throws IOException {
        socket = new Socket("localhost", port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * Executes Typechef. First calls the lexer, then converts the output and sends it back.
     * 
     * @throws IOException If communication with the main process fails.
     */
    public void run() throws IOException {
        try {
             readParameters();
             List<LexerToken> lexerTokens = runLexer();
             
             TypeChefBlock parsed;
             
             if (parseToAst) {
                 parsed = parseToAst(lexerTokens);
             } else {
                 parsed = parseToFlatPcs(lexerTokens);
             }
                 
             sendResult(parsed);
             
        } catch (ExtractorException e) {
            sendException(e);

        } finally {
            close();
        }
    }
    
    /**
     * Reads the TypeChef parameters that the main process sends us.
     * 
     * @throws IOException If communication fails.
     * @throws ExtractorException If the parameters can't be parsed by Typechef.
     */
    @SuppressWarnings("unchecked")
    private void readParameters() throws IOException, ExtractorException {
        
        parseToAst = in.readBoolean();
        
        List<String> params;
        try {
            params = (List<String>) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
        config = new FrontendOptionsWithConfigFiles() {
            @Override
            public boolean isPrintLexingSuccess() {
                return false;
            }
        };
        
        try {
            config.parseOptions(params.toArray(new String[0]));
        } catch (OptionException e) {
            throw new ExtractorException("Invalid parameters passed to typechef", e);
        }
    }
    
    /**
     * Runs the lexer of Typechef. As a side-effect, this fills lexerErrors.
     * 
     * @return The result of the lexer.
     * 
     * @throws ExtractorException If the lexer fails.
     */
    private List<LexerToken> runLexer() throws ExtractorException {
        LexerFrontend lexer = new LexerFrontend();
        Conditional<LexerResult> result;
        try {
            result = lexer.run(new VALexer.LexerFactory() {
                @Override
                public VALexer create(FeatureModel model) {
//                return new XtcPreprocessor(config.getMacroFilter(), model);
                    return new MyXtcPreprocessor(config.getMacroFilter(), model);
                }
            }, config, true);
        } catch (LexerException | IOException e) {
            throw new ExtractorException(e);
        }
        
        scala.collection.immutable.List<Tuple2<FeatureExpr, LexerResult>> list = result.toList();
        
        List<LexerToken> lexerTokens = null;
        lexerErrors = new LinkedList<>();
        
        for (int i = 0; i < list.size(); i++) {
            Tuple2<FeatureExpr, LexerResult> t = list.apply(i);
            if (t._2 instanceof LexerSuccess) {
                lexerTokens = ((LexerSuccess) t._2).getTokens();
                
            } else if (t._2 instanceof LexerError) {
                LexerError err = (LexerError) t._2;
                lexerErrors.add(err.getPositionStr() + " " + err.getMessage());
                
            } else {
                throw new ExtractorException("Unexpected lexer result of type " + t._2.getClass().getName());
            }
        }
        
        if (lexerTokens == null) {
            throw new ExtractorException("Lexer did not return a result, see \"Lexer errors\" in info log for details");
        }
        
        return lexerTokens;
    }
    
    /**
     * Uses the Typechef parser to parse the tokens into an AST. The AST is then converted into our {@link TypeChefBlock} hierarchy.
     * 
     * @param lexerTokens The result of the lexer.
     * @return The parsed and converted AST.
     * 
     * @throws ExtractorException If the parser fails.
     */
    private TypeChefBlock parseToAst(List<LexerToken> lexerTokens) throws ExtractorException {
        System.out.println("parseToAst()");

        LexerSuccess wrapper = new LexerSuccess(lexerTokens);
        TokenReader<de.fosd.typechef.parser.c.CToken, CTypeContext> tokenReader
                = CLexerAdapter.prepareTokens(new One<LexerResult>(wrapper));
        
//        TokenReader<AbstractToken, Object> tokenReader2 = (TokenReader<AbstractToken, Object>) (TokenReader<?, ?>) tokenReader;
//        CParser p = new CParser(null, false);
//        MultiParseResult<TranslationUnit> result2 = p.phrase(p.translationUnit()).apply(tokenReader2, FeatureExprFactory.True());
        
        ParserMain parser = new ParserMain(new CParser(null, false));
        TranslationUnit unit = parser.parserMain(tokenReader, config, null);
        
        TypeChefBlock parsed = null;
        
        if (unit != null) {
            System.out.println("Converting AST...");
            AstConverter converter = new AstConverter();
            parsed = converter.convertToFile(unit);
            
        } else {
            throw new ExtractorException("Parser did not return an AST");
        }
        
        return parsed;
    }
    
    /**
     * Goes through the tokens and creates a flat list of presence conditions. This is much faster than parsing.
     * 
     * @param lexerTokens The result of the lexer.
     * @return A {@link TypeChefBlock} containing a flat list of {@link TypeChefBlock}s with all found presence conditions.
     * 
     * @throws ExtractorException If conversion fails.
     */
    private TypeChefBlock parseToFlatPcs(List<LexerToken> lexerTokens) throws ExtractorException {
        System.out.println("parseToFlatPcs()");
        
        List<Formula> pcs = new LinkedList<>();
        String previous = "";
        
        VariableCache varCache = new VariableCache();
        TypeChefPresenceConditionGrammar grammar = new TypeChefPresenceConditionGrammar(varCache);
        Parser<Formula> parser = new Parser<>(grammar);
        
        for (LexerToken token : lexerTokens) {
            String expr = token.getFeature().toTextExpr();
            
            if (!expr.equals(previous)) {
                previous = expr;
                try {
                    pcs.add(parser.parse(expr));
                } catch (ExpressionFormatException e) {
                    throw new ExtractorException(e);
                }
                varCache.clear();
            }
            
        }
        
        TypeChefBlock parsed = new TypeChefBlock(null, new True(), "File", "");
        
        for (Formula f : pcs) {
            new TypeChefBlock(parsed, f, "PresenceCondition", "");
        }
        
        return parsed;
    }
    
    /**
     * Sends the result to the main process.
     * 
     * @param parsed The result to send.
     */
    private void sendResult(TypeChefBlock parsed) {
        try {
            out.writeObject(parsed);
            out.writeObject(lexerErrors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sends an exception to the main process. Call this instead of {@link #sendResult(TypeChefBlock)} if we
     * can't produce a result.
     * 
     * @param exc The exception to send.
     */
    private void sendException(ExtractorException exc) {
        try {
            out.writeObject(exc);
            out.writeObject(lexerErrors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Close the sockets.
     */
    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point for this process.
     * 
     * @param args Command line parameters. Expects an integer (the TCP port number) in args[0].
     * 
     * @throws IOException If communication with the main process fails.
     */
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("TypeChefRunner");
        int port = Integer.parseInt(args[0]);
        
        Runner runner = new Runner(port);
        
        runner.run();
    }
    
}