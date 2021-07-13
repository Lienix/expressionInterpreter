package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionLenTest {

	@Test
	public void test() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(null)";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals((Integer) 0, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "C.0 = null; C.1 = 1; C.2 = \"gaga\";B = LEN(C)";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals((Integer) 3, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "C=(null,1,\"blub\"); B = LEN(C);";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals((Integer) 3, jh.get("B", Integer.class));
		}
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(\"\")";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals((Integer) 0, jh.get("B", Integer.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(\"12345abc\")";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals((Integer) 8, jh.get("B", Integer.class));
		}
	}

	@Test
	public void testParameterListSize() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN()";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=1 (expected=3)"));
			ExpressionTester.assertParameterCount(errors, 0, 1, 1);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(1,2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=1 (expected=3)"));
			ExpressionTester.assertParameterCount(errors, 2, 1, 1);
		}
	}

	@Test
	public void testParameterType() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(1<2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:Boolean (expected:Integer or
			// String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Boolean", "String or List");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = LEN(1)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[1]:String (expected:Integer)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Integer", "String or List");
		}
	}
}
