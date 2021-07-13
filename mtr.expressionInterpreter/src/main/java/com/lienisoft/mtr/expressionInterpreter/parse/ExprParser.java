package com.lienisoft.mtr.expressionInterpreter.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.expressionInterpreter.basic.BetterStringParser;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoCreateStackError;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoTokenizeError;
import com.lienisoft.mtr.expressionInterpreter.basic.LgEntry;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.Prefx;
import com.lienisoft.mtr.expressionInterpreter.function.Func;
import com.lienisoft.mtr.expressionInterpreter.function.FuncFactory;
import com.lienisoft.mtr.expressionInterpreter.stack.Expr;
import com.lienisoft.mtr.expressionInterpreter.stack.ExprStack;

/**
 * Parser for logical or arithmetical expressions given as plain text string.
 * Two main steps are performed:
 * <li>splitting the text into a list of expression components
 * (operators, brackets, functions, values)
 * <li>building up an executable expression stack
 * Now: re-usable and stateless!
 *
 * @author u083950
 */
public class ExprParser {
	/**
	 * Static logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExprParser.class);

	/**
	 * This pattern recognizes a string in quotation marks in the given expression string.
	 * e.g. tokens like: "Tom Smith" "123" "asdf" "km/h" "records/hour"
	 * These tokens may contain separators inside the quoation marks.
	 */
	static final String STRING_PATTERN_EXPR = "(\"\")|(\"[\\s\\S][^\"]{0,}\")";
	static final Pattern STRING_PATTERN = Pattern.compile(STRING_PATTERN_EXPR);

	/**
	 * This pattern recognizes all kind of separators between the tokens (except of quoted strings -
	 * see above)
	 */
	static final String SEPARATORS = "[ !\\+\\-\\*/=<>&\\|\\(\\)\\,;\n\t\r\\\\]";
	static final String SEPARATORS_PATTERN_EXPR = "(" + SEPARATORS + "+)";
	static final Pattern SEPARATORS_PATTERN = Pattern.compile(SEPARATORS_PATTERN_EXPR);

	/**
	 * A function factory is to be injected or set in runtime in order to map the tokens
	 * to the correct function implementations. Functions are the main extension point for
	 * this framework
	 */
	private FuncFactory functionFactory;

	/**
	 * An expression factory is to be injected or set in runtime in order to map the rest of the
	 * tokens (which ar nor strings neither functions) to appropriate expression value
	 * implementations for constant or variable values, for different types (e.g. as Strings,
	 * Numbers, Lists, Booleans, ...)
	 */
	private ExprArgFactory valueFactory;

	// int bracketLevel = 0;

	/**
	 * Instantiate an Expression parser with given factories for expression values and functions
	 *
	 * @param valueFactory
	 * @param functionFactory
	 */
	public ExprParser(ExprArgFactory valueFactory, FuncFactory functionFactory) {
		this.valueFactory = valueFactory;
		this.functionFactory = functionFactory;
	}

	/**
	 * Main access method to create an executable stack from a given text
	 * representing arithmetical or logical expressions
	 *
	 * @param input
	 * @return
	 */
	public List<ExprStack> parse(String input) {

		final List<ExprToken> list = parseTokenList(input);

		if (list != null) {
			int lineCounter = 0;
			final List<ExprStack> expressionList = new ArrayList<>();
			List<ExprToken> singleExpression = new ArrayList<>();
			for (final ExprToken token : list) {
				if (token instanceof ExprTokenOperate) {
					if (Operate.SEMICOLON.equals(((ExprTokenOperate) token).getOperator())) {
						expressionList.add(createStack(singleExpression, lineCounter++));
						singleExpression = new ArrayList<>();
					} else {
						singleExpression.add(token);
					}
				} else {
					singleExpression.add(token);
				}
			}
			if (!singleExpression.isEmpty()) {
				expressionList.add(createStack(singleExpression, lineCounter++));
			}
			return expressionList;
		}

		return null;
	}

