package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncStartsWith extends Func {

	public FuncStartsWith() {
		super(2, 3);
	}

	public static final String name = "STARTSWITH";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {

		final String fullString = funcArg.tryGet(0, String.class);
		if (fullString != null) {
			final String startString = funcArg.tryGet(1, String.class);
			if (startString != null) {
				if (funcArg.size() == 3) {
					final Integer offset = funcArg.tryGet(2, Integer.class);
					if (offset != null) {
						return funcArg.create(fullString.startsWith(startString, offset));
					}
				} else {
					return funcArg.create(fullString.startsWith(startString));
				}
			}
		}
		return null;
	}
}
