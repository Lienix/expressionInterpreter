package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncAbs extends Func {

	public FuncAbs() {
		super(1, 1);
	}

	public static final String name = "ABS";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		final Integer value = funcArg.tryGet(0, Integer.class);
		if (value != null) {
			return funcArg.create(Math.abs(value));
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
