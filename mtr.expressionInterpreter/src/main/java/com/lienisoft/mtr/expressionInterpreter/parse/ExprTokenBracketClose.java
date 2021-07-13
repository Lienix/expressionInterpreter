package com.lienisoft.mtr.expressionInterpreter.parse;

public class ExprTokenBracketClose extends ExprToken {

	public ExprTokenBracketClose() {
		super(0);
	}

	@Override
	public String toString() {
		return getPrioStr() + " bracketClose: )";
	}

	@Override
	public Type getType() {
		return Type.BRACKET_CLOSE;
	}

	@Override
	public String debug() {
		return ")";
	}

}
