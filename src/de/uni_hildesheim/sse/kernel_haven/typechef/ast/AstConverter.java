package de.uni_hildesheim.sse.kernel_haven.typechef.ast;

import java.util.Iterator;

import de.fosd.typechef.conditional.Opt;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.parser.c.AST;
import de.fosd.typechef.parser.c.AbstractDeclarator;
import de.fosd.typechef.parser.c.ArrayAccess;
import de.fosd.typechef.parser.c.AsmAttributeSpecifier;
import de.fosd.typechef.parser.c.AsmExpr;
import de.fosd.typechef.parser.c.AssignExpr;
import de.fosd.typechef.parser.c.AtomicAttribute;
import de.fosd.typechef.parser.c.AtomicNamedDeclarator;
import de.fosd.typechef.parser.c.Attribute;
import de.fosd.typechef.parser.c.AttributeSequence;
import de.fosd.typechef.parser.c.AttributeSpecifier;
import de.fosd.typechef.parser.c.BreakStatement;
import de.fosd.typechef.parser.c.CDef;
import de.fosd.typechef.parser.c.CFGStmt;
import de.fosd.typechef.parser.c.CaseStatement;
import de.fosd.typechef.parser.c.CompoundAttribute;
import de.fosd.typechef.parser.c.CompoundDeclaration;
import de.fosd.typechef.parser.c.CompoundStatement;
import de.fosd.typechef.parser.c.Constant;
import de.fosd.typechef.parser.c.ContinueStatement;
import de.fosd.typechef.parser.c.DeclArrayAccess;
import de.fosd.typechef.parser.c.DeclIdentifierList;
import de.fosd.typechef.parser.c.DeclParameterDeclList;
import de.fosd.typechef.parser.c.Declaration;
import de.fosd.typechef.parser.c.DeclarationStatement;
import de.fosd.typechef.parser.c.Declarator;
import de.fosd.typechef.parser.c.DeclaratorAbstrExtension;
import de.fosd.typechef.parser.c.DeclaratorExtension;
import de.fosd.typechef.parser.c.DefaultStatement;
import de.fosd.typechef.parser.c.DoStatement;
import de.fosd.typechef.parser.c.ElifStatement;
import de.fosd.typechef.parser.c.EmptyExternalDef;
import de.fosd.typechef.parser.c.EmptyStatement;
import de.fosd.typechef.parser.c.EnumSpecifier;
import de.fosd.typechef.parser.c.Enumerator;
import de.fosd.typechef.parser.c.Expr;
import de.fosd.typechef.parser.c.ExprList;
import de.fosd.typechef.parser.c.ExprStatement;
import de.fosd.typechef.parser.c.ExternalDef;
import de.fosd.typechef.parser.c.ForStatement;
import de.fosd.typechef.parser.c.FunctionCall;
import de.fosd.typechef.parser.c.FunctionDef;
import de.fosd.typechef.parser.c.GnuAsmExpr;
import de.fosd.typechef.parser.c.GnuAttributeSpecifier;
import de.fosd.typechef.parser.c.GotoStatement;
import de.fosd.typechef.parser.c.Id;
import de.fosd.typechef.parser.c.IfStatement;
import de.fosd.typechef.parser.c.InitDeclarator;
import de.fosd.typechef.parser.c.InitDeclaratorE;
import de.fosd.typechef.parser.c.InitDeclaratorI;
import de.fosd.typechef.parser.c.Initializer;
import de.fosd.typechef.parser.c.InitializerElementLabel;
import de.fosd.typechef.parser.c.LabelStatement;
import de.fosd.typechef.parser.c.LocalLabelDeclaration;
import de.fosd.typechef.parser.c.NAryExpr;
import de.fosd.typechef.parser.c.NArySubExpr;
import de.fosd.typechef.parser.c.NestedFunctionDef;
import de.fosd.typechef.parser.c.NestedNamedDeclarator;
import de.fosd.typechef.parser.c.OffsetofMemberDesignator;
import de.fosd.typechef.parser.c.OldParameterDeclaration;
import de.fosd.typechef.parser.c.OtherSpecifier;
import de.fosd.typechef.parser.c.ParameterDeclaration;
import de.fosd.typechef.parser.c.ParameterDeclarationAD;
import de.fosd.typechef.parser.c.ParameterDeclarationD;
import de.fosd.typechef.parser.c.PlainParameterDeclaration;
import de.fosd.typechef.parser.c.Pointer;
import de.fosd.typechef.parser.c.PointerPostfixSuffix;
import de.fosd.typechef.parser.c.PostfixExpr;
import de.fosd.typechef.parser.c.PostfixSuffix;
import de.fosd.typechef.parser.c.Pragma;
import de.fosd.typechef.parser.c.PrimaryExpr;
import de.fosd.typechef.parser.c.PrimitiveTypeSpecifier;
import de.fosd.typechef.parser.c.ReturnStatement;
import de.fosd.typechef.parser.c.SimplePostfixSuffix;
import de.fosd.typechef.parser.c.Specifier;
import de.fosd.typechef.parser.c.Statement;
import de.fosd.typechef.parser.c.StringLit;
import de.fosd.typechef.parser.c.StructDecl;
import de.fosd.typechef.parser.c.StructDeclaration;
import de.fosd.typechef.parser.c.StructDeclarator;
import de.fosd.typechef.parser.c.StructInitializer;
import de.fosd.typechef.parser.c.StructOrUnionSpecifier;
import de.fosd.typechef.parser.c.SwitchStatement;
import de.fosd.typechef.parser.c.TranslationUnit;
import de.fosd.typechef.parser.c.TypeDefTypeSpecifier;
import de.fosd.typechef.parser.c.TypeName;
import de.fosd.typechef.parser.c.TypeSpecifier;
import de.fosd.typechef.parser.c.TypedefSpecifier;
import de.fosd.typechef.parser.c.TypelessDeclaration;
import de.fosd.typechef.parser.c.UnaryExpr;
import de.fosd.typechef.parser.c.UnaryOpExpr;
import de.fosd.typechef.parser.c.UnsignedSpecifier;
import de.fosd.typechef.parser.c.VarArgs;
import de.fosd.typechef.parser.c.WhileStatement;
import de.uni_hildesheim.sse.kernel_haven.typechef.util.TypeChefPresenceConditionGrammar;
import de.uni_hildesheim.sse.kernel_haven.util.logic.Formula;
import de.uni_hildesheim.sse.kernel_haven.util.logic.True;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.ExpressionFormatException;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.Parser;
import de.uni_hildesheim.sse.kernel_haven.util.logic.parser.VariableCache;

