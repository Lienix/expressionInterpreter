package com.lienisoft.mtr.expressionInterpreter.function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class FuncGetPat extends Func {
	public FuncGetPat() {
		super(2, 4);
	}
	public static final String name = "GETPAT";

	@Override
	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		final String argument = funcArg.tryGet(0, String.class);

		if (argument != null) {
			final String pattern = funcArg.tryGet(1, String.class);
			
			if(pattern != null) {
				Integer start = null;
				if (funcArg.size() > 2) {
					start = funcArg.tryGet(2, Integer.class);
					/**
					 * start == null is allowed if it is really null and no other class than Integer
					 */
					if(start == null && funcArg.getObj(2) != null) {
						return null;
					}
				}

				Integer group = null;
				if (funcArg.size() > 3) {
					group = funcArg.tryGet(3, Integer.class);
					if(group == null) {
						return null;
					}
				}
				
				final Pattern r = Pattern.compile(pattern);
				final Matcher m = r.matcher(argument);
				if (m.find()) {
					if(start != null) {
						if (m.start() == start) {
							if (group == null) {
								return funcArg.create(m.group(0));
							} else {
								return funcArg.create(m.group(group));
							}
						} else {
							return funcArg.create("");
						}
					} else {
						if (group == null) {
							return funcArg.create(m.group(0));
						} else {
							return funcArg.create(m.group(group));
						}
					}
				} else {
					return funcArg.create("");
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
