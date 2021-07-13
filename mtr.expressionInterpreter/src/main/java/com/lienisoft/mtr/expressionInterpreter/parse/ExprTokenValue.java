package com.lienisoft.mtr.expressionInterpreter.parse;

public class ExprTokenValue extends ExprToken {

	private String value;

	public ExprTokenValue(String value) {
		super(0);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void addToValue(String string) {
		value += string;
	}

	@Override
	public String toString() {
		return getPrioStr() + " value       : " + value;
	}

	@Override
	public Type getType() {
		return Type.VALUE;
	}

	@Override
	public String debug() {
		return value;
	}

	/**
	 * some special tokens are concatenated, like b"true", b"false".
	 * Concatenation may happen only one time, so it must be checked if there was already a
	 * concatenation:
	 *
	 * @return
	 */
	public boolean isConcatenated() {
		if (value != null && value.endsWith("\"")) {
			return true;
		} else {
			return false;
		}
	}

}
