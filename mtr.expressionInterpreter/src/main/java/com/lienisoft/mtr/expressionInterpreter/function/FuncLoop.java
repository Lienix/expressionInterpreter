package com.lienisoft.mtr.expressionInterpreter.function;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncLoop extends Func {

	public FuncLoop() {
		super(1, null);
	}

	public static final String name = "LOOP";

	@Override
	public TArgument getResult(TArgument arg) {
		/** not called by framework **/
		return null;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Type getType() {
		return Type.LOOP;
	}
}
