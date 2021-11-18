package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester.Errors;
import com.lienisoft.mtr.utils.JsonSerializer;

public class FunctionContainsTest {
	
	@Test
	public void test() {

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = CONTAINS(\"ABCD\",\"CD\")";
			Errors errs = ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertTrue(jh.get("B", Boolean.class));
			System.out.println(errs);
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = CONTAINS(C, A)";
			
			jh.put("A", "LH");
			jh.put("C", "LX,DE,OS,LH,YY");
			
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertTrue(jh.get("B", Boolean.class));
		}

		{
			final TDataArg jh = new TDataArg();
			final String expression = "B = CONTAINS(C, A)";
			
			jh.put("A", "EW");
			jh.put("C", "LX,DE,OS,LH,YY");
			
			ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(jh);
			assertFalse(jh.get("B", Boolean.class));
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
	
	
	public final static String MEGA_FILTER_EXPRESSION = "flightRouting \r\n" + 
			"&& (airline == \"LH\" \r\n" + 
			"  || (airport == \"AMS\" && airline == \"OU\")\r\n" + 
			"  || (airport == \"BCN\" && airline == \"OU\")\r\n" + 
			"  || (airport == \"BEG\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"BKK\" && CONTAINS(\"DE,MS,EW\", airline))\r\n" + 
			"  || (airport == \"BRU\" && airline == \"OU\")\r\n" + 
			"  || (airport == \"BUD\" && CONTAINS(\"SK,LO,TP\", airline))\r\n" + 
			"  || (airport == \"CAI\" && airline == \"LX\")\r\n" + 
			"  || (airport == \"CDG\" && CONTAINS(\"LO,OU,JP\", airline))\r\n" + 
			"  || (airport == \"CGN\" && CONTAINS(\"TU,LX,JK,AP\", airline))\r\n" + 
			"  || (airport == \"DRS\" && CONTAINS(\"DE,VL,OU,TU,6K,FY,JK,XQ,EW,PG,4R,8Q,UX,C9,UI,KY,2W,3L,BJ,CB,O2,YM\", airline)) \r\n" + 
			"  || (airport == \"DUS\" && CONTAINS(\"EW,MS,SK,LO,OU,TU,LG,LX,JK,C9\", airline)) \r\n" + 
			"  || (airport == \"DXB\" && CONTAINS(\"DE,SK\", airline))\r\n" + 
			"  || (airport == \"FCO\" && airline == \"OU\")\r\n" + 
			"  || (airport == \"FMO\" && CONTAINS(\"EW,JK,YY\", airline))\r\n" + 
			"  || (airport == \"FRA\" && CONTAINS(\"9U,AH,AT,BM,CL,CY,EN,EW,IQ,JP,LG,LO,LX,ME,MS,MU,OU,RO,SK,TP,V3,VN,YY,ET\", airline))\r\n" + 
			"  || (airport == \"GLA\" && CONTAINS(\"EI,WW\", airline)) \r\n" + 
			"  || (airport == \"GRU\" && airline == \"LX\")\r\n" + 
			"  || (airport == \"HAJ\" && CONTAINS(\"SK,CI,OU,TU,JK,LX,LG\", airline))\r\n" + 
			"  || (airport == \"HAM\" && CONTAINS(\"EW,SK,LO,TU,JK,LG,KF,LX,KI,AT,JZ,OU,TP,C9\", airline))\r\n" + 
			"  || (airport == \"INN\" && CONTAINS(\"VO,A6,NG,SK\", airline))\r\n" + 
			"  || (airport == \"JFK\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"LCA\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"LEJ\" && CONTAINS(\"DE,LX,YY\", airline))\r\n" + 
			"  || (airport == \"LHR\" && CONTAINS(\"LO,OU,TP,UN\", airline))\r\n" + 
			"  || (airport == \"MAD\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"MEX\" && airline == \"MX\")\r\n" + 
			"  || (airport == \"MUC\" && CONTAINS(\"A3,C3,C9,CA,CL,EN,EW,IQ,JK,JP,KM,LG,LO,LX,MS,OU,QI,QR,SA,SK,SN,TG,TK,TP,TU,UA,US,VO,XQ\", airline))\r\n" + 
			"  || (airport == \"MXP\" && CONTAINS(\"LO,EN\", airline))\r\n" + 
			"  || (airport == \"NRT\" && airline == \"LX\")\r\n" + 
			"  || (airport == \"NUE\" && CONTAINS(\"EW,SK,TU,XQ,4R,LX,OU\", airline))\r\n" + 
			"  || (airport == \"ORD\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"OTP\" && CONTAINS(\"LO,SK\", airline))\r\n" + 
			"  || (airport == \"PRG\" && CONTAINS(\"LO,CF,SK,OK\", airline))\r\n" + 
			"  || (airport == \"STR\" && CONTAINS(\"MA,SK,LX,C9,4R,H9,JK,LO,OU,TU\", airline))\r\n" + 
			"  || (airport == \"TLV\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"TXL\" && CONTAINS(\"LX,IA\", airline))\r\n" + 
			"  || (airport == \"VIE\" && CONTAINS(\"3B,4R,9U,A9,AF,BJ,DE,EK,EN,FB,FI,FV,HV,IG,IN,IR,JP,JU,KM,LG,LO,LX,LY,MS,OA,OK,OU,PS,QR,RJ,RO,S4,SV,TU,UX,VO,YY,ZB\", airline))\r\n" + 
			"  || (airport == \"WAW\" && CONTAINS(\"LO,VV,SR,8A,B2,BJ,JP,S1,SU\", airline))\r\n" + 
			"  || (airport == \"YVR\" && airline == \"DE\")\r\n" + 
			"  || (airport == \"YVZ\" && airline == \"LO\")\r\n" + 
			"  || (airport == \"ZAG\" && CONTAINS(\"AL,SK\", airline))\r\n" + 
			")";
	
	
	@Test
	public void testBMBStuff() {

		{
			final TDataArg jh = new TDataArg();
			
			jh.put("flightRouting", true);
			jh.put("airline", "ZB");
			jh.put("airport", "VIE");
			
			
			final String expression = "result = " + MEGA_FILTER_EXPRESSION;
			
			System.out.println(expression);
			System.out.println();
			Errors errs = ExpressionTester.evaluateExpression(jh, expression);
			System.out.println(JsonSerializer.toJson(jh.get()));
			assertTrue(jh.get("result", Boolean.class));
			System.out.println(errs);
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