public class AstConverter {
    
    private static final VariableCache CACHE = new VariableCache();
    
    private static final TypeChefPresenceConditionGrammar GRAMMAR = new TypeChefPresenceConditionGrammar(CACHE);
    
    private static final Parser<Formula> PARSER = new Parser<>(GRAMMAR);
    
    public TypeChefBlock convertToFile(TranslationUnit unit) {
        String name = "<unknown>";
        
        if (unit.getFile().isDefined()) {
            name = unit.getFile().get();
        }
        
        TypeChefBlock result = new TypeChefBlock(null, new True(), "File " + name, "");
        
        for (Opt<ExternalDef> def : scalaIterator(unit.defs())) {
            convertExternalDef(result, toFormula(def.condition()), def.entry(), "");
        }
        
        return result;
    }

    private void convertAst(TypeChefBlock parent, Formula condition, AST ast, String relation) {
        if (ast instanceof AbstractDeclarator) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof Attribute) {
            convertAttribute(parent, condition, (Attribute) ast, relation);
        } else if (ast instanceof AttributeSequence) {
            convertAttributeSequence(parent, condition, (AttributeSequence) ast, relation);
        } else if (ast instanceof Declarator) {
            convertDeclarator(parent, condition, (Declarator) ast, relation);
        } else if (ast instanceof DeclaratorExtension) {
            convertDeclarationExtension(parent, condition, (DeclaratorExtension) ast, relation);
        } else if (ast instanceof ElifStatement) {
            convertElifStatement(parent, condition, (ElifStatement) ast, relation);
        } else if (ast instanceof Enumerator) {
            convertEnumerator(parent, condition, (Enumerator) ast, relation);
        } else if (ast instanceof InitDeclarator) {
            convertInitDeclarator(parent, condition, (InitDeclarator) ast, relation);
        } else if (ast instanceof Initializer) {
            convertInitializer(parent, condition, (Initializer) ast, relation);
        } else if (ast instanceof InitializerElementLabel) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof NArySubExpr) {
            convertNArySubExpr(parent, condition, (NArySubExpr) ast, relation);
        } else if (ast instanceof OffsetofMemberDesignator) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof ParameterDeclaration) {
            convertParameterDeclaration(parent, condition, (ParameterDeclaration) ast, relation);
        } else if (ast instanceof Pointer) {
            convertPointer(parent, condition, (Pointer) ast, relation);
        } else if (ast instanceof PostfixSuffix) {
            convertPostfixSuffix(parent, condition, (PostfixSuffix) ast, relation);
        } else if (ast instanceof Specifier) {
            convertSpecifier(parent, condition, (Specifier) ast, relation);
        } else if (ast instanceof Statement) {
            convertStatement(parent, condition, (Statement) ast, relation);
        } else if (ast instanceof StructDecl) {
            convertStructDecl(parent, condition, (StructDecl) ast, relation);
        } else if (ast instanceof StructDeclaration) {
            convertStructDeclaration(parent, condition, (StructDeclaration) ast, relation);
        } else if (ast instanceof TranslationUnit) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof TypeName) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof CDef) {
            new TypeChefBlock(parent, condition, "TODO: " + ast.getClass(), relation); // TODO
        } else if (ast instanceof CFGStmt) {
            convertCfgStmt(parent, condition, (CFGStmt) ast, relation);
        } else if (ast instanceof OldParameterDeclaration) {
            convertOldParameterDeclaration(parent, condition, (OldParameterDeclaration) ast, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown AST element: " + ast.getClass(), relation);
        }
    }
    
    private void convertStatement(TypeChefBlock parent, Formula condition, Statement statement, String relation) {
        if (statement instanceof BreakStatement) {
            convertBreakStatement(parent, condition, (BreakStatement) statement, relation);
        } else if (statement instanceof CaseStatement) {
            convertCaseStatement(parent, condition, (CaseStatement) statement, relation);
        } else if (statement instanceof CompoundDeclaration) {
            convertCompoundDeclaration(parent, condition, (CompoundDeclaration) statement, relation);
        } else if (statement instanceof CompoundStatement) {
            convertCompoundStatement(parent, condition, (CompoundStatement) statement, relation);
        } else if (statement instanceof ContinueStatement) {
            convertContinueStatement(parent, condition, (ContinueStatement) statement, relation);
        } else if (statement instanceof DefaultStatement) {
            convertDefaultStatement(parent, condition, (DefaultStatement) statement, relation);
        } else if (statement instanceof DoStatement) {
            convertDoStatement(parent, condition, (DoStatement) statement, relation);
        } else if (statement instanceof EmptyStatement) {
            convertEmptyStatement(parent, condition, (EmptyStatement) statement, relation);
        } else if (statement instanceof ExprStatement) {
            convertExprStatement(parent, condition, (ExprStatement) statement, relation);
        } else if (statement instanceof ForStatement) {
            convertForStatement(parent, condition, (ForStatement) statement, relation);
        } else if (statement instanceof GotoStatement) {
            convertGotoStatement(parent, condition, (GotoStatement) statement, relation);
        } else if (statement instanceof IfStatement) {
            convertIfStatement(parent, condition, (IfStatement) statement, relation);
        } else if (statement instanceof LabelStatement) {
            convertLabelStatement(parent, condition, (LabelStatement) statement, relation);
        } else if (statement instanceof ReturnStatement) {
            convertReturnStatement(parent, condition, (ReturnStatement) statement, relation);
        } else if (statement instanceof SwitchStatement) {
            convertSwitchStatement(parent, condition, (SwitchStatement) statement, relation);
        } else if (statement instanceof WhileStatement) {
            convertWhileStatement(parent, condition, (WhileStatement) statement, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown Statement: " + statement.getClass(), relation);
        }
    }
    
    private void convertCompoundDeclaration(TypeChefBlock parent, Formula condition, CompoundDeclaration decl, String relation) {
        if (decl instanceof DeclarationStatement) {
            convertDeclarationStatement(parent, condition, (DeclarationStatement) decl, relation);
        } else if (decl instanceof LocalLabelDeclaration) {
            convertLocalLabelDeclaration(parent, condition, (LocalLabelDeclaration) decl, relation);
        } else if (decl instanceof NestedFunctionDef) {
            convetNestedFunctionDef(parent, condition, (NestedFunctionDef) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown CompoundDeclaration: " + decl.getClass(), relation);
        }
    }
    
    private void convertCfgStmt(TypeChefBlock parent, Formula condition, CFGStmt stmt, String relation) {
        if (stmt instanceof BreakStatement) {
            convertBreakStatement(parent, condition, (BreakStatement) stmt, relation);
        } else if (stmt instanceof CaseStatement) {
            convertCaseStatement(parent, condition, (CaseStatement) stmt, relation);
        } else if (stmt instanceof CompoundDeclaration) {
            convertCompoundDeclaration(parent, condition, (CompoundDeclaration) stmt, relation);
        } else if (stmt instanceof ContinueStatement) {
            convertContinueStatement(parent, condition, (ContinueStatement) stmt, relation);
        } else if (stmt instanceof DefaultStatement) {
            convertDefaultStatement(parent, condition, (DefaultStatement) stmt, relation);
        } else if (stmt instanceof EmptyStatement) {
            convertEmptyStatement(parent, condition, (EmptyStatement) stmt, relation);
        } else if (stmt instanceof Expr) {
            convertExpr(parent, condition, (Expr) stmt, relation);
        } else if (stmt instanceof ExprStatement) {
            convertExprStatement(parent, condition, (ExprStatement) stmt, relation);
        } else if (stmt instanceof GotoStatement) {
            convertGotoStatement(parent, condition, (GotoStatement) stmt, relation);
        } else if (stmt instanceof LabelStatement) {
            convertLabelStatement(parent, condition, (LabelStatement) stmt, relation);
        } else if (stmt instanceof ReturnStatement) {
            convertReturnStatement(parent, condition, (ReturnStatement) stmt, relation);
        } else if (stmt instanceof ExternalDef) {
            convertExternalDef(parent, condition, (ExternalDef) stmt, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown CFGStmt: " + stmt.getClass(), relation);
        }
    }
    
    private void convertOldParameterDeclaration(TypeChefBlock parent, Formula condition, OldParameterDeclaration decl, String relation) {
        if (decl instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            new TypeChefBlock(parent, condition, "TODO: " + decl.getClass(), relation); // TODO
        } else {
            new TypeChefBlock(parent, condition, "Unknown OldParameterDeclaration: " + decl.getClass(), relation);
        }
    }
    
    private void convertExternalDef(TypeChefBlock parent, Formula condition, ExternalDef externalDef, String relation) {
        if (externalDef instanceof AsmExpr) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation); // TODO
        } else if (externalDef instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) externalDef, relation);
        } else if (externalDef instanceof EmptyExternalDef) {
            convertEmptyExternalDef(parent, condition, (EmptyExternalDef) externalDef, relation);
        } else if (externalDef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) externalDef, relation);
        } else if (externalDef instanceof Pragma) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation); // TODO
        } else if (externalDef instanceof TypelessDeclaration) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation); // TODO
        } else {
            new TypeChefBlock(parent, condition, "Unknown ExternalDef: " + externalDef.getClass(), relation);
        }
    }
    
    private void convertExpr(TypeChefBlock parent, Formula condition, Expr expr, String relation) {
        if (expr instanceof AssignExpr) {
            convertAssignExpr(parent, condition, (AssignExpr) expr, relation);
        } else if (expr instanceof ExprList) {
            convertExprList(parent, condition, (ExprList) expr, relation);
        } else if (expr instanceof GnuAsmExpr) {
            convertGnuAsmExpr(parent, condition, (GnuAsmExpr) expr, relation);
        } else if (expr instanceof NAryExpr) {
            convertNAryExpr(parent, condition, (NAryExpr) expr, relation);
        } else if (expr instanceof PostfixExpr) {
            convertPostfixExpr(parent, condition, (PostfixExpr) expr, relation);
        } else if (expr instanceof PrimaryExpr) {
            convertPrimaryExpression(parent, condition, (PrimaryExpr) expr, relation);
        } else if (expr instanceof UnaryExpr) {
            convertUnaryExpr(parent, condition, (UnaryExpr) expr, relation);
        } else if (expr instanceof UnaryOpExpr) {
            convertUnaryOpExpr(parent, condition, (UnaryOpExpr) expr, relation);
        } else {
            new TypeChefBlock(parent, condition, "TODO Expr: " + expr.getClass(), relation); // TODO
        }
    }
    
    private void convertPrimaryExpression(TypeChefBlock parent, Formula condition, PrimaryExpr expr, String relation) {
        if (expr instanceof Constant) {
            convertConstant(parent, condition, (Constant) expr, relation);
        } else if (expr instanceof Id) {
            convertId(parent, condition, (Id) expr, relation);
        } else if (expr instanceof StringLit) {
            convertStringLit(parent, condition, (StringLit) expr, relation);
        } else {
            new TypeChefBlock(parent, condition, "TODO PrimaryExpr: " + expr.getClass(), relation); // TODO
        }
    }
    
    private void convertDeclarator(TypeChefBlock parent, Formula condition, Declarator decl, String relation) {
        if (decl instanceof AtomicNamedDeclarator) {
            convertAtomicNamedDeclarator(parent, condition, (AtomicNamedDeclarator) decl, relation);
        } else if (decl instanceof NestedNamedDeclarator) {
            convertNestedNamedDeclarator(parent, condition, (NestedNamedDeclarator) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown Declarator: " + decl.getClass(), relation);
        }
    }
    
    private void convertSpecifier(TypeChefBlock parent, Formula condition, Specifier spec, String relation) {
        if (spec instanceof AttributeSpecifier) {
            convertAttributeSpecifier(parent, condition, (AttributeSpecifier) spec, relation);
        } else if (spec instanceof OtherSpecifier) {
            convertOtherSpecifier(parent, condition, (OtherSpecifier) spec, relation);
        } else if (spec instanceof TypedefSpecifier) {
            convertTypeDefSpecifier(parent, condition, (TypedefSpecifier) spec, relation);
        } else if (spec instanceof TypeSpecifier) {
            convertTypeSpecifier(parent, condition, (TypeSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, "TODO Specifier: " + spec.getClass(), relation); // TODO
        }
    }
    
    private void convertAttributeSpecifier(TypeChefBlock parent, Formula condition, AttributeSpecifier spec, String relation) {
        if (spec instanceof AsmAttributeSpecifier) {
            convertAsmAttributeSpecifier(parent, condition, (AsmAttributeSpecifier) spec, relation);
        } else if (spec instanceof GnuAttributeSpecifier) {
            convertGnuAttributeSpecifier(parent, condition, (GnuAttributeSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown AttributeSpecifier: " + spec.getClass(), relation);
        }
    }
    
    private void convertOtherSpecifier(TypeChefBlock parent, Formula condition, OtherSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, spec.getClass().getSimpleName(), relation);
    }
    
    private void convertTypeSpecifier(TypeChefBlock parent, Formula condition, TypeSpecifier spec, String relation) {
        if (spec instanceof EnumSpecifier) {
            convertEnumSpecifier(parent, condition, (EnumSpecifier) spec, relation);
        } else if (spec instanceof PrimitiveTypeSpecifier) {
            convertPrimitiveTypeSpecifier(parent, condition, (PrimitiveTypeSpecifier) spec, relation);
        } else if (spec instanceof StructOrUnionSpecifier) {
            convertStructOrUnionSpecifier(parent, condition, (StructOrUnionSpecifier) spec, relation);
        } else if (spec instanceof TypeDefTypeSpecifier) {
            convertTypeDefTypeSpecifier(parent, condition, (TypeDefTypeSpecifier) spec, relation);
        } else if (spec instanceof UnsignedSpecifier) {
            convertUnsignedSpecifier(parent, condition, (UnsignedSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, "TODO TypeSpecifier: " + spec.getClass(), relation); // TODO
        }
    }
    
    private void convertInitDeclarator(TypeChefBlock parent, Formula condition, InitDeclarator decl, String relation) {
        if (decl instanceof InitDeclaratorE) {
            convertInitDeclaratorE(parent, condition, (InitDeclaratorE) decl, relation);
        } else if (decl instanceof InitDeclaratorI) {
            convertInitDeclaratorI(parent, condition, (InitDeclaratorI) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown InitDeclarator: " + decl.getClass(), relation);
        }
    }
    
    private void convertDeclarationExtension(TypeChefBlock parent, Formula condition, DeclaratorExtension ext, String relation) {
        if (ext instanceof DeclaratorAbstrExtension) {
            convertDeclaratorAbstrExtension(parent, condition, (DeclaratorAbstrExtension) ext, relation);
        } else if (ext instanceof DeclIdentifierList) {
            convertDeclIdentifierList(parent, condition, (DeclIdentifierList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown DeclaratorExtension: " + ext.getClass(), relation);
        }
    }
    
    private void convertDeclaratorAbstrExtension(TypeChefBlock parent, Formula condition, DeclaratorAbstrExtension ext, String relation) {
        if (ext instanceof DeclArrayAccess) {
            convertDeclArrayAccess(parent, condition, (DeclArrayAccess) ext, relation);
        } else if (ext instanceof DeclParameterDeclList) {
            convertDeclParameterDeclList(parent, condition, (DeclParameterDeclList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown DeclaratorAbstrExtension: " + ext.getClass(), relation);
        }
    }
    
    private void convertPostfixSuffix(TypeChefBlock parent, Formula condition, PostfixSuffix suffix, String relation) {
        if (suffix instanceof ArrayAccess) {
            new TypeChefBlock(parent, condition, "TODO: " + suffix.getClass(), relation); // TODO
        } else if (suffix instanceof FunctionCall) {
            convertFunctionCall(parent, condition, (FunctionCall) suffix, relation);
        } else if (suffix instanceof PointerPostfixSuffix) {
            convertPointerPostfixSuffix(parent, condition, (PointerPostfixSuffix) suffix, relation);
        } else if (suffix instanceof SimplePostfixSuffix) {
            convertSimplePostfixSuffix(parent, condition, (SimplePostfixSuffix) suffix, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown PostfixSuffix: " + suffix.getClass(), relation);
        }
    }
    
    private void convertParameterDeclaration(TypeChefBlock parent, Formula condition, ParameterDeclaration decl, String relation) {
        if (decl instanceof ParameterDeclarationAD) {
            convertParameterDeclarationAD(parent, condition, (ParameterDeclarationAD) decl, relation);
        } else if (decl instanceof ParameterDeclarationD) {
            convertParameterDeclarationD(parent, condition, (ParameterDeclarationD) decl, relation);
        } else if (decl instanceof PlainParameterDeclaration) {
            convertPlainParameterDeclaration(parent, condition, (PlainParameterDeclaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            new TypeChefBlock(parent, condition, "TODO: " + decl.getClass(), relation); // TODO
        } else {
            new TypeChefBlock(parent, condition, "Unkown ParameterDeclaration: " + decl.getClass(), relation);
        }
    }
    
    private void convertAttribute(TypeChefBlock parent, Formula condition, Attribute attr, String relation) {
        if (attr instanceof AtomicAttribute) {
            convertAtomicAttribute(parent, condition, (AtomicAttribute) attr, relation);
        } else if (attr instanceof CompoundAttribute) {
            convertCompoundAttribute(parent, condition, (CompoundAttribute) attr, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown Attribute: " + attr.getClass(), relation);
        }
    }
    
    private void convertStructDecl(TypeChefBlock parent, Formula condition, StructDecl decl, String relation) {
        if (decl instanceof StructDeclarator) {
            convertStructDeclarator(parent, condition, (StructDeclarator) decl, relation);
        } else if (decl instanceof StructInitializer) {
            new TypeChefBlock(parent, condition, "TODO: " + decl.getClass(), relation); // TODO
        } else {
            new TypeChefBlock(parent, condition, "Unkown StructDecl: " + decl.getClass(), relation);
        }
    }
    
    
    //---------------------------------------
    
    private void convertAtomicAttribute(TypeChefBlock parent, Formula condition, AtomicAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AtomicAttribute", relation);

        new TypeChefBlock(block, new True(), attr.n(), "Name");
    }
    
    private void convertAttributeSequence(TypeChefBlock parent, Formula condition, AttributeSequence seq, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AttributeSequence", relation);
        
        for (Opt<Attribute> it : scalaIterator(seq.attributes())) {
            convertAttribute(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertAsmAttributeSpecifier(TypeChefBlock parent, Formula condition, AsmAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AsmAttributeSpecifier", relation);
        
        convertStringLit(block, new True(), spec.stringConst(), "");
    }
    
    private void convertAssignExpr(TypeChefBlock parent, Formula condition, AssignExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AssignExpr", relation);
        
        convertExpr(block, new True(), expr.target(), "Target");
        new TypeChefBlock(block, new True(), expr.operation(), "Operator");
        convertExpr(block, new True(), expr.source(), "Source");
    }
    
    private void convertAtomicNamedDeclarator(TypeChefBlock parent, Formula condition, AtomicNamedDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AtomicNamedDeclarator", relation);
        
        convertId(block, new True(), decl.getId(), "ID");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private void convertBreakStatement(TypeChefBlock parent, Formula condition, BreakStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "BreakStatement", relation);
    }
    
    private void convertCaseStatement(TypeChefBlock parent, Formula condition, CaseStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CaseStatement", relation);
        
        convertExpr(block, new True(), statement.c(), "");
    }
    
    private void convertCompoundAttribute(TypeChefBlock parent, Formula condition, CompoundAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CompoundAttribute", relation);
        
        for (Opt<AttributeSequence> it : scalaIterator(attr.inner())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertCompoundStatement(TypeChefBlock parent, Formula condition, CompoundStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CompoundStatement", relation);
        
        for (Opt<Statement> it : scalaIterator(statement.innerStatements())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertConstant(TypeChefBlock parent, Formula condition, Constant expr, String relation) {
        new TypeChefBlock(parent, condition, "Constant " + expr.value(), relation);
    }
    
    private void convertContinueStatement(TypeChefBlock parent, Formula condition, ContinueStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "ContinueStatement", relation);
    }
    
    private void convertDeclaration(TypeChefBlock parent, Formula condition, Declaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Declaration", relation);
        
        for (Opt<Specifier> it : scalaIterator(decl.declSpecs())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
            convertInitDeclarator(block, toFormula(it.condition()), it.entry(), "InitDeclarator");
        }
    }
    
    private void convertDeclarationStatement(TypeChefBlock parent, Formula condition, DeclarationStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclarationStatement", relation);
        
        convertDeclaration(block, new True(), statement.decl(), "");
    }
    
    private void convertDeclIdentifierList(TypeChefBlock parent, Formula condition, DeclIdentifierList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclIdentifierList", relation);
        
        for (Opt<Id> it : scalaIterator(decl.idList())) {
            convertId(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertDeclArrayAccess(TypeChefBlock parent, Formula condition, DeclArrayAccess decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclArrayAccess", relation);
        
        if (decl.expr().isDefined()) {
            convertExpr(block, new True(), decl.expr().get(), "Expr");
        }
    }
    
    private void convertDeclParameterDeclList(TypeChefBlock parent, Formula condition, DeclParameterDeclList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclParameterDeclList", relation);
        
        for (Opt<ParameterDeclaration> it : scalaIterator(decl.parameterDecls())) {
            convertParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
    }
    
    private void convertDefaultStatement(TypeChefBlock parent, Formula condition, DefaultStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "DefaultStatement", relation);
    }
    
    private void convertDoStatement(TypeChefBlock parent, Formula condition, DoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DoStatement", relation);
        
        convertExpr(block, new True(), statement.expr(), "Expr");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private void convertElifStatement(TypeChefBlock parent, Formula condition, ElifStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ElifStatement", relation);
        
        for (Opt<Expr> it : scalaIterator(statement.condition().toOptList())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "Expr");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.thenBranch().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Then");
        }
    }
    
    private void convertEmptyExternalDef(TypeChefBlock parent, Formula condition, EmptyExternalDef statement, String relation) {
        new TypeChefBlock(parent, condition, "EmptyExternalDef", relation);
    }
    
    private void convertEmptyStatement(TypeChefBlock parent, Formula condition, EmptyStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "EmptyStatement", relation);
    }
    
    private void convertEnumerator(TypeChefBlock parent, Formula condition, Enumerator spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Enumerator", relation);
        
        convertId(block, new True(), spec.id(), "ID");
        
        if (spec.assignment().isDefined()) {
            convertExpr(block, new True(), spec.assignment().get(), "Value");
        }
    }
    
    private void convertEnumSpecifier(TypeChefBlock parent, Formula condition, EnumSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "EnumSpecifier", relation);
        
        if (spec.id().isDefined()) {
            convertId(block, new True(), spec.id().get(), "ID");
        }
        
        if (spec.enumerators().isDefined()) {
            for (Opt<Enumerator> it : scalaIterator(spec.enumerators().get())) {
                convertEnumerator(block, toFormula(it.condition()), it.entry(), "Element");
            }
        }
    }
    
    private void convertExprList(TypeChefBlock parent, Formula condition, ExprList expr, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ExprList", relation);
        
        for (Opt<Expr> it : scalaIterator(expr.exprs())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertExprStatement(TypeChefBlock parent, Formula condition, ExprStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ExprStatement", relation);
        
        convertExpr(block, new True(), statement.expr(), "");
    }
    
    private void convertForStatement(TypeChefBlock parent, Formula condition, ForStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ForStatement", relation);
        
        if (statement.expr1().isDefined()) {
            convertExpr(block, new True(), statement.expr1().get(), "Init");
        }
        if (statement.expr2().isDefined()) {
            convertExpr(block, new True(), statement.expr2().get(), "Condition");
        }
        if (statement.expr3().isDefined()) {
            convertExpr(block, new True(), statement.expr3().get(), "Increment");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private void convertFunctionCall(TypeChefBlock parent, Formula condition, FunctionCall call, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "FunctionCall", relation);
        
        convertExprList(block, new True(), call.params(), "Parameters");
    }
    
    private void convertFunctionDef(TypeChefBlock parent, Formula condition, FunctionDef functionDef, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "FunctionDef", relation);
        
        convertDeclarator(block, new True(), functionDef.declarator(), "Declarator");
        
        for (Opt<OldParameterDeclaration> it : scalaIterator(functionDef.oldStyleParameters())) {
            convertOldParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(functionDef.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, new True(), functionDef.stmt(), "Body");
    }
    
    private void convertGnuAsmExpr(TypeChefBlock parent, Formula condition, GnuAsmExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GnuAsmExpr", relation);
        
        convertStringLit(block, new True(), expr.expr(), "Expression");
        new TypeChefBlock(block, new True(), expr.stuff().toString(), "Stuff?"); // TODO
    }
    
    private void convertGnuAttributeSpecifier(TypeChefBlock parent, Formula condition, GnuAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GnuAttributeSpecifier", relation);
        
        for (Opt<AttributeSequence> it : scalaIterator(spec.attributeList())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertGotoStatement(TypeChefBlock parent, Formula condition, GotoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GotoStatement", relation);
        
        convertExpr(block, new True(), statement.target(), "Target");
    }
    
    private void convertId(TypeChefBlock parent, Formula condition, Id id, String relation) {
        new TypeChefBlock(parent, condition, "Id " + id.name(), relation);
    }
    
    private void convertSimplePostfixSuffix(TypeChefBlock parent, Formula condition, SimplePostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SimplePostfixSuffix", relation);
        
        new TypeChefBlock(block, new True(), suffix.t(), "Operator");
    }
    
    private void convertIfStatement(TypeChefBlock parent, Formula condition, IfStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "IfStatement", relation);
        
        for (Opt<Expr> it : scalaIterator(statement.condition().toOptList())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "Condition");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.thenBranch().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Then");
        }
        
        for (Opt<ElifStatement> it : scalaIterator(statement.elifs())) {
            convertElifStatement(block, toFormula(it.condition()), it.entry(), "Elif");
        }
        
        if (statement.elseBranch().isDefined()) {
            for (Opt<Statement> it : scalaIterator(statement.elseBranch().get().toOptList())) {
                convertStatement(block, toFormula(it.condition()), it.entry(), "Else");
            }
        }
    }
    
    private void convertInitializer(TypeChefBlock parent, Formula condition, Initializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Initializer", relation);
        
        convertExpr(block, new True(), init.expr(), "Expr");
        
        if (init.initializerElementLabel().isDefined()) {
            convertAst(block, new True(), init.initializerElementLabel().get(), "ElementLabel"); // TODO convertInitializerElementLabel()
        }
    }
    
    private void convertInitDeclaratorE(TypeChefBlock parent, Formula condition, InitDeclaratorE decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitDeclaratorE", relation);
        
        convertDeclarator(block, new True(), decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, new True(), decl.e(), "Expression");
        
    }
    
    private void convertInitDeclaratorI(TypeChefBlock parent, Formula condition, InitDeclaratorI decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitDeclaratorI", relation);
        
        convertDeclarator(block, new True(), decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.i().isDefined()) {
            convertInitializer(block, new True(), decl.i().get(), "Initializer");
        }
    }
    
    private void convertLabelStatement(TypeChefBlock parent, Formula condition, LabelStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "LabelStatement", relation);
        
        if (statement.attribute().isDefined()) {
            convertAttributeSpecifier(block, new True(), statement.attribute().get(), "Attribute");
        }
        
        convertId(block, new True(), statement.id(), "Id");
    }
    
    private void convertLocalLabelDeclaration(TypeChefBlock parent, Formula condition, LocalLabelDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "LocalLabelDeclaration", relation);
        
        for (Opt<Id> it : scalaIterator(decl.ids())) {
            convertId(block, toFormula(it.condition()), it.entry(), "Id");
        }
    }
    
    private void convertNAryExpr(TypeChefBlock parent, Formula condition, NAryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NAryExpr", relation);
        
        convertExpr(block, new True(), expr.e(), "");
        
        for (Opt<NArySubExpr> it : scalaIterator(expr.others())) {
            convertNArySubExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertNArySubExpr(TypeChefBlock parent, Formula condition, NArySubExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NAryExpr", relation);
        
        new TypeChefBlock(block, new True(), expr.op(), "Operator");
        convertExpr(block, new True(), expr.e(), "");
    }
    
    private void convetNestedFunctionDef(TypeChefBlock parent, Formula condition, NestedFunctionDef def, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NestedFunctionDef", relation);
        
        convertDeclarator(block, new True(), def.declarator(), "Declarator");
        
        
        for (Opt<Declaration> it : scalaIterator(def.parameters())) {
            convertDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(def.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, new True(), def.stmt(), "Body");
    }
    
    private void convertNestedNamedDeclarator(TypeChefBlock parent, Formula condition, NestedNamedDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NestedNamedDeclarator", relation);
        
        convertId(block, new True(), decl.getId(), "Id");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, new True(), decl.nestedDecl(), "NestedDecl");
    }
    
    private void convertParameterDeclarationAD(TypeChefBlock parent, Formula condition, ParameterDeclarationAD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ParameterDeclarationAD", relation);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAst(block, new True(), decl.decl(), "Declarator"); // TODO: convertAbstractDeclarator()
    }
    
    private void convertParameterDeclarationD(TypeChefBlock parent, Formula condition, ParameterDeclarationD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ParameterDeclarationD", relation);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, new True(), decl.decl(), "Declarator");
    }
    
    private void convertPlainParameterDeclaration(TypeChefBlock parent, Formula condition, PlainParameterDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PlainParameterDeclaration", relation);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertPrimitiveTypeSpecifier(TypeChefBlock parent, Formula condition, PrimitiveTypeSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, spec.getClass().getSimpleName(), relation);
    }
    
    private void convertPointer(TypeChefBlock parent, Formula condition, Pointer pointer, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Pointer", relation);
        
        for (Opt<Specifier> it : scalaIterator(pointer.specifier())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private void convertPointerPostfixSuffix(TypeChefBlock parent, Formula condition, PointerPostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PointerPostfixSuffix", relation);
        
        convertId(block, new True(), suffix.id(), "ID");
        new TypeChefBlock(block, new True(), suffix.kind(), "Kind");
    }
    
    private void convertPostfixExpr(TypeChefBlock parent, Formula condition, PostfixExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PostfixExpr", relation);
        
        convertPostfixSuffix(block, new True(), expr.s(), "Operator");
        convertExpr(block, new True(), expr.p(), "Expr");
    }
    
    private void convertReturnStatement(TypeChefBlock parent, Formula condition, ReturnStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ReturnStatement", relation);
        
        if (statement.expr().isDefined()) {
            convertExpr(block, new True(), statement.expr().get(), "Value");
        }
    }
    
    private void convertStringLit(TypeChefBlock parent, Formula condition, StringLit lit, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StringLit", relation);

        for (Opt<String> it : scalaIterator(lit.name())) {
            new TypeChefBlock(block, toFormula(it.condition()), it.entry(), "Value");
        }
    }
    
    private void convertStructOrUnionSpecifier(TypeChefBlock parent, Formula condition, StructOrUnionSpecifier spec, String relation) {
        String name = (spec.isUnion() ? "Union" : "Struct") + "Specifier";
        TypeChefBlock block = new TypeChefBlock(parent, condition, name, relation);
        
        if (spec.id().isDefined()) {
            convertId(block, new True(), spec.id().get(), "ID");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(spec.attributesBeforeBody())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "BeforeBodyAttribute");
        }
        for (Opt<AttributeSpecifier> it : scalaIterator(spec.attributesAfterBody())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "AfterBodyAttribute");
        }
        
        if (spec.enumerators().isDefined()) {
            for (Opt<StructDeclaration> it : scalaIterator(spec.enumerators().get())) {
                convertStructDeclaration(block, toFormula(it.condition()), it.entry(), "BodyElement");
            }
        }
        
    }
    
    private void convertStructDeclaration(TypeChefBlock parent, Formula condition, StructDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StructDeclaration", relation);
        
        for (Opt<Specifier> it : scalaIterator(decl.qualifierList())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Qualifier");
        }
        
        for (Opt<StructDecl> it : scalaIterator(decl.declaratorList())) {
            convertStructDecl(block, toFormula(it.condition()), it.entry(), "Declarator");
        }
    }
    
    private void convertStructDeclarator(TypeChefBlock parent, Formula condition, StructDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StructDeclarator", relation);
        
        convertDeclarator(block, new True(), decl.decl(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.initializer().isDefined()) {
            convertExpr(block, new True(), decl.initializer().get(), "Initializer");
        }
    }
    
    private void convertSwitchStatement(TypeChefBlock parent, Formula condition, SwitchStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SwitchStatement", relation);
        
        convertExpr(block, new True(), statement.expr(), "Expression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private void convertTypeDefSpecifier(TypeChefBlock parent, Formula condition, TypedefSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, "TypedefSpecifier", relation);
    }
    
    private void convertTypeDefTypeSpecifier(TypeChefBlock parent, Formula condition, TypeDefTypeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TypeDefTypeSpecifier", relation);
        
        convertId(block, new True(), spec.name(), "ID");
    }
    
    private void convertUnaryExpr(TypeChefBlock parent, Formula condition, UnaryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "UnaryExpr", relation);
        
        new TypeChefBlock(block, new True(), expr.kind(), "Operator");
        convertExpr(block, new True(), expr.e(), "Expr");
    }
    
    private void convertUnaryOpExpr(TypeChefBlock parent, Formula condition, UnaryOpExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "UnaryOpExpr", relation);
        
        new TypeChefBlock(block, new True(), expr.kind(), "Operator");
        convertExpr(block, new True(), expr.castExpr(), "Expr");
    }
    
    private void convertUnsignedSpecifier(TypeChefBlock parent, Formula condition, UnsignedSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, "UnsignedSpecifier", relation);
    }
    
    private void convertWhileStatement(TypeChefBlock parent, Formula condition, WhileStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "WhileStatement", relation);
        
        convertExpr(block, new True(), statement.expr(), "Epxression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
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
