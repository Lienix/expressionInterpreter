package com.lienisoft.mtr.expressionInterpreter.parse;

import java.util.List;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.function.Func;
import com.lienisoft.mtr.expressionInterpreter.stack.Expr;
import com.lienisoft.mtr.expressionInterpreter.stack.ExprStack;

/**
 * Helper class for the Expression Parser to build up an hierarchical, recursive stack of
 * expressions
 *
 * @author u083950
 */
public class StackBuilder_NEW {
	/**
	 * Static logger.
	 */
	// private static final Logger LOGGER = LoggerFactory.getLogger(StackBuilder.class);

	/**
	 * the root stack object - entry point
	 */
	private ExprStack rootStack = null;
	/**
	 * current sub-stack to add expressions
	 */
	private ExprStack currentStack = null;

	private final int lineNumber;

	public StackBuilder_NEW(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * Return stack after parsing
	 *
	 * @return
	 */
	public ExprStack getStack() {
		return rootStack;
	}

	public ExprStack getCurrentStack() {
		return currentStack;
	}

	/**
	 * Set the operator priority for the current sub-stack. Called from external ExpressionParser if
	 * an operator was parsed.
	 * <li>if current sub-stack does not yet have a priority it is used to
	 * set current sub-stack priority
	 * <li>else the value is saved for the next sub-stack to be
	 * inserted!
	 *
	 * @param operator
	 */
	public void setOperatorPriority(Operate operator) {
		// LOGGER.debug("setOperatorPriority(...)");

		// LOGGER.debug(operator);
		// LOGGER.debug(currentStack);

		if (currentStack != null) {
			if (currentStack.getOperatorPrio() != 0) {
				// LOGGER.debug(debugCompare(operator));

				if (currentStack.compareOperatorPrio(operator) == Expr.OPERATOR_PRIO_GREATER) {
					final Expr lastExpression = currentStack.list.remove(currentStack.list.size() - 1);
					// insertStack(currentStack.bracketLevel, lastExpression.operator,
					// lastExpression.function);
					insertStack(currentStack.getBracketLevel(), lastExpression.getOperator(), null, null);
					lastExpression.resetOperator();
					currentStack.add(lastExpression);
				} else if (currentStack.compareOperatorPrio(operator) == Expr.OPERATOR_PRIO_LESS) {

					final List<ExprInfo> saveErrors = rootStack.getErrors();

					final int currentBracketLevel = currentStack.getBracketLevel();

					ExprStack oldCurrentStack = null;
					while (currentStack != null
							&& currentStack.comparePrio(currentBracketLevel, operator) == Expr.OPERATOR_PRIO_LESS) {
						oldCurrentStack = currentStack;
						currentStack = currentStack.fromStack;
					}

					if (currentStack == null
							|| currentStack.comparePrio(currentBracketLevel, operator) == Expr.OPERATOR_PRIO_GREATER) {
						// int bracketLevel = 0;
						if (currentStack != null) {
							currentStack.list.remove(oldCurrentStack);
							// bracketLevel = currentStack.bracketLevel;
						}

						// insertStack(currentBracketLevel, oldCurrentStack.operator,
						// oldCurrentStack.function);
						insertStack(currentBracketLevel, oldCurrentStack.getOperator(), oldCurrentStack.getFunction(),
								oldCurrentStack.getPrefix());
						oldCurrentStack.resetOperator();
						oldCurrentStack.resetFunction();
						oldCurrentStack.resetPrefix();

						currentStack.add(oldCurrentStack);
						oldCurrentStack.fromStack = currentStack;
					}

					if (oldCurrentStack == rootStack) {
						rootStack = currentStack;
					}

					rootStack.setErrors(saveErrors);
				}
			}
			currentStack.setOperatorPrio(operator.prio);
		}
		// DEBUG:
		// LOGGER.debug(rootStack);
	}

	/**
	 * Insert a new sub-stack
	 * <li>called from external ExpressionParser if a bracket open "(" was
	 * parsed
	 * <li>called from internal if Expression with different operator priority is inserted
	 *
	 * @param bracketLevel
	 * @param operator
	 * @param function
	 */
	public void insertStack(int bracketLevel, Operate operator, Func function, Prefx prefix) {
		final ExprStack newStack = new ExprStack(lineNumber, prefix, operator, function, bracketLevel);
		// LOGGER.debug("StackBuilderX.insertStack(...) - ##");

		// newStack.operator = leftEx.operator; => must set later!
		// => set operator if stacksize is 0 - if first element is set
		// currentStack.setOperatorPrio(operatorPrio);

		// newStack.operator = operator;
		// newStack.function = function;
		// newStack.bracketLevel = bracketLevel;

		newStack.fromStack = currentStack;
		if (currentStack != null) {
			currentStack.add(newStack);
		}
		currentStack = newStack;

		if (rootStack == null) {
			rootStack = currentStack;
		}

		// DEBUG:
		// LOGGER.debug(rootStack);
	}

	/**
	 * Reduce the current bracket level. Called from external ExpressionParser if a bracket close
	 * "(" was parsed. This may insert a new sub-stack if a stack for the new level does not yet
	 * exist
	 *
	 * @param bracketLevel
	 */
	public void reduceStack(int bracketLevel) {
		// LOGGER.debug("StackBuilderX.reduceStack(...) - ##");

		ExprStack oldCurrentStack = null;
		while (currentStack != null && currentStack.getBracketLevel() > bracketLevel) {
			if (currentStack.getFunction() != null) {
				checkFunctionParameterCount(currentStack);
			}
			oldCurrentStack = currentStack;
			currentStack = currentStack.fromStack;
		}
		if (oldCurrentStack == null) {
			return;
		}
		if (currentStack == null || currentStack.getBracketLevel() < bracketLevel) {
			if (currentStack != null) {
				currentStack.list.remove(oldCurrentStack);
			}

			insertStack(bracketLevel, null, null, null);

			currentStack.add(oldCurrentStack);
			oldCurrentStack.fromStack = currentStack;
		}
		if (oldCurrentStack == rootStack) {
			rootStack = currentStack;
		}

		// DEBUG:
		// LOGGER.debug(rootStack);
	}

	/**
	 * check parametercount of a function
	 *
	 * @param currentStack
	 */
	private void checkFunctionParameterCount(ExprStack currentStack) {
		if (currentStack != null && currentStack.getFunction() != null) {

			final Func fun = currentStack.getFunction();

			try {
				// We must count only the comma separated expressions here!
				// ohter operators must not be counted !!!!!
				if (currentStack.list != null && currentStack.list.size() > 0) {
					int i = 0;
					int commaCounter = 0;

					for (final Expr expression : currentStack.list) {
						if (i > 0 && (Operate.COMMA_FIRST.equals(expression.getOperator())
								|| Operate.COMMA.equals(expression.getOperator()))) {
							commaCounter++;
						}
						i++;
					}

					fun.checkSize(commaCounter + 1);
				} else {
					fun.checkSize(0);
				}
			} catch (final ExprException e) {
				e.getExpressionInfo().setLineNumber(currentStack.lineNumber);
				e.getExpressionInfo().setExpressionAsString(rootStack.toString());
				// e.getExpressionInfo().setExpressionDetail(currentStack.toString(-1));
				e.getExpressionInfo().setExpressionDetail(currentStack.toString());
				addError(e.getExpressionInfo());
			}
		}
	}

	/**
	 * Insert a value into the stack. Inserting a value may insert a new sub-stack it the operator
	 * priority is different than the operator priority of the current stack!
	 *
	 * @param value
	 */
	public void insertValue(Expr value) {
		// LOGGER.debug("StackBuilder.insertValue(Expression)");

		// Initial - if expression starts with a value or a prefix+value:
		if (currentStack == null) {
			insertStack(0, null, null, null);
		}

		currentStack.add(value);

		/*
		 * // add first and second value:
		 * if (currentStack.getOperatorPrio() == 0) {
		 * currentStack.add(value);
		 * } else {
		 * LOGGER.debug(debugCompare(value));
		 *
		 * if (currentStack.compareOperatorPrio(value) == Expression.greater) {
		 *
		 * // if (currentStack.list.size() > 0) {
		 * Expression lastExpression = currentStack.list.remove(currentStack.list.size() - 1);
		 * insertStack(currentStack.bracketLevel, lastExpression.operator, lastExpression.function);
		 * lastExpression.operator = null;
		 * currentStack.add(lastExpression);
		 * // }
		 *
		 * currentStack.add(value);
		 * } else if (currentStack.compareOperatorPrio(value) == Expression.less) {
		 *
		 * ExpressionStack oldCurrentStack = null;
		 * while (currentStack != null && currentStack.compareOperatorPrio(value) ==
		 * Expression.less) {
		 * oldCurrentStack = currentStack;
		 * currentStack = currentStack.fromStack;
		 * }
		 *
		 * if (currentStack == null || currentStack.compareOperatorPrio(value) ==
		 * Expression.greater) {
		 * int bracketLevel = 0;
		 * if (currentStack != null) {
		 * currentStack.list.remove(oldCurrentStack);
		 * bracketLevel = currentStack.bracketLevel;
		 * }
		 *
		 * insertStack(bracketLevel, oldCurrentStack.operator, oldCurrentStack.function);
		 * oldCurrentStack.operator = null;
		 *
		 * // insertStack(oldCurrentStack, value);
		 * currentStack.add(oldCurrentStack);
		 * oldCurrentStack.fromStack = currentStack;
		 * }
		 *
		 * if (oldCurrentStack == rootStack)
		 * rootStack = currentStack;
		 *
		 * currentStack.add(value);
		 *
		 * } else {
		 * currentStack.add(value);
		 * }
		 * }
		 */

		// DEBUG:
		// LOGGER.debug(rootStack);
	}

	public void addError(ExprInfo errorText) {
		if (rootStack == null) {
			rootStack = new ExprStack(lineNumber, null, null, null, 0);
		}
		rootStack.addError(errorText);
	}

	/**
	 * debug output to print the priority relationship between two expressions
	 *
	 * @param rightEx
	 * @return
	 */
	String debugCompare(Operate rightOperator) {
		if (currentStack == null) {
			return "=> currentStack is null";
		}

		final StringBuilder s = new StringBuilder();

		s.append("=> comparePrio : ");
		s.append(rightOperator.prio);

		switch (currentStack.compareOperatorPrio(rightOperator)) {
		case (Expr.OPERATOR_PRIO_GREATER):
			s.append(" > (greater) ");
			break;
		case (Expr.OPERATOR_PRIO_LESS):
			s.append(" < (less) ");
			break;
		case (Expr.OPERATOR_PRIO_EQUALS):
			s.append(" == (equal) ");
			break;
		}
		// TODO !
		// s.append(currentStack.getPrioStr());

		return s.toString();
	}

}
