package com.lienisoft.mtr.expressionInterpreter.basic;

public enum Prefx {
	PLUS {
		@Override
		public String toString() {
			return "+";
		}
	},
	MINUS {
		@Override
		public String toString() {
			return "-";
		}
	};

	@Override
	public abstract String toString();
	// {
	// switch(this) {
	// case minus:
	// return "-";
	// case plus:
	// return "+";
	// default:
	// return "?";
	// }
	// }
}
