package com.lienisoft.mtr.expressionInterpreter.main;

public class Main {

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			final TDataArg argument = new TDataArg();
			for (final String expressionStr : args) {
				final Object retValue = ExpressionProcessor.evaluateExpression(argument, expressionStr);
				System.out.println(retValue);
			}
		} else {
			System.out.println("Enter at least one argument expression!");
		}
	}

}
