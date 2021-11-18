package com.lienisoft.mtr.expressionInterpreter.function;

import java.util.regex.Pattern;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FunctionFind extends Func {

	public FunctionFind() {
		super(2, 2);
	}
	public static final String name = "FIND";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		String argument =  funcArg.tryGet(0, String.class);
		if (argument != null) {
			String pattern =  funcArg.tryGet(1, String.class);
			if (pattern != null) {
				final Pattern r = Pattern.compile(pattern);
				return funcArg.create(r.matcher(argument).find());
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}