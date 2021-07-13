package com.lienisoft.mtr.expressionInterpreter.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoTokenizeError;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoWrongParameterCount;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester.Errors;

public class ExpressionTest {

	@Test
	public void test() {
		final TDataArg jh = new TDataArg();

		final String expression = "X = \"\n\"; B = \"gaga\"; C1 = B + X + B; A = 1 + (1 + 3) * -100 + \"test\";C.Wurg=B+A;C.urg.murxl=SUB(A,2,6);D=120/40;E=NOW()";

		System.out.println(expression);

		// TODO: implement function SUB, NOW
		ExpressionTester.evaluateExpression(jh, expression, Code.UNKNOWN_FUNCTION, Code.UNKNOWN_FUNCTION);

		System.out.println(jh);
	}

	@Test
	public void testNulPattern() {

		final TDataArg jh = new TDataArg();
		;

		jh.put("A", null);
		System.out.println(jh);
		System.out.println("------<1>------------------");

		String expression = "B = null; C.D = null; E =1234";

		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println("--- As Pretty JSON: ---------------------");
		// System.out.println(TJson.toJson(jh.getObj()));

		System.out.println(jh);
		System.out.println("------<2>------------------");
		assertNull(jh.get("A", Object.class));
		assertNull(jh.get("A", String.class));
		assertNull(jh.get("A", Boolean.class));
		assertNull(jh.get("B", Object.class));
		assertNull(jh.get("B", Integer.class));
		assertNull(jh.get("C.D", Object.class));
		assertEquals(1234, jh.get("E", Object.class));

		// Arbitrary: something which was not defined:
		assertNull(jh.get("Undefined", String.class));

		expression = "E = null";
		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);
		System.out.println("------<3>------------------");

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Set<Entry<?, ?>> set = ((Map) jh.get()).entrySet();

		/**
		 * two members:
		 * A = null, because inserted by put
		 * E because not null
		 */
		assertEquals(2, set.size());

		int i = 0;
		for (final Entry<?, ?> entry : set) {
			if (i == 0) {
				assertEquals("A", entry.getKey());
				assertNull(entry.getValue());
			}
			// if (i == 1) {
			// assertEquals("B", entry.getKey());
			// assertNull(entry.getValue());
			// }
			// if (i == 2) {
			// assertEquals("C", entry.getKey());
			// assertNotNull(entry.getValue());
			// }
			if (i == 1) {
				assertEquals("E", entry.getKey());
				assertNull(entry.getValue());
			}
			i++;
		}

