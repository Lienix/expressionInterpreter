package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncIf extends Func {

	public FuncIf() {
		super(1, null);
	}

	public static final String name = "IF";

	@Override
	public TArgument getResult(TArgument arg) {
		return arg;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Type getType() {
		return Type.IF;
	}
}
