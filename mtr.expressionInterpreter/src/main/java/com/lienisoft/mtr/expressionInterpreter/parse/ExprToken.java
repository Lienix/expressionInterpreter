package com.lienisoft.mtr.expressionInterpreter.parse;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;

/**
 * base class for parsed expression token
 *
 * @author u083950
 */
abstract public class ExprToken {

	public enum Type {
		BRACKET_OPEN, BRACKET_CLOSE, OPERATOR, PREFIX, FUNCTION, VALUE
	}

	// public final int bracketLevel;
	public final int operatorPrio;

	private ExprInfo error = null;

	protected ExprToken(final int operatorPrio) {
		// this.bracketLevel = bracketLevel;
		this.operatorPrio = operatorPrio;
	}

	abstract public Type getType();

	/**
	 * Derived class must implement a nice toString for debugging the expression parser output!
	 */
	@Override
	abstract public String toString();

	/**
	 * Debug output for expression priority, based on operator
	 *
	 * @return
	 */
	public String getPrioStr() {
		String s = String.valueOf(operatorPrio);

		while (s.length() < 2) {
			s = " " + s;
		}

		// String level = String.valueOf(bracketLevel);
		// while (level.length() < 2)
		// level = " " + level;

		return s + "/"; // + level;
	}

	abstract public String debug();

	public ExprInfo getError() {
		return error;
	}

	public void setError(ExprInfo error) {
		this.error = error;
	}
}