		System.out.println("--- As Pretty JSON: ---------------------");
		// System.out.println(TJson.toJson(jh.getObj()));
	}

	@Test
	public void testEscapedText() {
		final TDataArg jh = new TDataArg();

		// String expression = "M.FreeText = \"This is a text with \\\"quote signs\\\"\\nline
		// breaks\\n\\tand tabs";
		final String expression = "M.FreeText = \"This is a text with \\\"quote signs\\\"\\nline breaks\\n\\tand tabs\"";

		System.out.println(expression);

		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);
		System.out.println("-------------------");
		System.out.println(jh.get("M.FreeText", String.class));

		assertEquals("This is a text with \"quote signs\"\n" + "line breaks\n" + "\tand tabs",
				jh.get("M.FreeText", String.class));
	}

	@Test
	public void testEscapedSemicolon() {
		/**
		 * for "historic" reasons: a semicolon could be escaped optionally
		 */

		final TDataArg jh = new TDataArg();
		;

		// String expression = "M.FreeText = \"This is a text with \\\"quote signs\\\"\\nline
		// breaks\\n\\tand tabs";
		final String expression = "M.FreeText = \"This is a text with; and\\;!\"";

		System.out.println(expression);

		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);
		System.out.println("-------------------");
		System.out.println(jh.get("M.FreeText", String.class));

		assertEquals("This is a text with; and;!", jh.get("M.FreeText", String.class));
	}

	@Test
	public void testTrueFalsePattern() {

		final TDataArg jh = new TDataArg();
		;

		jh.put("A", null);

		// new (true/false) and old (b"true"/b"false") boolean patterns:
		final String expression = "B = true; C = b\"true\"; D = false; E = b\"false\";";

		ExpressionTester.evaluateExpression(jh, expression);

		assertNull(jh.get("A", Object.class));
		assertEquals(true, jh.get("B", Object.class));
		assertEquals(true, jh.get("C", Object.class));
		assertEquals(false, jh.get("D", Object.class));
		assertEquals(false, jh.get("E", Object.class));

		System.out.println(jh);
	}

	@Test
	public void testOperatorOr() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1<2 || 2>1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 2<1 || 2>1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 2<1 || 0>1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertNotNull(jh.get("B", Object.class));
			assertFalse(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = null || 0<1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertNotNull(jh.get("B", Object.class));
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = null || null";
			ExpressionTester.evaluateExpression(jh, expression);
			assertNotNull(jh.get("B", Object.class));
			assertFalse(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 || null";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Boolean.class));
		}

	}

	@Test
	public void testOperatorAND() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1<2 && 2>=1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1<=0 && 2>1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1<=0 && null";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = null && null";
			ExpressionTester.evaluateExpression(jh, expression);
			// TODO !
			// assertFalse(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 && null";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Boolean.class));
		}
	}

	@Test
	public void testOperatorAND_2() {
		final TDataArg jh = new TDataArg();
		;
		final String expression = "B = null && true";
		// final String expression = "B = null || null";
		ExpressionTester.evaluateExpression(jh, expression);

		// TODO !
		// assertFalse(jh.get("B", Boolean.class));
	}

	@Test
	public void testOperatorEquals() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1 == 1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1 == 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}
	}

	@Test
	public void testOperatorPlusEqual() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "C = 2; C += 3";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 5, jh.get("C", Integer.class));
		}
		// {
		// JsonHelper jh = new JsonHelper();
		// String expression = "C = null; C += 3";
		// ExpressionTester_NEW.evaluateExpression(jh, expression, Code.OperationNotDefined);
		// assertEquals(true,jh.getBoolean("C"));
		// }
	}

	@Test
	public void testOperatorNotEquals() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1 != 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 1 != 1";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}
	}

	@Test
	public void testOperatorMinus() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 - 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 1, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 - null";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Integer.class));
		}
	}

	@Test
	public void testOperatorPlus() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 + 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 5, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 + null";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 + (1<2)";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Integer.class));
		}
	}

	@Test
	public void testAssign() {
		{
			final TDataArg jh = new TDataArg();
			;
			;
			final String expression = "B = 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 2, jh.get("B", Integer.class));
		}

		{ // TODO! prefix on left side must not be allowed!
			final TDataArg jh = new TDataArg();
			;
			;
			final String expression = "-A = 3";
			// ExpressionTester.evaluateExpression(jh, expression, Code.NOT_AN_LVALUE,
			// Code.NOT_AN_LVALUE);
			ExpressionTester.evaluateExpression(jh, expression, Code.NOT_AN_LVALUE);
			// assertNull(jh.get("A", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			;
			final String expression = "B = true; B = 2";

			ExpressionTester.evaluateExpression(jh, expression, Code.TYPE_MISMATCH);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			;
			final String expression = "3 = B";
			ExpressionTester.evaluateExpression(jh, expression, Code.NOT_AN_LVALUE, Code.NOT_AN_LVALUE);
			assertNull(jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			;
			final String expression = "3 * 4 = 5";
			ExpressionTester.evaluateExpression(jh, expression, Code.NOT_AN_LVALUE, Code.NOT_AN_LVALUE);
			assertNull(jh.get("B", Integer.class));
		}
	}

	@Test
	public void testOperatorDevide() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 4 / 2";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 2, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 / null";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "B = 3 / 0";
			ExpressionTester.evaluateExpression(jh, expression, Code.OPERATION_NOT_DEFINED);
			assertNull(jh.get("B", Integer.class));
		}
	}

	@Test
	public void testValuesWithNamespacePrefix() {
		{
			final TDataArg jh = new TDataArg();
			;
			final String expression = "Namespace:A = 2; Namespace:B = 4 * Namespace:A";
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals((Integer) 8, jh.get("Namespace:B", Integer.class));
		}
	}

	@Test
	public void testEscapedSemicolonProblem() {

		// String expression = "Result = (1, 2); Result = IF(2 > 1, (A = 3 , B = 4), \"test\")";
		// String expression = "Result = (1, 2); Result = IF(2 > 1, A = 3 \; B = 4, \"test\")";

		final String expression = "Result = (A = 1 %&? B = \"TEST\" # 3 * 4 - 2)";

		final TDataArg jh = new TDataArg();
		;
		ExpressionTester.evaluateExpression(jh, expression, Code.UNEXPECTED_TOKEN, // %
				Code.UNEXPECTED_TOKEN, // ?
				Code.UNRECOGNIZED_TOKEN, // ?
				Code.UNEXPECTED_TOKEN, // B
				Code.UNEXPECTED_TOKEN,// ? B=
				Code.UNRECOGNIZED_TOKEN, // #
				Code.UNEXPECTED_TOKEN);

		System.out.println(jh);
	}

	@Test
	public void testBracketLevelFailures() {

		// String expression = "Result = (1, 2); Result = IF(2 > 1, (A = 3 , B = 4), \"test\")";
		// String expression = "Result = (1, 2); Result = IF(2 > 1, A = 3 \; B = 4, \"test\")";

		final String expression = "Result =  ((3 * 2) + IF(3>2, 4";
		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;
		ExpressionTester.evaluateExpression(jh, expression, Code.BRACKETS_DO_NOT_SUM_UP);
		System.out.println(jh);
	}

	@Test
	public void testUnknwonToken() {

		// String expression = "Result = (1, 2); Result = IF(2 > 1, (A = 3 , B = 4), \"test\")";
		// String expression = "Result = (1, 2); Result = IF(2 > 1, A = 3 \; B = 4, \"test\")";

		final String expression = "Result =  (A = 4 \\; B = 3 ); D = 1+2";
		// String expression = "Result = 2 * * +3; A = 2 **3";
		// String expression = "A = B + C DE * 4 +/ 5;\nResult = 2 * * +3;\nX = 3 * ((4 + 12) -17 *
		// 3;\nT = DUMM(1, 2 , 3);\nR = 3 + 1) ### (C" ;
		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;
		ExpressionTester.evaluateExpression(jh, expression, Code.BRACKETS_DO_NOT_SUM_UP,
				Code.BRACKET_CLOSE_WITHOUT_OPEN, Code.BRACKETS_DO_NOT_SUM_UP);
		System.out.println(jh);
	}

	@Test
	public void testUnknwonToken1() {

		// String expression = "Result = (1, 2); Result = IF(2 > 1, (A = 3 , B = 4), \"test\")";
		// String expression = "Result = (1, 2); Result = IF(2 > 1, A = 3 \; B = 4, \"test\")";

		// String expression = "M.RefuelingNotificationReport.FirstETicket =
		// 2;M.RefuelingNotificationReport.FirstETicket = ;";
		// String expression = "M.RefuelingNotificationReport.FirstETicket = ;";
		final String expression = "M.RefuelingNotificationReport.FirstETicket = ;";
		// String expression = "Result = 2 * * +3; A = 2 **3";
		// String expression = "A = B + C DE * 4 +/ 5;\nResult = 2 * * +3;\nX = 3 * ((4 + 12) -17 *
		// 3;\nT = DUMM(1, 2 , 3);\nR = 3 + 1) ### (C" ;
		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;
		ExpressionTester.evaluateExpression(jh, expression, Code.MISPLACED_OPERATOR);
		System.out.println(jh);
	}

	@Test
	public void testUnknwonToken2() {

		// String expression = "Result = (1, 2); Result = IF(2 > 1, (A = 3 , B = 4), \"test\")";
		// String expression = "Result = (1, 2); Result = IF(2 > 1, A = 3 \; B = 4, \"test\")";

		final String expression = "M.Header.Id = DEC64(\"ABC\", 2, 3); EFG B =  MATCHES(\"abc\", 3 + 4, (1,2,3)) + 2;"
				+ "M.Header.TEST = IF(M.Header.Id, SUB(\"TEST\", 3), ((2+3) * 5) - 7, \"true\", \"false\", 3 + 1); B = MATCHES(\"abc\", 123)";

		// String expression = "M.RefuelingNotificationReport.FirstETicket =
		// 2;M.RefuelingNotificationReport.FirstETicket = ;";
		// String expression = "Result = 2 * * +3; A = 2 **3";
		// String expression = "A = B + C DE * 4 +/ 5;\nResult = 2 * * +3;\nX = 3 * ((4 + 12) -17 *
		// 3;\nT = DUMM(1, 2 , 3);\nR = 3 + 1) ### (C" ;
		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;
		// ExpressionTester_NEW.evaluateExpression(jh, expression,
		// ExpressionInfo.Code.WrongParameterCount);
		System.out.println(jh);
	}

	@Test
	public void testTokenizeErrorMisplacedOperator() {

		final String expression = "A = 1; B = 2+ A */ 3";

		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;

		final Errors errors = ExpressionTester.evaluateExpression(jh, expression, Code.MISPLACED_OPERATOR);
		System.out.println(jh);

		assertEquals(1, errors.parseErrors.size());
		assertEquals(0, errors.runtimeErrors.size());

		assertEquals(1, errors.parseErrors.get(0).getLineNumber());
		assertEquals("line 1: 'B = 2 + A * /'<- " + Code.MISPLACED_OPERATOR + "[/]",
				errors.parseErrors.get(0).toString());

		assertTrue(errors.parseErrors.get(0) instanceof ExprInfoTokenizeError);
		final ExprInfoTokenizeError error = (ExprInfoTokenizeError) errors.parseErrors.get(0);

		assertEquals(Code.MISPLACED_OPERATOR, error.getCode());
	}

	@Test
	public void testTokenizeErrorOperatorNotAllowed() {

		final String expression = "A = 1; B = 2+ A *-/ 3";

		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;

		final Errors errors = ExpressionTester.evaluateExpression(jh, expression, Code.OPERATOR_NOT_ALLOWED);
		System.out.println(jh);

		assertEquals(1, errors.parseErrors.size());
		assertEquals(0, errors.runtimeErrors.size());

		assertEquals(1, errors.parseErrors.get(0).getLineNumber());
		assertEquals("line 1: 'B = 2 + A * - /'<- " + Code.OPERATOR_NOT_ALLOWED + "[/]",
				errors.parseErrors.get(0).toString());

		assertTrue(errors.parseErrors.get(0) instanceof ExprInfoTokenizeError);
		final ExprInfoTokenizeError error = (ExprInfoTokenizeError) errors.parseErrors.get(0);

		assertEquals(Code.OPERATOR_NOT_ALLOWED, error.getCode());
	}

	@Test
	public void testKeyStructWithNamespacePrefix() {
		final String expression = "M.Test.foxns2:Bla = \"Test\";";

		final TDataArg jh = new TDataArg();
		;
		final Errors errors = ExpressionTester.evaluateExpression(jh, expression);
		System.out.println(jh);

		assertEquals(0, errors.parseErrors.size());
		assertEquals(0, errors.runtimeErrors.size());

		assertEquals("Test", jh.get("M.Test.foxns2:Bla", String.class));

		// assertEquals(0, errors.parseErrors.get(0).getLineNumber());
		// assertEquals("line 0: 'M.Test.foxns2:Bla'<- UnrecognizedToken[M.Test.foxns2:Bla]",
		// errors.parseErrors.get(0).toString());
		//
		// System.out.println(errors.parseErrors.get(0).getClass().getSimpleName());
		// assertTrue(errors.parseErrors.get(0) instanceof ExpressionInfoCreateStackError);
		// ExpressionInfoCreateStackError error = (ExpressionInfoCreateStackError)
		// errors.parseErrors.get(0);
		//
		// assertEquals(Code.UnrecognizedToken, error.getCode());
	}

	@Test
	public void testTokenizeErrorUnclosedString() {
		final String expression = "A = \"Test;";

		final TDataArg jh = new TDataArg();
		;
		final Errors errors = ExpressionTester.evaluateExpression(jh, expression, Code.UNCLOSED_STRING);
		System.out.println(jh);

		assertEquals(1, errors.parseErrors.size());
		assertEquals(0, errors.runtimeErrors.size());

		assertEquals(0, errors.parseErrors.get(0).getLineNumber());
		assertEquals("line 0: 'A = \"Test;'<- " + Code.UNCLOSED_STRING + "[\"Test;]",
				errors.parseErrors.get(0).toString());

		assertTrue(errors.parseErrors.get(0) instanceof ExprInfoTokenizeError);
		final ExprInfoTokenizeError error = (ExprInfoTokenizeError) errors.parseErrors.get(0);

		assertEquals(Code.UNCLOSED_STRING, error.getCode());
	}

	@Test
	public void testErrorWrongParameterCount() {

		final String expression = "A = 1; B = ABS(A, 2)";

		System.out.println(expression);
		final TDataArg jh = new TDataArg();
		;

		final Errors errors = ExpressionTester.evaluateExpression(jh, expression, ExprInfo.Code.WRONG_PARAMETER_COUNT,
				ExprInfo.Code.WRONG_PARAMETER_COUNT);
		System.out.println(jh);

		assertEquals(1, errors.parseErrors.size());
		assertEquals(1, errors.runtimeErrors.size());

		// ---- PARSE ERROR
		assertEquals(1, errors.parseErrors.get(0).getLineNumber());
		assertEquals(
				"line 1: 'B = ABS(A , 2)'<- " + ExprInfo.Code.WRONG_PARAMETER_COUNT + "=2 (expected=1)[= ABS(A , 2)]",
				errors.parseErrors.get(0).toString());

		assertTrue(errors.parseErrors.get(0) instanceof ExprInfoWrongParameterCount);
		ExprInfoWrongParameterCount error = (ExprInfoWrongParameterCount) errors.parseErrors.get(0);

		assertEquals(Code.WRONG_PARAMETER_COUNT, error.getCode());

		assertEquals(1, error.getMinimum());
		assertEquals((Integer) 1, error.getMaximum());
		assertEquals(2, error.getReceivedCount());

		// ---- RUNTIME ERROR
		assertEquals(1, errors.runtimeErrors.get(0).getLineNumber());
		assertEquals("line 1: 'B = ABS(A , 2)'<- " + Code.WRONG_PARAMETER_COUNT + "=2 (expected=1)[= ABS(A , 2)]",
				errors.runtimeErrors.get(0).toString());

		assertTrue(errors.runtimeErrors.get(0) instanceof ExprInfoWrongParameterCount);
		error = (ExprInfoWrongParameterCount) errors.parseErrors.get(0);

		assertEquals(Code.WRONG_PARAMETER_COUNT, error.getCode());

		assertEquals(1, error.getMinimum());
		assertEquals((Integer) 1, error.getMaximum());
		assertEquals(2, error.getReceivedCount());

	}

	/**
	 * causes index out of bounds exception
	 */
	@Test
	public void testIndexOutOfBounds() {

		// String input = "abc";
		// int beginIndex = 4;
		//
		// System.out.println(input.length());
		//
		// if(beginIndex <= input.length()) {
		// System.out.println(input.substring(beginIndex)+ "<<<<<<<<<<<<<<<<<<<<<<<");
		// }

		final String expression =
				// "E.ErrorReason = \"Missing mandatory element:\";" +
				// "E.ErrorReason += IF(M.Indicator.IsReportIntervalGiven, \"\", \"
				// ReportInterval\");" +
				"E.ErrorReason += IF(M.Indicator.IsReportIntervalGiven\", \"\", \" ReportInterval\");";
		// "E.ErrorReason += IF(M.Indicator.IsCoordinatesRequested, \"\", \" Coordinates,\");" +
		// "E.ErrorReason += IF(M.Indicator.IsAltitudeRequested, \"\", \" Altitude\")";

		final TDataArg jh = new TDataArg();
		;
		ExpressionTester.evaluateExpression(jh, expression, Code.UNRECOGNIZED_TOKEN, // M.Indicator.IsReportIntervalGiven",
																					 // "
				Code.UNEXPECTED_TOKEN, // ", "
				Code.UNEXPECTED_TOKEN, // ReportInterval
				Code.UNCLOSED_STRING, // ");
				Code.BRACKETS_DO_NOT_SUM_UP // (
		);
		System.out.println(jh);

	}

	@Test
	public void testMatchesWithInternalQuotes() {

		// String expression = "M.ACMFRequest.RequestCode == null ||
		// MATCHES(M.ACMFRequest.RequestCode,\"[A-Za-z0-9
		// #()*+,-./:!\"\"$%&'\;<=>?@\[\\\]^_`\{\|\}~\n\r]{1,200}\") != b\"true\"";
		final String expression = "result = (M.ACMFRequest.RequestCode == null || MATCHES(M.ACMFRequest.RequestCode,\"[A-Za-z0-9 #()*+,-./:!\\\"$%&'\\;<=>@\\[\\\\\\]^_`\\{\\|\\}~\\n\\r]{1,200}\") != b\"true\")";
		// final String expression = "result = (M.ACMFRequest.RequestCode == null ||
		// MATCHES(M.ACMFRequest.RequestCode,\"[A-Za-z0-9
		// #()*+,-./:!\\\"$%&'\\;<=>@\\[\\\\\\]^_`\\{\\|\\}~\\n\\r]{1,200}\") != true)";

		final TDataArg jh = new TDataArg();
		;
		final String input = "ABC #()+,-./:!\"$%&';<=>@[]abc\\^_`{|}~\r\n0123456789";
		jh.put("M.ACMFRequest.RequestCode", input);
		// TODO: implement MATCHES
		ExpressionTester.evaluateExpression(jh, expression, Code.UNKNOWN_FUNCTION);
		System.out.println(expression);
		System.out.println(input);
		System.out.println(jh);

		// TODO: FIXME!
		// assertFalse(jh.get("result", Boolean.class));
	}

	@Test
	public void testMatchesEscapes() {

		// String expression = "M.ACMFRequest.RequestCode == null ||
		// MATCHES(M.ACMFRequest.RequestCode,\"[A-Za-z0-9
		// #()*+,-./:!\"\"$%&'\;<=>?@\[\\\]^_`\{\|\}~\n\r]{1,200}\") != b\"true\"";
		final String expression = "result = \"[A-Za-z0-9 #()*+,-./:!\\\"$%&';<=>@\\[\\\\\\\\\\]^_`\\{\\|\\}~\\n\\r]{1,200}\";";
		System.out.println(expression);

		final TDataArg jh = new TDataArg();
		;
		// String input = "ABC #()+,-./:!\"$%&';<=>@[]abc\\^_`{|}~\r\n0123456789";
		// jh.setString("M.ACMFRequest.RequestCode", input);
		ExpressionTester.evaluateExpression(jh, expression);
		// System.out.println(input);
		System.out.println(jh);
		System.out.println(jh.get("result", String.class));

	}

	@Test
	public void testEscapebackSlash() {

		// String expression = "M.ACMFRequest.RequestCode == null ||
		// MATCHES(M.ACMFRequest.RequestCode,\"[A-Za-z0-9
		// #()*+,-./:!\"\"$%&'\;<=>?@\[\\\]^_`\{\|\}~\n\r]{1,200}\") != b\"true\"";
		final String expression = "result = \"\\\\\\\\\";";
		System.out.println(expression);

		final TDataArg jh = new TDataArg();
		;
		// String input = "ABC #()+,-./:!\"$%&';<=>@[]abc\\^_`{|}~\r\n0123456789";
		// jh.setString("M.ACMFRequest.RequestCode", input);
		ExpressionTester.evaluateExpression(jh, expression);
		// System.out.println(input);
		System.out.println(jh);
		System.out.println(jh.get("result", String.class));
	}

	@Test
	public void testSubstructures() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "A.FirstName = \"Hans\";" //
					+ "A.SecondName = \"Schuster\";" //
					+ "B.FirstName =\"Hermine\";" //
					+ "B.SecondName = \"Meier\";" //
					+ "M.AddA = A;" //
					+ "M.AddB = B;" //
			;
			ExpressionTester.evaluateExpression(jh, expression);

			assertEquals("Hans", jh.get("A.FirstName", String.class));
			assertEquals("Schuster", jh.get("A.SecondName", String.class));
			assertEquals("Hermine", jh.get("B.FirstName", String.class));
			assertEquals("Meier", jh.get("B.SecondName", String.class));

			assertEquals("Hans", jh.get("%.AddA.FirstName", String.class));
			assertEquals("Schuster", jh.get("%.AddA.SecondName", String.class));
			assertEquals("Hermine", jh.get("%.AddB.FirstName", String.class));
			assertEquals("Meier", jh.get("%.AddB.SecondName", String.class));

			// change one name in the original structure:
			ExpressionTester.evaluateExpression(jh, "B.SecondName = \"Schulze\"");

			assertEquals("Hans", jh.get("A.FirstName", String.class));
			assertEquals("Schuster", jh.get("A.SecondName", String.class));
			assertEquals("Hermine", jh.get("B.FirstName", String.class));
			// Name in original structure has changed:
			assertEquals("Schulze", jh.get("B.SecondName", String.class));

			assertEquals("Hans", jh.get("%.AddA.FirstName", String.class));
			assertEquals("Schuster", jh.get("%.AddA.SecondName", String.class));
			assertEquals("Hermine", jh.get("%.AddB.FirstName", String.class));
			// Name in copied structure has NOT changed:
			assertEquals("Meier", jh.get("%.AddB.SecondName", String.class));

			// change one name in the copied structure:
			ExpressionTester.evaluateExpression(jh, "%.AddB.FirstName = \"Kunigunde\"");

			assertEquals("Hans", jh.get("A.FirstName", String.class));
			assertEquals("Schuster", jh.get("A.SecondName", String.class));
			// Name in original structure has NOT changed:
			assertEquals("Hermine", jh.get("B.FirstName", String.class));
			assertEquals("Schulze", jh.get("B.SecondName", String.class));

			assertEquals("Hans", jh.get("%.AddA.FirstName", String.class));
			assertEquals("Schuster", jh.get("%.AddA.SecondName", String.class));
			// Name in copied structure has changed:
			assertEquals("Kunigunde", jh.get("%.AddB.FirstName", String.class));
			assertEquals("Meier", jh.get("%.AddB.SecondName", String.class));
		}
	}

	@Test
	public void testListAccess() {

		{
			final List<Integer> list = Arrays.asList(2, 4, 6, 8);
			final HashMap<String, Object> map = new LinkedHashMap<>();

			map.put("A", list);

			final TDataArg jh = new TDataArg("A", map);
			// final String expression = "A = (2,4,6,8);" //
			// ;
			// ExpressionTester.evaluateExpression(jh, expression);

			System.out.println(jh);

			final TDataArg njh = jh.create(jh.get());

			System.out.println(njh);

			System.out.println(njh.get(1));
			System.out.println(njh.get("2"));
		}
	}

}
