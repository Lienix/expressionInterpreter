package com.lienisoft.mtr.expressionInterpreter.stack;

import java.util.Objects;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class ExprConst extends Expr {

	/**
	 * Static logger.
	 */
	// private static Logger log = LoggerFactory.getLogger(ExpressionValueGenericConstant.class);

	private final TArgument val;

	public ExprConst(TArgument val, Prefx prefix, Operate operator) {
		super(prefix, operator);
		Objects.requireNonNull(val);
		this.val = evaluateMinus(val);
		// if (isMinus()) {
		// final Integer result = val.tryGet(Integer.class);
		// if (result != null) {
		// this.val = val.create(-result);
		// } else {
		// this.val = val;
		// }
		// } else {
		// this.val = val;
		// }
	}

	@Override
	protected TArgument calculate(TArgument arg) throws ExprException {
		return val;
	}

	@Override
	protected void debugInner(ExprDebug ed) {
		final StringBuilder s = ed.get();
		if (val == null) {
			s.append("null");
		} else if (val.get() instanceof String) {
			s.append('"');
			s.append(val.asString());
			s.append('"');
		} else {
			s.append(val.asString());
		}
		// return s.toString();
	}

	// @Override
	// public String debugStack(String prefix, String info) {
	// final StringBuilder s = new StringBuilder(); // toStringHeader(prefix, ">Constant", info);
	// s.append(" ");
	// s.append(val);
	// s.append("\n");
	// return s.toString();
	// }
	//
	// @Override
	// public String debugStackProcessing(String prefix, TArg dataProvider, String info) {
	// final StringBuilder s = new StringBuilder();
	// // getStringHeader(s, prefix, dataProvider, "*Constant", info);
	// s.append("\n");
	// return s.toString();
	// }
	//
	// @Override
	// public String userOutputInner() {
	// if (val != null) {
	// return val.toString();
	// } else {
	// return "null";
	// }
	// }
	//

}