	/**
	 * Parse the tokens from given text expressions
	 *
	 * @param input : text expressions
	 * @return : list of parsed tokens
	 */
	public List<ExprToken> parseTokenList(String input) {

		// LOGGER.debug("+++ parseTokenList input = {}", input);

		final List<ExprToken> parsedTokenList = new LinkedList<>();

		int beginIndex = 0;
		// Matcher stringMatcher = STRING_PATTERN.matcher(input);

		final BetterStringParser stringMatcher = new BetterStringParser(input);

		while (stringMatcher.find(beginIndex)) {

			// LOGGER.debug("beginIndex = {}", beginIndex);
			// LOGGER.debug("stringMatcher.start = {}", stringMatcher.start());

			if (beginIndex < stringMatcher.start()) {
				// LOGGER.debug("input.substring = {}",input.substring(beginIndex,
				// stringMatcher.start()));
				parseExpression(input.substring(beginIndex, stringMatcher.start()), parsedTokenList);

				// if(LOGGER.isDebugEnabled()) {
				// for(ExToken tok : parsedTokenList) {
				// LOGGER.debug("+++ token0 = {}", tok);
				// }
				// }

			}
			// string is a token in "" e.g.: "TEST-BLA"
			// String string = stringMatcher.group(0);
			final String string = stringMatcher.parse();
			// LOGGER.debug("+++ STRING = {}", string);
			if (string == null) {
				/** do nothing **/
			} else if (!string.endsWith("\"")) {
				final ExprTokenValue ex = new ExprTokenValue(string);
				ex.setError(new ExprInfoTokenizeError(ExprInfo.Code.UNCLOSED_STRING, string));
				parsedTokenList.add(ex);
			} else {
				if (parsedTokenList.size() > 0) {
					final ExprToken token = parsedTokenList.get(parsedTokenList.size() - 1);
					if (token.getType() == ExprToken.Type.VALUE && !((ExprTokenValue) token).isConcatenated()) {
						// concatenation of sepcial tokens - e.g. for: b + "true" => b"true"
						((ExprTokenValue) token).addToValue(string);

						// LOGGER.debug("+++ concatenatedToken = {}", ((ExTokenValue)
						// token).getValue());
					} else {
						parsedTokenList.add(new ExprTokenValue(string));
					}
				} else {
					parsedTokenList.add(new ExprTokenValue(string));
				}
			}
			beginIndex = stringMatcher.end() + 1;
		}

		if (beginIndex <= input.length()) {
			parseExpression(input.substring(beginIndex), parsedTokenList);
		}

		// if(LOGGER.isDebugEnabled()) {
		// for(ExToken tok : parsedTokenList) {
		// LOGGER.debug("+++ token = {}", tok);
		// }
		// }

		return parsedTokenList;
	}

	/**
	 * helper class to collect stackInfo for user info in case of createStack Errors
	 *
	 * @author u083950
	 */
	public class StackInfo {

		private final int lineNumber;
		static final int LAST_TOKEN_COUNT = 10;
		int lastTokenIndex = 0;
		public final ExprToken[] lastTokens = new ExprToken[LAST_TOKEN_COUNT];

		public StackInfo(int lineNumber) {
			this.lineNumber = lineNumber;
		}

		public ExprToken currentToken;

		public void setCurrentToken(ExprToken exToken) {
			currentToken = exToken;
			lastTokens[lastTokenIndex % LAST_TOKEN_COUNT] = exToken;
			lastTokenIndex++;
		}

		/**
		 * create human readable expression String from tokens
		 *
		 * @return
		 */
		private String expressionAsString() {
			int startIndex = lastTokenIndex;

			final StringBuilder s = new StringBuilder();

			if (startIndex > LAST_TOKEN_COUNT) {
				s.append("...");
			}

			boolean appended = false;
			for (int i = 0; i < LAST_TOKEN_COUNT; i++) {
				if (lastTokens[startIndex % LAST_TOKEN_COUNT] != null) {
					if (appended) {
						s.append(' ');
					} else {
						appended = true;
					}
					s.append(lastTokens[startIndex % LAST_TOKEN_COUNT].debug());
				}
				startIndex++;
			}
			return s.toString();
		}

		public ExprInfo evaluateExpressionInfo(ExprInfo expressionInfo) {
			expressionInfo.setLineNumber(lineNumber);
			expressionInfo.setExpressionAsString(expressionAsString());
			return expressionInfo;
		}
	}

