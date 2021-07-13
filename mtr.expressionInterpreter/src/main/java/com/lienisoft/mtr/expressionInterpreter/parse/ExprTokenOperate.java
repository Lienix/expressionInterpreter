package com.lienisoft.mtr.expressionInterpreter.parse;

import com.lienisoft.mtr.expressionInterpreter.basic.Operate;

public class ExprTokenOperate extends ExprToken {

	private final Operate operator;

	public ExprTokenOperate(Operate operator) {
		super(operator != null ? operator.prio : -1);
		this.operator = operator;
	}

	public Operate getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		if (operator != null) {
			return getPrioStr() + " operator    : " + operator.toString();
		} else {
			return getPrioStr() + " operator    : " + null;
		}
	}

	@Override
	public Type getType() {
		return Type.OPERATOR;
	}

	@Override
	public String debug() {
		if (operator != null) {
			return operator.toString();
		} else if (getError() != null) {
			return getError().getExpressionDetail();
		} else {
			return "null";
		}
	}
}
