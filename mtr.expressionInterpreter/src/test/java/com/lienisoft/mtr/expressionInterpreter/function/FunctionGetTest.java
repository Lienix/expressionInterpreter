package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionGetTest {

	@Test
	public void test() {

		/**
		 * test with integer values
		 */
		final TDataArg jh = new TDataArg();

		jh.put("M.test.val1", "ABC");
		jh.put("M.test.val2", "DEF");

		System.out.println("jh:" + jh);

		ExpressionTester.evaluateExpression(jh, "P.result.test.get = GET(\"M.test.val1\") + \"xxx\"");
		assertEquals("ABCxxx", jh.get("P.result.test.get", String.class));

		jh.put("M.test.key", "val1");

		ExpressionTester.evaluateExpression(jh, "P.result.test.get = GET(\"M.test.\"+M.test.key) + \"yyy\"");
		assertEquals("ABCyyy", jh.get("P.result.test.get", String.class));

		jh.put("M.test.key", "val2");

		ExpressionTester.evaluateExpression(jh, "P.result.test.get = GET(\"M.test.\"+M.test.key) + \"zzz\"");
		assertEquals("DEFzzz", jh.get("P.result.test.get", String.class));

		/**
		 * unknown key
		 */
		ExpressionTester.evaluateExpression(jh, "P.result.test.get = GET(\"gagaga\")");
		assertEquals(null, jh.get("P.result.test.get", String.class));

		/**
		 * unknown key + String"zzz"
		 */
		ExpressionTester.evaluateExpression(jh, "P.result.test.get = GET(\"M.test.unknown\") + \"zzz\"");
		assertEquals("zzz", jh.get("P.result.test.get", String.class));
	}

	@Test
	public void testAssign() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "GET(\"A.\" + 0) = 123; GET(\"A.\" + 1) = 456";
			ExpressionTester.evaluateExpression(jh, expression);

			System.out.println(jh);

			assertNotNull(jh.get("A"));
			assertEquals((Integer) 123, jh.get("A.0", Integer.class));
			assertEquals((Integer) 456, jh.get("A.1", Integer.class));
		}
	}

	@Test
	public void testParameterListSize() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET(\"ABC\", \"ISO-GAGA\", 2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=3 (expected=1)"));
			ExpressionTester.assertParameterCount(errors, 3, 1, 1);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET(\"ABC\",\"ABC123\",\"ABC\", 0)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=4 (expected=1)"));
			ExpressionTester.assertParameterCount(errors, 4, 1, 1);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET()";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=0 (expected=1)"));
			ExpressionTester.assertParameterCount(errors, 0, 1, 1);
		}
	}

	@Test
	public void testParameterType() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET(1)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:Integer (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Integer", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET(2<1)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:Boolean (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Boolean", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GET(null)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:null (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, null, "String");
		}

	}

}
