package com.lienisoft.mtr.expressionInterpreter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.expressionInterpreter.process.ExpressionTester;

public class FunctionLoopTest {

	@Test
	public void test() {
	}

	@Test
	public void TestLoopOverListToCreateListOfStructures() {
		// read from list and write new List

		final TDataArg jh = new TDataArg();
		final String expression = "A = (2,4,6, 8, 10); LOOP(A, GET(\"List.\" + INDEX +\".name\") = \"TEST_\" + INDEX, BREAK = VALUE > 7, GET(\"List.\" + INDEX +\".val\") = VALUE)";
		ExpressionTester.evaluateExpression(jh, expression);
		System.out.println("--------------\n" + jh);

		assertEquals("TEST_0", jh.get("List.0.name", String.class));
		assertEquals((Integer) 2, jh.get("List.0.val", Integer.class));

		assertEquals("TEST_1", jh.get("List.1.name", String.class));
		assertEquals((Integer) 4, jh.get("List.1.val", Integer.class));

		assertEquals("TEST_2", jh.get("List.2.name", String.class));
		assertEquals((Integer) 6, jh.get("List.2.val", Integer.class));
	}

	@Test
	public void TestLoopOverStructToConcatenateString() {
		final String expression = "M.TEILine.AN = \"D-MARC\";" + "M.TEILine.FI = \"LH12345\";"
				+ "M.TEILine.MA = \"159B\";" +

				"LOOP(M.TEILine, M.Assembled = M.Assembled + KEY + \" \" + VALUE + \"/\")";

		System.out.println(expression);

		final TDataArg jh = new TDataArg();
		// String expression = "A = (2,4,6); LOOP(A, GET(\"List.\" + INDEX +\".name\") = \"TEST\",
		// GET(\"List.\" + INDEX +\".val\") = VALUE)";
		ExpressionTester.evaluateExpression(jh, expression);
		System.out.println("--------------\n" + jh);

		System.out.println(jh.get("M.Assembled", String.class));
		assertEquals("AN D-MARC/FI LH12345/MA 159B/", jh.get("M.Assembled", String.class));
	}

	@Test
	public void TestLoopOverStructToConcatenateTEIComplex() {
		final String expression = "M.TEILine.AN = \"D-MARC\";\n" + "M.TEILine.FI = \"LH12345\";\n"
				+ "M.TEILine.MA = \"159S\";\n" + "M.TEILine.X1 = \"0123456789\";\n" + "M.TEILine.X2 = \"0123456789\";\n"
				+ "M.TEILine.X3 = \"0123456789\";\n" + "M.TEILine.X4 = \"0123456789\";\n"
				+ "M.TEILine.X5 = \"0123456789\";\n" + "M.TEILine.X6 = \"0123456789\";\n"
				+ "M.TEILine.X7 = \"0123456789\";\n" + "M.TEILine.X8 = \"0123456789\";\n"
				+ "M.TEILine.X9 = \"0123456789\";\n" + "M.TEILine.X0 = \"0123456789\";\n" +
				// "M.Assembled = \"\";\n" +
				// "LOOP(M.TEILine, M.Assembled = M.Assembled + KEY + \" \" + VALUE + NewLine)";

				"LOOP(M.TEILine,\n" + "  IF(T.Currline == null,\n" + "    T.Currline = KEY + \" \" + VALUE,\n"
				+ "    IF(LEN(T.Currline + \"/\" + KEY + \" \" + VALUE) < 69,\n"
				+ "      T.Currline = T.Currline + \"/\" + KEY + \" \" + VALUE,\n" + "      (\n"
				+ "        M.Assembled = M.Assembled + T.Currline,\n"
				+ "        T.Currline = + \"\\n\" + KEY + \" \" + VALUE\n" + "       )\n" + "    )\n" + "  )\n" + ");\n"
				+ "M.Assembled = M.Assembled + T.Currline;\n" + "REMOVE(T);\n" + "\n";
		// "\n";

		System.out.println(expression);
		System.out.println("---------------------------------------------------------------");

		final TDataArg jh = new TDataArg();
		// String expression = "A = (2,4,6); LOOP(A, GET(\"List.\" + INDEX +\".name\") = \"TEST\",
		// GET(\"List.\" + INDEX +\".val\") = VALUE)";
		ExpressionTester.evaluateExpression(jh, expression);
		System.out.println("--------------\n" + jh);
		System.out.println(jh.get("M.Assembled", String.class));

		final String expected = "AN D-MARC/FI LH12345/MA 159S/X1 0123456789/X2 0123456789\n"
				+ "X3 0123456789/X4 0123456789/X5 0123456789/X6 0123456789\n"
				+ "X7 0123456789/X8 0123456789/X9 0123456789/X0 0123456789";

		assertEquals(expected, jh.get("M.Assembled", String.class));
	}

