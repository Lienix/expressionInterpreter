package com.lienisoft.mtr.expressionInterpreter.stack;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoOperationNotDefined;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoTypeMismatch;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;
import com.lienisoft.mtr.expressionInterpreter.function.Func;
import com.lienisoft.mtr.expressionInterpreter.function.FuncIf;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Implementation of an executable stack of arithmetical or logical expression.
 * The stack is recursive an may consist out of single expressions and sub-stacks
 * in order to handle bracket levels and operator precedence.
 *
 * @author u083950
 */
public class ExprStack extends Expr {
	/**
	 * Static logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ExprStack.class);

	public final static String KEY = "KEY";
	public final static String VALUE = "VALUE";
	public final static String INDEX = "INDEX";
	public final static String BREAK = "BREAK";

	protected Func function;

	private final int bracketLevel;

	/**
	 * Parent stack if this is a sub-stack. The value is null, if this is the
	 * root stack!
	 */
	public ExprStack fromStack = null;

	/**
	 * Operator priority of this stack. Only expressions with the same priority
	 * are in one sub-stack.
	 */
	private int stackOperatorPrio = 0; // default: above all Operator.prio (!)

	/**
	 * Holds information about errors/warnings when creating the stack
	 */
	private List<ExprInfo> errors = null;

	// static int instanceCount = 0;
	// public int instanceNr;

	// the line number of this expressionStack in a list of expressionStacks
	public final int lineNumber;

	public ExprStack(int lineNr, Prefx prefix, Operate operator, Func function, int bracketLevel) {
		super(prefix, operator);
		// instanceCount ++;
		this.lineNumber = lineNr;
		this.function = function;
		this.bracketLevel = bracketLevel;
	}

	@Override
	public boolean isLValue() {
		return !isMinus() && (function != null && function.isLValue());
	}

	/**
	 * List of arithmetical or logical expressions of equal priority (single or sub-stacks) that
	 * form this stack
	 */
	public List<Expr> list = new ArrayList<>();

	/**
	 * Execute the expression stack.
	 * <p>
	 * <b> Main access method
	 *
	 * @param dataProvider : optional object to provide the data context (implementation dependent)
	 * @return
	 * @throws ExprException
	 */
	// @Override
	// public TArg calculate(TArg arg) throws ExpressionException {
	// if (arg instanceof TArg) {
	// final TArg resultArg = processStack(arg);
	//
	// /**
	// * handle potential prefix (-) of the whole stack - in case of Integer result:
	// */
	// if (!isMinus() || resultArg.tryGet(Integer.class) == null) {
	// return resultArg;
	// } else {
	// // return resultArg.minus();
	// return new TConst(-resultArg.get(Integer.class));
	// }
	// }
	// return null;
	// }

	@Override
	protected TArgument calculate(TArgument arg) throws ExprException {
		if (arg instanceof TArgument) {
			final TArgument resultArg = evaluateMinus(processStack(arg));

			// /**
			// * handle potential prefix (-) of the whole stack - in case of Integer result:
			// */
			// if (isMinus()) {
			// final Integer result = resultArg.tryGet(Integer.class);
			// if (result != null) {
			// resultArg = resultArg.create(-result);
			// }
			// }

			LOG.debug("[{} -> {}]", debug(1, (char) 0), resultArg);
			return resultArg;
		}
		return null;
	}

