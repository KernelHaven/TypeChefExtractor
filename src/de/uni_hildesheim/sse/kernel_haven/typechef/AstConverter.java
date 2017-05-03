package de.uni_hildesheim.sse.kernel_haven.typechef;

import java.util.Iterator;
import java.util.List;

import de.fosd.typechef.conditional.Opt;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.parser.c.CompoundStatement;
import de.fosd.typechef.parser.c.DeclarationStatement;
import de.fosd.typechef.parser.c.Declarator;
import de.fosd.typechef.parser.c.ElifStatement;
import de.fosd.typechef.parser.c.Expr;
import de.fosd.typechef.parser.c.ExprStatement;
import de.fosd.typechef.parser.c.ExternalDef;
import de.fosd.typechef.parser.c.FunctionDef;
import de.fosd.typechef.parser.c.InitDeclarator;
import de.fosd.typechef.parser.c.TranslationUnit;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.File;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions.ErrorExpression;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.expressions.Expression;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements.ErrorFileElement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements.FileElement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.file_elements.Function;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.Conditional;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.Declaration;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.ErrorStatement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.ExpressionStatement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.IfStatement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.ReturnStatement;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.Sequence;
import de.uni_hildesheim.sse.kernel_haven.typechef.ast.statements.Statement;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.True;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.ExpressionFormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.Parser;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.VariableCache;
import scala.Tuple2;

public class AstConverter {


    private static final VariableCache CACHE = new VariableCache();
    
    private static final TypeChefPresenceConditionGrammar GRAMMAR = new TypeChefPresenceConditionGrammar(CACHE);
    
    private static final Parser<Formula> PARSER = new Parser<>(GRAMMAR);

    private TranslationUnit unit;
    
    private File result;
    
    public AstConverter(TranslationUnit unit) {
        this.unit = unit;
    }
    
    public File convertToFile() {
        String name = "<unkown>";
        
        if (unit.getFile().isDefined()) {
            name = unit.getFile().get();
        }
        
        result = new File(name);
        
        for (Opt<ExternalDef> def : scalaIterator(unit.defs())) {
            convertExternalDef(def.entry(), def.condition());
        }
        
        return result;
    }
    
    private void convertExternalDef(ExternalDef def, FeatureExpr condition) {
        FileElement element = null;
        
        if (def instanceof FunctionDef) {
            FunctionDef funcDef = (FunctionDef) def;
            // TODO: funcDef.specifiers()
            // TODO: funcDef.getFile()
            // TODO: funcDef.oldStyleParameters()
            
            Declarator decl = funcDef.declarator();
            // TODO: decl.extentions()
            // TODO: decl.pointers()
            
            Function function = new Function(decl.getName(), toFormula(condition));
            
            List<Statement> body = function.getBody().statements();
            for (Opt<de.fosd.typechef.parser.c.Statement> stmt : scalaIterator(funcDef.stmt().innerStatements())) {
                body.add(convertStatement(stmt.entry(), toFormula(stmt.condition())));
            }
            
            element = function;
            
        } else {
            element = new ErrorFileElement("Unkown case in convertExternalDef (" + def.getClass() + ")",
                    toFormula(condition));
        }
        
        result.getElements().add(element);
    }
    
    private Statement convertStatement(de.fosd.typechef.parser.c.Statement stmt, Formula condition) {
        
        if (stmt instanceof DeclarationStatement) {
            de.fosd.typechef.parser.c.Declaration decl = ((DeclarationStatement) stmt).decl();
            
            Declaration result = new Declaration(condition);
            
            for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
                result.addName(new Conditional<String>(toFormula(it.condition()), it.entry().getName()));
            }
           
            
            return result;
            
            
        } else if (stmt instanceof CompoundStatement) {
            CompoundStatement cStmt = (CompoundStatement) stmt;
            Sequence result = new Sequence(condition);
            
            for (Opt<de.fosd.typechef.parser.c.Statement> elem : scalaIterator(cStmt.innerStatements())) {
                result.statements().add(convertStatement(elem.entry(), toFormula(elem.condition())));
            }
            
            return result;
            
            
        } else if (stmt instanceof ExprStatement) {
            ExprStatement exprSt = (ExprStatement) stmt;
            return new ExpressionStatement(condition, convertExpression(exprSt.expr()));
            
            
        } else if (stmt instanceof de.fosd.typechef.parser.c.IfStatement) {
            de.fosd.typechef.parser.c.IfStatement ifStmt = (de.fosd.typechef.parser.c.IfStatement) stmt;
            
            IfStatement result = new IfStatement(condition);
            
            for (Tuple2<FeatureExpr, Expr> it : scalaIterator(ifStmt.condition().toList())) {
                result.addCondition(new Conditional<Expression>(toFormula(it._1), convertExpression(it._2)));
            }
            
            for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                    scalaIterator(ifStmt.thenBranch().toList())) {
                result.addThenBranch(convertStatement(it._2, toFormula(it._1)));
            }
            
            for (Opt<ElifStatement> elif : scalaIterator(ifStmt.elifs())) {
                IfStatement nestedElif = new IfStatement(toFormula(elif.condition()));
                
                for (Tuple2<FeatureExpr, Expr> it : scalaIterator(elif.entry().condition().toList())) {
                    nestedElif.addCondition(new Conditional<Expression>(toFormula(it._1), convertExpression(it._2)));
                }
                
                for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                        scalaIterator(elif.entry().thenBranch().toList())) {
                    nestedElif.addThenBranch(convertStatement(it._2, toFormula(it._1)));
                }
                
                result.addElif(nestedElif);
            }
            
            if (ifStmt.elseBranch().isDefined()) {
                for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                        scalaIterator(ifStmt.elseBranch().get().toList())) {
                    result.addElseBranch(convertStatement(it._2, toFormula(it._1)));
                }
            }
            
            
            return result;
            
        } else if (stmt instanceof de.fosd.typechef.parser.c.ReturnStatement) {
            de.fosd.typechef.parser.c.ReturnStatement ret = (de.fosd.typechef.parser.c.ReturnStatement) stmt;
            
            ReturnStatement result = new ReturnStatement(condition);
            
            if (ret.expr().isDefined()) {
                result.setReturnValue(convertExpression(ret.expr().get()));
            }
            
            return result;
            
            
        } else {
            return new ErrorStatement(condition, "Unkown case in convertStatement (" + stmt.getClass() + ")");
        }
    }
    

    private Expression convertExpression(Expr expr) {
        return new ErrorExpression(expr.getClass().getCanonicalName());
    }
    
    
//------------------------------------------------------------------------------------------------------------//
    
    private static Formula toFormula(FeatureExpr featureExpr) {
        try {
            Formula formula = PARSER.parse(featureExpr.toTextExpr());
            CACHE.clear();
            return formula;
        } catch (ExpressionFormatException e) {
            // TODO
            e.printStackTrace();
            return new True();
        }
    }
    
    private static <T> Iterable<T> scalaIterator(final scala.collection.immutable.List<T> a) {
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {

                    private scala.collection.Iterator<T> it = a.iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public T next() {
                        return it.next();
                    }
                };
            }
        };
        
    }
    
}
