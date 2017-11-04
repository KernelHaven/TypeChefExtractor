package net.ssehub.kernel_haven.typechef.ast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.fosd.typechef.conditional.Opt;
import de.fosd.typechef.error.WithPosition;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.parser.c.*;
import net.ssehub.kernel_haven.code_model.ErrorSyntaxElement;
import net.ssehub.kernel_haven.code_model.ISyntaxElementType;
import net.ssehub.kernel_haven.code_model.LiteralSyntaxElement;
import net.ssehub.kernel_haven.code_model.SyntaxElement;
import net.ssehub.kernel_haven.code_model.SyntaxElementTypes;
import net.ssehub.kernel_haven.typechef.util.TypeChefPresenceConditionGrammar;
import net.ssehub.kernel_haven.util.logic.Conjunction;
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
    
    private static Path sourceTree;
    
    /**
     * Converts the given Typechef AST to our own format. Not thread safe.
     * 
     * @param unit The AST to convert.
     * @param sourceTree The path to the source tree for relativizing filenames.
     * 
     * @return The result of the conversion.
     */
    public SyntaxElement convertToFile(TranslationUnit unit, File sourceTree) {
        try {
            AstConverter.sourceTree = sourceTree.getCanonicalFile().toPath();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        
        SyntaxElement tmp = createSyntaxElement(null, True.INSTANCE, new LiteralSyntaxElement(""), "", null);
        convertTranslationUnit(tmp, True.INSTANCE, unit, "");
        
        return (SyntaxElement) tmp.getNestedElement(0);
    }
    
    // CHECKSTYLE:OFF
    // this file is so ugly... might aswell disable checkstyle

    @SuppressWarnings("unused")
    private static void convertAst(SyntaxElement parent, Formula condition, AST ast, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown AST element: " + ast.getClass()), relation, ast);
        }
    }
    
    private static void convertStatement(SyntaxElement parent, Formula condition, Statement statement, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown Statement: " + statement.getClass()), relation, statement);
        }
    }
    
    private static void convertCompoundDeclaration(SyntaxElement parent, Formula condition, CompoundDeclaration decl, String relation) {
        if (decl instanceof DeclarationStatement) {
            convertDeclarationStatement(parent, condition, (DeclarationStatement) decl, relation);
        } else if (decl instanceof LocalLabelDeclaration) {
            convertLocalLabelDeclaration(parent, condition, (LocalLabelDeclaration) decl, relation);
        } else if (decl instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown CompoundDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertCfgStmt(SyntaxElement parent, Formula condition, CFGStmt stmt, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown CFGStmt: " + stmt.getClass()), relation, stmt);
        }
    }
    
    private static void convertOldParameterDeclaration(SyntaxElement parent, Formula condition, OldParameterDeclaration decl, String relation) {
        if (decl instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown OldParameterDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertExternalDef(SyntaxElement parent, Formula condition, ExternalDef externalDef, String relation) {
        if (externalDef instanceof AsmExpr) {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else if (externalDef instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) externalDef, relation);
        } else if (externalDef instanceof EmptyExternalDef) {
            convertEmptyExternalDef(parent, condition, (EmptyExternalDef) externalDef, relation);
        } else if (externalDef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) externalDef, relation);
        } else if (externalDef instanceof Pragma) {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else if (externalDef instanceof TypelessDeclaration) {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("TODO: " + externalDef.getClass()), relation, externalDef); // TODO
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown ExternalDef: " + externalDef.getClass()), relation, externalDef);
        }
    }
    
    private static void convertExpr(SyntaxElement parent, Formula condition, Expr expr, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("TODO Expr: " + expr.getClass()), relation, expr); // TODO
        }
    }
    
    private static void convertPrimaryExpression(SyntaxElement parent, Formula condition, PrimaryExpr expr, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("TODO PrimaryExpr: " + expr.getClass()), relation, expr); // TODO
        }
    }
    
    private static void convertDeclarator(SyntaxElement parent, Formula condition, Declarator decl, String relation) {
        if (decl instanceof AtomicNamedDeclarator) {
            convertAtomicNamedDeclarator(parent, condition, (AtomicNamedDeclarator) decl, relation);
        } else if (decl instanceof NestedNamedDeclarator) {
            convertNestedNamedDeclarator(parent, condition, (NestedNamedDeclarator) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown Declarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertSpecifier(SyntaxElement parent, Formula condition, Specifier spec, String relation) {
        if (spec instanceof AttributeSpecifier) {
            convertAttributeSpecifier(parent, condition, (AttributeSpecifier) spec, relation);
        } else if (spec instanceof OtherSpecifier) {
            convertOtherSpecifier(parent, condition, (OtherSpecifier) spec, relation);
        } else if (spec instanceof TypedefSpecifier) {
            convertTypeDefSpecifier(parent, condition, (TypedefSpecifier) spec, relation);
        } else if (spec instanceof TypeSpecifier) {
            convertTypeSpecifier(parent, condition, (TypeSpecifier) spec, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown Specifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertAttributeSpecifier(SyntaxElement parent, Formula condition, AttributeSpecifier spec, String relation) {
        if (spec instanceof AsmAttributeSpecifier) {
            convertAsmAttributeSpecifier(parent, condition, (AsmAttributeSpecifier) spec, relation);
        } else if (spec instanceof GnuAttributeSpecifier) {
            convertGnuAttributeSpecifier(parent, condition, (GnuAttributeSpecifier) spec, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown AttributeSpecifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertOtherSpecifier(SyntaxElement parent, Formula condition, OtherSpecifier spec, String relation) {
        createSyntaxElement(parent, condition, new LiteralSyntaxElement(spec.getClass().getSimpleName()), relation, spec);
    }
    
    private static void convertTypeSpecifier(SyntaxElement parent, Formula condition, TypeSpecifier spec, String relation) {
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
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown TypeSpecifier: " + spec.getClass()), relation, spec);
        }
    }
    
    private static void convertInitDeclarator(SyntaxElement parent, Formula condition, InitDeclarator decl, String relation) {
        if (decl instanceof InitDeclaratorE) {
            convertInitDeclaratorE(parent, condition, (InitDeclaratorE) decl, relation);
        } else if (decl instanceof InitDeclaratorI) {
            convertInitDeclaratorI(parent, condition, (InitDeclaratorI) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown InitDeclarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertDeclarationExtension(SyntaxElement parent, Formula condition, DeclaratorExtension ext, String relation) {
        if (ext instanceof DeclaratorAbstrExtension) {
            convertDeclaratorAbstrExtension(parent, condition, (DeclaratorAbstrExtension) ext, relation);
        } else if (ext instanceof DeclIdentifierList) {
            convertDeclIdentifierList(parent, condition, (DeclIdentifierList) ext, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown DeclaratorExtension: " + ext.getClass()), relation, ext);
        }
    }
    
    private static void convertDeclaratorAbstrExtension(SyntaxElement parent, Formula condition, DeclaratorAbstrExtension ext, String relation) {
        if (ext instanceof DeclArrayAccess) {
            convertDeclArrayAccess(parent, condition, (DeclArrayAccess) ext, relation);
        } else if (ext instanceof DeclParameterDeclList) {
            convertDeclParameterDeclList(parent, condition, (DeclParameterDeclList) ext, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown DeclaratorAbstrExtension: " + ext.getClass()), relation, ext);
        }
    }
    
    private static void convertPostfixSuffix(SyntaxElement parent, Formula condition, PostfixSuffix suffix, String relation) {
        if (suffix instanceof ArrayAccess) {
            convertArrayAccess(parent, condition, (ArrayAccess) suffix, relation);
        } else if (suffix instanceof FunctionCall) {
            convertFunctionCall(parent, condition, (FunctionCall) suffix, relation);
        } else if (suffix instanceof PointerPostfixSuffix) {
            convertPointerPostfixSuffix(parent, condition, (PointerPostfixSuffix) suffix, relation);
        } else if (suffix instanceof SimplePostfixSuffix) {
            convertSimplePostfixSuffix(parent, condition, (SimplePostfixSuffix) suffix, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown PostfixSuffix: " + suffix.getClass()), relation, suffix);
        }
    }
    
    private static void convertParameterDeclaration(SyntaxElement parent, Formula condition, ParameterDeclaration decl, String relation) {
        if (decl instanceof ParameterDeclarationAD) {
            convertParameterDeclarationAD(parent, condition, (ParameterDeclarationAD) decl, relation);
        } else if (decl instanceof ParameterDeclarationD) {
            convertParameterDeclarationD(parent, condition, (ParameterDeclarationD) decl, relation);
        } else if (decl instanceof PlainParameterDeclaration) {
            convertPlainParameterDeclaration(parent, condition, (PlainParameterDeclaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown ParameterDeclaration: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertAttribute(SyntaxElement parent, Formula condition, Attribute attr, String relation) {
        if (attr instanceof AtomicAttribute) {
            convertAtomicAttribute(parent, condition, (AtomicAttribute) attr, relation);
        } else if (attr instanceof CompoundAttribute) {
            convertCompoundAttribute(parent, condition, (CompoundAttribute) attr, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown Attribute: " + attr.getClass()), relation, attr);
        }
    }
    
    private static void convertStructDecl(SyntaxElement parent, Formula condition, StructDecl decl, String relation) {
        if (decl instanceof StructDeclarator) {
            convertStructDeclarator(parent, condition, (StructDeclarator) decl, relation);
        } else if (decl instanceof StructInitializer) {
            convertStructInitializer(parent, condition, (StructInitializer) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown StructDecl: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertAbstractDeclarator(SyntaxElement parent, Formula condition, AbstractDeclarator decl, String relation) {
        if (decl instanceof AtomicAbstractDeclarator) {
            convertAtomicAbstractDeclarator(parent, condition, (AtomicAbstractDeclarator) decl, relation);
        } else if (decl instanceof NestedAbstractDeclarator) {
            convertNestedAbstractDeclarator(parent, condition, (NestedAbstractDeclarator) decl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown AbstractDeclarator: " + decl.getClass()), relation, decl);
        }
    }
    
    private static void convertCDef(SyntaxElement parent, Formula condition, CDef cdef, String relation) {
        if (cdef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) cdef, relation);
        } else if (cdef instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) cdef, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown CDef: " + cdef.getClass()), relation, cdef);
        }
    }
    
    private static void convertOffsetofMemberDesignator(SyntaxElement parent, Formula condition, OffsetofMemberDesignator des, String relation) {
        if (des instanceof OffsetofMemberDesignatorExpr) {
            convertOffsetofMemberDesignatorExpr(parent, condition, (OffsetofMemberDesignatorExpr) des, relation);
        } else if (des instanceof OffsetofMemberDesignatorID) {
            convertOffsetofMemberDesignatorID(parent, condition, (OffsetofMemberDesignatorID) des, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown OffsetofMemberDesignator: " + des.getClass()), relation, des);
        }
    }
    
    private static void convertInitializerElementLabel(SyntaxElement parent, Formula condition, InitializerElementLabel lbl, String relation) {
        if (lbl instanceof InitializerArrayDesignator) {
            convertInitializerArrayDesignator(parent, condition, (InitializerArrayDesignator) lbl, relation);
        } else if (lbl instanceof InitializerAssigment) {
            convertInitializerAssigment(parent, condition, (InitializerAssigment) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorC) {
            convertInitializerDesignatorC(parent, condition, (InitializerDesignatorC) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorD) {
            convertInitializerDesignatorD(parent, condition, (InitializerDesignatorD) lbl, relation);
        } else {
            createSyntaxElement(parent, condition, new ErrorSyntaxElement("Unknown InitializerElementLabel: " + lbl.getClass()), relation, lbl);
        }
    }
    
    //---------------------------------------
    
    private static void convertAlignOfExprT(SyntaxElement parent, Formula condition, AlignOfExprT expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ALIGN_OF_EXPR_T, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
    }
    
    private static void convertAlignOfExprU(SyntaxElement parent, Formula condition, AlignOfExprU expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ALIGN_OF_EXPR_U, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertArrayAccess(SyntaxElement parent, Formula condition, ArrayAccess access, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ARRAY_ACCESS, relation, access);
        
        convertExpr(block, True.INSTANCE, access.expr(), "Expression");
    }
    
    private static void convertAtomicAbstractDeclarator(SyntaxElement parent, Formula condition, AtomicAbstractDeclarator decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ATOMIC_ABSTRACT_DECLARATOR, relation, decl);
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorAbstrExtension> it : scalaIterator(decl.extensions())) {
            convertDeclaratorAbstrExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private static void convertAtomicAttribute(SyntaxElement parent, Formula condition, AtomicAttribute attr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ATOMIC_ATTRIBUTE, relation, attr);

        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(attr.n()), "Name", attr);
    }
    
    private static void convertAttributeSequence(SyntaxElement parent, Formula condition, AttributeSequence seq, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ATTRIBUTE_SEQUENCE, relation, seq);
        
        for (Opt<Attribute> it : scalaIterator(seq.attributes())) {
            convertAttribute(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertAsmAttributeSpecifier(SyntaxElement parent, Formula condition, AsmAttributeSpecifier spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ASM_ATTRIBUTE_SPECIFIER, relation, spec);
        
        convertStringLit(block, True.INSTANCE, spec.stringConst(), "");
    }
    
    private static void convertAssignExpr(SyntaxElement parent, Formula condition, AssignExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ASSIGN_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.target(), "Target");
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(expr.operation()), "Operator", expr);
        convertExpr(block, True.INSTANCE, expr.source(), "Source");
    }
    
    private static void convertAtomicNamedDeclarator(SyntaxElement parent, Formula condition, AtomicNamedDeclarator decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ATOMIC_NAMED_DECLARATOR, relation, decl);
        
        convertId(block, True.INSTANCE, decl.getId(), "ID");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private static void convertBreakStatement(SyntaxElement parent, Formula condition, BreakStatement statement, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.BREAK_STATEMENT, relation, statement);
    }
    
    private static void convertBuiltinOffsetof(SyntaxElement parent, Formula condition, BuiltinOffsetof expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.BUILTIN_OFFSETOF, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
        
        for (Opt<OffsetofMemberDesignator> it : scalaIterator(expr.offsetofMemberDesignator())) {
            convertOffsetofMemberDesignator(block, toFormula(it.condition()), it.entry(), "OffsetMemberDesignator");
        }
    }
    
    private static void convertBuiltinTypesCompatible(SyntaxElement parent, Formula condition, BuiltinTypesCompatible expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.BUILTIN_TYPES_COMPATIBLE, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName1(), "Type1");
        convertTypeName(block, True.INSTANCE, expr.typeName2(), "Type2");
    }
    
    private static void convertCaseStatement(SyntaxElement parent, Formula condition, CaseStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.CASE_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.c(), "");
    }
    
    private static void convertCastExpr(SyntaxElement parent, Formula condition, CastExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.CAST_EXPR, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertCompoundAttribute(SyntaxElement parent, Formula condition, CompoundAttribute attr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.COMPOUND_ATTRIBUTE, relation, attr);
        
        for (Opt<AttributeSequence> it : scalaIterator(attr.inner())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertCompoundStatement(SyntaxElement parent, Formula condition, CompoundStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.COMPOUND_STATEMENT, relation, statement);
        
        for (Opt<Statement> it : scalaIterator(statement.innerStatements())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertCompoundStatementExpr(SyntaxElement parent, Formula condition, CompoundStatementExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.COMPOUND_STATEMENT_EXPR, relation, expr);

        convertCompoundStatement(block, True.INSTANCE, expr.compoundStatement(), "Statement");
    }
    
    private static void convertConditionalExpr(SyntaxElement parent, Formula condition, ConditionalExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.CONDITIONAL_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.condition(), "Condition");
        
        if (expr.thenExpr().isDefined()) {
            convertExpr(block, True.INSTANCE, expr.thenExpr().get(), "Then");
        }
        
        convertExpr(block, True.INSTANCE, expr.elseExpr(), "Else");
    }
    
    private static void convertConstant(SyntaxElement parent, Formula condition, Constant expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.CONSTANT, relation, expr);
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(expr.value()), "Value", expr);
    }
    
    private static void convertContinueStatement(SyntaxElement parent, Formula condition, ContinueStatement statement, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.CONTINUE_STATEMENT, relation, statement);
    }
    
    private static void convertDeclaration(SyntaxElement parent, Formula condition, Declaration decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.declSpecs())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
            convertInitDeclarator(block, toFormula(it.condition()), it.entry(), "InitDeclarator");
        }
    }
    
    private static void convertDeclarationStatement(SyntaxElement parent, Formula condition, DeclarationStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DECLARATION_STATEMENT, relation, statement);
        
        convertDeclaration(block, True.INSTANCE, statement.decl(), "");
    }
    
    private static void convertDeclIdentifierList(SyntaxElement parent, Formula condition, DeclIdentifierList decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DECL_IDENTIFIER_LIST, relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.idList())) {
            convertId(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertDeclArrayAccess(SyntaxElement parent, Formula condition, DeclArrayAccess decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DECL_ARRAY_ACCESS, relation, decl);
        
        if (decl.expr().isDefined()) {
            convertExpr(block, True.INSTANCE, decl.expr().get(), "Expr");
        }
    }
    
    private static void convertDeclParameterDeclList(SyntaxElement parent, Formula condition, DeclParameterDeclList decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DECL_PARAMETER_DECL_LIST, relation, decl);
        
        for (Opt<ParameterDeclaration> it : scalaIterator(decl.parameterDecls())) {
            convertParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
    }
    
    private static void convertDefaultStatement(SyntaxElement parent, Formula condition, DefaultStatement statement, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.DEFAULT_STATEMENT, relation, statement);
    }
    
    private static void convertDoStatement(SyntaxElement parent, Formula condition, DoStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.DO_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Expr");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private static void convertElifStatement(SyntaxElement parent, Formula condition, ElifStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ELIF_STATEMENT, relation, statement);
        
        for (Opt<Expr> it : scalaIterator(statement.condition().toOptList())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "Expr");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.thenBranch().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Then");
        }
    }
    
    private static void convertEmptyExternalDef(SyntaxElement parent, Formula condition, EmptyExternalDef statement, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.EMPTY_EXTERNAL_DEF, relation, statement);
    }
    
    private static void convertEmptyStatement(SyntaxElement parent, Formula condition, EmptyStatement statement, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.EMPTY_STATEMENT, relation, statement);
    }
    
    private static void convertEnumerator(SyntaxElement parent, Formula condition, Enumerator spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ENUMERATOR, relation, spec);
        
        convertId(block, True.INSTANCE, spec.id(), "ID");
        
        if (spec.assignment().isDefined()) {
            convertExpr(block, True.INSTANCE, spec.assignment().get(), "Value");
        }
    }
    
    private static void convertEnumSpecifier(SyntaxElement parent, Formula condition, EnumSpecifier spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ENUM_SPECIFIER, relation, spec);
        
        if (spec.id().isDefined()) {
            convertId(block, True.INSTANCE, spec.id().get(), "ID");
        }
        
        if (spec.enumerators().isDefined()) {
            for (Opt<Enumerator> it : scalaIterator(spec.enumerators().get())) {
                convertEnumerator(block, toFormula(it.condition()), it.entry(), "Element");
            }
        }
    }
    
    private static void convertExprList(SyntaxElement parent, Formula condition, ExprList expr, String relation) {
        SyntaxElement block =  createSyntaxElement(parent, condition, SyntaxElementTypes.EXPR_LIST, relation, expr);
        
        for (Opt<Expr> it : scalaIterator(expr.exprs())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertExprStatement(SyntaxElement parent, Formula condition, ExprStatement statement, String relation) {
        SyntaxElement block =  createSyntaxElement(parent, condition, SyntaxElementTypes.EXPR_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "");
    }
    
    private static void convertForStatement(SyntaxElement parent, Formula condition, ForStatement statement, String relation) {
        SyntaxElement block =  createSyntaxElement(parent, condition, SyntaxElementTypes.FOR_STATEMENT, relation, statement);
        
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
    
    private static void convertFunctionCall(SyntaxElement parent, Formula condition, FunctionCall call, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.FUNCTION_CALL, relation, call);
        
        convertExprList(block, True.INSTANCE, call.params(), "Parameters");
    }
    
    private static void convertFunctionDef(SyntaxElement parent, Formula condition, FunctionDef functionDef, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.FUNCTION_DEF, relation, functionDef);
        
        convertDeclarator(block, True.INSTANCE, functionDef.declarator(), "Declarator");
        
        for (Opt<OldParameterDeclaration> it : scalaIterator(functionDef.oldStyleParameters())) {
            convertOldParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(functionDef.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, True.INSTANCE, functionDef.stmt(), "Body");
    }
    
    private static void convertGnuAsmExpr(SyntaxElement parent, Formula condition, GnuAsmExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.GNU_ASM_EXPR, relation, expr);
        
        convertStringLit(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertGnuAttributeSpecifier(SyntaxElement parent, Formula condition, GnuAttributeSpecifier spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.GNU_ATTRIBUTE_SPECIFIER, relation, spec);
        
        for (Opt<AttributeSequence> it : scalaIterator(spec.attributeList())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertGotoStatement(SyntaxElement parent, Formula condition, GotoStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.GOTO_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.target(), "Target");
    }
    
    private static void convertId(SyntaxElement parent, Formula condition, Id id, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.ID, relation, id);
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(id.name()), "Name", id);
    }
    
    private static void convertSimplePostfixSuffix(SyntaxElement parent, Formula condition, SimplePostfixSuffix suffix, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.SIMPLE_POSTFIX_SUFFIX, relation, suffix);
        
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(suffix.t()), "Operator", suffix);
    }
    
    private static void convertIfStatement(SyntaxElement parent, Formula condition, IfStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.IF_STATEMENT, relation, statement);
        
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
    
    private static void convertInitializer(SyntaxElement parent, Formula condition, Initializer init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INITIALIZER, relation, init);
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expr");
        
        if (init.initializerElementLabel().isDefined()) {
            convertInitializerElementLabel(block, True.INSTANCE, init.initializerElementLabel().get(), "ElementLabel");
        }
    }
    
    private static void convertInitializerArrayDesignator(SyntaxElement parent, Formula condition, InitializerArrayDesignator init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INITIALIZER_ARRAY_DESIGNATOR, relation, init);
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expression");
    }
    
    private static void convertInitializerAssigment(SyntaxElement parent, Formula condition, InitializerAssigment init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INITIALIZER_ASSIGMENT, relation, init);
        
        for (Opt<InitializerElementLabel> it : scalaIterator(init.designators())) {
            convertInitializerElementLabel(block, toFormula(it.condition()), it.entry(), "Designator");
        }
    }
    
    private static void convertInitializerDesignatorC(SyntaxElement parent, Formula condition, InitializerDesignatorC init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INITIALIZER_DESIGNATOR_C, relation, init);

        convertId(block, True.INSTANCE, init.id(), "ID");
    }
    
    private static void convertInitializerDesignatorD(SyntaxElement parent, Formula condition, InitializerDesignatorD init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INITIALIZER_DESIGNATOR_D, relation, init);
        
        convertId(block, True.INSTANCE, init.id(), "ID");
    }
    
    private static void convertInitDeclaratorE(SyntaxElement parent, Formula condition, InitDeclaratorE decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INIT_DECLARATOR_E, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, True.INSTANCE, decl.e(), "Expression");
        
    }
    
    private static void convertInitDeclaratorI(SyntaxElement parent, Formula condition, InitDeclaratorI decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.INIT_DECLARATOR_I, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.i().isDefined()) {
            convertInitializer(block, True.INSTANCE, decl.i().get(), "Initializer");
        }
    }
    
    private static void convertLabelStatement(SyntaxElement parent, Formula condition, LabelStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.LABEL_STATEMENT, relation, statement);
        
        if (statement.attribute().isDefined()) {
            convertAttributeSpecifier(block, True.INSTANCE, statement.attribute().get(), "Attribute");
        }
        
        convertId(block, True.INSTANCE, statement.id(), "Id");
    }
    
    private static void convertLcurlyInitializer(SyntaxElement parent, Formula condition, LcurlyInitializer init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.LCURLY_INITIALIZER, relation, init);
        
        for (Opt<Initializer> it : scalaIterator(init.inits())) {
            convertInitializer(block, toFormula(it.condition()), it.entry(), "Initializer");
        }
    }
    
    private static void convertLocalLabelDeclaration(SyntaxElement parent, Formula condition, LocalLabelDeclaration decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.LOCAL_LABEL_DECLARATION, relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.ids())) {
            convertId(block, toFormula(it.condition()), it.entry(), "Id");
        }
    }
    
    private static void convertNAryExpr(SyntaxElement parent, Formula condition, NAryExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.N_ARY_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.e(), "");
        
        for (Opt<NArySubExpr> it : scalaIterator(expr.others())) {
            convertNArySubExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertNArySubExpr(SyntaxElement parent, Formula condition, NArySubExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.N_ARY_SUB_EXPR, relation, expr);
        
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(expr.op()), "Operator", expr);
        convertExpr(block, True.INSTANCE, expr.e(), "");
    }
    
    private static void convertNestedAbstractDeclarator(SyntaxElement parent, Formula condition, NestedAbstractDeclarator decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.NESTED_ABSTRACT_DECLARATOR, relation, decl);
        
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
    
    private static void convertNestedFunctionDef(SyntaxElement parent, Formula condition, NestedFunctionDef def, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.NESTED_FUNCTION_DEF, relation, def);
        
        convertDeclarator(block, True.INSTANCE, def.declarator(), "Declarator");
        
        
        for (Opt<Declaration> it : scalaIterator(def.parameters())) {
            convertDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
        
        for (Opt<Specifier> it : scalaIterator(def.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        convertCompoundStatement(block, True.INSTANCE, def.stmt(), "Body");
    }
    
    private static void convertNestedNamedDeclarator(SyntaxElement parent, Formula condition, NestedNamedDeclarator decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.NESTED_NAMED_DECLARATOR, relation, decl);
        
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
    
    private static void convertOffsetofMemberDesignatorExpr(SyntaxElement parent, Formula condition, OffsetofMemberDesignatorExpr offset, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.OFFSETOF_MEMBER_DESIGNATOR_EXPR, relation, offset);
        
        convertExpr(block, True.INSTANCE, offset.expr(), "Expression");
    }
    
    private static void convertOffsetofMemberDesignatorID(SyntaxElement parent, Formula condition, OffsetofMemberDesignatorID offset, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.OFFSETOF_MEMBER_DESIGNATOR_ID, relation, offset);
        
        convertId(block, True.INSTANCE, offset.id(), "ID");
    }
    
    private static void convertOtherPrimitiveTypeSpecifier(SyntaxElement parent, Formula condition, OtherPrimitiveTypeSpecifier spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.OTHER_PRIMITIVE_TYPE_SPECIFIER, relation, spec);
        
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(spec.typeName()), "TypeName", spec);
    }
    
    private static void convertParameterDeclarationAD(SyntaxElement parent, Formula condition, ParameterDeclarationAD decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.PARAMETER_DECLARATION_A_D, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAbstractDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
    }
    
    private static void convertParameterDeclarationD(SyntaxElement parent, Formula condition, ParameterDeclarationD decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.PARAMETER_DECLARATION_D, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
    }
    
    private static void convertPlainParameterDeclaration(SyntaxElement parent, Formula condition, PlainParameterDeclaration decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.PLAIN_PARAMETER_DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private static void convertPrimitiveTypeSpecifier(SyntaxElement parent, Formula condition, PrimitiveTypeSpecifier spec, String relation) {
        createSyntaxElement(parent, condition, new LiteralSyntaxElement(spec.getClass().getSimpleName()), relation, spec);
    }
    
    private static void convertPointer(SyntaxElement parent, Formula condition, Pointer pointer, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.POINTER, relation, pointer);
        
        for (Opt<Specifier> it : scalaIterator(pointer.specifier())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private static void convertPointerCreationExpr(SyntaxElement parent, Formula condition, PointerCreationExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.POINTER_CREATION_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expression");
    }
    
    private static void convertPointerDerefExpr(SyntaxElement parent, Formula condition, PointerDerefExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.POINTER_DEREF_EXPR, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expression");
    }
    
    private static void convertPointerPostfixSuffix(SyntaxElement parent, Formula condition, PointerPostfixSuffix suffix, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.POINTER_POSTFIX_SUFFIX, relation, suffix);
        
        convertId(block, True.INSTANCE, suffix.id(), "ID");
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(suffix.kind()), "Kind", suffix);
    }
    
    private static void convertPostfixExpr(SyntaxElement parent, Formula condition, PostfixExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.POSTFIX_EXPR, relation, expr);
        
        convertPostfixSuffix(block, True.INSTANCE, expr.s(), "Operator");
        convertExpr(block, True.INSTANCE, expr.p(), "Expr");
    }
    
    private static void convertRangeExpr(SyntaxElement parent, Formula condition, RangeExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.RANGE_EXPR, relation, expr);

        convertExpr(block, True.INSTANCE, expr.from(), "From");
        convertExpr(block, True.INSTANCE, expr.to(), "To");
    }
    
    private static void convertReturnStatement(SyntaxElement parent, Formula condition, ReturnStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.RETURN_STATEMENT, relation, statement);
        
        if (statement.expr().isDefined()) {
            convertExpr(block, True.INSTANCE, statement.expr().get(), "Value");
        }
    }
    
    private static void convertSignedSpecifier(SyntaxElement parent, Formula condition, SignedSpecifier spec, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.SIGNED_SPECIFIER, relation, spec);
    }
    
    private static void convertSizeOfExprT(SyntaxElement parent, Formula condition, SizeOfExprT expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.SIZE_OF_EXPR_T, relation, expr);
        
        convertTypeName(block, True.INSTANCE, expr.typeName(), "Type");
    }
    
    private static void convertSizeOfExprU(SyntaxElement parent, Formula condition, SizeOfExprU expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.SIZE_OF_EXPR_U, relation, expr);
        
        convertExpr(block, True.INSTANCE, expr.expr(), "Expression");
    }
    
    private static void convertStringLit(SyntaxElement parent, Formula condition, StringLit lit, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.STRING_LIT, relation, lit);

        for (Opt<String> it : scalaIterator(lit.name())) {
            createSyntaxElement(block, toFormula(it.condition()), new LiteralSyntaxElement(it.entry()), "Value", lit);
        }
    }
    
    private static void convertStructOrUnionSpecifier(SyntaxElement parent, Formula condition, StructOrUnionSpecifier spec, String relation) {
        SyntaxElement block;
        if (spec.isUnion()) {
            block = createSyntaxElement(parent, condition, SyntaxElementTypes.UNION_SPECIFIER, relation, spec);
        } else {
            block = createSyntaxElement(parent, condition, SyntaxElementTypes.STRUCT_SPECIFIER, relation, spec);
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
    
    private static void convertStructDeclaration(SyntaxElement parent, Formula condition, StructDeclaration decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.STRUCT_DECLARATION, relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.qualifierList())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Qualifier");
        }
        
        for (Opt<StructDecl> it : scalaIterator(decl.declaratorList())) {
            convertStructDecl(block, toFormula(it.condition()), it.entry(), "Declarator");
        }
    }
    
    private static void convertStructDeclarator(SyntaxElement parent, Formula condition, StructDeclarator decl, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.STRUCT_DECLARATOR, relation, decl);
        
        convertDeclarator(block, True.INSTANCE, decl.decl(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.initializer().isDefined()) {
            convertExpr(block, True.INSTANCE, decl.initializer().get(), "Initializer");
        }
    }
    
    private static void convertStructInitializer(SyntaxElement parent, Formula condition, StructInitializer init, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.STRUCT_INITIALIZER, relation, init);
        
        for (Opt<AttributeSpecifier> it : scalaIterator(init.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, True.INSTANCE, init.expr(), "Expression");
    }
    
    private static void convertSwitchStatement(SyntaxElement parent, Formula condition, SwitchStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.SWITCH_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Expression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private static void convertTranslationUnit(SyntaxElement parent, Formula condition, TranslationUnit unit, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.TRANSLATION_UNIT, relation, unit);
        
        for (Opt<ExternalDef> it : scalaIterator(unit.defs())) {
            convertExternalDef(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private static void convertTypeDefSpecifier(SyntaxElement parent, Formula condition, TypedefSpecifier spec, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.TYPEDEF_SPECIFIER, relation, spec);
    }
    
    private static void convertTypeDefTypeSpecifier(SyntaxElement parent, Formula condition, TypeDefTypeSpecifier spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.TYPE_DEF_TYPE_SPECIFIER, relation, spec);
        
        convertId(block, True.INSTANCE, spec.name(), "ID");
    }
    
    private static void convertTypeName(SyntaxElement parent, Formula condition, TypeName name, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.TYPE_NAME, relation, name);
        
        if (name.decl().isDefined()) {
            convertAbstractDeclarator(block, True.INSTANCE, name.decl().get(), "Declaration");
        }
        
        for (Opt<Specifier> it : scalaIterator(name.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private static void convertTypeOfSpecifierT(SyntaxElement parent, Formula condition, TypeOfSpecifierT spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.TYPE_OF_SPECIFIER_T, relation, spec);
        
        convertTypeName(block, True.INSTANCE, spec.typeName(), "Type");
    }
    
    private static void convertTypeOfSpecifierU(SyntaxElement parent, Formula condition, TypeOfSpecifierU spec, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.TYPE_OF_SPECIFIER_U, relation, spec);
        
        convertExpr(block, True.INSTANCE, spec.expr(), "Expression");
    }
    
    private static void convertUnaryExpr(SyntaxElement parent, Formula condition, UnaryExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.UNARY_EXPR, relation, expr);
        
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(expr.kind()), "Operator", expr);
        convertExpr(block, True.INSTANCE, expr.e(), "Expr");
    }
    
    private static void convertUnaryOpExpr(SyntaxElement parent, Formula condition, UnaryOpExpr expr, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.UNARY_OP_EXPR, relation, expr);
        
        createSyntaxElement(block, True.INSTANCE, new LiteralSyntaxElement(expr.kind()), "Operator", expr);
        convertExpr(block, True.INSTANCE, expr.castExpr(), "Expr");
    }
    
    private static void convertUnsignedSpecifier(SyntaxElement parent, Formula condition, UnsignedSpecifier spec, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.UNSIGNED_SPECIFIER, relation, spec);
    }
    
    private static void convertVarArgs(SyntaxElement parent, Formula condition, VarArgs varargs, String relation) {
        createSyntaxElement(parent, condition, SyntaxElementTypes.VAR_ARGS, relation, varargs);
    }
    
    private static void convertWhileStatement(SyntaxElement parent, Formula condition, WhileStatement statement, String relation) {
        SyntaxElement block = createSyntaxElement(parent, condition, SyntaxElementTypes.WHILE_STATEMENT, relation, statement);
        
        convertExpr(block, True.INSTANCE, statement.expr(), "Epxression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    // -----------------------------------------------------------
    
    private static SyntaxElement createSyntaxElement(SyntaxElement parent, Formula condition,
            ISyntaxElementType type, String relation, WithPosition position) {
        
        Formula pc = condition;
        
        if (parent != null) {
            Formula parentCondition = parent.getPresenceCondition();
            if (parentCondition != True.INSTANCE && !pc.equals(parentCondition)) {
                if (condition != True.INSTANCE) {
                    // Avoid repeating of parent conditions 
                    pc = new Conjunction(parentCondition, pc);
                } else {
                    // Avoid conjunction of a TRUE statement to a non-empty pc
                    pc = parentCondition;
                }
            }
        }
        
        SyntaxElement result = new SyntaxElement(type, condition, pc);
        
        if (parent != null) {
            parent.addNestedElement(result, relation);
        }
        if (position != null) {
            result.setLineStart(position.getPositionFrom().getLine());
            result.setLineEnd(position.getPositionTo().getLine());
            String filename = position.getPositionFrom().getFile();
            if (filename == null) {
                filename = "<unknown>";
            } else if (sourceTree != null) {
                try {
                    Path file = new File(filename).getCanonicalFile().toPath();
                    filename = sourceTree.relativize(file).toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            result.setSourceFile(new File(filename)); // TODO: cache
        }
        
        return result;
    }
    
    private static final VariableCache CACHE = new VariableCache();
    
    private static final TypeChefPresenceConditionGrammar GRAMMAR = new TypeChefPresenceConditionGrammar(CACHE);
    
    private static final Parser<Formula> PARSER = new Parser<>(GRAMMAR);
    
    private static final Map<String, Formula> formulaCache = new HashMap<>(15000);
    
    private static Formula toFormula(FeatureExpr featureExpr) {
        String text = featureExpr.toTextExpr();
        Formula result = formulaCache.get(text);
        if (result == null) {
            try {
                result = PARSER.parse(text);
                // TODO: shorten here?
                formulaCache.put(text, result);
            } catch (ExpressionFormatException e) {
                // TODO
                e.printStackTrace();
                return True.INSTANCE;
            }
        }
        return result;
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
