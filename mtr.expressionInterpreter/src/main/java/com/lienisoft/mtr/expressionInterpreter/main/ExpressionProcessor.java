package com.lienisoft.mtr.expressionInterpreter.main;

import java.util.List;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;
import com.lienisoft.mtr.expressionInterpreter.function.FuncFactory;
import com.lienisoft.mtr.expressionInterpreter.parse.ExprArgFactory;
import com.lienisoft.mtr.expressionInterpreter.parse.ExprParser;
import com.lienisoft.mtr.expressionInterpreter.stack.ExprStack;

/**
 * Simple Expression Processor
 * Input Expression or List of expressions separated by ";".
 * Output Result of the last expression
 *
 * @author U083950
 */
public class ExpressionProcessor {

	/**
	 * this method may be used in case there is only one expression in expressionStr or when no
	 * exception is expected
	 *
	 * @param valStruct
	 * @param expressionStr
	 * @param expectedExceptions expected exceptions. Must enter parsing as well as runtime
	 *            exceptions here!
	 * @return
	 */
	static public Object evaluateExpression(TArgument data, String expressionStr) {

		// StringBuilder errors = new StringBuilder();

		final FuncFactory functionFactory = new FuncFactory(new ValFactorySimple(data));
		final ExprArgFactory valueFactory = new ExprArgFactory(data);
		final ExprParser expressionParser = new ExprParser(valueFactory, functionFactory);

		final List<ExprStack> expressionStackList = expressionParser.parse(expressionStr);

		// final int index = 0;
		for (final ExprStack expressionStack : expressionStackList) {
			if (expressionStack != null) {
				if (expressionStack != null && expressionStack.getErrors() != null) {
					// errors.parseErrors.addAll(expressionStack.getErrors());
					System.err.println(expressionStack.getErrors());
					return null;
				}
			}
		}

		TArgument result = null;
		for (final ExprStack expressionStack : expressionStackList) {
			if (expressionStack != null) {
				// if (expectedExceptions != null && expectedExceptions.length > index) {
				// expectedExpressionReceived[index] = expectedExceptions[index] == null;
				// }
				try {
					// expressionStack.getResult(new TVar("ROOT", data));
					result = expressionStack.getResult(data);
				} catch (final ExprException e) {
					/**
					 * check runtime errors:
					 */
					// errors.runtimeErrors.add(e.getExpressionInfo());
					System.err.println("Runtime Error:");
					System.err.println(e.getExpressionInfo());
					return null;
				}
			}
		}

		return result != null ? result.get() : null;
	}

	// static public TArgument evaluateSingleExpression(TArgument data, String expressionStr,
	// ExprInfo.Code... expectedExceptions) {
	//
	// // StringBuilder errors = new StringBuilder();
	//
	// final FuncFactory functionFactory = new FuncFactory(new ValFactorySimple(data));
	// final ExprArgFactory valueFactory = new ExprArgFactory(data);
	// final ExprParser expressionParser = new ExprParser(valueFactory, functionFactory);
	//
	// final Errors errors = new Errors();
	//
	// final List<ExprStack> expressionStackList = expressionParser.parse(expressionStr);
	//
	// int index = 0;
	// for (final ExprStack expressionStack : expressionStackList) {
	// if (expressionStack != null) {
	// assertEquals(index++, expressionStack.lineNumber);
	// if (expressionStack != null && expressionStack.getErrors() != null) {
	// errors.parseErrors.addAll(expressionStack.getErrors());
	// }
	// }
	// }
	//
	// /**
	// * index of exception
	// */
	// index = 0;
	//
	// Boolean[] expectedExpressionReceived = null;
	// if (expectedExceptions != null) {
	// expectedExpressionReceived = new Boolean[expectedExceptions.length];
	// }
	//
	// /**
	// * check parser errors:
	// */
	// if (errors.parseErrors.size() > 0) {
	// for (final ExprInfo expressionInfo : errors.parseErrors) {
	// System.out.println("Parser Error:");
	// System.out.println(expressionInfo);
	// checkException(expectedExpressionReceived, expressionInfo.getCode(), index++,
	// expectedExceptions);
	// }
	// }
	//
	// // for (final ExprStack expressionStack : expressionStackList) {
	// final ExprStack expressionStack = expressionStackList.get(0);
	// TArgument result = null;
	// if (expressionStack != null) {
	// try {
	// // result = expressionStack.getResult(new TVar("ROOT", data));
	// result = expressionStack.getResult(data);
	// } catch (final ExprException e) {
	// /**
	// * check runtime errors:
	// */
	// errors.runtimeErrors.add(e.getExpressionInfo());
	// System.out.println("Runtime Error:");
	// System.err.println(e.getExpressionInfo());
	//
	// checkException(expectedExpressionReceived, e.getCode(), index++, expectedExceptions);
	// }
	// }
	// // }
	//
	// /**
	// * check for exceptions expected but not received
	// */
	// if (expectedExpressionReceived != null) {
	// index = 0;
	// for (final Boolean expectedReceived : expectedExpressionReceived) {
	// if (expectedReceived == null || expectedReceived == false) {
	// fail(" - exception expected (" + index + "):" + expectedExceptions[index]);
	// }
	// index++;
	// }
	// }
	//
	// return result;
	// }