	/**
	 * central method for parsing and syntax checking expressions
	 *
	 * @param expressionString
	 * @param errors
	 * @param stackType
	 * @param id
	 * @param name
	 * @return
	 */
	public List<ExprStack> createExpressionStack(String expressionString, boolean isCondtionStack, Integer id,
			String name, List<LgEntry> errors) {

		if (expressionString == null || expressionString.isEmpty()) {
			return null;
		}

		final List<ExprStack> stackList = parse(expressionString);

		if (stackList != null) {
			StringBuilder err = null;
			if (isCondtionStack && stackList.size() != 1) {
				err = LgEntry.add(err, "only 1 Expression allowed! (is:" + stackList.size() + ")");
			}

			for (final ExprStack expressionStack : stackList) {
				if (expressionStack.getErrors() != null) {
					for (final ExprInfo ex : expressionStack.getErrors()) {
						err = LgEntry.add(err, ex.toString());
					}
				}
			}
			if (err != null) {
				if (isCondtionStack) {
					err.insert(0, "Parsing Condition:");
				} else {
					err.insert(0, "Parsing SetValues:");
				}
				LOGGER.error("In Step {}: {}", id, err);
				errors.add(new LgEntry(name, id, null, err.toString()));
			}
		}

		return stackList;
	}

	/**
	 * Stack creation from a given, parsed token list
	 *
	 * @param parsedTokenList : list of tokens
	 * @return : executable stack object
	 */
	public ExprStack createStack(List<ExprToken> parsedTokenList, int lineCounter) {

		final StackBuilder_NEW stackBuilder = new StackBuilder_NEW(lineCounter);

		int localBracketLevel = 0;
		Prefx prefix = null;
		Operate operator = null;
		Func function = null;
		boolean isFirstCommaInStack = true;

		final StackInfo stackInfo = new StackInfo(lineCounter);

		ExprToken lastToken = null;
		for (final ExprToken exToken : parsedTokenList) {

			stackInfo.setCurrentToken(exToken);

			if (exToken.getError() != null) {
				stackBuilder.addError(stackInfo.evaluateExpressionInfo(exToken.getError()));
			} else {
				switch (exToken.getType()) {
				case BRACKET_OPEN:

					if (lastToken != null && ExprToken.Type.VALUE.equals(lastToken.getType())) {
						final ExprInfoCreateStackError pError = new ExprInfoCreateStackError(
								ExprInfo.Code.UNKNOWN_FUNCTION, lastToken.debug());
						pError.setExpressionDetail(lastToken.debug());
						stackBuilder.addError(pError);
					}

					localBracketLevel++;
					stackBuilder.insertStack(localBracketLevel, operator, function, prefix);

					prefix = null;
					operator = null;
					function = null;
					isFirstCommaInStack = true;

					break;
				case BRACKET_CLOSE:
					localBracketLevel--;
					stackBuilder.reduceStack(localBracketLevel);
					if (localBracketLevel < 0) {
						stackBuilder.addError(stackInfo.evaluateExpressionInfo(
								new ExprInfoCreateStackError(ExprInfo.Code.BRACKET_CLOSE_WITHOUT_OPEN, ")")));
					}
					break;
				case PREFIX:
					prefix = ((ExprTokenPrefix) exToken).getPrefix();
					break;
				case OPERATOR:
					operator = ((ExprTokenOperate) exToken).getOperator();
					if (Operate.COMMA.equals(operator) && isFirstCommaInStack) {
						isFirstCommaInStack = false;
						operator = Operate.COMMA_FIRST;
					}

					if (operator.equals(Operate.ASSIGN)) {
						final ExprStack stack = stackBuilder.getCurrentStack();
						final int index = stack.list.size() - 1;
						if (!stack.list.get(index).isLValue()) {
							LOGGER.warn(ExprInfo.Code.NOT_AN_LVALUE + " >" + stack.list.get(index));
							stackBuilder.addError(stackInfo.evaluateExpressionInfo(
									new ExprInfoCreateStackError(ExprInfo.Code.NOT_AN_LVALUE, stack.toString())));
						}
					}

					stackBuilder.setOperatorPriority(operator);
					break;
				case FUNCTION:
					function = ((ExprTokenFunc) exToken).getFunction();
					break;
				case VALUE:
					final String text = ((ExprTokenValue) exToken).getValue();
					if (lastToken != null && lastToken.getType().equals(exToken.getType())) {
						// ExpressionParserError pError = new
						// ExpressionParserError(ExpressionParserError.Type.CREATESTACK,
						// "UnexpectedToken", text);
						// exToken.setError(pError);
						stackBuilder.addError(stackInfo.evaluateExpressionInfo(
								new ExprInfoCreateStackError(ExprInfo.Code.UNEXPECTED_TOKEN, text)));
					}
					// LOGGER.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					// LOGGER.debug("+++ text = {}", text);
					// LOGGER.debug("+++ bracketLevel = {}", localBracketLevel);
					// LOGGER.debug("+++ function = {}", function);

					try {
						final Expr value = valueFactory.createExprArg(text, prefix, operator);
						// value.bracketLevel = localBracketLevel;
						//
						// value.prefix = prefix;
						// value.operator = operate;
						// value.function = function;

						stackBuilder.insertValue(value);
						prefix = null;
						operator = null;
						function = null;
					} catch (final ExprException e) {
						// ExpressionParserError pError = new
						// ExpressionParserError(ExpressionParserError.Type.CREATESTACK,
						// "UnrecognizedToken", text);
						// exToken.setError(pError);
						stackBuilder.addError(stackInfo.evaluateExpressionInfo(
								new ExprInfoCreateStackError(ExprInfo.Code.UNRECOGNIZED_TOKEN, text)));

						// stackBuilder.addError("error building value! '" + text + "' " +
						// e.getMessage() + getDebugString(lineCounter, lastTokenCount,
						// lastTokenIndex, lastTokens ));
						LOGGER.warn("Error when creating stack for value: {}", e.getExpressionInfo());
					}

					break;
				default:
					break;
				}
			}
			lastToken = exToken;
		}

		if (localBracketLevel != 0) {
			// stackBuilder.getStack().bracketLevel = localBracketLevel;
			// stackBuilder.addError("Brackets do not sum up! " + localBracketLevel + " " +
			// stackBuilder.getStack().toString());
			// stackBuilder.addError("Brackets do not sum up! " + getDebugString(lineCounter,
			// lastTokenCount, lastTokenIndex, lastTokens ));
			// LOGGER.warn("Brackets do not sum up! " + stackBuilder.getStack().toString() + " " +
			// localBracketLevel);

			String brackets = "";
			if (localBracketLevel > 0) {
				for (int i = 0; i < localBracketLevel; i++) {
					brackets += '(';
				}
			} else {
				for (int i = 0; i < (-localBracketLevel); i++) {
					brackets += ')';
				}
			}

			// ExpressionParserError pError = new
			// ExpressionParserError(ExpressionParserError.Type.CREATESTACK, "BracketsDoNotSumUp",
			// brackets);
			// lastToken.setError(pError);
			stackBuilder.addError(stackInfo.evaluateExpressionInfo(
					new ExprInfoCreateStackError(ExprInfo.Code.BRACKETS_DO_NOT_SUM_UP, brackets)));
		}
		// else if (bracketCloseWithoutOpen != 0) {
		// // stackBuilder.getStack().bracketLevel = localBracketLevel;
		// // stackBuilder.addError("Close Bracket without open! " + bracketCloseWithoutOpen + " " +
		// stackBuilder.getStack().toString());
		// stackBuilder.addError("Close Bracket without open! " + error);
		// }
		return stackBuilder.getStack();
	}

