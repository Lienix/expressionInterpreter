package com.lienisoft.mtr.expressionInterpreter.stack;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

/**
 * Abstract base class for ExpressionStack and single Expressions.
 * Used by the ExpressionParser to retrieve executable interpreter code from a given logical or
 * mathematical expression
 *
 * @author u083950
 */
abstract public class Expr {

	protected Prefx prefix;
	protected Operate operator;

	static final public int OPERATOR_PRIO_EQUALS = 0;
	static final public int OPERATOR_PRIO_LESS = -1;
	static final public int OPERATOR_PRIO_GREATER = 1;

	public Expr(Prefx prefix, Operate operator) {
		super();
		this.prefix = prefix;
		this.operator = operator;
	}

	public boolean isLValue() {
		return false;
	}

	public void resetOperator() {
		operator = null;
	}

	public void resetPrefix() {
		prefix = null;
	}

	public Operate getOperator() {
		return operator;
	}

	public Prefx getPrefix() {
		return prefix;
	}

	public boolean isMinus() {
		return Prefx.MINUS.equals(prefix);
	}

	protected TArgument evaluateMinus(TArgument argument) {
		if (isMinus()) {
			final Integer result = argument.tryGet(Integer.class);
			if (result != null) {
				return argument.create(-result);
			}
		}
		return argument;
	}

	/**
	 * This method executes the expression stack! Must be implemented in derived classes
	 *
	 * @param arg : optional object to provide the data context (implementation dependent)
	 * @return
	 * @throws ExprException
	 */

	protected abstract TArgument calculate(TArgument arg) throws ExprException;

	// protected abstract TArg processStack(TArg arg) throws ExpressionException;

	// public abstract String getReference(String infoTxt, String reference);

	protected int getOperatorPrio() {
		if (operator == null) {
			return 0;
		}
		return operator.prio;
	}

	/**
	 * compares the operator priority, based on the precedence table:
	 *
	 * @param right
	 * @return
	 */
	public int compareOperatorPrio(Operate rightOperator) {
		if (Operate.ASSIGN.equals(rightOperator)) {
			return OPERATOR_PRIO_GREATER;
		} else if (this.getOperatorPrio() > rightOperator.prio) {
			return OPERATOR_PRIO_GREATER;
		} else if (getOperatorPrio() < rightOperator.prio) {
			return OPERATOR_PRIO_LESS;
		} else {
			return OPERATOR_PRIO_EQUALS;
		}
	}

	@Override
	public String toString() {
		final ExprDebug ed = new ExprDebug(true, 0, ' ');
		debug(0, ed);
		return ed.toString();
	}

	public String debug(int depth, char indent) {
		final ExprDebug ed = new ExprDebug(false, depth, indent);
		debug(0, ed);
		return ed.toString();
	}

	// protected abstract String toStringInner();

	protected abstract void debugInner(ExprDebug ed);

	protected void debug(int bracketLevelParent, ExprDebug ed) {
		final StringBuilder s = ed.get();
		if (operator != null) {
			s.append(operator.toString());
			s.append(' ');
		}
		if (prefix != null) {
			s.append(prefix.toString());
		}
		debugInner(ed);
	}

	// /**
	// * Retrieve debug string with a given left margin
	// *
	// * @param indent : string to be used as left margin
	// * @return
	// */
	// abstract public String debugStack(String indent, String info);
	//
	// /**
	// * Retrieve debug string with a given left margin and calculate and display the result
	// *
	// * @param prefix : string to be used as left margin
	// * @return
	// */
	// public String debugStackProcessing(String indent, TArg arg) {
	// return debugStackProcessing(indent, arg, "(ROOT)");
	// };
	//
	// public abstract String debugStackProcessing(String indent, TArg arg, String info);
	//
	// protected abstract String userOutputInner();
}
