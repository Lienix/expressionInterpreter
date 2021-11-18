package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo.Code;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionGetPatTest {
	
	@Test
	public void test() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = GETPAT(\"CD23BDEF\",\"[0-9]{2}\")";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals("23", jh.get("B", String.class));
		}

		{
			final TDataArg jh = new TDataArg();
			jh.put("A.TEST", "Bla 0123 xyz 17");
			final String expression = "B = GETPAT(A.TEST,\"[0-9]{2}\")";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals("01", jh.get("B", String.class));
		}

		{
			final TDataArg jh = new TDataArg();
			jh.put("A.TEST", "Bla yz 1 9");
			final String expression = "B = GETPAT(A.TEST,\"[0-9]{2}\")";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals("", jh.get("B", String.class));
		}
	
		
		
		
		
		
		
		
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "C=(null,1,\"blub\"); B = LEN(C);";
//			ExpressionTester.evaluateExpression(jh, expression);
//			System.out.println(jh);
//			assertEquals((Integer) 3, jh.get("B", Integer.class));
//		}
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN(\"\")";
//			ExpressionTester.evaluateExpression(jh, expression);
//			System.out.println(jh);
//			assertEquals((Integer) 0, jh.get("B", Integer.class));
//		}
//
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN(\"12345abc\")";
//			ExpressionTester.evaluateExpression(jh, expression);
//			System.out.println(jh);
//			assertEquals((Integer) 8, jh.get("B", Integer.class));
//		}
	}
	
	
	
	  public final static String TEST_MSG_1 = "QK QLHQ3LH\r\n"//
	            + ".PTMAPET 122321 MESX007 JAN14\r\n"//
	            + "BSM\r\n"//
	            + ".V/1TADD/PART1\r\n"//
	            + ".I/ET2619/13JAN/BKK\r\n"//
	            + ".F/LH599/13JAN/FRA/X\r\n"//
	            + ".N/02207501805003\r\n"//
	            + ".W/K/3/59\r\n"//
	            + ".P/KANTOR/INGELINTMRS\r\n"//
	            + ".L/BBVBEU\r\n"//
	            + "ENDBSM";

	    public final static String TEST_MSG_2 = "BPM\r\n"//
	            + ".V/1LIST\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965001\r\n"//
	            + ".N/0220415964001\r\n"//
	            + "ENDBPM";
	    public final static String TEST_MSG_5 = "BSM\r\n"//
	            + ".V/1LIST\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965003\r\n"//
	            + ".T/XXX\r\n"//
	            + ".N/0220415964003\r\n"//
	            + "ENDBSM\r\n";
	    public final static String TEST_MSG_5_BAGMESSAGE_EXEPECTED_ADDRESSTAMP = "LX10\r\n"//
	            + "BSM\r\n"//
	            + ".V/1LIST\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965003\r\n"//
	            + ".T/XXX\r\n"//
	            + ".N/0220415964003\r\n"//
	            + "ENDBSM\r\n";

	    public final static String TEST_MSG_3 = "BPM\r\n"//
	            + ".V/1LIST/PART1\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965001\r\n"//
	            + ".N/0220415964001\r\n"//
	            + "ENDPART1";

	    public final static String TEST_MSG_4 = "\u0001QD MUCBPLH\r\n"//
	            + ".MUCDPLH 281602\r\n"//
	            + "\u0002BCM\r\n"//
	            + "FOM\r\n"//
	            + ".V/1LMUC\r\n"//
	            + ".F/LH2860/29JAN/BUD\r\n"//
	            + "ENDBCM\r\n"//
	            + "\u0003";

	    public final static String TEST_MSG_6 = "BPM\r\n"//
	            + ".V/1LIST/PART1\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965001\r\n"//
	            + ".N/0220415964001\r\n"//
	            + "ENDPART11";

	    public final static String TEST_MSG_7 = "BPM\r\n"//
	            + ".V/1LIST/PART1\r\n"//
	            + ".J/S///18MAR/145625L/RECBSM\r\n"//
	            + ".F/LH1301/18MAR/FRA/M\r\n"//
	            + ".N/0220415965001\r\n"//
	            + ".N/0220415964001\r\n"//
	            + "ENDPART9999";
	
	    
	    
	    public static final String MSG1 = "BSM\r\n" + //
	            ".V/1LMEX\r\n" + //
	            ".F/LH499/27JAN/FRA/M\r\n" + //
	            ".I/OS4064/28JAN/FLR/M\r\n" + //
	            ".O/BD4064/28JAN/FLR/M\r\n" + //
	            ".N/0220123456001\r\n" + //
	            ".S/Y/40D/C/191\r\n" + //
	            ".P/GONZALES/SPEEDYMR\r\n" + //
	            ".L/ABCDEF\r\n" + //
	            ".T/1234\r\n" + //
	            "ENDBSM\r\n";

	    public static final String MSG2 = "BSM\r\n" + //
	            ".V/1LLAX\r\n" + //
	            ".F/DLH0505/27JAN/MEX/M\r\n" + //
	            ".N/0220123456001\r\n" + //
	            ".S/Y/40D/C/191\r\n" + //
	            ".P/GONZALES/SPEEDYMR\r\n" + //
	            ".L/ABCDEF\r\n" + //
	            ".T/1234\r\n" + //
	            "ENDBSM\r\n";
	    
	    
	    
		public final static String AIRLINE_PATTERN = "\n\\.F/([A-Z][0-9]|[0-9][A-Z]|[A-Z]{2,3})";   
		
		public final static String AIRPORT_PATTERN = "\n\\.V/1[LTXR]([A-Za-z]{3})";
		
		
		
		
		// public final static String AIRLINE_PATTERN = "\r\n\\.F/[A-Z]{2,3}";    
	    
	
	@Test
	public void testBMBStuff() {
		{
			final TDataArg jh = new TDataArg();
			jh.put("message", MSG2);
			jh.put("airlinePattern", AIRLINE_PATTERN);
			jh.put("airportPattern", AIRPORT_PATTERN);
			
			final String expression = "airline = GETPAT(message,airlinePattern,null,1); airport = GETPAT(message,airportPattern,null,1)";
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertEquals("DLH", jh.get("airline", String.class));
			assertEquals("LAX", jh.get("airport", String.class));
		}
	}
	    
	@Test
	public void testParameterListSize() {
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN()";
//			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
//					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
//			// assertTrue(errors.contains("invalid parameter count=1 (expected=3)"));
//			ExpressionTester.assertParameterCount(errors, 0, 1, 1);
//		}
//
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN(1,2)";
//			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
//					Code.WRONG_PARAMETER_COUNT, Code.WRONG_PARAMETER_COUNT);
//			// assertTrue(errors.contains("invalid parameter count=1 (expected=3)"));
//			ExpressionTester.assertParameterCount(errors, 2, 1, 1);
//		}
	}

	@Test
	public void testParameterType() {
//
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN(1<2)";
//			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
//					Code.TYPE_MISMATCH);
//			// assertTrue(errors.contains("invalid parameter[0]:Boolean (expected:Integer or
//			// String)"));
//			ExpressionTester.assertTypeMismatch(errors, 0, "Boolean", "String or List");
//		}
//
//		{
//			final TDataArg jh = new TDataArg();
//			final String expression = "B = LEN(1)";
//			final ExpressionTester.Errors errors = ExpressionTester.evaluateExpression(jh, expression,
//					Code.TYPE_MISMATCH);
//			// assertTrue(errors.contains("invalid parameter[1]:String (expected:Integer)"));
//			ExpressionTester.assertTypeMismatch(errors, 0, "Integer", "String or List");
//		}
	}


}
