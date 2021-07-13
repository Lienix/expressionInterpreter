package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionAbsTest {

	@Test
	public void test() {
		final TDataArg jh = new TDataArg();
		final String expression = "B = ABS(-123); C = ABS(100 - B)";

		ExpressionTester.evaluateExpression(jh, expression);

		assertEquals((Integer) 123, jh.get("B", Integer.class));
		assertEquals((Integer) 23, jh.get("C", Integer.class));
	}

	@Test
	public void testParameterListSize() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS(1,2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			ExpressionTester.assertParameterCount(errors, 2, 1, 1);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS(1,2,3)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			ExpressionTester.assertParameterCount(errors, 3, 1, 1);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS()";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			ExpressionTester.assertParameterCount(errors, 0, 1, 1);
		}
	}

	@Test
	public void testParameterType() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS(\"123\")";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			ExpressionTester.assertTypeMismatch(errors, 0, "String", "Integer");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS(1<2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			ExpressionTester.assertTypeMismatch(errors, 0, "Boolean", "Integer");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = ABS(null)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			ExpressionTester.assertTypeMismatch(errors, 0, null, "Integer");
		}
	}

}