	// static void checkException(Boolean[] expectedExpressionReceived, ExprInfo.Code code, int
	// index,
	// ExprInfo.Code[] expected) {
	// if (expected != null && expected.length > index && expected[index] != null) {
	// assertEquals(expected[index], code);
	// expectedExpressionReceived[index] = true;
	// } else {
	//
	// ExprInfo.Code exCode = null;
	// if (expected != null && expected.length > index) {
	// exCode = expected[index];
	// }
	// fail("other exception expected: " + exCode + " but got: " + code);
	// }
	// }

	// // Helper - test:
	// static Date newDate(String set) {
	// Date dt = new Date();
	// // Festlegung des Formats:
	// final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	// df.setTimeZone(TimeZone.getTimeZone("UTC"));
	// try {
	// // Einlesen vom String:
	// dt = df.parse(set);
	// } catch (final ParseException e) {
	// e.printStackTrace();
	// }
	// return dt;
	// }
	//
	// static String format(Date date, String format) {
	// final SimpleDateFormat df = new SimpleDateFormat(format);
	// df.setTimeZone(TimeZone.getTimeZone("UTC"));
	// return df.format(date);
	// }
	//
	// public static void assertParameterCount(Errors errors, int count, int min, Integer max) {
	// assertEquals(1, errors.parseErrors.size());
	// assertEquals(0, errors.parseErrors.get(0).getLineNumber());
	// assertTrue(errors.parseErrors.get(0) instanceof ExprInfoWrongParameterCount);
	//
	// assertEquals(1, errors.runtimeErrors.size());
	// assertEquals(0, errors.runtimeErrors.get(0).getLineNumber());
	// assertTrue(errors.runtimeErrors.get(0) instanceof ExprInfoWrongParameterCount);
	//
	// assertParameterCount((ExprInfoWrongParameterCount) errors.parseErrors.get(0), count, min,
	// max);
	// assertParameterCount((ExprInfoWrongParameterCount) errors.runtimeErrors.get(0), count, min,
	// max);
	// }
	//
	// public static void assertParameterCount(ExprInfoWrongParameterCount wc, int count, int min,
	// Integer max) {
	// assertEquals(min, wc.getMinimum());
	// assertEquals(max, wc.getMaximum());
	// assertEquals(count, wc.getReceivedCount());
	//
	// final boolean condition1 = (count >= min);
	// boolean condition2 = true;
	//
	// if (max != null) {
	// condition2 = (count <= max);
	// }
	//
	// /** at least one condition must be false **/
	// assertTrue(!condition1 || !condition2);
	// }
	//
	// public static void assertTypeMismatch(Errors errors, int i, String received, String expected)
	// {
	// assertEquals(0, errors.parseErrors.size());
	//
	// assertEquals(1, errors.runtimeErrors.size());
	// assertEquals(0, errors.runtimeErrors.get(0).getLineNumber());
	// assertTrue(errors.runtimeErrors.get(0) instanceof ExprInfoTypeMismatch);
	//
	// final ExprInfoTypeMismatch mis = (ExprInfoTypeMismatch) errors.runtimeErrors.get(0);
	//
	// assertEquals(i, mis.getIndex());
	// assertEquals(expected, mis.getExpectedType());
	// assertEquals(received, mis.getReceivedType());
	//
	// // assertParameterCount((ExpressionInfoWrongParameterCount) errors.parseErrors.get(0),
	// // count, min, max);
	// // assertParameterCount((ExpressionInfoWrongParameterCount) errors.runtimeErrors.get(0),
	// // count, min, max);
	// }
	//
	// public static void assertInvalidExpressionValue(Errors errors, int i, String info) {
	// assertEquals(0, errors.parseErrors.size());
	//
	// assertEquals(1, errors.runtimeErrors.size());
	// assertEquals(0, errors.runtimeErrors.get(0).getLineNumber());
	// assertTrue(errors.runtimeErrors.get(0) instanceof ExprInfoInvalidValue);
	//
	// final ExprInfoInvalidValue mis = (ExprInfoInvalidValue) errors.runtimeErrors.get(0);
	//
	// assertEquals(i, mis.getIndex());
	// assertEquals(info, mis.getInfo());
	// }

}
