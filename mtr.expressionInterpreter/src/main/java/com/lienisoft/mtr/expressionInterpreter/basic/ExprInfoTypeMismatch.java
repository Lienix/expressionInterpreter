package com.lienisoft.mtr.expressionInterpreter.basic;

public class ExprInfoTypeMismatch extends ExprInfo {
	
	
	private final int index;
	private final String expectedType; // name(s) of the expected type(s)
	private final String receivedType; // name(s) of the received type(s)
	
	public ExprInfoTypeMismatch(int index, String receivedType, String expectedType) {
		super(ExprInfo.Code.TYPE_MISMATCH);
		this.index = index;
		this.expectedType = expectedType;
		this.receivedType = receivedType;
	}
	
	public int getIndex() {
		return index;
	}
//	public void setIndex(int index) {
//		this.index = index;
//	}
	public String getExpectedType() {
		return expectedType;
	}
//	public void setExpectedType(String expectedType) {
//		this.expectedType = expectedType;
//	}
	public String getReceivedType() {
		return receivedType;
	}
//	public void setReceivedType(String receivedType) {
//		this.receivedType = receivedType;
//	}

	public String getInfoText() {
		if(index < 0) {
			return getCode().toString() +"=" + getExpectedReceived();
		} else {
			return getCode().toString() +":Param[" + index + "]=" + getExpectedReceived();
		}
	}
	
	public String getExpectedReceived() {
		StringBuilder s = new StringBuilder();
		// if(receivedType != null) {
			s.append(receivedType);
		// } 
		s.append(' ');
		// if(expectedType != null) {
			s.append("(expected=");
			s.append(expectedType);
			s.append(')');
		// }
		return s.toString();
	}
}
