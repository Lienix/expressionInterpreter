package com.lienisoft.mtr.expressionInterpreter.basic;

public class ExprInfoWrongParameterCount extends ExprInfo {

	private int receivedCount = -1; 
	private int minimum = -1;
	private Integer maximum = null;
	
	public ExprInfoWrongParameterCount() {
		super(ExprInfo.Code.WRONG_PARAMETER_COUNT);
	}

	public int getReceivedCount() {
		return receivedCount;
	}

	public void setReceivedCount(int receivedCount) {
		this.receivedCount = receivedCount;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	@Override
	protected String getInfoText() {
		return getCode().toString() + "=" + getWrongCount();
	}

	private String getWrongCount() {
		StringBuilder s = new StringBuilder();
		s.append(receivedCount);
		s.append(' ');
		if(maximum != null && minimum == maximum) {
			s.append("(expected=");
			s.append(minimum);
		} else {
			if(maximum == null) {
				s.append("(expected>=");
				s.append(minimum);
			} else {
				s.append("(expected:");
				s.append(minimum);
				s.append("-");
				s.append(maximum);
			}
		}
		s.append(')');
		return s.toString();
	}
}
