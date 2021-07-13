package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionIfTest {

	@Test
	public void test() {
		TDataArg jh = new TDataArg();

		String expression = "B = 1 < 2; IF(2>3, X = (3, A= 2 +9), A = 1)";
		// String expression = "C = \"ABCD\"; A = (C, 2); D = SUB(A)";

		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);

		assertEquals(true, jh.get("B", Boolean.class));
		assertEquals((Integer) 1, jh.get("A", Integer.class));

		expression = "B = true; IF(B, X = (3, A= 2 +9), A = 1)";
		// String expression = "C = \"ABCD\"; A = (C, 2); D = SUB(A)";
		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);

		assertEquals(true, jh.get("B", Boolean.class));
		assertEquals((Integer) 11, jh.get("A", Integer.class));
		assertEquals((Integer) 3, jh.get("X.0", Integer.class));
		assertEquals((Integer) 11, jh.get("X.1", Integer.class));

		expression = "B = true; C = IF(B, \"GAGA\", (27, 123 + 4))";
		// String expression = "C = \"ABCD\"; A = (C, 2); D = SUB(A)";
		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);

		assertEquals(true, jh.get("B", Boolean.class));
		assertEquals("GAGA", jh.get("C", String.class));

		expression = "B = true; C = IF(B == false, \"GAGA\", (27, 123 + 4))";
		// String expression = "C = \"ABCD\"; A = (C, 2); D = SUB(A)";
		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, expression);

		System.out.println(jh);

		assertEquals(true, jh.get("B", Boolean.class));
		assertEquals((Integer) 27, jh.get("C.0", Integer.class));
		assertEquals((Integer) 127, jh.get("C.1", Integer.class));

		// assertEquals((Integer) 123, jh.getInteger("B"));
		// assertEquals((Integer) 23, jh.getInteger("C"));
	}

	// @Test
	// public void testParameterListSize() {
	// {
	// final TDataArg jh = new TDataArg();
	// String expression = "B = ABS(1,2)";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter count=2 (expected=1)"));
	// }
	//
	// {
	// final TDataArg jh = new TDataArg();
	// String expression = "B = ABS(1,2,3)";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter count=3 (expected=1)"));
	// }
	//
	// {
	// final TDataArg jh = new TDataArg();
	// String expression = "B = ABS()";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter count=0 (expected=1)"));
	// }
	// }
	//
	// @Test
	// public void testParameterType() {
	// {
	// final TDataArg jh = new TDataArg();
	// String expression = "B = ABS(\"123\")";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter[0]:String (expected:Integer)"));
	// }
	//
	// {
	// final TDataArg jh = new TDataArg();
	// String expression = "B = ABS(1<2)";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter[0]:Boolean (expected:Integer)"));
	// }
	//
	// {
	// final TDataArg jh = new TDataArg();
	// //jh.setObject("C", null);
	// String expression = "B = ABS(null)";
	// ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
	// Code.InvalidExpressionValue);
	// assertTrue(errors.contains("invalid parameter[0]:null (expected:Integer)"));
	// }
	// }

	@Test
	public void testSpecialComparisons() {

		// Compare different Types:
		TDataArg jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, " A = 1; B = \"1\"; IF(A == B,C = \"A == B\", C = \"A != B\")");
		System.out.println(jh);
		assertEquals("A != B", jh.get("C", String.class));

		// Compare with null Types:
		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, " A = null; B = \"1\"; IF(A == B,C = \"A == B\", C = \"A != B\")");
		System.out.println(jh);
		assertEquals("A != B", jh.get("C", String.class));

		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, " A = null; B = null; IF(A == B,C = \"A == B\", C = \"A != B\")");
		System.out.println(jh);
		assertEquals("A == B", jh.get("C", String.class));

		jh = new TDataArg();
		ExpressionTester.evaluateExpression(jh, " A = 123; B = null; IF(A == B,C = \"A == B\", C = \"A != B\")");
		System.out.println(jh);
		assertEquals("A != B", jh.get("C", String.class));

	}

}
