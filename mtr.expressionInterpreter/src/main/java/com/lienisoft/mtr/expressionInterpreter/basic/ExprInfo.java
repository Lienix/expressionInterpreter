package com.lienisoft.mtr.expressionInterpreter.basic;

/**
 * info needed for concatenation of errors for user info.
 * concatenate Parser Errors
 * concatenate Runtime Errors
 *
 * @author u083950
 */
public abstract class ExprInfo {

	public enum Code {
		EXPRESSION_ERROR,			// general error in an expression - Parsing/Interpretation
										// failed
		NOT_AN_LVALUE,				// Error: Assignment to a constant value!
		TYPE_MISMATCH,				// Error: Operation between incompatible types! / parameter type
										// not supported by function
		OPERATION_NOT_DEFINED,		// Error: Operation not defined for data type!
		ASSIGNMENT_FAILED,			// Assignment failed ... (TODO! description)

		INVALID_EXPRESSION_VALUE,	// Syntax error of a value in an expression
		WRONG_PARAMETER_COUNT,   	// Count of parameters not supported by function

		/** Parser Errors **/

		/** Tokenize Errors **/
		MISPLACED_OPERATOR,			// Wrong Operator was used as arithmetical sign (+ -)
		OPERATOR_NOT_ALLOWED,		// Operator after Operator.

		/** CreateStack Errors **/
		BRACKET_CLOSE_WITHOUT_OPEN, //
		BRACKETS_DO_NOT_SUM_UP, 	//
		UNEXPECTED_TOKEN, 			//
		UNRECOGNIZED_TOKEN, 		//
		UNKNOWN_FUNCTION, 			// Undefined function
		UNCLOSED_STRING;			// Missing " at end of Sring
	}

	private int lineNumber;

	private String expressionAsString = null;

	private String expressionDetail = null;

	private final Code code;

	public ExprInfo(Code code) {
		this.code = code;
	}

	protected abstract String getInfoText();

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append("line ");
		s.append(lineNumber);
		if (expressionAsString != null) {
			s.append(": '");
			s.append(expressionAsString);
			s.append("'<- ");
		} else {
			s.append(": ");
		}
		final String infoText = getInfoText();
		if (infoText != null) {
			s.append(infoText);
		}
		if (expressionDetail != null) {
			s.append("[");
			s.append(expressionDetail);
			s.append("]");
		}
		return s.toString();
	}

	public String getExpressionAsString() {
		return expressionAsString;
	}

	public void setExpressionAsString(String expressionAsString) {
		this.expressionAsString = expressionAsString;
	}

	public String getExpressionDetail() {
		return expressionDetail;
	}

	public void setExpressionDetail(String expressionDetail) {
		this.expressionDetail = expressionDetail;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Code getCode() {
		return code;
	}
}
