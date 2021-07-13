package com.lienisoft.mtr.expressionInterpreter.basic;

import java.util.Date;

public enum Operate {

	// Praezedenztabelle / precedence table
	// ------------------------------------
	// ORDER/RANGE of logic operators
	//
	// prio | implemented | java standard
	// -----+-------------+-----------------------------------------
	// 1 .. | prefix- ... | ++,--,(prefix)+-,~,!,(cast)
	// 2 .. | *,/ ....... | *,/,%
	// 3 .. | +,- ....... | +,-,(string concatenation)+
	// 4 .. | ........... | <<,>>,>>>
	// 5 .. | <,<=,>,>= . | <,<=,>,>=,instanceof
	// 6 .. | ==,!= ..... | ==,!=
	// 7 .. | ........... | & (bit and)
	// 8 .. | ........... | ^
	// 9 .. | ........... | | (bit or)
	// 10 . | && ........ | && (logic and)
	// 11 . | || ........ | || (logic or)
	// 12 . | ........... | ?:
	// 13 . | =,+= ...... | =,*=,/=,%=,+=,-=,<<=,>>=,>>>=,&=,^=,|=
	// 14 . | , (comma)
	// 15 . | ; (semicolon)
	//

	PLUS_ASSIGN(13, "+=") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			if (left.get() == null) {
				return ASSIGN.exe(left, right);
			}
			return ASSIGN.exe(left, PLUS.exe(left, right));
		}
	}, 		// \\+=
	MULTIPLICATE(2, "*") {

		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			{
				final Integer leftInteger = left.tryGet(Integer.class);
				final Integer rightInteger = right.tryGet(Integer.class);
				if (leftInteger != null && rightInteger != null) {
					return left.create(leftInteger * rightInteger);
				}
				throw exception(left, right);
			}
		}
	},	 	// \\*
	DIVIDE(2, "/") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			final Integer leftInteger = left.tryGet(Integer.class);
			final Integer rightInteger = right.tryGet(Integer.class);
			if (leftInteger != null && rightInteger != null && rightInteger != 0) {
				return left.create(leftInteger / rightInteger);
			}
			if (rightInteger != null && rightInteger == 0) {
				throw new ExprException(getClass(),
						new ExprInfoOperationNotDefined("division by 0!", left.get(), right.get(), this));
			}
			throw exception(left, right);
		}
	}, 			//
	PLUS(3, "+") {

		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			final Integer leftInteger = left.tryGet(Integer.class);
			if (leftInteger != null) {
				final Integer rightInteger = right.tryGet(Integer.class);
				if (rightInteger != null) {
					return right.create(leftInteger + rightInteger);
				}
				final Date rightDate = right.tryGet(Date.class);
				if (rightDate != null) {
					final long t = rightDate.getTime();
					return right.create(new Date(t + leftInteger * MINUTES_IN_MILLISECONDS));
				}
				final String rightString = right.tryGet(String.class);
				if (rightString != null) {
					return right.create(left.asString() + rightString);
				}
				throw exception(left, right);
			}

			final String leftString = left.tryGet(String.class);
			if (leftString != null) {
				if (right.get() == null) {
					// no append in case of null on right side
					return left.create(leftString);
				}
				return left.create(leftString + right.asString());
			}

			final Date leftdate = left.tryGet(Date.class);
			if (leftdate != null) {
				final Integer rightInteger = right.tryGet(Integer.class);
				if (rightInteger != null) {
					final long t = leftdate.getTime();
					return left.create(new Date(t + rightInteger * MINUTES_IN_MILLISECONDS));
				}
				final String rightString = right.tryGet(String.class);
				if (rightString != null) {
					return left.create(left.asString() + rightString);
				}
				throw exception(left, right);
			}

			if (left.get() == null) {
				final String rightString = right.tryGet(String.class);
				if (rightString != null) {
					return right.create(rightString);
				}
			}
			throw exception(left, right);
		}
	},				// \\+
	MINUS(3, "-") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			final Integer leftInteger = left.tryGet(Integer.class);
			if (leftInteger != null) {
				final Integer rightInteger = right.tryGet(Integer.class);
				if (rightInteger != null) {
					return left.create(leftInteger - rightInteger);
				}
				throw exception(left, right);
			}

			final Date leftDate = left.tryGet(Date.class);
			if (leftDate != null) {
				final Integer rightInteger = right.tryGet(Integer.class);
				if (rightInteger != null) {
					final long t = leftDate.getTime();
					return right.create(new Date(t - rightInteger * MINUTES_IN_MILLISECONDS));
				}
				final Date rightDate = right.tryGet(Date.class);
				if (rightDate != null) {
					final long timeDiffMinutes = (leftDate.getTime() - rightDate.getTime()) / MINUTES_IN_MILLISECONDS;
					return right.create((int) timeDiffMinutes);
				}
				if (right.get() == null) {
					return left.create(leftDate);
				}
			}
			throw exception(left, right);
		}

	},				// \\-
	LESS_OR_EQUAL(5, "<=") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			if (left.isComparable(right)) {
				return left.create(left.compareTo(right) <= 0);
			}
			throw exception(left, right);
		}
	}, 	//
	LESS(5, "<") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			if (left.isComparable(right)) {
				return left.create(left.compareTo(right) < 0);
			}
			throw exception(left, right);
		}
	}, 				//
	GREATER_OR_EQUAL(5, ">=") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			if (left.isComparable(right)) {
				return left.create(left.compareTo(right) >= 0);
			}
			throw exception(left, right);
		}
	}, 	//
	GREATER(5, ">") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			if (left.isComparable(right)) {
				return left.create(left.compareTo(right) > 0);
			}
			throw exception(left, right);
		}
	}, 			//
	EQUAL(6, "==") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			return left.create(left.equals(right));
		}
	}, 			//
	NOT_EQUAL(6, "!=") {
		@Override
		public TArgument exe(TArgument left, TArgument right) {
			return left.create(!left.equals(right));
		}
	}, 		//
	AND(10, "&&", true) {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			final Boolean leftBoolean = getBool(left);
			final Boolean rightBoolean = getBool(right);
			if (leftBoolean != null && rightBoolean != null) {
				return left.create(leftBoolean && rightBoolean);
			}
			throw exception(left, right);
		};
	},		//
	OR(11, "||", true) {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			final Boolean leftBoolean = getBool(left);
			final Boolean rightBoolean = getBool(right);
			if (leftBoolean != null && rightBoolean != null) {
				return left.create(leftBoolean || rightBoolean);
			}
			throw exception(left, right);
		};
	},			// \\|\\|
	ASSIGN(13, "=") {
		@Override
		public TArgument exe(TArgument left, TArgument right) throws ExprException {
			/**
			 * throws ExprException if assignment not possible!
			 */
			left.assign(right);
			return left;
		}

	},			//
	COMMA(14, ",", true) {
		@Override
		public TArgument exe(TArgument left, TArgument right) {
			// final List<Object> list = left.get(List.class);
			// list.add(right.get());
			// return left;
			// if (left instanceof TStruct) {
			// @SuppressWarnings("unchecked")
			// final List<Object> list = left.get(List.class);
			// ((TStruct) left).putContainer(list.size(), right.getContainer());
			// return left;
			// }

			final Integer listSize = left.listSize();
			if (listSize != null) {
				left.put(listSize, right.get());
				return left;
			}
			return left.create(null);
		}
	},
	SEMICOLON(15, ";") {
		@Override
		public TArgument exe(TArgument left, TArgument right) {
			// Not implemented.
			return null;
		}
	},
	COMMA_FIRST(14, null, true) { // First Comma in a Stack!

		@Override
		public TArgument exe(TArgument left, TArgument right) {

			final TArgument listArgument = left.create(left.createList());
			if (listArgument != null) {
				listArgument.put(0, left.get());
				listArgument.put(1, right.get());
				return listArgument;
			}
			return left.create(null);
		}

		@Override
		public String toString() {
			return ",";
		}

	};

	// private static final Logger LOG = LoggerFactory.getLogger(Operate.class);

	private final String symbol;

	public final int prio;
	// private final String pattern;
	public final boolean checkForBreak;

	static public final String OPERATORS_PATTERN = getOperatorsPattern();
	private static final long MINUTES_IN_MILLISECONDS = 60000L;

	Operate(int prio, String symbol) {
		this(prio, symbol, false);
	}

	Operate(int prio, String symbol, boolean checkIfBreak) {
		this.prio = prio;
		this.symbol = symbol;
		this.checkForBreak = checkIfBreak;
	}

	static public Operate get(int index) {

		if (index < 0 || index > values().length) {
			return null;
		}

		return values()[index];
	}

	static private String getOperatorsPattern() {
		StringBuilder s = null;
		for (final Operate o : values()) {
			if (o.symbol != null) {
				String pattern = o.symbol;

				// escape certain characters to create a regular expression
				pattern = pattern.replaceAll("\\+", "\\\\+").replaceAll("\\-", "\\\\-").replaceAll("\\*", "\\\\*")
						.replaceAll("\\|", "\\\\|");

				if (s == null) {
					s = new StringBuilder();
				} else {
					s.append('|');
				}
				s.append('(');
				s.append(pattern);
				s.append(')');
			}
		}
		return s.toString();
	}

	@Override
	public String toString() {
		return symbol;
	}

	abstract public TArgument exe(TArgument left, TArgument right) throws ExprException;

	protected ExprException exception(TArgument left, TArgument right) {
		return new ExprException(getClass(),
				new ExprInfoOperationNotDefined(name() + " between incompatible types", left.get(), right.get(), this));
	}

	public static Boolean getBool(TArgument arg) {
		return arg.get() == null ? false : arg.tryGet(Boolean.class);
	}
}