	protected TArgument processStack(TArgument arg) throws ExprException {
		TArgument result = null;
		Expr expression = null;

		for (int i = 0; i < list.size(); i++) {
			expression = list.get(i);

			// LOG.info("..{} of {}: {}", i, list.size(), expression);

			try {
				if (result != null && expression.operator != null) {

					/**
					 * break if: "true" || ... and if: "false" && ...
					 * and in special cases function argument list (Function IF) "boolean" , ...
					 */
					if (expression.operator.checkForBreak) {

						// LOGGER.debug(result.checkIfBreak(expression.operator, function));
						switch (expression.operator) {
						/**
						 * Logic Expression with or ||
						 */
						case OR: {
							final Boolean checked = Operate.getBool(result);
							if (checked != null && checked) {
								return processBREAK(result, i);
							}
						}
							break;
						/**
						 * Logic Expression with and &&
						 */
						case AND: {
							final Boolean checked = Operate.getBool(result);
							if (checked != null && !checked) {
								return processBREAK(result, i);
							}
						}
							break;
						/**
						 * Comma separated List
						 */
						case COMMA_FIRST:
						case COMMA:
							/**
							 * comma separated List of function arguments
							 */
							if (function != null) {
								switch (function.getType()) {
								case IF: {
									final Boolean checked = Operate.getBool(result);
									if (checked != null) {
										if (checked) {
											return processIF(arg, expression, i);
										} else {
											return processELSE(arg, i);
										}
									} else {
										final Object obj = result.get();
										throw new ExprException(FuncIf.class, new ExprInfoTypeMismatch(-1,
												obj != null ? obj.getClass().getSimpleName() : "null", "Boolean"));
									}
								}
								// break;
								case LOOP:
									return processLOOP(arg, result, i);
								default:
									break;
								}
							}
							break;
						default:
							break;
						}
					}
					// result = (Result<K, V>)
					// result.operate(expression.getResultInner(dataProvider),
					// expression.operator, expression.prefix);

					result = expression.operator.exe(result, expression.calculate(arg));
				} else {
					result = expression.calculate(arg);
				}
			} catch (final ExprException e) {
				e.getExpressionInfo().setLineNumber(lineNumber);
				// enrich exception by providing the expression that failed:
				if (e.getExpressionInfo() instanceof ExprInfoOperationNotDefined
						&& e.getExpressionInfo().getExpressionDetail() == null) {
					final ExprInfoOperationNotDefined exInfo = (ExprInfoOperationNotDefined) e.getExpressionInfo();
					// exInfo.setOperator(expression.operator);
					// exInfo.setExpressionDetail(expression.toString(getBracketLevel()));
					exInfo.setExpressionDetail(expression.toString());
				}
				throw e;
			}
		}

		try {
			if (function != null) {
				return function.getResult(result);

				// if (result != null) {
				// return function.getResult(result);
				// } else if (dataProvider != null) {
				// // TODO! some functions have null arguments but need a dataProvider!
				// return function.getResult(null);
				// }
			}
		} catch (final ExprException e) {
			e.getExpressionInfo().setLineNumber(lineNumber);
			if (e.getExpressionInfo().getExpressionDetail() == null) {
				if (e.getExpressionInfo() instanceof ExprInfoTypeMismatch) {
					final ExprInfoTypeMismatch exInfo = (ExprInfoTypeMismatch) e.getExpressionInfo();
					Expr errorExpression = null;
					if (exInfo.getIndex() >= 0) {
						errorExpression = list.get(exInfo.getIndex());
					} else {
						errorExpression = this;
					}
					// exInfo.setExpressionDetail(errorExpression.toString(getBracketLevel()));
					exInfo.setExpressionDetail(errorExpression.toString());
				} else {
					// e.getExpressionInfo().setExpressionDetail(toString(-1));
					e.getExpressionInfo().setExpressionDetail(toString());
				}
			}
			throw e;
		}
		return result;
	}

	/**
	 * process a BREAK: in case the stack is a logic expresion with "AND" (&&) or "OR"
	 * processing could be broken if the first condition of an OR returns true or
	 * if the first condition of an AND returns false.
	 *
	 * @param result
	 * @param i
	 * @return
	 * @throws ExprException
	 */
	private TArgument processBREAK(TArgument result, int i) throws ExprException {
		// TODO!?
		// result.breakIndex = i - 1;
		if (function != null) {
			result = function.getResult(result);
		}
		return result;
	}

	/**
	 * process first part of internal IF function:
	 * if the if condition is true
	 *
	 * @param dataProvider
	 * @param expression
	 * @param i current index of the stack
	 * @return
	 * @throws ExprException
	 */
	private TArgument processIF(TArgument dataProvider, Expr expression, int i) throws ExprException {
		TArgument result = expression.calculate(dataProvider);
		if (function != null) {
			result = function.getResult(result);
		}
		// TODO!?
		// result.breakIndex = i;
		return result;
	}

