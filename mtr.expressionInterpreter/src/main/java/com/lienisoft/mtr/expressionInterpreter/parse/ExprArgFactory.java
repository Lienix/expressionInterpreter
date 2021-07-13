package com.lienisoft.mtr.expressionInterpreter.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoCreateStackError;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;
import com.lienisoft.mtr.expressionInterpreter.stack.Expr;
import com.lienisoft.mtr.expressionInterpreter.stack.ExprConst;
import com.lienisoft.mtr.expressionInterpreter.stack.ExprVar;

public class ExprArgFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExprArgFactory.class);

	// Token definitions (Token starts with pattern):
	static public final String key = "([A-Za-z%][A-Za-z0-9%_\\.:]{0,200})"; // % is a wildcard key!
	static public final String number = "([\\-]{0,1}[0-9]{1,30})";
	// static public final String string = "\"(.{0,50000})\"";
	static public final String string = "\"([\\s\\S]{0,50000})\"";
	static public final String bool_old = "b\"((true)|(false))\"";
	static public final String bool = "(true)|(false)";
	static public final String nul = "null";

	// static public final String dynamic = "(\\[[A-Za-z]{1,30}\\])";
	// static public final String datetime = "d\"(.{1,30})\"";
	// static public final String codekey = "cok\"(.{1,30})\"";
	// static public final String airline = "air\"((.{1,30})\\,(.{1,30})\\,(.{1,30}))\"";
	// static public final String registration = "reg\"((.{1,30})\\,(.{1,30})\\,(.{1,30}))\"";
	// static public final String station = "sta\"((.{1,30})\\,(.{1,30})\\,(.{1,30}))\"";

	// create final Patterns from token definitions:
	static final Pattern keyPattern = Pattern.compile(key);
	static final Pattern numberPattern = Pattern.compile(number);
	static final Pattern stringPattern = Pattern.compile(string);
	static final Pattern boolPattern_old = Pattern.compile(bool_old);
	static final Pattern boolPattern = Pattern.compile(bool);
	static final Pattern nulPattern = Pattern.compile(nul);

	// static final Pattern dynamicPattern = Pattern.compile(dynamic);
	// static final Pattern datetimePattern = Pattern.compile(datetime);
	// static final Pattern codekeyPattern = Pattern.compile(codekey);
	// static final Pattern airlinePattern = Pattern.compile(airline);
	// static final Pattern registrationPattern = Pattern.compile(registration);
	// static final Pattern stationPattern = Pattern.compile(station);

	// protected final Result<K, V> dataProvider;

	private final TArgument struct;

	public ExprArgFactory(TArgument struct) {
		// this.dataProvider = dataProvider;
		this.struct = struct;
	}

	// @Override
	public Expr createExprArg(String text, Prefx prefix, Operate operator) throws ExprException {

		final Expr expression = createExpArgFromString(text, prefix, operator);
		if (expression != null) {
			return expression;
		}
		// throw new ExpressionException(ExpressionInfo.Code.InvalidExpressionValue, getClass(),
		// "Value=" + text);
		LOGGER.warn("Throw Exception InvalidExpressionValue: {}", text);
		throw new ExprException(getClass(), new ExprInfoCreateStackError(ExprInfo.Code.INVALID_EXPRESSION_VALUE, text));
	}

	/**
	 * Override/use this method for extensions in derived classes
	 *
	 * @param text
	 * @return
	 */
	protected Expr createExpArgFromString(String text, Prefx prefix, Operate operator) {

		final KeyAndTContainer keyValue = parsePatterns(text);

		/** could not be identified **/
		if (keyValue == null) {
			return null;
		}

		/** variable expression **/
		if (keyValue.key != null) {
			return new ExprVar(keyValue.key, prefix, operator);
		}
		/** constant expression **/
		return new ExprConst(keyValue.container, prefix, operator);
	}

	/**
	 * Override/use this method for extensions in derived classes
	 *
	 * @param text
	 * @return
	 */
	protected KeyAndTContainer parsePatterns(String text) {

		final Matcher nulMatcher = nulPattern.matcher(text);
		if (nulMatcher.matches()) {
			return new KeyAndTContainer(null, struct.create(null));
		}

		Matcher boolMatcher = boolPattern_old.matcher(text.toLowerCase());
		if (boolMatcher.matches()) {
			return new KeyAndTContainer(null, struct.create(Boolean.parseBoolean(boolMatcher.group(1))));
		}

		boolMatcher = boolPattern.matcher(text.toLowerCase());
		if (boolMatcher.matches()) {
			return new KeyAndTContainer(null, struct.create(Boolean.parseBoolean(boolMatcher.group(1))));
		}

		final Matcher numberMatcher = numberPattern.matcher(text);
		if (numberMatcher.matches()) {
			return new KeyAndTContainer(null, struct.create(Integer.valueOf(text)));
		}

		final Matcher stringMatcher = stringPattern.matcher(text);
		if (stringMatcher.matches()) {
			return new KeyAndTContainer(null, struct.create(stringMatcher.group(1)));
		}

		final Matcher keyMatcher = keyPattern.matcher(text);
		if (keyMatcher.matches()) {
			return new KeyAndTContainer(struct.createKey(text), null);
		}

		LOGGER.warn("Unrecognized TokenPattern: {}", text);

		return null;
	}
}
