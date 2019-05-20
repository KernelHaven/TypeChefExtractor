/*
 * Copyright 2017-2019 University of Hildesheim, Software Systems Engineering
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
package net.ssehub.kernel_haven.typechef;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import net.ssehub.kernel_haven.code_model.simple_ast.ErrorSyntaxElement;
import net.ssehub.kernel_haven.code_model.simple_ast.LiteralSyntaxElement;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElement;
import net.ssehub.kernel_haven.code_model.simple_ast.SyntaxElementTypes;
import net.ssehub.kernel_haven.typechef.wrapper.comm.SyntaxElementCsvUtil;
import net.ssehub.kernel_haven.util.FormatException;
import net.ssehub.kernel_haven.util.logic.Conjunction;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.True;
import net.ssehub.kernel_haven.util.logic.Variable;
import net.ssehub.kernel_haven.util.logic.parser.CStyleBooleanGrammar;
import net.ssehub.kernel_haven.util.logic.parser.Parser;
import net.ssehub.kernel_haven.util.logic.parser.VariableCache;

/**
 * Tests the CSV utility class for syntax elements.
 * 
 * @author Adam
 */
public class SyntaxElementCsvUtilTest {
    
    private static final VariableCache CACHE = new VariableCache();
    private static final Parser<Formula> PARSER = new Parser<>(new CStyleBooleanGrammar(CACHE));

    /**
     * Tests whether CSV is correctly converted into an element.
     * 
     * @throws FormatException unwanted.
     */
    @Test
    public void testCsvToElement() throws FormatException {
        Formula varA = new Variable("VAR_A");
        Formula varB = new Variable("VAR_B");
        
        SyntaxElement main = new SyntaxElement(SyntaxElementTypes.COMPOUND_STATEMENT, varA, varA);
        SyntaxElement nested1 = new SyntaxElement(SyntaxElementTypes.BREAK_STATEMENT, True.INSTANCE, varA);
        SyntaxElement nested2 = new SyntaxElement(SyntaxElementTypes.EXPR_STATEMENT, varB, new Conjunction(varA, varB));
        SyntaxElement nested3 = new SyntaxElement(SyntaxElementTypes.EXPR_STATEMENT, null, varA);
        nested3.setSourceFile(new File("testfile.c"));
        nested3.setLineStart(1);
        nested3.setLineEnd(2);
        SyntaxElement nested4 = new SyntaxElement(new LiteralSyntaxElement("SomeText"), True.INSTANCE, varA);
        SyntaxElement nested5 = new SyntaxElement(new ErrorSyntaxElement("OtherText"), True.INSTANCE, varA);
        
        main.addNestedElement(nested1);
        main.addNestedElement(nested2, "second");
        main.addNestedElement(nested3, "third");
        main.addNestedElement(nested4);
        main.addNestedElement(nested5);

        
        
        SyntaxElement mainRead = SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(main).toArray(new String[0]), PARSER);
        
