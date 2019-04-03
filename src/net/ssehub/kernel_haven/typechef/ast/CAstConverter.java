/*
 * Copyright 2017-2018 University of Hildesheim, Software Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ssehub.kernel_haven.typechef.ast;

import java.io.File;
import java.io.IOException;

import de.fosd.typechef.conditional.Opt;
import de.fosd.typechef.parser.c.ExternalDef;
import de.fosd.typechef.parser.c.TranslationUnit;
import net.ssehub.kernel_haven.code_model.simple_ast.LiteralSyntaxElement;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElementTypes;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.True;
import scala.Option;

/**
 * Simplified version of {@link FullAstConverter}, which translates the ASt of TypeChef into an AST-structure
 * provided by KernelHaven. Contrary to {@link FullAstConverter}, this converter omits all header included macros and
 * considers only macros used in C code.
 * @author El-Sharkawy
 *
 */
public class CAstConverter implements IAstConverter {
    
    private static boolean isCCode = false;
    
    @Override
    public SyntaxElement convertToFile(TranslationUnit unit, File sourceTree) {
        try {
            FullAstConverter.sourceTree = sourceTree.getCanonicalFile().toPath();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        
        SyntaxElement tmp = FullAstConverter.createSyntaxElement(null, True.INSTANCE, new LiteralSyntaxElement(""), "",
            null);
        convertTranslationUnit(tmp, True.INSTANCE, unit, "");
        
        return tmp.getNestedElement(0);
    }
    
    /**
     * Translation method for the root element of the AST.
     * @param parent A temporary fake root element.
     * @param condition The condition of the root element (usually it should be {@link True#INSTANCE}.
     * @param unit The unit to be translated.
     * @param relation An empty string.
     */
    private static void convertTranslationUnit(SyntaxElement parent, Formula condition, TranslationUnit unit,
        String relation) {
        
        SyntaxElement block = FullAstConverter.createSyntaxElement(parent, condition,
            SyntaxElementTypes.TRANSLATION_UNIT, relation, unit);
        
        for (Opt<ExternalDef> it : FullAstConverter.scalaIterator(unit.defs())) {
            ExternalDef elem = it.entry();
            
            if (!isCCode) {
                // Test if we now reached the start of the C code.
                Option<String> srcPath = elem.getFile();
                if (srcPath.isDefined()) {
                    String path = srcPath.get();
                    isCCode = path.endsWith(".c");
                }
            }
            
            if (isCCode) {
                FullAstConverter.convertExternalDef(block, FullAstConverter.toFormula(it.condition()), it.entry(), "");
            }
        }
    }

}