	// private String getDebugString(int lineCounter, int lastTokenCount, int lastTokenIndex,
	// ExToken[] lastTokens ) {
	//
	// int startIndex = lastTokenIndex++;
	//
	// StringBuilder s = new StringBuilder();
	//
	// s.append("line:");
	// s.append(lineCounter);
	// s.append(" at: '");
	//
	// if(startIndex > lastTokenCount) {
	// s.append("...");
	// }
	//
	// boolean appended = false;
	// for(int i = 0; i<lastTokenCount; i++) {
	// if(lastTokens[startIndex%lastTokenCount] != null) {
	// if(appended) {
	// s.append(' ');
	// } else {
	// appended = true;
	// }
	// s.append(lastTokens[startIndex%lastTokenCount].debug());
	// }
	// startIndex++;
	// }
	// s.append("'<- ");
	//
	// return s.toString();
	// }

	/**
	 * @param input
	 * @param parsedTokenList
	 */
	private void parseExpression(String input, List<ExprToken> parsedTokenList) {
		int beginIndex = 0;

		input = input.replaceAll("^,\\s*,", ",null,");

		final Matcher separatorMatcher = SEPARATORS_PATTERN.matcher(input);

		while (separatorMatcher.find(beginIndex)) {
			final String separators = separatorMatcher.group(1);

			parseText(input, beginIndex, separatorMatcher.start(), parsedTokenList);
			beginIndex = separatorMatcher.end();

			if (separators != null && separators.length() > 0) {
				parseSeparators(separators, parsedTokenList);
			}
		}
		parseText(input, beginIndex, input.length(), parsedTokenList);
	}