        mainRead.addNestedElement(SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(nested1).toArray(new String[0]), PARSER));
        mainRead.addNestedElement(SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(nested2).toArray(new String[0]), PARSER));
        mainRead.addNestedElement(SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(nested3).toArray(new String[0]), PARSER));
        mainRead.addNestedElement(SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(nested4).toArray(new String[0]), PARSER));
        mainRead.addNestedElement(SyntaxElementCsvUtil.csvToElement(
                SyntaxElementCsvUtil.elementToCsv(nested5).toArray(new String[0]), PARSER));
        
        assertSyntaxElementEqual(mainRead, main);
    }
    
    /**
     * Tests whether an element is correctly converted into CSV.
     */
    @Test
    public void testElementToCsv() {
        Formula varA = new Variable("VAR_A");
        Formula varB = new Variable("VAR_B");
        
        SyntaxElement main = new SyntaxElement(SyntaxElementTypes.COMPOUND_STATEMENT, varA, varA);
        SyntaxElement nested1 = new SyntaxElement(SyntaxElementTypes.BREAK_STATEMENT, True.INSTANCE, varA);
        SyntaxElement nested2 = new SyntaxElement(SyntaxElementTypes.EXPR_STATEMENT, varB, new Conjunction(varA, varB));
        SyntaxElement nested3 = new SyntaxElement(SyntaxElementTypes.EXPR_STATEMENT, null, varA);
        nested3.setSourceFile(new File("testfile.c"));
        nested3.setLineStart(1);
        nested3.setLineEnd(2);
        SyntaxElement nested4 = new SyntaxElement(new LiteralSyntaxElement("SomeText"), True.INSTANCE, varA);
        SyntaxElement nested5 = new SyntaxElement(new ErrorSyntaxElement("OtherText"), True.INSTANCE, varA);
        
        main.addNestedElement(nested1);
        main.addNestedElement(nested2, "second");
        main.addNestedElement(nested3, "third");
        main.addNestedElement(nested4);
        main.addNestedElement(nested5);
        
        assertThat(SyntaxElementCsvUtil.elementToCsv(main), CoreMatchers.is(Arrays.asList(
                "-1", "-1", "<unknown>", "VAR_A", "VAR_A", "CompoundStatement", "5", "", "second", "third", "", ""
        )));
        assertThat(SyntaxElementCsvUtil.elementToCsv(nested1), CoreMatchers.is(Arrays.asList(
                "-1", "-1", "<unknown>", "1", "VAR_A", "BreakStatement", "0"
        )));
        assertThat(SyntaxElementCsvUtil.elementToCsv(nested2), CoreMatchers.is(Arrays.asList(
                "-1", "-1", "<unknown>", "VAR_B", "VAR_A && VAR_B", "ExprStatement", "0"
        )));
        assertThat(SyntaxElementCsvUtil.elementToCsv(nested3), CoreMatchers.is(Arrays.asList(
                "1", "2", "testfile.c", "null", "VAR_A", "ExprStatement", "0"
        )));
        assertThat(SyntaxElementCsvUtil.elementToCsv(nested4), CoreMatchers.is(Arrays.asList(
                "-1", "-1", "<unknown>", "1", "VAR_A", "Literal: SomeText", "0"
        )));
        assertThat(SyntaxElementCsvUtil.elementToCsv(nested5), CoreMatchers.is(Arrays.asList(
                "-1", "-1", "<unknown>", "1", "VAR_A", "Error: OtherText", "0"
        )));
    }
    
    /**
     * Tests whether a malformed CSV correctly throws an exception.
     * 
     * @throws FormatException wanted.
     */
    @Test(expected = FormatException.class)
    public void testReadInvalidLineNumber() throws FormatException {
        String[] csv = {"notanumber", "-1", "<unknown>", "1", "VAR_A", "BreakStatement", "0"};
        SyntaxElementCsvUtil.csvToElement(csv, PARSER);
    }
    
    /**
     * Tests whether a malformed CSV correctly throws an exception.
     * 
     * @throws FormatException wanted.
     */
    @Test(expected = FormatException.class)
    public void testTooShortCsv() throws FormatException {
        String[] csv = {"-1", "-1", "<unknown>", "1", "VAR_A", "BreakStatement"};
        SyntaxElementCsvUtil.csvToElement(csv, PARSER);
    }
    
    /**
     * Tests whether a malformed CSV correctly throws an exception.
     * 
     * @throws FormatException wanted.
     */
    @Test(expected = FormatException.class)
    public void testWrongType() throws FormatException {
        String[] csv = {"-1", "-1", "<unknown>", "1", "VAR_A", "DoesntExist", "0"};
        SyntaxElementCsvUtil.csvToElement(csv, PARSER);
    }
    
    /**
     * Tests whether a malformed CSV correctly throws an exception.
     * 
     * @throws FormatException wanted.
     */
    @Test(expected = FormatException.class)
    public void testInvalidNumRelationsNumber() throws FormatException {
        String[] csv = {"-1", "-1", "<unknown>", "1", "VAR_A", "BreakStatement", "notanumber"};
        SyntaxElementCsvUtil.csvToElement(csv, PARSER);
    }
    
    /**
     * Tests whether a malformed CSV correctly throws an exception.
     * 
     * @throws FormatException wanted.
     */
    @Test(expected = FormatException.class)
    public void testInvalidFormula() throws FormatException {
        String[] csv = {"-1", "-1", "<unknown>", "1", "VAR_A &&", "BreakStatement", "0"};
        SyntaxElementCsvUtil.csvToElement(csv, PARSER);
    }
    
    /**
     * Tests the cache for deserializing.
     * 
     * @throws FormatException unwanted.
     */
    @Test
    public void testCache() throws FormatException {
        Map<String, Formula> formulaCache = new HashMap<>();
        Map<String, File> fileNameCache = new HashMap<>();
        String[] csv1 = {"-1", "-1", "test.c", "1", "VAR_A", "BreakStatement", "0"};
        String[] csv2 = {"-1", "-1", "test.c", "1", "VAR_A", "ExprStatement", "1", "one"};
        SyntaxElement s1 = SyntaxElementCsvUtil.csvToElement(csv1, PARSER, formulaCache, fileNameCache);
        SyntaxElement s2 = SyntaxElementCsvUtil.csvToElement(csv2, PARSER, formulaCache, fileNameCache);
        
        assertThat(s1.getCondition(), sameInstance(s2.getCondition()));
        assertThat(s1.getPresenceCondition(), sameInstance(s2.getPresenceCondition()));
        assertThat(s1.getSourceFile(), sameInstance(s2.getSourceFile()));
        
        assertThat(formulaCache.size(), is(2));
        assertThat(fileNameCache.size(), is(1));
    }
    
    /**
     * Asserts that both syntax elements are equal. Recursively checks child
     * elements, too.
     * 
     * @param actual
     *            The actual value.
     * @param expected
     *            The expected value.
     */
    public static void assertSyntaxElementEqual(SyntaxElement actual, SyntaxElement expected) {
        assertThat(actual.getClass(), is((Object) expected.getClass()));
        assertThat(actual.getSourceFile().getPath(), is(expected.getSourceFile().getPath()));
        assertThat(actual.getLineStart(), is(expected.getLineStart()));
        assertThat(actual.getLineEnd(), is(expected.getLineEnd()));
        assertThat(actual.getCondition(), is(expected.getCondition()));
        assertThat(actual.getPresenceCondition(), is(expected.getPresenceCondition()));
        assertThat(actual.getNestedElementCount(), is(expected.getNestedElementCount()));

        assertThat(actual.getType().toString(), is(expected.getType().toString()));
        for (int i = 0; i < actual.getNestedElementCount(); i++) {
            assertThat(actual.getRelation(i), is(expected.getRelation(i)));
        }

        Iterator<SyntaxElement> actualIt = actual.iterator();
        Iterator<SyntaxElement> expectedIt = expected.iterator();

        while (expectedIt.hasNext()) {
            assertThat(actualIt.hasNext(), is(true));

            SyntaxElement actualChild = actualIt.next();
            SyntaxElement expectedChild = expectedIt.next();
            assertSyntaxElementEqual(actualChild, expectedChild);
        }
        assertThat(actualIt.hasNext(), is(false));
    }
    
}
