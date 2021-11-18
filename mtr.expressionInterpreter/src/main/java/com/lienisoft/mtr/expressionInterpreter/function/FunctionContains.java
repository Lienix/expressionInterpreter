package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FunctionContains extends Func {

	public FunctionContains() {
		super(2, 2);
	}
	public static final String name = "CONTAINS";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		String argument =  funcArg.tryGet(0, String.class);
		if (argument != null) {
			String param =  funcArg.tryGet(1, String.class);
			if (param != null) {
				return funcArg.create(argument.contains(param));
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
