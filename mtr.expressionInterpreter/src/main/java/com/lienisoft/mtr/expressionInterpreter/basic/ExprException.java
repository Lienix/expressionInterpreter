package com.lienisoft.mtr.expressionInterpreter.basic;

/**
 * Exception thrown if an expression stack is built or in runtime if a stack is executed
 * with concrete message data
 *
 * @author u083950
 */
public class ExprException extends Exception {
	private static final long serialVersionUID = -4539783531581300660L;

	private final Class<?> clazz;
	private final String messageText;
	private final ExprInfo expressionInfo;

//	public ExpressionException(ExpressionException right) {
//		this.code = right.code;
//		this.clazz = right.clazz;
//		this.messageText = right.messageText;
//		this.expressionInfo = right.expressionInfo;
//	}

	/**
	 * Standard Constructor
	 * 
	 * @param code
	 * @param clazz the class which threw the exception for better logging
	 * @param messageText
	 */
	public ExprException(Class<?> clazz, ExprInfo expressionInfo) {
		// this.code = expressionInfo.getCode();
		this.clazz = clazz;
		this.messageText = null;
		this.expressionInfo = expressionInfo;
//		
//		expressionInfo.setInfoText(code.name());
		
//		this.leftClazz = null;
//		this.rightClazz = null;
//		this.printLeftAndRight = false;
	}

	/**
	 * Fallback Constructor! Available for historical reason. Prefer Standard Constructor!
	 * 
	 * @param code
	 * @param clazz
	 * @param info
	 */
	public ExprException(ExprInfo.Code code, Class<?> clazz, String info) {
		this.clazz = clazz;
		this.messageText = null;
		this.expressionInfo = new ExprInfoInvalidValue(code, -1, info);
//		this.leftClazz = null;
//		this.rightClazz = null;
//		this.printLeftAndRight = false;
	}

	
	
	
//	public ExpressionException(Code code, Class<?> clazz, String messageText, Object leftClazz, Object rightClazz) {
//		this.code = code;
//		this.clazz = clazz;
//		this.index = -1;
//		if(leftClazz == null) {
//			this.leftClazz = null;
//		} else {
//			this.leftClazz = leftClazz.getClass();	
//		}
//		this.messageText = messageText;
//		if(rightClazz == null) {
//			this.rightClazz = null;
//		} else {
//			this.rightClazz = rightClazz.getClass();	
//		}
//		this.printLeftAndRight = true;
//	}

	public String getMessage() {
		StringBuilder s = new StringBuilder();
		if(getCode() != null) {
			s.append(getCode());
		}
		s.append('|');
		if(clazz != null) {
			s.append(clazz.getSimpleName());
		}
		s.append('|');
		if(messageText != null) {
			s.append(messageText);
		}
//		if(printLeftAndRight) {
//			s.append(": ");
//			s.append(leftClazz != null ? leftClazz.getSimpleName() : "null");
//			s.append(" <> ");
//			s.append(rightClazz != null ? rightClazz.getSimpleName() : "null");
//		}
		s.append('|');
		return s.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.getClass().getName());
		s.append('\n');
		s.append(expressionInfo.toString());
		return s.toString();
//		if(! printLeftAndRight) {
//			return (code != null ? code.toString() : "") + "|" + (clazz != null ? clazz.getSimpleName() : "") + "|"
//				+ (messageText != null ? messageText : "") + "|";
//		} else {
//			return (code != null ? code.toString() : "") + "|" + (clazz != null ? clazz.getSimpleName() : "") + "|"
//				+ (messageText != null ? messageText : "") + ": " + (leftClazz != null ? leftClazz.getSimpleName() : "null") + " <> " + (rightClazz != null ? rightClazz.getSimpleName() : "null") + "|" ;
//		}
 	} 

	public String getMessageText() {
		return messageText;
	}

	public ExprInfo.Code getCode() {
		return expressionInfo.getCode();
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public ExprInfo getExpressionInfo() {
		return expressionInfo;
	}
}
