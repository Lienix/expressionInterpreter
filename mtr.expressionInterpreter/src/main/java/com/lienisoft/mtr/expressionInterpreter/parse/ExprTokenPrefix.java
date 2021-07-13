package com.lienisoft.mtr.expressionInterpreter.parse;

import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;

public class ExprTokenPrefix extends ExprToken {

	private final Prefx prefix;

	public ExprTokenPrefix(Prefx prefix) {
		super(0);
		this.prefix = prefix;
	}

	public Prefx getPrefix() {
		return prefix;
	}

	@Override
	public String toString() {
		return getPrioStr() + " prefix      : " + prefix;
	}

	@Override
	public Type getType() {
		return Type.PREFIX;
	}

	@Override
	public String debug() {
		if (prefix != null) {
			return prefix.toString();
		} else if (getError() != null) {
			return getError().getExpressionDetail();
		} else {
			return "null";
		}
	}

}
