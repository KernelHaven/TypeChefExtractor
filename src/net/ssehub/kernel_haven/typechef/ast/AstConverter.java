package net.ssehub.kernel_haven.typechef.ast;

import java.util.Iterator;

import de.fosd.typechef.conditional.Opt;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.parser.c.*;
import net.ssehub.kernel_haven.typechef.util.TypeChefPresenceConditionGrammar;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.True;
import net.ssehub.kernel_haven.util.logic.parser.ExpressionFormatException;
import net.ssehub.kernel_haven.util.logic.parser.Parser;
import net.ssehub.kernel_haven.util.logic.parser.VariableCache;

/**
 * Converts an AST created by Typechef into our own format.
 * 
 * @author Adam
 */
public class AstConverter {
    
    private static final VariableCache CACHE = new VariableCache();
    
    private static final TypeChefPresenceConditionGrammar GRAMMAR = new TypeChefPresenceConditionGrammar(CACHE);
    
    private static final Parser<Formula> PARSER = new Parser<>(GRAMMAR);
    
    /**
     * Converts the given Typechef AST to our own format.
     * 
     * @param unit The AST to convert.
     * 
     * @return The result of the conversion.
     */
    public TypeChefBlock convertToFile(TranslationUnit unit) {
        TypeChefBlock tmp = new TypeChefBlock(null, True.INSTANCE, new LiteralSyntaxElement(""), "");
        convertTranslationUnit(tmp, True.INSTANCE, unit, "");
        
        return (TypeChefBlock) tmp.iterator().next();
    }
    
    // CHECKSTYLE:OFF
    // this file is so ugly... might aswell disable checkstyle

    @SuppressWarnings("unused")
    private static void convertAst(TypeChefBlock parent, Formula condition, AST ast, String relation) {
        if (ast instanceof AbstractDeclarator) {
            convertAbstractDeclarator(parent, condition, (AbstractDeclarator) ast, relation);
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
            convertInitializerElementLabel(parent, condition, (InitializerElementLabel) ast, relation);
        } else if (ast instanceof NArySubExpr) {
            convertNArySubExpr(parent, condition, (NArySubExpr) ast, relation);
        } else if (ast instanceof OffsetofMemberDesignator) {
            convertOffsetofMemberDesignator(parent, condition, (OffsetofMemberDesignator) ast, relation);
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
            convertTranslationUnit(parent, condition, (TranslationUnit) ast, relation);
        } else if (ast instanceof TypeName) {
            convertTypeName(parent, condition, (TypeName) ast, relation);
        } else if (ast instanceof CDef) {
            convertCDef(parent, condition, (CDef) ast, relation);
        } else if (ast instanceof CFGStmt) {
            convertCfgStmt(parent, condition, (CFGStmt) ast, relation);
        } else if (ast instanceof OldParameterDeclaration) {
            convertOldParameterDeclaration(parent, condition, (OldParameterDeclaration) ast, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown AST element: " + ast.getClass()), relation, ast);
        }
    }
    
    private static void convertStatement(TypeChefBlock parent, Formula condition, Statement statement, String relation) {
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
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown Statement: " + statement.getClass()), relation, statement);
        }
    }
    
