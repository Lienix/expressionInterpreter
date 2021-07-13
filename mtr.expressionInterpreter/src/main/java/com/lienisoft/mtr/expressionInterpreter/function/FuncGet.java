package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncGet extends Func {

	public FuncGet() {
		super(1, 1);
	}

	public static final String name = "GET";

	@Override
	public TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		final String s = funcArg.tryGet(0, String.class);
		if (s != null) {
			final Object key = funcArg.createKey(s);
			if (key != null) {
				final TArgument argument = funcArg.createByKey(key);

				System.out.println("Argument: " + argument);

				return argument;
			}
		}
		return null;
	}

	@Override
	public boolean isLValue() {
		return true;
	};

	@Override
	public String toString() {
		return name;
	}
}
