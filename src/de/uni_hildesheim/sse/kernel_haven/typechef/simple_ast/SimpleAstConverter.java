package de.uni_hildesheim.sse.kernel_haven.typechef.simple_ast;

import java.util.Iterator;

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
import de.fosd.typechef.parser.c.Specifier;
import de.fosd.typechef.parser.c.TranslationUnit;
import de.uni_hildesheim.sse.kernel_haven.typechef.TypeChefPresenceConditionGrammar;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.True;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.ExpressionFormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.Parser;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.VariableCache;
import scala.Tuple2;

public class SimpleAstConverter {

private static final VariableCache CACHE = new VariableCache();
    
    private static final TypeChefPresenceConditionGrammar GRAMMAR = new TypeChefPresenceConditionGrammar(CACHE);
    
    private static final Parser<Formula> PARSER = new Parser<>(GRAMMAR);

    public SimpleAstConverter() {
    }
    
    public TypeChefBlock convertToFile(TranslationUnit unit) {
        String name = "<unkown>";
        
        if (unit.getFile().isDefined()) {
            name = unit.getFile().get();
        }
        
        TypeChefBlock result = new TypeChefBlock(null, new True(), "File " + name);
        
        for (Opt<ExternalDef> def : scalaIterator(unit.defs())) {
            convertExternalDef(result, def.entry(), toFormula(def.condition()));
        }
        
        return result;
    }
    
    private void convertExternalDef(TypeChefBlock parent, ExternalDef def, Formula condition) {
        if (def instanceof FunctionDef) {
            FunctionDef funcDef = (FunctionDef) def;
//             TODO: funcDef.specifiers()
            // TODO: funcDef.getFile()
            // TODO: funcDef.oldStyleParameters()
            
            Declarator decl = funcDef.declarator();
            // TODO: decl.extentions()
            // TODO: decl.pointers()
            
            TypeChefBlock func = new TypeChefBlock(parent, condition, "Function " + decl.getName());
            
            for (Opt<de.fosd.typechef.parser.c.Statement> stmt : scalaIterator(funcDef.stmt().innerStatements())) {
                convertStatement(func, stmt.entry(), toFormula(stmt.condition()));
            }
            
        } else {
            new TypeChefBlock(parent, condition, "==TODO== " + def.getClass());
        }
        
    }
    
    private void convertStatement(TypeChefBlock parent, de.fosd.typechef.parser.c.Statement stmt, Formula condition) {
        
        if (stmt instanceof DeclarationStatement) {
            de.fosd.typechef.parser.c.Declaration decl = ((DeclarationStatement) stmt).decl();
            
            TypeChefBlock result = new TypeChefBlock(parent, condition, "Declaration");
            
            for (Opt<Specifier> it : scalaIterator(decl.declSpecs())) {
                new TypeChefBlock(result, toFormula(it.condition()), "Specifier " + it.entry().toString());
            }
            
            for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
                new TypeChefBlock(result, toFormula(it.condition()), "==TODO== InitDeclarator " + it.entry());
            }
            
            
        } else if (stmt instanceof CompoundStatement) {
            CompoundStatement cStmt = (CompoundStatement) stmt;
            
            TypeChefBlock result = new TypeChefBlock(parent, condition, "Sequence");
            
            for (Opt<de.fosd.typechef.parser.c.Statement> elem : scalaIterator(cStmt.innerStatements())) {
                convertStatement(result, elem.entry(), toFormula(elem.condition()));
            }
            
            
        } else if (stmt instanceof ExprStatement) {
            ExprStatement exprSt = (ExprStatement) stmt;
            new TypeChefBlock(parent, condition, "ExprStatement " + exprSt.toString());
            
            
        } else if (stmt instanceof de.fosd.typechef.parser.c.IfStatement) {
            de.fosd.typechef.parser.c.IfStatement ifStmt = (de.fosd.typechef.parser.c.IfStatement) stmt;
            
            TypeChefBlock result = new TypeChefBlock(parent, condition, "If");
            
            // condition
            for (Tuple2<FeatureExpr, Expr> it : scalaIterator(ifStmt.condition().toList())) {
                new TypeChefBlock(result, toFormula(it._1), "Condition " + it._2.toString());
            }
            
            // then branches
            for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                    scalaIterator(ifStmt.thenBranch().toList())) {
                
                TypeChefBlock then = new TypeChefBlock(result, toFormula(it._1), "Then");
                convertStatement(then, it._2, new True());
            }
            
            // elif branches
            for (Opt<ElifStatement> elif : scalaIterator(ifStmt.elifs())) {
                TypeChefBlock nestedElif = new TypeChefBlock(result, toFormula(elif.condition()), "Else-If");
                
                
                for (Tuple2<FeatureExpr, Expr> it : scalaIterator(elif.entry().condition().toList())) {
                    new TypeChefBlock(nestedElif, toFormula(it._1), "Condition " + it._2.toString());
                }
                
                for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                        scalaIterator(elif.entry().thenBranch().toList())) {
                    
                    TypeChefBlock then = new TypeChefBlock(nestedElif, toFormula(it._1), "Then");
                    convertStatement(then, it._2, new True());
                }
            }
            
            // else branch
            if (ifStmt.elseBranch().isDefined()) {
                for (Tuple2<FeatureExpr, de.fosd.typechef.parser.c.Statement> it :
                        scalaIterator(ifStmt.elseBranch().get().toList())) {
                    
                    TypeChefBlock elze = new TypeChefBlock(result, toFormula(it._1), "Else");
                    convertStatement(elze, it._2, new True());
                }
            }
            
            
        } else if (stmt instanceof de.fosd.typechef.parser.c.ReturnStatement) {
            de.fosd.typechef.parser.c.ReturnStatement ret = (de.fosd.typechef.parser.c.ReturnStatement) stmt;
            
            String expr = "";
            if (ret.expr().isDefined()) {
                expr = " " + ret.expr().get().toString();
            }
            
            new TypeChefBlock(parent, condition, "Return" + expr);
            
        } else {
            new TypeChefBlock(parent, condition, "==TODO== " + stmt.getClass());
        }
    }
    
    // -----------------------------------------------------------
    
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
