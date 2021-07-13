package com.lienisoft.mtr.expressionInterpreter.basic;

public class ExprInfoTokenizeError extends ExprInfo {

//	private String name = null;
	
	public ExprInfoTokenizeError(ExprInfo.Code code, String expressionDetail) {
		super(code);
		setExpressionDetail(expressionDetail);
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

	@Override
	protected String getInfoText() {
		// return getCode().toString() + ": " + getName();
		return getCode().toString();
	}

//	private String getNotDefined() {
//		StringBuilder s = new StringBuilder();
//		s.append(leftClazz != null ? leftClazz : "null");
//		s.append(operator != null ? operator.toString() : " ");
//		s.append(rightClazz != null ? rightClazz : "null");
//		return s.toString();
//	}
}
