package com.lienisoft.mtr.expressionInterpreter.parse;

import com.lienisoft.mtr.expressionInterpreter.function.Func;

public class ExprTokenFunc extends ExprToken {
	private final Func function;

	public ExprTokenFunc(Func function) {
		super(0);
		this.function = function;
	}

	public Func getFunction() {
		return function;
	}

	@Override
	public String toString() {
		return getPrioStr() + " function    : " + function.toString();
	}

	@Override
	public Type getType() {
		return Type.FUNCTION;
	}

	@Override
	public String debug() {
		if (function != null) {
			return function.toString();
		} else if (getError() != null) {
			return getError().getCode().toString();
		} else {
			return "null";
		}
	}
}