	/**
	 * process second part of internal IF function
	 * if the if condition returned false
	 *
	 * @param dataProvider
	 * @param i current index of the stack
	 * @return
	 * @throws ExprException
	 */
	private TArgument processELSE(TArgument dataProvider, int i) throws ExprException {
		if (i + 1 < list.size()) {
			TArgument result = list.get(i + 1).calculate(dataProvider);
			if (function != null) {
				result = function.getResult(result);
			}
			// TODO!
			// result.skipIndex = i;
			return result;
		} else {
			// final Result<K, V> result = dataProvider.resultNull();
			final TArgument result = dataProvider.create(null);
			// TODO!
			// result.skipIndex = i;
			return result;
		}
	}

	/**
	 * process internal LOOP function.
	 * The first result on the stack must be a List or a Structure!
	 *
	 * @param dataProvider
	 * @param result first result of the stack
	 * @param i should actually be always one
	 * @return result
	 * @throws ExprException
	 */
	private TArgument processLOOP(TArgument arg, TArgument result, int i) throws ExprException {

		/**
		 * loop over list
		 */

		final List<Object> keyList = result.getKeyList();
		final TArgument struct = result.create(result.get());

		/**
		 * loop over structure
		 */
		if (keyList != null) {
			boolean doBreak = false;
			Integer index = 0;
			for (final Object key : keyList) {
				arg.put(INDEX, index++);
				arg.put(VALUE, struct.getCopy(key));
				arg.put(KEY, key.toString());

				for (int j = i; j < list.size(); j++) {
					list.get(j).calculate(arg);
					// if (arg.get(BREAK) != null) {

					final Boolean isBreak = arg.get(BREAK, Boolean.class);

					if (isBreak != null && isBreak) {
						doBreak = true;
						break;
					}
				}
				arg.remove(VALUE);
				if (doBreak) {
					System.out.println("BREAK!!!");
					break;
				}
			}

			/**
			 * remove temporary loop variables:
			 */
			arg.remove(INDEX);
			arg.remove(BREAK);
			arg.remove(KEY);
		} else {
			// TODO: Error Handling !
		}

		return arg.create(null); // dataProvider.resultNull();
	}

	@Override
	public int getOperatorPrio() {
		return stackOperatorPrio;
	}

	public void setOperatorPrio(int stackOperatorPrio) {
		this.stackOperatorPrio = stackOperatorPrio;
	}

	public void add(Expr expression) {
		list.add(expression);
	}

