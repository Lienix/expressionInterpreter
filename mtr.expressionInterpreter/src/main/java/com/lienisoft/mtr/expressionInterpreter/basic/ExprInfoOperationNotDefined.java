package com.lienisoft.mtr.expressionInterpreter.basic;

public class ExprInfoOperationNotDefined extends ExprInfo {

	// OperationNotDefined information
	private final String leftClazz; // class SimpleName
	private final String rightClazz; // class SimpleName
	private final String info;
	private final Operate operator;
	
//	public ExpressionInfoOperationNotDefined() {
//		super(ExpressionInfo.Code.OperationNotDefined);
//	}

	public ExprInfoOperationNotDefined(ExprInfo.Code code, String info, Object left, Object right, Operate operator) {
		super(code);
		if(left != null) {
			leftClazz = left.getClass().getSimpleName();
		} else {
			leftClazz = null;
		}
		if(right != null) {
			rightClazz = right.getClass().getSimpleName();
		} else {
			rightClazz = null;
		}
		this.info = info;
		this.operator = operator;
	}

	
	public ExprInfoOperationNotDefined(String info, Object left, Object right, Operate operator) {
		super(ExprInfo.Code.OPERATION_NOT_DEFINED);
		if(left != null) {
			leftClazz = left.getClass().getSimpleName();
		} else {
			leftClazz = null;
		}
		if(right != null) {
			rightClazz = right.getClass().getSimpleName();
		} else {
			rightClazz = null;
		}
		this.info = info;
		this.operator = operator;
	}

	public String getLeftClazz() {
		return leftClazz;
	}

//	public void setLeftClazz(String leftClazz) {
//		this.leftClazz = leftClazz;
//	}

	public String getRightClazz() {
		return rightClazz;
	}

//	public void setRightClazz(String rightClazz) {
//		this.rightClazz = rightClazz;
//	}

	public Operate getOperator() {
		return operator;
	}

//	public void setOperator(Operator operator) {
//		this.operator = operator;
//	}

	@Override
	protected String getInfoText() {
		return getCode().toString() + " " + info + ": " + getNotDefined();
	}

	private String getNotDefined() {
		StringBuilder s = new StringBuilder();
		s.append(leftClazz != null ? leftClazz : "null");
		s.append(operator != null ? operator.toString() : " ");
		s.append(rightClazz != null ? rightClazz : "null");
		return s.toString();
	}
}
