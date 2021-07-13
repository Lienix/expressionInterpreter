package com.lienisoft.mtr.expressionInterpreter.stack;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class ExprVar extends Expr {

	/**
	 * Static log.
	 */
	// private static Logger log = LoggerFactory.getLogger(ExpressionValueGenericVariable.class);

	private final Object key;

	public ExprVar(Object key, Prefx prefix, Operate operator) {
		super(prefix, operator);
		this.key = key;
	}

	@Override
	public boolean isLValue() {
		return !isMinus();
	}

	@Override
	protected TArgument calculate(TArgument arg) throws ExprException {

		// TArgument resultArg = arg.createByKey(key);
		//
		// /**
		// * handle potential prefix (-) of the whole stack - in case of Integer result:
		// */
		// if (isMinus()) {
		// final Integer result = resultArg.tryGet(Integer.class);
		// if (result != null) {
		// resultArg = resultArg.create(-result);
		// }
		// }
		// return resultArg;

		return evaluateMinus(arg.createByKey(key));
	}

	@Override
	public void debugInner(ExprDebug ed) {
		final StringBuilder s = ed.get();
		// s.append(super.toString());
		s.append(key != null ? key.toString() : "null");
		// return s.toString();
	}

	// @Override
	// public String debugStack(String prefix, String info) {
	// final StringBuilder s = new StringBuilder(); // toStringHeader(prefix, ">Variable", info);
	// s.append(" Key(");
	// s.append(key != null ? key.toString() : "null");
	// s.append(")\n");
	// return s.toString();
	// }
	//
	// @Override
	// public String debugStackProcessing(String prefix, TArg dataProvider, String info) {
	// final StringBuilder s = new StringBuilder();
	// // getStringHeader(s, prefix, dataProvider, "*Variable", info);
	// s.append("\n");
	// return s.toString();
	// }
	//
	// @Override
	// public String userOutputInner() {
	// if (key != null) {
	// return key.toString();
	// } else {
	// return "null";
	// }
	// }
	//
	// @Override
	// public void debug(List<StringBuilder> lines, int currentLine) {
	// StringBuilder s = lines.get(currentLine);
	// s.append(key != null ? key.toString() : "null");
	// }
}