	public void addError(ExprInfo errorText) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(errorText);
	}

	public void setErrors(List<ExprInfo> errors) {
		this.errors = errors;
	}

	public List<ExprInfo> getErrors() {
		return errors;
	}

	// @Override
	// public String debugStack(String indent, String info) {
	// final StringBuilder s = toStringHeader(indent, "Stack", info);
	// s.append(" [\n");
	// for (final Expr expression : list) {
	// s.append(expression.debugStack(indent + " ", null));
	// }
	// s.append(indent);
	// s.append("]\n");
	// return s.toString();
	// }

	// @Override
	// public String debugStackProcessing(String indent, TArg arg, String info) {
	//
	// final StringBuilder s = new StringBuilder();
	// final TArg result = getStringHeader(s, indent, arg, "Stack", info);
	//
	// // StringBuilder s = toStringHeader(prefix, dataProvider, "Stack");
	// s.append(" [\n");
	//
	// // int i = 0;
	// for (final Expr expression : list) {
	// if (result == null) {
	// s.append(expression.debugStackProcessing(indent + " ", arg, null));
	// // TODO:
	// // } else if (i == result.skipIndex) {
	// // s.append(expression.debugStack(prefix + " ", "(SKIP)"));
	// // } else if (i < result.breakIndex) {
	// // s.append(expression.debugStackProcessing(prefix + " ", dataProvider, null));
	// // } else if (i == result.breakIndex) {
	// // s.append(expression.debugStackProcessing(prefix + " ", dataProvider, "(BREAK)"));
	// } else {
	// s.append(expression.debugStack(indent + " ", null));
	// }
	// // i++;
	// }
	//
	// s.append(indent);
	// s.append("]\n");
	// return s.toString();
	// }
	//
	// @Override
	// public String userOutputInner() {
	// final StringBuilder s = new StringBuilder();
	//
	// if (list != null) {
	// for (final Expr expression : list) {
	// s.append(' ');
	// // TODO!
	// // s.append(expression.userOutput(getBracketLevel()));
	// }
	// }
	// return s.toString();
	// }
	//
	// @Override
	// public void debug(List<StringBuilder> lines, int currentLine) {
	//
	// StringBuilder current = lines.get(currentLine);
	// int currentLen = current.length();
	//
	// StringBuilder s = new StringBuilder();
	// lines.add(s);
	// int newCurrentLine = lines.size() - 1;
	//
	// s.append(makeIndent(currentLen));
	//
	// if (list != null) {
	// for (final Expr expression : list) {
	// // TODO!
	// // expression.debugFrame(lines, newCurrentLine, getBracketLevel());
	//
	// StringBuilder newLine = lines.get(newCurrentLine);
	// current.append(makeIndent(newLine.length() - currentLen));
	// currentLen = newLine.length();
	// }
	// }
	// }

	// public ExpressionInfo getExpressionInfo() {
	// ExpressionInfo info = new ExpressionInfo();
	// info.setLineNumber(lineNumber);
	// info.setExpressionDetail(userOutput());
	// return info;
	// }

	// public String getReference(String infoTxt, String reference) {
	// return createReference(lineNr, userOutput(), infoTxt, reference);
	// }
	//
	// public static String createReference(int lineNr, String stackAsString, String infoTxt, String
	// reference ) {
	// StringBuilder s = new StringBuilder();
	// s.append("line ");
	// s.append(lineNr);
	// s.append(": '");
	// s.append(stackAsString);
	// s.append("'<- ");
	// s.append(infoTxt);
	// s.append("[");
	// s.append(reference);
	// s.append("]");
	// return s.toString();
	// }

	public void resetFunction() {
		function = null;
	}

	public Func getFunction() {
		return function;
	}

	public int getBracketLevel() {
		return bracketLevel;
	}

	/**
	 * compares the total priority, regarding bracket level too.
	 *
	 * @param right
	 * @return
	 */
	public int comparePrio(int bracketLevel, Operate operator) {
		if (bracketLevel > this.getBracketLevel()) {
			return OPERATOR_PRIO_GREATER;
		} else if (bracketLevel < this.getBracketLevel()) {
			return OPERATOR_PRIO_LESS;
		} else {
			return compareOperatorPrio(operator);
		}
	}

	public TArgument getResult(TArgument arg) throws ExprException {

		try {
			final TArgument result = calculate(arg);
			return result;
		} catch (final ExprException e) {
			// enrich exception by providing the expression that failed:
			e.getExpressionInfo().setExpressionAsString(toString());
			throw e;
		}
	}

	@Override
	public String toString() {
		final ExprDebug ed = new ExprDebug(true, 0, ' ');
		debug(bracketLevel, ed);
		return ed.toString();
	}

	@Override
	public String debug(int depth, char indent) {
		final ExprDebug ed = new ExprDebug(false, depth, indent);
		debug(bracketLevel, ed);
		return ed.toString();
	}

	@Override
	protected void debug(int bracketLevelParent, ExprDebug ed) {

		StringBuilder s = ed.get();
		if (operator != null) {
			s.append(operator.toString());
			s.append(' ');
		}

		if (ed.isMaxDepth()) {
			s.append('.');
			return;
		}

		ed.incrementCurrentLine();
		s = ed.get();

		if (prefix != null) {
			s.append(prefix.toString());
		}
		if (function != null) {
			s.append(function.toString());
		}
		if (bracketLevelParent < bracketLevel || function != null) {
			s.append('(');
		}

		debugInner(ed);

		if (bracketLevelParent < bracketLevel || function != null) {
			ed.get().append(')');
		}
		ed.decrementCurrentLine();
	}

	@Override
	protected void debugInner(ExprDebug ed) {
		if (list != null) {
			boolean isFirst = true;
			for (final Expr expression : list) {
				if (isFirst) {
					isFirst = false;
				} else {
					ed.get().append(' ');
				}
				expression.debug(bracketLevel, ed);
			}
		}

	}

	// public String debug() {
	// List<StringBuilder> lines = new ArrayList<>();
	// lines.add(new StringBuilder());
	// debugFrame(lines, 0, getBracketLevel());
	//
	// StringBuilder s = new StringBuilder();
	// for(int i = 0; i<lines.size(); i++) {
	// String l = lines.get(i).toString();
	// if(! l.trim().isEmpty()) {
	// s.append(String.format("%3d: ", i));
	// s.append(l);
	// if(i < lines.size() - 1) {
	// s.append('\n');
	// }
	// }
	// }
	// return s.toString();
	// }

	// /**
	// * generates a common header debug info without result
	// *
	// * @param prefix
	// * @param arg
	// * @return
	// */
	// private StringBuilder toStringHeader(String indent, String name) {
	//
	// final StringBuilder s = new StringBuilder();
	// s.append(indent);
	// s.append(name);
	// s.append(" ");
	// s.append(prefix);
	// s.append(" (");
	// s.append(getPrioStr());
	// s.append(" ");
	// s.append(operator);
	// s.append(" ");
	// s.append(function);
	// s.append(")");
	// return s;
	// }
	//
	// /**
	// * generates a common header debug info with result
	// *
	// * @param prefix
	// * @param arg
	// * @return
	// */
	// protected TArg getStringHeader(StringBuilder s, String prefix, TArg arg, String name, String
	// info) {
	// TArg result = null;
	//
	// ExpressionInfo.Code code = null;
	//
	// try {
	// result = calculate(arg);
	// } catch (final ExpressionException e) {
	// code = e.getCode();
	// }
	//
	// s.append(toStringHeader(prefix, name));
	// s.append(" = ");
	// if (result != null) {
	// s.append(result.toString());
	// } else {
	// s.append("?");
	// }
	//
	// if (info != null) {
	// s.append(" ");
	// s.append(info);
	// }
	// if (code != null) {
	// s.append(" ");
	// s.append(code);
	// }
	// return result;
	// }
	//
	// public String userOutput() {
	// return userOutput(getBracketLevel());
	// }
	//
	// protected StringBuilder toStringHeader(String prefix, String name, String info) {
	// final StringBuilder s = toStringHeader(prefix, name);
	// if (info != null) {
	// s.append(" ");
	// s.append(info);
	// }
	// return s;
	// }
	//
	// public String userOutput(int bracketLevelParent) {
	// final StringBuilder s = new StringBuilder();
	// if (operator != null) {
	// s.append(operator.toString());
	// s.append(' ');
	// }
	// if (prefix != null) {
	// s.append(prefix.toString());
	// }
	// if (function != null) {
	// s.append(function.toString());
	// }
	// if (bracketLevelParent < getBracketLevel()) {
	// s.append('(');
	// }
	//
	// s.append(userOutputInner());
	//
	// if (bracketLevelParent < getBracketLevel()) {
	// s.append(')');
	// }
	//
	// return s.toString();
	// }
	//
	//
	//
	// /**
	// * Get the priority of expression encoded N..M:
	// * <li>N = bracket level
	// * <li>M = operator priority
	// *
	// * @return
	// */
	// public String getPrioStr() {
	// String s = String.valueOf(getOperatorPrio());
	//
	// while (s.length() < 2) {
	// s = " " + s;
	// }
	//
	// String level = String.valueOf(getBracketLevel());
	// while (level.length() < 2) {
	// level = " " + level;
	// }
	//
	// return s + "/" + level;
	// }
	//
	// /**
	// * Retrieve debug string for this expression
	// */
	// @Override
	// public String toString() {
	// // return debugStack("", "(ROOT)");
	// List<StringBuilder> lines = new ArrayList<>();
	// lines.add(new StringBuilder());
	// debugFrame(lines, 0, getBracketLevel());
	//
	// StringBuilder s = new StringBuilder();
	// for(int i = 0; i<lines.size(); i++) {
	// String l = lines.get(i).toString();
	// if(! l.trim().isEmpty()) {
	// s.append(String.format("%3d: ", i));
	// s.append(l);
	// if(i < lines.size() - 1) {
	// s.append('\n');
	// }
	// }
	// }
	// return s.toString();
	// }
	//
	// public void debugFrame(List<StringBuilder> lines, int currentLine, int bracketLevelParent) {
	//
	// StringBuilder s = lines.get(currentLine);
	// if (operator != null) {
	// s.append(operator.toString());
	// s.append(' ');
	// }
	// if (prefix != null) {
	// s.append(prefix.toString());
	// }
	// if (function != null) {
	// s.append(function.toString());
	// }
	// if (bracketLevelParent < getBracketLevel()) {
	// s.append('(');
	// }
	//
	// debug(lines, currentLine);
	//
	// if (bracketLevelParent < getBracketLevel()) {
	// s.append(')');
	// }
	// }

}
