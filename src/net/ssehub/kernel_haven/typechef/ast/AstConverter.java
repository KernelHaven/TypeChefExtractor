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
        TypeChefBlock tmp = new TypeChefBlock(null, new True(), "", "");
        convertTranslationUnit(tmp, new True(), unit, "");
        
        return (TypeChefBlock) tmp.iterator().next();
    }
    
    // CHECKSTYLE:OFF
    // this file is so ugly... might aswell disable checkstyle

    @SuppressWarnings("unused")
    private void convertAst(TypeChefBlock parent, Formula condition, AST ast, String relation) {
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
            new TypeChefBlock(parent, condition, "Unknown AST element: " + ast.getClass(), relation, ast);
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
            new TypeChefBlock(parent, condition, "Unknown Statement: " + statement.getClass(), relation, statement);
        }
    }
    
    private void convertCompoundDeclaration(TypeChefBlock parent, Formula condition, CompoundDeclaration decl, String relation) {
        if (decl instanceof DeclarationStatement) {
            convertDeclarationStatement(parent, condition, (DeclarationStatement) decl, relation);
        } else if (decl instanceof LocalLabelDeclaration) {
            convertLocalLabelDeclaration(parent, condition, (LocalLabelDeclaration) decl, relation);
        } else if (decl instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown CompoundDeclaration: " + decl.getClass(), relation, decl);
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
            new TypeChefBlock(parent, condition, "Unknown CFGStmt: " + stmt.getClass(), relation, stmt);
        }
    }
    
    private void convertOldParameterDeclaration(TypeChefBlock parent, Formula condition, OldParameterDeclaration decl, String relation) {
        if (decl instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) decl, relation);
        } else if (decl instanceof VarArgs) {
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown OldParameterDeclaration: " + decl.getClass(), relation, decl);
        }
    }
    
    private void convertExternalDef(TypeChefBlock parent, Formula condition, ExternalDef externalDef, String relation) {
        if (externalDef instanceof AsmExpr) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation, externalDef); // TODO
        } else if (externalDef instanceof Declaration) {
            convertDeclaration(parent, condition, (Declaration) externalDef, relation);
        } else if (externalDef instanceof EmptyExternalDef) {
            convertEmptyExternalDef(parent, condition, (EmptyExternalDef) externalDef, relation);
        } else if (externalDef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) externalDef, relation);
        } else if (externalDef instanceof Pragma) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation, externalDef); // TODO
        } else if (externalDef instanceof TypelessDeclaration) {
            new TypeChefBlock(parent, condition, "TODO: " + externalDef.getClass(), relation, externalDef); // TODO
        } else {
            new TypeChefBlock(parent, condition, "Unknown ExternalDef: " + externalDef.getClass(), relation, externalDef);
        }
    }
    
    private void convertExpr(TypeChefBlock parent, Formula condition, Expr expr, String relation) {
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
            new TypeChefBlock(parent, condition, "TODO Expr: " + expr.getClass(), relation, expr); // TODO
        }
    }
    
    private void convertPrimaryExpression(TypeChefBlock parent, Formula condition, PrimaryExpr expr, String relation) {
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
            new TypeChefBlock(parent, condition, "TODO PrimaryExpr: " + expr.getClass(), relation, expr); // TODO
        }
    }
    
    private void convertDeclarator(TypeChefBlock parent, Formula condition, Declarator decl, String relation) {
        if (decl instanceof AtomicNamedDeclarator) {
            convertAtomicNamedDeclarator(parent, condition, (AtomicNamedDeclarator) decl, relation);
        } else if (decl instanceof NestedNamedDeclarator) {
            convertNestedNamedDeclarator(parent, condition, (NestedNamedDeclarator) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown Declarator: " + decl.getClass(), relation, decl);
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
            new TypeChefBlock(parent, condition, "Unknown Specifier: " + spec.getClass(), relation, spec);
        }
    }
    
    private void convertAttributeSpecifier(TypeChefBlock parent, Formula condition, AttributeSpecifier spec, String relation) {
        if (spec instanceof AsmAttributeSpecifier) {
            convertAsmAttributeSpecifier(parent, condition, (AsmAttributeSpecifier) spec, relation);
        } else if (spec instanceof GnuAttributeSpecifier) {
            convertGnuAttributeSpecifier(parent, condition, (GnuAttributeSpecifier) spec, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unknown AttributeSpecifier: " + spec.getClass(), relation, spec);
        }
    }
    
    private void convertOtherSpecifier(TypeChefBlock parent, Formula condition, OtherSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, spec.getClass().getSimpleName(), relation, spec);
    }
    
    private void convertTypeSpecifier(TypeChefBlock parent, Formula condition, TypeSpecifier spec, String relation) {
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
            new TypeChefBlock(parent, condition, "Unkown TypeSpecifier: " + spec.getClass(), relation, spec);
        }
    }
    
    private void convertInitDeclarator(TypeChefBlock parent, Formula condition, InitDeclarator decl, String relation) {
        if (decl instanceof InitDeclaratorE) {
            convertInitDeclaratorE(parent, condition, (InitDeclaratorE) decl, relation);
        } else if (decl instanceof InitDeclaratorI) {
            convertInitDeclaratorI(parent, condition, (InitDeclaratorI) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown InitDeclarator: " + decl.getClass(), relation, decl);
        }
    }
    
    private void convertDeclarationExtension(TypeChefBlock parent, Formula condition, DeclaratorExtension ext, String relation) {
        if (ext instanceof DeclaratorAbstrExtension) {
            convertDeclaratorAbstrExtension(parent, condition, (DeclaratorAbstrExtension) ext, relation);
        } else if (ext instanceof DeclIdentifierList) {
            convertDeclIdentifierList(parent, condition, (DeclIdentifierList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown DeclaratorExtension: " + ext.getClass(), relation, ext);
        }
    }
    
    private void convertDeclaratorAbstrExtension(TypeChefBlock parent, Formula condition, DeclaratorAbstrExtension ext, String relation) {
        if (ext instanceof DeclArrayAccess) {
            convertDeclArrayAccess(parent, condition, (DeclArrayAccess) ext, relation);
        } else if (ext instanceof DeclParameterDeclList) {
            convertDeclParameterDeclList(parent, condition, (DeclParameterDeclList) ext, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown DeclaratorAbstrExtension: " + ext.getClass(), relation, ext);
        }
    }
    
    private void convertPostfixSuffix(TypeChefBlock parent, Formula condition, PostfixSuffix suffix, String relation) {
        if (suffix instanceof ArrayAccess) {
            convertArrayAccess(parent, condition, (ArrayAccess) suffix, relation);
        } else if (suffix instanceof FunctionCall) {
            convertFunctionCall(parent, condition, (FunctionCall) suffix, relation);
        } else if (suffix instanceof PointerPostfixSuffix) {
            convertPointerPostfixSuffix(parent, condition, (PointerPostfixSuffix) suffix, relation);
        } else if (suffix instanceof SimplePostfixSuffix) {
            convertSimplePostfixSuffix(parent, condition, (SimplePostfixSuffix) suffix, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown PostfixSuffix: " + suffix.getClass(), relation, suffix);
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
            convertVarArgs(parent, condition, (VarArgs) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown ParameterDeclaration: " + decl.getClass(), relation, decl);
        }
    }
    
    private void convertAttribute(TypeChefBlock parent, Formula condition, Attribute attr, String relation) {
        if (attr instanceof AtomicAttribute) {
            convertAtomicAttribute(parent, condition, (AtomicAttribute) attr, relation);
        } else if (attr instanceof CompoundAttribute) {
            convertCompoundAttribute(parent, condition, (CompoundAttribute) attr, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown Attribute: " + attr.getClass(), relation, attr);
        }
    }
    
    private void convertStructDecl(TypeChefBlock parent, Formula condition, StructDecl decl, String relation) {
        if (decl instanceof StructDeclarator) {
            convertStructDeclarator(parent, condition, (StructDeclarator) decl, relation);
        } else if (decl instanceof StructInitializer) {
            convertStructInitializer(parent, condition, (StructInitializer) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown StructDecl: " + decl.getClass(), relation, decl);
        }
    }
    
    private void convertAbstractDeclarator(TypeChefBlock parent, Formula condition, AbstractDeclarator decl, String relation) {
        if (decl instanceof AtomicAbstractDeclarator) {
            convertAtomicAbstractDeclarator(parent, condition, (AtomicAbstractDeclarator) decl, relation);
        } else if (decl instanceof NestedAbstractDeclarator) {
            convertNestedAbstractDeclarator(parent, condition, (NestedAbstractDeclarator) decl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown AbstractDeclarator: " + decl.getClass(), relation, decl);
        }
    }
    
    private void convertCDef(TypeChefBlock parent, Formula condition, CDef cdef, String relation) {
        if (cdef instanceof FunctionDef) {
            convertFunctionDef(parent, condition, (FunctionDef) cdef, relation);
        } else if (cdef instanceof NestedFunctionDef) {
            convertNestedFunctionDef(parent, condition, (NestedFunctionDef) cdef, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown CDef: " + cdef.getClass(), relation, cdef);
        }
    }
    
    private void convertOffsetofMemberDesignator(TypeChefBlock parent, Formula condition, OffsetofMemberDesignator des, String relation) {
        if (des instanceof OffsetofMemberDesignatorExpr) {
            convertOffsetofMemberDesignatorExpr(parent, condition, (OffsetofMemberDesignatorExpr) des, relation);
        } else if (des instanceof OffsetofMemberDesignatorID) {
            convertOffsetofMemberDesignatorID(parent, condition, (OffsetofMemberDesignatorID) des, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown OffsetofMemberDesignator: " + des.getClass(), relation, des);
        }
    }
    
    private void convertInitializerElementLabel(TypeChefBlock parent, Formula condition, InitializerElementLabel lbl, String relation) {
        if (lbl instanceof InitializerArrayDesignator) {
            convertInitializerArrayDesignator(parent, condition, (InitializerArrayDesignator) lbl, relation);
        } else if (lbl instanceof InitializerAssigment) {
            convertInitializerAssigment(parent, condition, (InitializerAssigment) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorC) {
            convertInitializerDesignatorC(parent, condition, (InitializerDesignatorC) lbl, relation);
        } else if (lbl instanceof InitializerDesignatorD) {
            convertInitializerDesignatorD(parent, condition, (InitializerDesignatorD) lbl, relation);
        } else {
            new TypeChefBlock(parent, condition, "Unkown InitializerElementLabel: " + lbl.getClass(), relation, lbl);
        }
    }
    
    //---------------------------------------
    
    private void convertAlignOfExprT(TypeChefBlock parent, Formula condition, AlignOfExprT expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AlignOfExprT", relation, expr);
        
        convertTypeName(block, new True(), expr.typeName(), "Type");
    }
    
    private void convertAlignOfExprU(TypeChefBlock parent, Formula condition, AlignOfExprU expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AlignOfExprU", relation, expr);
        
        convertExpr(block, new True(), expr.expr(), "Expression");
    }
    
    private void convertArrayAccess(TypeChefBlock parent, Formula condition, ArrayAccess access, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ArrayAccess", relation, access);
        
        convertExpr(block, new True(), access.expr(), "Expression");
    }
    
    private void convertAtomicAbstractDeclarator(TypeChefBlock parent, Formula condition, AtomicAbstractDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AtomicAbstractDeclarator", relation, decl);
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorAbstrExtension> it : scalaIterator(decl.extensions())) {
            convertDeclaratorAbstrExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private void convertAtomicAttribute(TypeChefBlock parent, Formula condition, AtomicAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AtomicAttribute", relation, attr);

        new TypeChefBlock(block, new True(), attr.n(), "Name");
    }
    
    private void convertAttributeSequence(TypeChefBlock parent, Formula condition, AttributeSequence seq, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AttributeSequence", relation, seq);
        
        for (Opt<Attribute> it : scalaIterator(seq.attributes())) {
            convertAttribute(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertAsmAttributeSpecifier(TypeChefBlock parent, Formula condition, AsmAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AsmAttributeSpecifier", relation, spec);
        
        convertStringLit(block, new True(), spec.stringConst(), "");
    }
    
    private void convertAssignExpr(TypeChefBlock parent, Formula condition, AssignExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AssignExpr", relation, expr);
        
        convertExpr(block, new True(), expr.target(), "Target");
        new TypeChefBlock(block, new True(), expr.operation(), "Operator");
        convertExpr(block, new True(), expr.source(), "Source");
    }
    
    private void convertAtomicNamedDeclarator(TypeChefBlock parent, Formula condition, AtomicNamedDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "AtomicNamedDeclarator", relation, decl);
        
        convertId(block, new True(), decl.getId(), "ID");
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorExtension> it : scalaIterator(decl.extensions())) {
            convertDeclarationExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
    }
    
    private void convertBreakStatement(TypeChefBlock parent, Formula condition, BreakStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "BreakStatement", relation, statement);
    }
    
    private void convertBuiltinOffsetof(TypeChefBlock parent, Formula condition, BuiltinOffsetof expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "BuiltinOffsetof", relation, expr);
        
        convertTypeName(block, new True(), expr.typeName(), "Type");
        
        for (Opt<OffsetofMemberDesignator> it : scalaIterator(expr.offsetofMemberDesignator())) {
            convertOffsetofMemberDesignator(block, toFormula(it.condition()), it.entry(), "OffsetMemberDesignator");
        }
    }
    
    private void convertBuiltinTypesCompatible(TypeChefBlock parent, Formula condition, BuiltinTypesCompatible expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "BuiltinTypesCompatible", relation, expr);
        
        convertTypeName(block, new True(), expr.typeName1(), "Type1");
        convertTypeName(block, new True(), expr.typeName2(), "Type2");
    }
    
    private void convertCaseStatement(TypeChefBlock parent, Formula condition, CaseStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CaseStatement", relation, statement);
        
        convertExpr(block, new True(), statement.c(), "");
    }
    
    private void convertCastExpr(TypeChefBlock parent, Formula condition, CastExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CastExpr", relation, expr);
        
        convertTypeName(block, new True(), expr.typeName(), "Type");
        convertExpr(block, new True(), expr.expr(), "Expression");
    }
    
    private void convertCompoundAttribute(TypeChefBlock parent, Formula condition, CompoundAttribute attr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CompoundAttribute", relation, attr);
        
        for (Opt<AttributeSequence> it : scalaIterator(attr.inner())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertCompoundStatement(TypeChefBlock parent, Formula condition, CompoundStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CompoundStatement", relation, statement);
        
        for (Opt<Statement> it : scalaIterator(statement.innerStatements())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertCompoundStatementExpr(TypeChefBlock parent, Formula condition, CompoundStatementExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "CompoundStatementExpr", relation, expr);

        convertCompoundStatement(block, new True(), expr.compoundStatement(), "Statement");
    }
    
    private void convertConditionalExpr(TypeChefBlock parent, Formula condition, ConditionalExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ConditionalExpr", relation, expr);
        
        convertExpr(block, new True(), expr.condition(), "Condition");
        
        if (expr.thenExpr().isDefined()) {
            convertExpr(block, new True(), expr.thenExpr().get(), "Then");
        }
        
        convertExpr(block, new True(), expr.elseExpr(), "Else");
    }
    
    private void convertConstant(TypeChefBlock parent, Formula condition, Constant expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Constant", relation, expr);
        new TypeChefBlock(block, new True(), expr.value(), "Value");
    }
    
    private void convertContinueStatement(TypeChefBlock parent, Formula condition, ContinueStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "ContinueStatement", relation, statement);
    }
    
    private void convertDeclaration(TypeChefBlock parent, Formula condition, Declaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Declaration", relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.declSpecs())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<InitDeclarator> it : scalaIterator(decl.init())) {
            convertInitDeclarator(block, toFormula(it.condition()), it.entry(), "InitDeclarator");
        }
    }
    
    private void convertDeclarationStatement(TypeChefBlock parent, Formula condition, DeclarationStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclarationStatement", relation, statement);
        
        convertDeclaration(block, new True(), statement.decl(), "");
    }
    
    private void convertDeclIdentifierList(TypeChefBlock parent, Formula condition, DeclIdentifierList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclIdentifierList", relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.idList())) {
            convertId(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertDeclArrayAccess(TypeChefBlock parent, Formula condition, DeclArrayAccess decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclArrayAccess", relation, decl);
        
        if (decl.expr().isDefined()) {
            convertExpr(block, new True(), decl.expr().get(), "Expr");
        }
    }
    
    private void convertDeclParameterDeclList(TypeChefBlock parent, Formula condition, DeclParameterDeclList decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DeclParameterDeclList", relation, decl);
        
        for (Opt<ParameterDeclaration> it : scalaIterator(decl.parameterDecls())) {
            convertParameterDeclaration(block, toFormula(it.condition()), it.entry(), "Parameter");
        }
    }
    
    private void convertDefaultStatement(TypeChefBlock parent, Formula condition, DefaultStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "DefaultStatement", relation, statement);
    }
    
    private void convertDoStatement(TypeChefBlock parent, Formula condition, DoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "DoStatement", relation, statement);
        
        convertExpr(block, new True(), statement.expr(), "Expr");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private void convertElifStatement(TypeChefBlock parent, Formula condition, ElifStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ElifStatement", relation, statement);
        
        for (Opt<Expr> it : scalaIterator(statement.condition().toOptList())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "Expr");
        }
        
        for (Opt<Statement> it : scalaIterator(statement.thenBranch().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Then");
        }
    }
    
    private void convertEmptyExternalDef(TypeChefBlock parent, Formula condition, EmptyExternalDef statement, String relation) {
        new TypeChefBlock(parent, condition, "EmptyExternalDef", relation, statement);
    }
    
    private void convertEmptyStatement(TypeChefBlock parent, Formula condition, EmptyStatement statement, String relation) {
        new TypeChefBlock(parent, condition, "EmptyStatement", relation, statement);
    }
    
    private void convertEnumerator(TypeChefBlock parent, Formula condition, Enumerator spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Enumerator", relation, spec);
        
        convertId(block, new True(), spec.id(), "ID");
        
        if (spec.assignment().isDefined()) {
            convertExpr(block, new True(), spec.assignment().get(), "Value");
        }
    }
    
    private void convertEnumSpecifier(TypeChefBlock parent, Formula condition, EnumSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "EnumSpecifier", relation, spec);
        
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
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ExprList", relation, expr);
        
        for (Opt<Expr> it : scalaIterator(expr.exprs())) {
            convertExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertExprStatement(TypeChefBlock parent, Formula condition, ExprStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ExprStatement", relation, statement);
        
        convertExpr(block, new True(), statement.expr(), "");
    }
    
    private void convertForStatement(TypeChefBlock parent, Formula condition, ForStatement statement, String relation) {
        TypeChefBlock block =  new TypeChefBlock(parent, condition, "ForStatement", relation, statement);
        
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
        TypeChefBlock block = new TypeChefBlock(parent, condition, "FunctionCall", relation, call);
        
        convertExprList(block, new True(), call.params(), "Parameters");
    }
    
    private void convertFunctionDef(TypeChefBlock parent, Formula condition, FunctionDef functionDef, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "FunctionDef", relation, functionDef);
        
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
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GnuAsmExpr", relation, expr);
        
        convertStringLit(block, new True(), expr.expr(), "Expression");
    }
    
    private void convertGnuAttributeSpecifier(TypeChefBlock parent, Formula condition, GnuAttributeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GnuAttributeSpecifier", relation, spec);
        
        for (Opt<AttributeSequence> it : scalaIterator(spec.attributeList())) {
            convertAttributeSequence(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertGotoStatement(TypeChefBlock parent, Formula condition, GotoStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "GotoStatement", relation, statement);
        
        convertExpr(block, new True(), statement.target(), "Target");
    }
    
    private void convertId(TypeChefBlock parent, Formula condition, Id id, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Id", relation, id);
        new TypeChefBlock(block, new True(), id.name(), "Name");
    }
    
    private void convertSimplePostfixSuffix(TypeChefBlock parent, Formula condition, SimplePostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SimplePostfixSuffix", relation, suffix);
        
        new TypeChefBlock(block, new True(), suffix.t(), "Operator");
    }
    
    private void convertIfStatement(TypeChefBlock parent, Formula condition, IfStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "IfStatement", relation, statement);
        
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
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Initializer", relation, init);
        
        convertExpr(block, new True(), init.expr(), "Expr");
        
        if (init.initializerElementLabel().isDefined()) {
            convertInitializerElementLabel(block, new True(), init.initializerElementLabel().get(), "ElementLabel");
        }
    }
    
    private void convertInitializerArrayDesignator(TypeChefBlock parent, Formula condition, InitializerArrayDesignator init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitializerArrayDesignator", relation, init);
        
        convertExpr(block, new True(), init.expr(), "Expression");
    }
    
    private void convertInitializerAssigment(TypeChefBlock parent, Formula condition, InitializerAssigment init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitializerAssigment", relation, init);
        
        for (Opt<InitializerElementLabel> it : scalaIterator(init.designators())) {
            convertInitializerElementLabel(block, toFormula(it.condition()), it.entry(), "Designator");
        }
    }
    
    private void convertInitializerDesignatorC(TypeChefBlock parent, Formula condition, InitializerDesignatorC init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitializerDesignatorC", relation, init);

        convertId(block, new True(), init.id(), "ID");
    }
    
    private void convertInitializerDesignatorD(TypeChefBlock parent, Formula condition, InitializerDesignatorD init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitializerDesignatorD", relation, init);
        
        convertId(block, new True(), init.id(), "ID");
    }
    
    private void convertInitDeclaratorE(TypeChefBlock parent, Formula condition, InitDeclaratorE decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitDeclaratorE", relation, decl);
        
        convertDeclarator(block, new True(), decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, new True(), decl.e(), "Expression");
        
    }
    
    private void convertInitDeclaratorI(TypeChefBlock parent, Formula condition, InitDeclaratorI decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "InitDeclaratorI", relation, decl);
        
        convertDeclarator(block, new True(), decl.declarator(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.i().isDefined()) {
            convertInitializer(block, new True(), decl.i().get(), "Initializer");
        }
    }
    
    private void convertLabelStatement(TypeChefBlock parent, Formula condition, LabelStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "LabelStatement", relation, statement);
        
        if (statement.attribute().isDefined()) {
            convertAttributeSpecifier(block, new True(), statement.attribute().get(), "Attribute");
        }
        
        convertId(block, new True(), statement.id(), "Id");
    }
    
    private void convertLcurlyInitializer(TypeChefBlock parent, Formula condition, LcurlyInitializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "LcurlyInitializer", relation, init);
        
        for (Opt<Initializer> it : scalaIterator(init.inits())) {
            convertInitializer(block, toFormula(it.condition()), it.entry(), "Initializer");
        }
    }
    
    private void convertLocalLabelDeclaration(TypeChefBlock parent, Formula condition, LocalLabelDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "LocalLabelDeclaration", relation, decl);
        
        for (Opt<Id> it : scalaIterator(decl.ids())) {
            convertId(block, toFormula(it.condition()), it.entry(), "Id");
        }
    }
    
    private void convertNAryExpr(TypeChefBlock parent, Formula condition, NAryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NAryExpr", relation, expr);
        
        convertExpr(block, new True(), expr.e(), "");
        
        for (Opt<NArySubExpr> it : scalaIterator(expr.others())) {
            convertNArySubExpr(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertNArySubExpr(TypeChefBlock parent, Formula condition, NArySubExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NAryExpr", relation, expr);
        
        new TypeChefBlock(block, new True(), expr.op(), "Operator");
        convertExpr(block, new True(), expr.e(), "");
    }
    
    private void convertNestedAbstractDeclarator(TypeChefBlock parent, Formula condition, NestedAbstractDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NestedAbstractDeclarator", relation, decl);
        
        for (Opt<Pointer> it : scalaIterator(decl.pointers())) {
            convertPointer(block, toFormula(it.condition()), it.entry(), "Pointer");
        }
        
        for (Opt<DeclaratorAbstrExtension> it : scalaIterator(decl.extensions())) {
            convertDeclaratorAbstrExtension(block, toFormula(it.condition()), it.entry(), "Extension");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAbstractDeclarator(block, new True(), decl.nestedDecl(), "NestedDeclarator");
    }
    
    private void convertNestedFunctionDef(TypeChefBlock parent, Formula condition, NestedFunctionDef def, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NestedFunctionDef", relation, def);
        
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
        TypeChefBlock block = new TypeChefBlock(parent, condition, "NestedNamedDeclarator", relation, decl);
        
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
    
    private void convertOffsetofMemberDesignatorExpr(TypeChefBlock parent, Formula condition, OffsetofMemberDesignatorExpr offset, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "OffsetofMemberDesignatorExpr", relation, offset);
        
        convertExpr(block, new True(), offset.expr(), "Expression");
    }
    
    private void convertOffsetofMemberDesignatorID(TypeChefBlock parent, Formula condition, OffsetofMemberDesignatorID offset, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "OffsetofMemberDesignatorID", relation, offset);
        
        convertId(block, new True(), offset.id(), "ID");
    }
    
    private void convertOtherPrimitiveTypeSpecifier(TypeChefBlock parent, Formula condition, OtherPrimitiveTypeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "OtherPrimitiveTypeSpecifier", relation, spec);
        
        new TypeChefBlock(block, new True(), spec.typeName(), "TypeName");
    }
    
    private void convertParameterDeclarationAD(TypeChefBlock parent, Formula condition, ParameterDeclarationAD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ParameterDeclarationAD", relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertAbstractDeclarator(block, new True(), decl.decl(), "Declarator");
    }
    
    private void convertParameterDeclarationD(TypeChefBlock parent, Formula condition, ParameterDeclarationD decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ParameterDeclarationD", relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertDeclarator(block, new True(), decl.decl(), "Declarator");
    }
    
    private void convertPlainParameterDeclaration(TypeChefBlock parent, Formula condition, PlainParameterDeclaration decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PlainParameterDeclaration", relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attr())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
    }
    
    private void convertPrimitiveTypeSpecifier(TypeChefBlock parent, Formula condition, PrimitiveTypeSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, spec.getClass().getSimpleName(), relation, spec);
    }
    
    private void convertPointer(TypeChefBlock parent, Formula condition, Pointer pointer, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "Pointer", relation, pointer);
        
        for (Opt<Specifier> it : scalaIterator(pointer.specifier())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private void convertPointerCreationExpr(TypeChefBlock parent, Formula condition, PointerCreationExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PointerCreationExpr", relation, expr);
        
        convertExpr(block, new True(), expr.castExpr(), "Expression");
    }
    
    private void convertPointerDerefExpr(TypeChefBlock parent, Formula condition, PointerDerefExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PointerDerefExpr", relation, expr);
        
        convertExpr(block, new True(), expr.castExpr(), "Expression");
    }
    
    private void convertPointerPostfixSuffix(TypeChefBlock parent, Formula condition, PointerPostfixSuffix suffix, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PointerPostfixSuffix", relation, suffix);
        
        convertId(block, new True(), suffix.id(), "ID");
        new TypeChefBlock(block, new True(), suffix.kind(), "Kind");
    }
    
    private void convertPostfixExpr(TypeChefBlock parent, Formula condition, PostfixExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "PostfixExpr", relation, expr);
        
        convertPostfixSuffix(block, new True(), expr.s(), "Operator");
        convertExpr(block, new True(), expr.p(), "Expr");
    }
    
    private void convertRangeExpr(TypeChefBlock parent, Formula condition, RangeExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "RangeExpr", relation, expr);

        convertExpr(block, new True(), expr.from(), "From");
        convertExpr(block, new True(), expr.to(), "To");
    }
    
    private void convertReturnStatement(TypeChefBlock parent, Formula condition, ReturnStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "ReturnStatement", relation, statement);
        
        if (statement.expr().isDefined()) {
            convertExpr(block, new True(), statement.expr().get(), "Value");
        }
    }
    
    private void convertSignedSpecifier(TypeChefBlock parent, Formula condition, SignedSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, "SignedSpecifier", relation, spec);
    }
    
    private void convertSizeOfExprT(TypeChefBlock parent, Formula condition, SizeOfExprT expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SizeOfExprT", relation, expr);
        
        convertTypeName(block, new True(), expr.typeName(), "Type");
    }
    
    private void convertSizeOfExprU(TypeChefBlock parent, Formula condition, SizeOfExprU expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SizeOfExprT", relation, expr);
        
        convertExpr(block, new True(), expr.expr(), "Expression");
    }
    
    private void convertStringLit(TypeChefBlock parent, Formula condition, StringLit lit, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StringLit", relation, lit);

        for (Opt<String> it : scalaIterator(lit.name())) {
            new TypeChefBlock(block, toFormula(it.condition()), it.entry(), "Value");
        }
    }
    
    private void convertStructOrUnionSpecifier(TypeChefBlock parent, Formula condition, StructOrUnionSpecifier spec, String relation) {
        String name = (spec.isUnion() ? "Union" : "Struct") + "Specifier";
        TypeChefBlock block = new TypeChefBlock(parent, condition, name, relation, spec);
        
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
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StructDeclaration", relation, decl);
        
        for (Opt<Specifier> it : scalaIterator(decl.qualifierList())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Qualifier");
        }
        
        for (Opt<StructDecl> it : scalaIterator(decl.declaratorList())) {
            convertStructDecl(block, toFormula(it.condition()), it.entry(), "Declarator");
        }
    }
    
    private void convertStructDeclarator(TypeChefBlock parent, Formula condition, StructDeclarator decl, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StructDeclarator", relation, decl);
        
        convertDeclarator(block, new True(), decl.decl(), "Declarator");
        
        for (Opt<AttributeSpecifier> it : scalaIterator(decl.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        if (decl.initializer().isDefined()) {
            convertExpr(block, new True(), decl.initializer().get(), "Initializer");
        }
    }
    
    private void convertStructInitializer(TypeChefBlock parent, Formula condition, StructInitializer init, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "StructInitializer", relation, init);
        
        for (Opt<AttributeSpecifier> it : scalaIterator(init.attributes())) {
            convertAttributeSpecifier(block, toFormula(it.condition()), it.entry(), "Attribute");
        }
        
        convertExpr(block, new True(), init.expr(), "Expression");
    }
    
    private void convertSwitchStatement(TypeChefBlock parent, Formula condition, SwitchStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "SwitchStatement", relation, statement);
        
        convertExpr(block, new True(), statement.expr(), "Expression");
        
        for (Opt<Statement> it : scalaIterator(statement.s().toOptList())) {
            convertStatement(block, toFormula(it.condition()), it.entry(), "Body");
        }
    }
    
    private void convertTranslationUnit(TypeChefBlock parent, Formula condition, TranslationUnit unit, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TranslationUnit", relation, unit);
        
        for (Opt<ExternalDef> it : scalaIterator(unit.defs())) {
            convertExternalDef(block, toFormula(it.condition()), it.entry(), "");
        }
    }
    
    private void convertTypeDefSpecifier(TypeChefBlock parent, Formula condition, TypedefSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, "TypedefSpecifier", relation, spec);
    }
    
    private void convertTypeDefTypeSpecifier(TypeChefBlock parent, Formula condition, TypeDefTypeSpecifier spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TypeDefTypeSpecifier", relation, spec);
        
        convertId(block, new True(), spec.name(), "ID");
    }
    
    private void convertTypeName(TypeChefBlock parent, Formula condition, TypeName name, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TypeName", relation, name);
        
        if (name.decl().isDefined()) {
            convertAbstractDeclarator(block, new True(), name.decl().get(), "Declaration");
        }
        
        for (Opt<Specifier> it : scalaIterator(name.specifiers())) {
            convertSpecifier(block, toFormula(it.condition()), it.entry(), "Specifier");
        }
    }
    
    private void convertTypeOfSpecifierT(TypeChefBlock parent, Formula condition, TypeOfSpecifierT spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TypeOfSpecifierT", relation, spec);
        
        convertTypeName(block, new True(), spec.typeName(), "Type");
    }
    
    private void convertTypeOfSpecifierU(TypeChefBlock parent, Formula condition, TypeOfSpecifierU spec, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "TypeOfSpecifierU", relation, spec);
        
        convertExpr(block, new True(), spec.expr(), "Expression");
    }
    
    private void convertUnaryExpr(TypeChefBlock parent, Formula condition, UnaryExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "UnaryExpr", relation, expr);
        
        new TypeChefBlock(block, new True(), expr.kind(), "Operator");
        convertExpr(block, new True(), expr.e(), "Expr");
    }
    
    private void convertUnaryOpExpr(TypeChefBlock parent, Formula condition, UnaryOpExpr expr, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "UnaryOpExpr", relation, expr);
        
        new TypeChefBlock(block, new True(), expr.kind(), "Operator");
        convertExpr(block, new True(), expr.castExpr(), "Expr");
    }
    
    private void convertUnsignedSpecifier(TypeChefBlock parent, Formula condition, UnsignedSpecifier spec, String relation) {
        new TypeChefBlock(parent, condition, "UnsignedSpecifier", relation, spec);
    }
    
    private void convertVarArgs(TypeChefBlock parent, Formula condition, VarArgs varargs, String relation) {
        new TypeChefBlock(parent, condition, "VarArgs", relation, varargs);
    }
    
    private void convertWhileStatement(TypeChefBlock parent, Formula condition, WhileStatement statement, String relation) {
        TypeChefBlock block = new TypeChefBlock(parent, condition, "WhileStatement", relation, statement);
        
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