    private static void convertCompoundDeclaration(TypeChefBlock parent, Formula condition, CompoundDeclaration decl, String relation) {
        if (decl instanceof DeclarationStatement) {
            convertDeclarationStatement(parent, condition, (DeclarationStatement) decl, relation);
        } else if (decl instanceof LocalLabelDeclaration) {
            convertLocalLabelDeclaration(parent, condition, (LocalLabelDeclaration) decl, relation);
        } else if (decl instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown CompoundDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertCfgStmt(TypeChefBlock parent, Formula condition, CFGStmt stmt, String relation) {
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
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown CFGStmt: " + stmt.getClass()), relation, stmt);
        }
    }
    
    private static void convertOldParameterDeclaration(TypeChefBlock parent, Formula condition, OldParameterDeclaration decl, String relation) {
        if (decl instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown OldParameterDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertExternalDef(TypeChefBlock parent, Formula condition, ExternalDef externalDef, String relation) {
        if (externalDef instanceof AsmExpr) {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else if (externalDef instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) externalDef, relation);
        } else if (externalDef instanceof EmptyExternalDef) {
            convertEmptyExternalDef(parent, condition, (EmptyExternalDef) externalDef, relation);
        } else if (externalDef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) externalDef, relation);
        } else if (externalDef instanceof Pragma) {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else if (externalDef instanceof TypelessDeclaration) {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown ExternalDef: " + externalDef.getClass()), relation, externalDef);
        }
    }
    
    private static void convertExpr(TypeChefBlock parent, Formula condition, Expr expr, String relation) {
        if (expr instanceof AlignOfExprT) {
            convertAlignOfExprT(parent, condition, (AlignOfExprT) expr, relation);
        } else if (expr instanceof AlignOfExprU) {
            convertAlignOfExprU(parent, condition, (AlignOfExprU) expr, relation);
        } else if (expr instanceof AssignExpr) {
            convertAssignExpr(parent, condition, (AssignExpr) expr, relation);
        } else if (expr instanceof CastExpr) {
            convertCastExpr(parent, condition, (CastExpr) expr, relation);
        } else if (expr instanceof ConditionalExpr) {
            convertConditionalExpr(parent, condition, (ConditionalExpr) expr, relation);
        } else if (expr instanceof ExprList) {
            convertExprList(parent, condition, (ExprList) expr, relation);
        } else if (expr instanceof GnuAsmExpr) {
            convertGnuAsmExpr(parent, condition, (GnuAsmExpr) expr, relation);
        } else if (expr instanceof LcurlyInitializer) {
            convertLcurlyInitializer(parent, condition, (LcurlyInitializer) expr, relation);
        } else if (expr instanceof NAryExpr) {
            convertNAryExpr(parent, condition, (NAryExpr) expr, relation);
        } else if (expr instanceof PointerCreationExpr) {
            convertPointerCreationExpr(parent, condition, (PointerCreationExpr) expr, relation);
        } else if (expr instanceof PointerDerefExpr) {
            convertPointerDerefExpr(parent, condition, (PointerDerefExpr) expr, relation);
        } else if (expr instanceof PostfixExpr) {
            convertPostfixExpr(parent, condition, (PostfixExpr) expr, relation);
        } else if (expr instanceof PrimaryExpr) {
            convertPrimaryExpression(parent, condition, (PrimaryExpr) expr, relation);
        } else if (expr instanceof RangeExpr) {
            convertRangeExpr(parent, condition, (RangeExpr) expr, relation);
        } else if (expr instanceof SizeOfExprT) {
            convertSizeOfExprT(parent, condition, (SizeOfExprT) expr, relation);
        } else if (expr instanceof SizeOfExprU) {
            convertSizeOfExprU(parent, condition, (SizeOfExprU) expr, relation);
        } else if (expr instanceof UnaryExpr) {
            convertUnaryExpr(parent, condition, (UnaryExpr) expr, relation);
        } else if (expr instanceof UnaryOpExpr) {
            convertUnaryOpExpr(parent, condition, (UnaryOpExpr) expr, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("TODO Expr: " + expr.getClass()), relation, expr); // TODO
        }
    }
    
    private static void convertPrimaryExpression(TypeChefBlock parent, Formula condition, PrimaryExpr expr, String relation) {
        if (expr instanceof BuiltinOffsetof) {
            convertBuiltinOffsetof(parent, condition, (BuiltinOffsetof) expr, relation);
        } else if (expr instanceof BuiltinTypesCompatible) {
            convertBuiltinTypesCompatible(parent, condition, (BuiltinTypesCompatible) expr, relation);
        } else if (expr instanceof CompoundStatementExpr) {
            convertCompoundStatementExpr(parent, condition, (CompoundStatementExpr) expr, relation);
        } else if (expr instanceof Constant) {
            convertConstant(parent, condition, (Constant) expr, relation);
        } else if (expr instanceof Id) {
            convertId(parent, condition, (Id) expr, relation);
        } else if (expr instanceof StringLit) {
            convertStringLit(parent, condition, (StringLit) expr, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("TODO PrimaryExpr: " + expr.getClass()), relation, expr); // TODO
        }
    }
    
    private static void convertDeclarator(TypeChefBlock parent, Formula condition, Declarator decl, String relation) {
        if (decl instanceof AtomicNamedDeclarator) {
            convertAtomicNamedDeclarator(parent, condition, (AtomicNamedDeclarator) decl, relation);
        } else if (decl instanceof NestedNamedDeclarator) {
            convertNestedNamedDeclarator(parent, condition, (NestedNamedDeclarator) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown Declarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertSpecifier(TypeChefBlock parent, Formula condition, Specifier spec, String relation) {
        if (spec instanceof AttributeSpecifier) {
            convertAttributeSpecifier(parent, condition, (AttributeSpecifier) spec, relation);
        } else if (spec instanceof OtherSpecifier) {
            convertOtherSpecifier(parent, condition, (OtherSpecifier) spec, relation);
        } else if (spec instanceof TypedefSpecifier) {
            convertTypeDefSpecifier(parent, condition, (TypedefSpecifier) spec, relation);
        } else if (spec instanceof TypeSpecifier) {
            convertTypeSpecifier(parent, condition, (TypeSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown Specifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertAttributeSpecifier(TypeChefBlock parent, Formula condition, AttributeSpecifier spec, String relation) {
        if (spec instanceof AsmAttributeSpecifier) {
            convertAsmAttributeSpecifier(parent, condition, (AsmAttributeSpecifier) spec, relation);
        } else if (spec instanceof GnuAttributeSpecifier) {
            convertGnuAttributeSpecifier(parent, condition, (GnuAttributeSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unknown AttributeSpecifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertOtherSpecifier(TypeChefBlock parent, Formula condition, OtherSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, new LiteralSyntaxElement(spec.getClass().getSimpleName()), relation, spec);
    }
    
    private static void convertTypeSpecifier(TypeChefBlock parent, Formula condition, TypeSpecifier spec, String relation) {
        if (spec instanceof EnumSpecifier) {
            convertEnumSpecifier(parent, condition, (EnumSpecifier) spec, relation);
        } else if (spec instanceof OtherPrimitiveTypeSpecifier) {
            convertOtherPrimitiveTypeSpecifier(parent, condition, (OtherPrimitiveTypeSpecifier) spec, relation);
        } else if (spec instanceof PrimitiveTypeSpecifier) {
            convertPrimitiveTypeSpecifier(parent, condition, (PrimitiveTypeSpecifier) spec, relation);
        } else if (spec instanceof SignedSpecifier) {
            convertSignedSpecifier(parent, condition, (SignedSpecifier) spec, relation);
        } else if (spec instanceof StructOrUnionSpecifier) {
            convertStructOrUnionSpecifier(parent, condition, (StructOrUnionSpecifier) spec, relation);
        } else if (spec instanceof TypeDefTypeSpecifier) {
            convertTypeDefTypeSpecifier(parent, condition, (TypeDefTypeSpecifier) spec, relation);
        } else if (spec instanceof TypeOfSpecifierT) {
            convertTypeOfSpecifierT(parent, condition, (TypeOfSpecifierT) spec, relation);
        } else if (spec instanceof TypeOfSpecifierU) {
            convertTypeOfSpecifierU(parent, condition, (TypeOfSpecifierU) spec, relation);
        } else if (spec instanceof UnsignedSpecifier) {
            convertUnsignedSpecifier(parent, condition, (UnsignedSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown TypeSpecifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertInitDeclarator(TypeChefBlock parent, Formula condition, InitDeclarator decl, String relation) {
        if (decl instanceof InitDeclaratorE) {
            convertInitDeclaratorE(parent, condition, (InitDeclaratorE) decl, relation);
        } else if (decl instanceof InitDeclaratorI) {
            convertInitDeclaratorI(parent, condition, (InitDeclaratorI) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown InitDeclarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertDeclarationExtension(TypeChefBlock parent, Formula condition, DeclaratorExtension ext, String relation) {
        if (ext instanceof DeclaratorAbstrExtension) {
            convertDeclaratorAbstrExtension(parent, condition, (DeclaratorAbstrExtension) ext, relation);
        } else if (ext instanceof DeclIdentifierList) {
            convertDeclIdentifierList(parent, condition, (DeclIdentifierList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown DeclaratorExtension: " + ext.getClass()), relation, ext);
        }
    }
    
    private static void convertDeclaratorAbstrExtension(TypeChefBlock parent, Formula condition, DeclaratorAbstrExtension ext, String relation) {
        if (ext instanceof DeclArrayAccess) {
            convertDeclArrayAccess(parent, condition, (DeclArrayAccess) ext, relation);
        } else if (ext instanceof DeclParameterDeclList) {
            convertDeclParameterDeclList(parent, condition, (DeclParameterDeclList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown DeclaratorAbstrExtension: " + ext.getClass()), relation, ext);
        }
    }
    
    private static void convertPostfixSuffix(TypeChefBlock parent, Formula condition, PostfixSuffix suffix, String relation) {
        if (suffix instanceof ArrayAccess) {
            convertArrayAccess(parent, condition, (ArrayAccess) suffix, relation);
        } else if (suffix instanceof FunctionCall) {
            convertFunctionCall(parent, condition, (FunctionCall) suffix, relation);
        } else if (suffix instanceof PointerPostfixSuffix) {
            convertPointerPostfixSuffix(parent, condition, (PointerPostfixSuffix) suffix, relation);
        } else if (suffix instanceof SimplePostfixSuffix) {
            convertSimplePostfixSuffix(parent, condition, (SimplePostfixSuffix) suffix, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown PostfixSuffix: " + suffix.getClass()), relation, suffix);
        }
    }
    
    private static void convertParameterDeclaration(TypeChefBlock parent, Formula condition, ParameterDeclaration decl, String relation) {
        if (decl instanceof ParameterDeclarationAD) {
            convertParameterDeclarationAD(parent, condition, (ParameterDeclarationAD) decl, relation);
        } else if (decl instanceof ParameterDeclarationD) {
            convertParameterDeclarationD(parent, condition, (ParameterDeclarationD) decl, relation);
        } else if (decl instanceof PlainParameterDeclaration) {
            convertPlainParameterDeclaration(parent, condition, (PlainParameterDeclaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown ParameterDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertAttribute(TypeChefBlock parent, Formula condition, Attribute attr, String relation) {
        if (attr instanceof AtomicAttribute) {
            convertAtomicAttribute(parent, condition, (AtomicAttribute) attr, relation);
        } else if (attr instanceof CompoundAttribute) {
            convertCompoundAttribute(parent, condition, (CompoundAttribute) attr, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown Attribute: " + attr.getClass()), relation, attr);
        }
    }
    
    private static void convertStructDecl(TypeChefBlock parent, Formula condition, StructDecl decl, String relation) {
        if (decl instanceof StructDeclarator) {
            convertStructDeclarator(parent, condition, (StructDeclarator) decl, relation);
        } else if (decl instanceof StructInitializer) {
            convertStructInitializer(parent, condition, (StructInitializer) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown StructDecl: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertAbstractDeclarator(TypeChefBlock parent, Formula condition, AbstractDeclarator decl, String relation) {
        if (decl instanceof AtomicAbstractDeclarator) {
            convertAtomicAbstractDeclarator(parent, condition, (AtomicAbstractDeclarator) decl, relation);
        } else if (decl instanceof NestedAbstractDeclarator) {
            convertNestedAbstractDeclarator(parent, condition, (NestedAbstractDeclarator) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown AbstractDeclarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertCDef(TypeChefBlock parent, Formula condition, CDef cdef, String relation) {
        if (cdef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) cdef, relation);
        } else if (cdef instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) cdef, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown CDef: " + cdef.getClass()), relation, cdef);
        }
    }
    
    private static void convertOffsetofMemberDesignator(TypeChefBlock parent, Formula condition, OffsetofMemberDesignator des, String relation) {
        if (des instanceof OffsetofMemberDesignatorExpr) {
            convertOffsetofMemberDesignatorExpr(parent, condition, (OffsetofMemberDesignatorExpr) des, relation);
        } else if (des instanceof OffsetofMemberDesignatorID) {
            convertOffsetofMemberDesignatorID(parent, condition, (OffsetofMemberDesignatorID) des, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown OffsetofMemberDesignator: " + des.getClass()), relation, des);
        }
    }
    
    private static void convertInitializerElementLabel(TypeChefBlock parent, Formula condition, InitializerElementLabel lbl, String relation) {
        if (lbl instanceof InitializerArrayDesignator) {
            convertInitializerArrayDesignator(parent, condition, (InitializerArrayDesignator) lbl, relation);
        } else if (lbl instanceof InitializerAssigment) {
            convertInitializerAssigment(parent, condition, (InitializerAssigment) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorC) {
            convertInitializerDesignatorC(parent, condition, (InitializerDesignatorC) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorD) {
            convertInitializerDesignatorD(parent, condition, (InitializerDesignatorD) lbl, relation);
        } else {
            new TypeChefBlock(parent, condition, new ErrorSyntaxElement("Unkown InitializerElementLabel: " + lbl.getClass()), relation, lbl);
        }
    }
    
    //---------------------------------------
    
    private static void convertAlignOfExprT(TypeChefBlock parent, Formula condition, AlignOfExprT expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ALIGN_OF_EXPR_T, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
    }
    
    private static void convertAlignOfExprU(TypeChefBlock parent, Formula condition, AlignOfExprU expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ALIGN_OF_EXPR_U, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertArrayAccess(TypeChefBlock parent, Formula condition, ArrayAccess access, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ARRAY_ACCESS, relation, access);
        
        convertExpr(block, True.INSTANCE, access.expr(), "Expression");
    }
    
    private static void convertAtomicAbstractDeclarator(TypeChefBlock parent, Formula condition, AtomicAbstractDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ATOMIC_ABSTRACT_DECLARATOR, relation, decl);
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorAbstrExtension> it : scalaIterator(decl.extensions())) {
            convertDeclaratorAbstrExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private static void convertAtomicAttribute(TypeChefBlock parent, Formula condition, AtomicAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ATOMIC_ATTRIBUTE, relation, attr);

        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(attr.n()), "Name");
    }
    
    private static void convertAttributeSequence(TypeChefBlock parent, Formula condition, AttributeSequence seq, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ATTRIBUTE_SEQUENCE, relation, seq);
        
        for (Opt<Attribute> it : scalaIterator(seq.attributes())) {
            convertAttribute(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertAsmAttributeSpecifier(TypeChefBlock parent, Formula condition, AsmAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ASM_ATTRIBUTE_SPECIFIER, relation, spec);
        
        convertStringLit(block, True.INSTANCE, spec.stringConst(), "");
    }
    
    private static void convertAssignExpr(TypeChefBlock parent, Formula condition, AssignExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ASSIGN_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.target(), "Target");
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(expr.operation()), "Operator");
        convertExpr(block, True.INSTANCE, expr.source(), "Source");
    }
    
    private static void convertAtomicNamedDeclarator(TypeChefBlock parent, Formula condition, AtomicNamedDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ATOMIC_NAMED_DECLARATOR, relation, decl);
        
        convertId(block, True.INSTANCE, decl.getId(), "ID");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private static void convertBreakStatement(TypeChefBlock parent, Formula condition, BreakStatement statement, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.BREAK_STATEMENT, relation, statement);
    }
    
    private static void convertBuiltinOffsetof(TypeChefBlock parent, Formula condition, BuiltinOffsetof expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.BUILTIN_OFFSETOF, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
        
        for (Opt<OffsetofMemberDesignator> it : scalaIterator(expr.offsetofMemberDesignator())) {
            convertOffsetofMemberDesignator(block, toFormula(it.condition()), it.entry(), "OffsetMemberDesignator");
        }
    }
    
    private static void convertBuiltinTypesCompatible(TypeChefBlock parent, Formula condition, BuiltinTypesCompatible expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.BUILTIN_TYPES_COMPATIBLE, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName1(), "Type1");
        convertTypeName(block, True.INSTANCE, expr.typeName2(), "Type2");
    }
    
    private static void convertCaseStatement(TypeChefBlock parent, Formula condition, CaseStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.CASE_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.c(), "");
    }
    
    private static void convertCastExpr(TypeChefBlock parent, Formula condition, CastExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.CAST_EXPR, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertCompoundAttribute(TypeChefBlock parent, Formula condition, CompoundAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.COMPOUND_ATTRIBUTE, relation, attr);
        
        for (Opt<AttributeSequence> it : scalaIterator(attr.inner())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertCompoundStatement(TypeChefBlock parent, Formula condition, CompoundStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.COMPOUND_STATEMENT, relation, statement);
        
        for (Opt<Statement> it : scalaIterator(statement.innerStatements())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertCompoundStatementExpr(TypeChefBlock parent, Formula condition, CompoundStatementExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.COMPOUND_STATEMENT_EXPR, relation, expr);

        convertCompoundStatement(block, True.INSTANCE, expr.compoundStatement(), "Statement");
    }
    
    private static void convertConditionalExpr(TypeChefBlock parent, Formula condition, ConditionalExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.CONDITIONAL_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.condition(), "Condition");
        
        if (expr.thenExpr().isDefined()) {
            convertExpr(block, True.INSTANCE, expr.thenExpr().get(), "Then");
        }
        
        convertExpr(block, True.INSTANCE, expr.elseExpr(), "Else");
    }
    
    private static void convertConstant(TypeChefBlock parent, Formula condition, Constant expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.CONSTANT, relation, expr);
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(expr.value()), "Value");
    }
    
    private static void convertContinueStatement(TypeChefBlock parent, Formula condition, ContinueStatement statement, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.CONTINUE_STATEMENT, relation, statement);
    }
    
    private static void convertDeclaration(TypeChefBlock parent, Formula condition, Declaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.declSpecs())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
            convertInitDeclarator(block, toFormula(it.condition()), it.entry(), "InitDeclarator");
        }
    }
    
    private static void convertDeclarationStatement(TypeChefBlock parent, Formula condition, DeclarationStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DECLARATION_STATEMENT, relation, statement);
        
        convertDeclaration(block, True.INSTANCE, statement.decl(), "");
    }
    
    private static void convertDeclIdentifierList(TypeChefBlock parent, Formula condition, DeclIdentifierList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DECL_IDENTIFIER_LIST, relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.idList())) {
            convertId(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertDeclArrayAccess(TypeChefBlock parent, Formula condition, DeclArrayAccess decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DECL_ARRAY_ACCESS, relation, decl);
        
        if (decl.expr().isDefined()) {
            convertExpr(block, True.INSTANCE, decl.expr().get(), "Expr");
        }
    }
    
    private static void convertDeclParameterDeclList(TypeChefBlock parent, Formula condition, DeclParameterDeclList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DECL_PARAMETER_DECL_LIST, relation, decl);
        
        for (Opt<ParameterDeclaration> it : scalaIterator(decl.parameterDecls())) {
            convertParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
    }
    
    private static void convertDefaultStatement(TypeChefBlock parent, Formula condition, DefaultStatement statement, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.DEFAULT_STATEMENT, relation, statement);
    }
    
    private static void convertDoStatement(TypeChefBlock parent, Formula condition, DoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.DO_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Expr");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private static void convertElifStatement(TypeChefBlock parent, Formula condition, ElifStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ELIF_STATEMENT, relation, statement);
        
        for (Opt<Expr> it : scalaIterator(statement.condition().toOptList())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "Expr");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.thenBranch().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Then");
        }
    }
    
    private static void convertEmptyExternalDef(TypeChefBlock parent, Formula condition, EmptyExternalDef statement, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.EMPTY_EXTERNAL_DEF, relation, statement);
    }
    
    private static void convertEmptyStatement(TypeChefBlock parent, Formula condition, EmptyStatement statement, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.EMPTY_STATEMENT, relation, statement);
    }
    
    private static void convertEnumerator(TypeChefBlock parent, Formula condition, Enumerator spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ENUMERATOR, relation, spec);
        
        convertId(block, True.INSTANCE, spec.id(), "ID");
        
        if (spec.assignment().isDefined()) {
            convertExpr(block, True.INSTANCE, spec.assignment().get(), "Value");
        }
    }
    
    private static void convertEnumSpecifier(TypeChefBlock parent, Formula condition, EnumSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ENUM_SPECIFIER, relation, spec);
        
        if (spec.id().isDefined()) {
            convertId(block, True.INSTANCE, spec.id().get(), "ID");
        }
        
        if (spec.enumerators().isDefined()) {
            for (Opt<Enumerator> it : scalaIterator(spec.enumerators().get())) {
                convertEnumerator(block, toFormula(it.condition()), it.entry(), "Element");
            }
        }
    }
    
    private static void convertExprList(TypeChefBlock parent, Formula condition, ExprList expr, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, SyntaxElements.EXPR_LIST, relation, expr);
        
        for (Opt<Expr> it : scalaIterator(expr.exprs())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertExprStatement(TypeChefBlock parent, Formula condition, ExprStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, SyntaxElements.EXPR_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "");
    }
    
    private static void convertForStatement(TypeChefBlock parent, Formula condition, ForStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, SyntaxElements.FOR_STATEMENT, relation, statement);
        
        if (statement.expr1().isDefined()) {
            convertExpr(block, True.INSTANCE, statement.expr1().get(), "Init");
        }
        if (statement.expr2().isDefined()) {
            convertExpr(block, True.INSTANCE, statement.expr2().get(), "Condition");
        }
        if (statement.expr3().isDefined()) {
            convertExpr(block, True.INSTANCE, statement.expr3().get(), "Increment");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private static void convertFunctionCall(TypeChefBlock parent, Formula condition, FunctionCall call, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.FUNCTION_CALL, relation, call);
        
        convertExprList(block, True.INSTANCE, call.params(), "Parameters");
    }
    
    private static void convertFunctionDef(TypeChefBlock parent, Formula condition, FunctionDef functionDef, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.FUNCTION_DEF, relation, functionDef);
        
        convertDeclarator(block, True.INSTANCE, functionDef.declarator(), "Declarator");
        
        for (Opt<OldParameterDeclaration> it : scalaIterator(functionDef.oldStyleParameters())) {
            convertOldParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(functionDef.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, True.INSTANCE, functionDef.stmt(), "Body");
    }
    
    private static void convertGnuAsmExpr(TypeChefBlock parent, Formula condition, GnuAsmExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.GNU_ASM_EXPR, relation, expr);
        
        convertStringLit(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertGnuAttributeSpecifier(TypeChefBlock parent, Formula condition, GnuAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.GNU_ATTRIBUTE_SPECIFIER, relation, spec);
        
        for (Opt<AttributeSequence> it : scalaIterator(spec.attributeList())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertGotoStatement(TypeChefBlock parent, Formula condition, GotoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.GOTO_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.target(), "Target");
    }
    
    private static void convertId(TypeChefBlock parent, Formula condition, Id id, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.ID, relation, id);
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(id.name()), "Name");
    }
    
    private static void convertSimplePostfixSuffix(TypeChefBlock parent, Formula condition, SimplePostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.SIMPLE_POSTFIX_SUFFIX, relation, suffix);
        
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(suffix.t()), "Operator");
    }
    
    private static void convertIfStatement(TypeChefBlock parent, Formula condition, IfStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.IF_STATEMENT, relation, statement);
        
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
    
    private static void convertInitializer(TypeChefBlock parent, Formula condition, Initializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INITIALIZER, relation, init);
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expr");
        
        if (init.initializerElementLabel().isDefined()) {
            convertInitializerElementLabel(block, True.INSTANCE, init.initializerElementLabel().get(), "ElementLabel");
        }
    }
    
    private static void convertInitializerArrayDesignator(TypeChefBlock parent, Formula condition, InitializerArrayDesignator init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INITIALIZER_ARRAY_DESIGNATOR, relation, init);
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expression");
    }
    
    private static void convertInitializerAssigment(TypeChefBlock parent, Formula condition, InitializerAssigment init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INITIALIZER_ASSIGMENT, relation, init);
        
        for (Opt<InitializerElementLabel> it : scalaIterator(init.designators())) {
            convertInitializerElementLabel(block, toFormula(it.condition()), it.entry(), "Designator");
        }
    }
    
    private static void convertInitializerDesignatorC(TypeChefBlock parent, Formula condition, InitializerDesignatorC init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INITIALIZER_DESIGNATOR_C, relation, init);

        convertId(block, True.INSTANCE, init.id(), "ID");
    }
    
    private static void convertInitializerDesignatorD(TypeChefBlock parent, Formula condition, InitializerDesignatorD init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INITIALIZER_DESIGNATOR_D, relation, init);
        
        convertId(block, True.INSTANCE, init.id(), "ID");
    }
    
    private static void convertInitDeclaratorE(TypeChefBlock parent, Formula condition, InitDeclaratorE decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INIT_DECLARATOR_E, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, True.INSTANCE, decl.e(), "Expression");
        
    }
    
    private static void convertInitDeclaratorI(TypeChefBlock parent, Formula condition, InitDeclaratorI decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.INIT_DECLARATOR_I, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.i().isDefined()) {
            convertInitializer(block, True.INSTANCE, decl.i().get(), "Initializer");
        }
    }
    
    private static void convertLabelStatement(TypeChefBlock parent, Formula condition, LabelStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.LABEL_STATEMENT, relation, statement);
        
        if (statement.attribute().isDefined()) {
            convertAttributeSpecifier(block, True.INSTANCE, statement.attribute().get(), "Attribute");
        }
        
        convertId(block, True.INSTANCE, statement.id(), "Id");
    }
    
    private static void convertLcurlyInitializer(TypeChefBlock parent, Formula condition, LcurlyInitializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.LCURLY_INITIALIZER, relation, init);
        
        for (Opt<Initializer> it : scalaIterator(init.inits())) {
            convertInitializer(block, toFormula(it.condition()), it.entry(), "Initializer");
        }
    }
    
    private static void convertLocalLabelDeclaration(TypeChefBlock parent, Formula condition, LocalLabelDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.LOCAL_LABEL_DECLARATION, relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.ids())) {
            convertId(block, toFormula(it.condition()), it.entry(), "Id");
        }
    }
    
    private static void convertNAryExpr(TypeChefBlock parent, Formula condition, NAryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.N_ARY_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.e(), "");
        
        for (Opt<NArySubExpr> it : scalaIterator(expr.others())) {
            convertNArySubExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertNArySubExpr(TypeChefBlock parent, Formula condition, NArySubExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.N_ARY_SUB_EXPR, relation, expr);
        
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(expr.op()), "Operator");
        convertExpr(block, True.INSTANCE, expr.e(), "");
    }
    
    private static void convertNestedAbstractDeclarator(TypeChefBlock parent, Formula condition, NestedAbstractDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.NESTED_ABSTRACT_DECLARATOR, relation, decl);
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorAbstrExtension> it : scalaIterator(decl.extensions())) {
            convertDeclaratorAbstrExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAbstractDeclarator(block, True.INSTANCE, decl.nestedDecl(), "NestedDeclarator");
    }
    
    private static void convertNestedFunctionDef(TypeChefBlock parent, Formula condition, NestedFunctionDef def, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.NESTED_FUNCTION_DEF, relation, def);
        
        convertDeclarator(block, True.INSTANCE, def.declarator(), "Declarator");
        
        
        for (Opt<Declaration> it : scalaIterator(def.parameters())) {
            convertDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(def.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, True.INSTANCE, def.stmt(), "Body");
    }
    
    private static void convertNestedNamedDeclarator(TypeChefBlock parent, Formula condition, NestedNamedDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.NESTED_NAMED_DECLARATOR, relation, decl);
        
        convertId(block, True.INSTANCE, decl.getId(), "Id");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, True.INSTANCE, decl.nestedDecl(), "NestedDecl");
    }
    
    private static void convertOffsetofMemberDesignatorExpr(TypeChefBlock parent, Formula condition, OffsetofMemberDesignatorExpr offset, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.OFFSETOF_MEMBER_DESIGNATOR_EXPR, relation, offset);
        
        convertExpr(block, True.INSTANCE, offset.expr(), "Expression");
    }
    
    private static void convertOffsetofMemberDesignatorID(TypeChefBlock parent, Formula condition, OffsetofMemberDesignatorID offset, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.OFFSETOF_MEMBER_DESIGNATOR_ID, relation, offset);
        
        convertId(block, True.INSTANCE, offset.id(), "ID");
    }
    
    private static void convertOtherPrimitiveTypeSpecifier(TypeChefBlock parent, Formula condition, OtherPrimitiveTypeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.OTHER_PRIMITIVE_TYPE_SPECIFIER, relation, spec);
        
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(spec.typeName()), "TypeName");
    }
    
    private static void convertParameterDeclarationAD(TypeChefBlock parent, Formula condition, ParameterDeclarationAD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.PARAMETER_DECLARATION_A_D, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAbstractDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
    }
    
    private static void convertParameterDeclarationD(TypeChefBlock parent, Formula condition, ParameterDeclarationD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.PARAMETER_DECLARATION_D, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
    }
    
    private static void convertPlainParameterDeclaration(TypeChefBlock parent, Formula condition, PlainParameterDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.PLAIN_PARAMETER_DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertPrimitiveTypeSpecifier(TypeChefBlock parent, Formula condition, PrimitiveTypeSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, new LiteralSyntaxElement(spec.getClass().getSimpleName()), relation, spec);
    }
    
    private static void convertPointer(TypeChefBlock parent, Formula condition, Pointer pointer, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.POINTER, relation, pointer);
        
        for (Opt<Specifier> it : scalaIterator(pointer.specifier())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private static void convertPointerCreationExpr(TypeChefBlock parent, Formula condition, PointerCreationExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.POINTER_CREATION_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expression");
    }
    
    private static void convertPointerDerefExpr(TypeChefBlock parent, Formula condition, PointerDerefExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.POINTER_DEREF_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expression");
    }
    
    private static void convertPointerPostfixSuffix(TypeChefBlock parent, Formula condition, PointerPostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.POINTER_POSTFIX_SUFFIX, relation, suffix);
        
        convertId(block, True.INSTANCE, suffix.id(), "ID");
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(suffix.kind()), "Kind");
    }
    
    private static void convertPostfixExpr(TypeChefBlock parent, Formula condition, PostfixExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.POSTFIX_EXPR, relation, expr);
        
        convertPostfixSuffix(block, True.INSTANCE, expr.s(), "Operator");
        convertExpr(block, True.INSTANCE, expr.p(), "Expr");
    }
    
    private static void convertRangeExpr(TypeChefBlock parent, Formula condition, RangeExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.RANGE_EXPR, relation, expr);

        convertExpr(block, True.INSTANCE, expr.from(), "From");
        convertExpr(block, True.INSTANCE, expr.to(), "To");
    }
    
    private static void convertReturnStatement(TypeChefBlock parent, Formula condition, ReturnStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.RETURN_STATEMENT, relation, statement);
        
        if (statement.expr().isDefined()) {
            convertExpr(block, True.INSTANCE, statement.expr().get(), "Value");
        }
    }
    
    private static void convertSignedSpecifier(TypeChefBlock parent, Formula condition, SignedSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.SIGNED_SPECIFIER, relation, spec);
    }
    
    private static void convertSizeOfExprT(TypeChefBlock parent, Formula condition, SizeOfExprT expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.SIZE_OF_EXPR_T, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
    }
    
    private static void convertSizeOfExprU(TypeChefBlock parent, Formula condition, SizeOfExprU expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.SIZE_OF_EXPR_U, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertStringLit(TypeChefBlock parent, Formula condition, StringLit lit, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.STRING_LIT, relation, lit);

        for (Opt<String> it : scalaIterator(lit.name())) {
            new TypeChefBlock(block, toFormula(it.condition()), new LiteralSyntaxElement(it.entry()), "Value");
        }
    }
    
    private static void convertStructOrUnionSpecifier(TypeChefBlock parent, Formula condition, StructOrUnionSpecifier spec, String relation) {
        TypeChefBlock block;
        if (spec.isUnion()) {
            block = new TypeChefBlock(parent, condition, SyntaxElements.UNION_SPECIFIER, relation, spec);
        } else {
            block = new TypeChefBlock(parent, condition, SyntaxElements.STRUCT_SPECIFIER, relation, spec);
        }
        
        if (spec.id().isDefined()) {
            convertId(block, True.INSTANCE, spec.id().get(), "ID");
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
    
    private static void convertStructDeclaration(TypeChefBlock parent, Formula condition, StructDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.STRUCT_DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.qualifierList())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Qualifier");
        }
        
        for (Opt<StructDecl> it : scalaIterator(decl.declaratorList())) {
            convertStructDecl(block, toFormula(it.condition()), it.entry(), "Declarator");
        }
    }
    
    private static void convertStructDeclarator(TypeChefBlock parent, Formula condition, StructDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.STRUCT_DECLARATOR, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.initializer().isDefined()) {
            convertExpr(block, True.INSTANCE, decl.initializer().get(), "Initializer");
        }
    }
    
    private static void convertStructInitializer(TypeChefBlock parent, Formula condition, StructInitializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.STRUCT_INITIALIZER, relation, init);
        
        for (Opt<AttributeSpecifier> it : scalaIterator(init.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expression");
    }
    
    private static void convertSwitchStatement(TypeChefBlock parent, Formula condition, SwitchStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.SWITCH_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Expression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private static void convertTranslationUnit(TypeChefBlock parent, Formula condition, TranslationUnit unit, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.TRANSLATION_UNIT, relation, unit);
        
        for (Opt<ExternalDef> it : scalaIterator(unit.defs())) {
            convertExternalDef(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertTypeDefSpecifier(TypeChefBlock parent, Formula condition, TypedefSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.TYPEDEF_SPECIFIER, relation, spec);
    }
    
    private static void convertTypeDefTypeSpecifier(TypeChefBlock parent, Formula condition, TypeDefTypeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.TYPE_DEF_TYPE_SPECIFIER, relation, spec);
        
        convertId(block, True.INSTANCE, spec.name(), "ID");
    }
    
    private static void convertTypeName(TypeChefBlock parent, Formula condition, TypeName name, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.TYPE_NAME, relation, name);
        
        if (name.decl().isDefined()) {
            convertAbstractDeclarator(block, True.INSTANCE, name.decl().get(), "Declaration");
        }
        
        for (Opt<Specifier> it : scalaIterator(name.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private static void convertTypeOfSpecifierT(TypeChefBlock parent, Formula condition, TypeOfSpecifierT spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.TYPE_OF_SPECIFIER_T, relation, spec);
        
        convertTypeName(block, True.INSTANCE, spec.typeName(), "Type");
    }
    
    private static void convertTypeOfSpecifierU(TypeChefBlock parent, Formula condition, TypeOfSpecifierU spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.TYPE_OF_SPECIFIER_U, relation, spec);
        
        convertExpr(block, True.INSTANCE, spec.expr(), "Expression");
    }
    
    private static void convertUnaryExpr(TypeChefBlock parent, Formula condition, UnaryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.UNARY_EXPR, relation, expr);
        
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(expr.kind()), "Operator");
        convertExpr(block, True.INSTANCE, expr.e(), "Expr");
    }
    
    private static void convertUnaryOpExpr(TypeChefBlock parent, Formula condition, UnaryOpExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.UNARY_OP_EXPR, relation, expr);
        
        new TypeChefBlock(block, True.INSTANCE, new LiteralSyntaxElement(expr.kind()), "Operator");
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expr");
    }
    
    private static void convertUnsignedSpecifier(TypeChefBlock parent, Formula condition, UnsignedSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.UNSIGNED_SPECIFIER, relation, spec);
    }
    
    private static void convertVarArgs(TypeChefBlock parent, Formula condition, VarArgs varargs, String relation) {
        new TypeChefBlock(parent, condition, SyntaxElements.VAR_ARGS, relation, varargs);
    }
    
    private static void convertWhileStatement(TypeChefBlock parent, Formula condition, WhileStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, SyntaxElements.WHILE_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Epxression");
        
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
            return True.INSTANCE;
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
