package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionStartsWithTest {

	@Test
	public void test() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"LABER\", \"LA\")";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"LABER\", \"AB\", 1)";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"LABER\", \"LAX\")";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"LABER\", \"AB\", 2)";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"\", \"\", 2)";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"\", \"\")";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"a\", \"\")";
			ExpressionTester.evaluateExpression(jh, expression);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"\", \"a\")";
			ExpressionTester.evaluateExpression(jh, expression);
			assertFalse(jh.get("B", Boolean.class));
		}

	}

	@Test
	public void testParameterListSize() {
		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(1,2,3, 4)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=4 (expected=2-3)"));
			ExpressionTester.assertParameterCount(errors, 4, 2, 3);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(01)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=1 (expected=2-3)"));
			ExpressionTester.assertParameterCount(errors, 1, 2, 3);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH()";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
			// assertTrue(errors.contains("invalid parameter count=0 (expected=2-3)"));
			ExpressionTester.assertParameterCount(errors, 0, 2, 3);
		}

	}

	@Test
	public void testParameterType() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(1<2,2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:Boolean (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Boolean", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(2,2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:Integer (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, "Integer", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(null,2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[0]:null (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 0, null, "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"asd\",1<2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[1]:Boolean (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 1, "Boolean", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"asd\",2)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[1]:Integer (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 1, "Integer", "String");
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = STARTSWITH(\"123\", null)";
			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
					Code.TYPE_MISMATCH);
			// assertTrue(errors.contains("invalid parameter[1]:null (expected:String)"));
			ExpressionTester.assertTypeMismatch(errors, 1, null, "String");
		}
	}
}
