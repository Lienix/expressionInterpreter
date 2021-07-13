package com.lienisoft.mtr.expressionInterpreter.basic;

public class ExprInfoInvalidValue extends ExprInfo {

	private final int index;
	
	private final String info;

	/**
	 * Standard Constructor 
	 * 
	 * @param index
	 * @param info
	 */
	public ExprInfoInvalidValue(int index, String info) {
		super(Code.INVALID_EXPRESSION_VALUE);
		this.index = index;
		this.info = info;
	}

	/**
	 * Fallback Constructor 
	 * 
	 * @param code
	 * @param index
	 * @param info
	 */
	//@Deprecated
	public ExprInfoInvalidValue(ExprInfo.Code code, int index, String info) {
		super(code);
		this.index = index;
		this.info = info;
	}
	

	@Override
	public String getInfoText() {
		if(index < 0) {
			return getCode().toString() + " " + info;
		} else {
			return getCode().toString() +":Param[" + index + "]" + " " + info;
		}
	}

	public int getIndex() {
		return index;
	}

	public String getInfo() {
		return info;
	}

	
	
}