	@Test
	public void TestLoopOverList_PenalizingSectorBlackList() {

		final String penalizingSectorBlackList = "A.Settings.PenalizingSectorBlacklist = (\n" + "\"A\",\n" + "\"C\",\n"
				+ "\"DF\",\n" + "\"EBSP\",\n" + "\"EDLE\",\n" + "\"EFHR\",\n" + "\"EHEY\",\n" + "\"EHU0\",\n"
				+ "\"EIXI\",\n" + "\"EKR\",\n" + "\"EPON\",\n" + "\"EPTV\",\n" + "\"ESE0\",\n" + "\"EST0\",\n"
				+ "\"F\",\n" + "\"GL\",\n" + "\"K\",\n" + "\"LAMG\",\n" + "\"LATE\",\n" + "\"LATW\",\n" + "\"LBE0\",\n"
				+ "\"LCAS\",\n" + "\"LE00\",\n" + "\"LEPE\",\n" + "\"LET9\",\n" + "\"M\",\n" + "\"N\",\n" + "\"P\",\n"
				+ "\"R\",\n" + "\"S\",\n" + "\"T\",\n" + "\"TT\",\n" + "\"V\",\n" + "\"W\",\n" + "\"Y\",\n"
				+ "\"Z\");\n";

		/**
		 * positive test: penalizing sector contained in blacklist:
		 */
		{
			String expression = penalizingSectorBlackList;

			expression += "M.PenalizingSector = \"DF123A \";\n";

			// A.Settings.PenalizingSectorBlacklist;IF(STARTSWITH(M.PenalizingSector,/X/),M.PenalizingSectorFiltered
			// = "Y",M.PenalizingSectorFiltered)

			expression += "LOOP(A.Settings.PenalizingSectorBlacklist, M.Iterations = INDEX, IF(STARTSWITH(M.PenalizingSector, VALUE),(M.PenalizingSectorFiltered = \"Y\", BREAK = true)))";

			System.out.println(expression);

			final TDataArg jh = new TDataArg();
			ExpressionTester.evaluateExpression(jh, expression);
			assertEquals("Y", jh.get("M.PenalizingSectorFiltered", String.class));
			System.out.println("--------------\n" + jh);
		}

		/**
		 * negative test: penalizing sector NOT contained in blacklist:
		 */
		{
			String expression = penalizingSectorBlackList;

			expression += "M.PenalizingSector = \"DE123A \";\n";

			// A.Settings.PenalizingSectorBlacklist;IF(STARTSWITH(M.PenalizingSector,/X/),M.PenalizingSectorFiltered
			// = "Y",M.PenalizingSectorFiltered)

			expression += "LOOP(A.Settings.PenalizingSectorBlacklist, M.Iterations = INDEX, IF(STARTSWITH(M.PenalizingSector, VALUE),(M.PenalizingSectorFiltered = \"Y\", BREAK = true)))";

			System.out.println(expression);

			final TDataArg jh = new TDataArg();
			ExpressionTester.evaluateExpression(jh, expression);
			assertNull(jh.get("M.PenalizingSectorFiltered"));
			System.out.println("--------------\n" + jh);
		}
	}

	@Test
	public void TestLoopOverList_DLK_AVAILABILITY_XML() {

		final String DatalinkAvaialability = "M.DatalinkAvailability.Registration = (" + "\"DAIKA\"," + "\"DAIUI\","
				+ "\"DACKF\");\n" + "M.DatalinkAvailability.DLKActive = (" + "true," + "false," + "true);\n";

		/**
		 * positive test: penalizing sector contained in blacklist:
		 */
		{
			String expression = DatalinkAvaialability;

			// A.Settings.PenalizingSectorBlacklist;IF(STARTSWITH(M.PenalizingSector,/X/),M.PenalizingSectorFiltered
			// = "Y",M.PenalizingSectorFiltered)

			// expression +=
			// "LOOP(M.DatalinkAvailability.Registration, "
			// + "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".XMLATTRsrcid\") = \"GADCOM\", "
			// + "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".XMLATTRsrcsp\") = \"2019-12-03\")"
			// + "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".keydlh.rbl\") = VALUE);\n";
			expression += "LOOP(M.DatalinkAvailability.Registration, "
					+ "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".XMLATTRsrcid\") = \"GADCOM\", "
					+ "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".XMLATTRsrcsp\") = \"2019-12-03\","
					+ "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".keydlh.rbl\") = VALUE);\n";

			expression += "LOOP(M.DatalinkAvailability.DLKActive, "
					+ "GET(\"M.XMLTODAS.packet.reg.\" + INDEX + \".data.gdcind\") = IF(VALUE == true,\"Y\",\"N\"));\n";

			System.out.println(expression);

			final TDataArg jh = new TDataArg();
			ExpressionTester.evaluateExpression(jh, expression);
			// assertEquals("Y", jh.getString("M.PenalizingSectorFiltered"));
			System.out.println("--------------\n" + jh);
		}

	}

}
