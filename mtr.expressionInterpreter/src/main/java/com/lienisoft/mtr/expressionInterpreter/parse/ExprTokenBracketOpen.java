package com.lienisoft.mtr.expressionInterpreter.parse;

public class ExprTokenBracketOpen extends ExprToken {

	public ExprTokenBracketOpen() {
		super(0);
	}

	@Override
	public String toString() {
		return getPrioStr() + " bracketOpen : (";
	}

	@Override
	public Type getType() {
		return Type.BRACKET_OPEN;
	}

	@Override
	public String debug() {
		return "(";
	}
}