	/**
	 * @param input
	 * @param beginIndex
	 * @param endIndex
	 * @param parsedTokenList
	 */
	private void parseText(String input, int beginIndex, int endIndex, List<ExprToken> parsedTokenList) {
		if (beginIndex < endIndex) {
			final String text = input.substring(beginIndex, endIndex);
			if (functionFactory != null) {
				final Func function = functionFactory.getFunction(text);
				if (function != null) {
					parsedTokenList.add(new ExprTokenFunc(function));
					return;
				}
				// {
				// ExTokenFunction<K, V> fun = new ExTokenFunction<K, V>(null);
				// fun.setError(new ExpressionParserError(ExpressionParserError.Type.TOKENIZE,
				// "unknown function", text));
				// parsedTokenList.add(fun);
				// }
			}
			parsedTokenList.add(new ExprTokenValue(text));
		}
	}

	final private static String BRACKET_OPEN = "(\\()";
	final private static String BRACKET_CLOSE = "(\\))";
	final private static Pattern BRACKETS_AND_OPERATORS_PATTERN = Pattern
			.compile("\\s*(" + BRACKET_OPEN + "|" + BRACKET_CLOSE + "|" + Operate.OPERATORS_PATTERN + ")");

	/**
	 * @param input
	 * @param parsedTokenList
	 */
	private void parseSeparators(String input, List<ExprToken> parsedTokenList) {

		// LOGGER.debug("+++ parseSeparators input {}", input);

		final Matcher matcher = BRACKETS_AND_OPERATORS_PATTERN.matcher(input);

		int operatorsFound = 0;
		while (matcher.find()) {

			int index = -1;
			for (int i = 2; i <= matcher.groupCount(); i++) {
				// LOGGER.debug(matcher.group(i));
				if (matcher.group(i) != null) {
					index = i;
					break;
				}
			}

			if (index == 2) {
				// LOGGER.debug(" + bracketOpen: " + index + " - (");
				parsedTokenList.add(new ExprTokenBracketOpen(/* ++retBracketLevel */));
			} else if (index == 3) {
				// LOGGER.debug(" + bracketClose:" + index + " - )");
				parsedTokenList.add(new ExprTokenBracketClose(/* retBracketLevel-- */));
			} else {
				// LOGGER.debug(" +++ operator: {} - {}", index, Operate.get(index - 4));
				operatorsFound++;

				ExprToken lastToken = null;
				if (parsedTokenList.size() > 0) {
					lastToken = parsedTokenList.get(parsedTokenList.size() - 1);
				}

				if (lastToken instanceof ExprTokenBracketOpen) {
					setPrefix(Operate.get(index - 4), parsedTokenList);
				} else if (operatorsFound == 1) {
					if (parsedTokenList.size() == 0) {
						setPrefix(Operate.get(index - 4), parsedTokenList);
					} else {
						final Operate operator = Operate.get(index - 4);
						parsedTokenList.add(new ExprTokenOperate(operator));
					}
				} else if (operatorsFound == 2 && parsedTokenList.size() > 0) {
					setPrefix(Operate.get(index - 4), parsedTokenList);
				} else {
					final Operate operator = Operate.get(index - 4);
					final ExprTokenOperate exTokenPrefix = new ExprTokenOperate(null);
					exTokenPrefix.setError(
							new ExprInfoTokenizeError(ExprInfo.Code.OPERATOR_NOT_ALLOWED, operator.toString()));
					parsedTokenList.add(exTokenPrefix);
				}
			}
		}
	}

	private void setPrefix(Operate operator, List<ExprToken> parsedTokenList) {
		if (operator == Operate.PLUS) {
			parsedTokenList.add(new ExprTokenPrefix(Prefx.PLUS));
		} else if (operator == Operate.MINUS) {
			parsedTokenList.add(new ExprTokenPrefix(Prefx.MINUS));
		} else {
			final ExprTokenPrefix exTokenPrefix = new ExprTokenPrefix(null);
			// exTokenPrefix.setError(new ExpressionParserError(ExpressionParserError.Type.TOKENIZE,
			// "MisplacedOperator" ,operator.toString()));
			exTokenPrefix.setError(new ExprInfoTokenizeError(ExprInfo.Code.MISPLACED_OPERATOR, operator.toString()));
			parsedTokenList.add(exTokenPrefix);
		}
	}

	public FuncFactory getFunctionFactory() {
		return functionFactory;
	}

	public void setFunctionFactory(FuncFactory functionFactory) {
		this.functionFactory = functionFactory;
	}

	public ExprArgFactory getValueFactory() {
		return valueFactory;
	}

	public void setValueFactory(ExprArgFactory valueFactory) {
		this.valueFactory = valueFactory;
	}

}
