package com.lienisoft.mtr.expressionInterpreter.function;

import java.util.List;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncLen extends Func {

	public FuncLen() {
		super(1, 1);
	}

	public static final String name = "LEN";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		// calculate String length if argument is a string or list!

		final String s = funcArg.tryGet(0, String.class);
		// return String length if argument is String
		if (s != null) {
			return funcArg.create(s.length());
		}
		final List<?> l = funcArg.tryGet(0, List.class);
		// return list size if argument is list
		if (l != null) {
			return funcArg.create(l.size());
		}
		// return 0 if argument's value is null
		if (funcArg.getObj(0) == null) {
			return funcArg.create(0);
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
