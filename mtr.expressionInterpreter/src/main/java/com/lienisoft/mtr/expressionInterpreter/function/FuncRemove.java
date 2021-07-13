package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncRemove extends Func {

	public FuncRemove() {
		super(1, 1);
	}

	public static final String name = "REMOVE";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		final Object key = funcArg.getKey();
		if (key != null) {
			funcArg.remove(key);
			return funcArg.create(true);
		}
		return funcArg.create(false);
	}
}
