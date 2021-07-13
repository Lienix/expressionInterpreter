package com.lienisoft.mtr.expressionInterpreter.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfo;
import com.lienisoft.mtr.expressionInterpreter.basic.Operate;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;

public class OperatorTest {

	@Test
	public void operatePlusAssignTest() {
		try {

			final TArgument data = new TDataArg().createByKey("result");

			Operate.PLUS_ASSIGN.exe(data, new TDataArg(3));
			assertEquals((Integer) 3, data.get("result", Integer.class));

			Operate.PLUS_ASSIGN.exe(data, new TDataArg(3));
			assertEquals((Integer) 6, data.get("result", Integer.class));

			Operate.PLUS_ASSIGN.exe(data, new TDataArg(3));
			assertEquals((Integer) 9, data.get("result", Integer.class));

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void operatePlusTest() {

		try {

			final TArgument v12 = new TDataArg(12);
			final TArgument v7 = new TDataArg(7);
			final TArgument vABCD = new TDataArg("ABCD");
			final TArgument vtrue = new TDataArg(true);
			final TArgument vfalse = new TDataArg(false);
			final TArgument vDate = new TDataArg(newDate("10.02.2020 17:30:00"));
			// final TArgument v1Date = new TDataArgArg(newDate("11.03.2020 17:30:00"));

			{ // Integer plus Integer
				final TArgument r = Operate.PLUS.exe(v12, v7);
				assertEquals(Integer.valueOf(19), r.get(Integer.class));
			}
			{ // Date plus Integer
				final TArgument r = Operate.PLUS.exe(vDate, v12);
				assertEquals(newDate("10.02.2020 17:42:00"), r.get(Date.class));
			}
			{ // Integer plus Date
				final TArgument r = Operate.PLUS.exe(v7, vDate);
				assertEquals(newDate("10.02.2020 17:37:00"), r.get(Date.class));
			}
			{ // String plus Integer
				final TArgument r = Operate.PLUS.exe(vABCD, v7);
				assertEquals("ABCD7", r.get(String.class));
			}
			{ // Integer plus String
				final TArgument r = Operate.PLUS.exe(v12, vABCD);
				assertEquals("12ABCD", r.get(String.class));
			}
			{ // String plus Date
				 // final TArgument r = Operate.PLUS.exe(vABCD, v1Date);

				// TODO: a default toString for DateTime!

				// assertEquals("ABCDWed Mar 11 17:30:00 GMT 2020", r.get(String.class));
			}
			{ // Date plus String
				 // final TArgument r = Operate.PLUS.exe(vDate, vABCD);
				 // TODO: a default toString for DateTime!
				 // assertEquals("Mon Feb 10 17:30:00 GMT 2020ABCD", r.get(String.class));
			}
			{ // Integer plus Boolean
				try {
					Operate.PLUS.exe(v12, vtrue);
					fail();
				} catch (final ExprException i) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, i.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}
			{ // Boolean plus Boolean
				try {
					Operate.PLUS.exe(vfalse, vtrue);
					fail();
				} catch (final ExprException i) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, i.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void operateMinusTest() {

		try {

			final TArgument v12 = new TDataArg(12);
			final TArgument v7 = new TDataArg(7);
			// final Val vABCD = new Val("ABCD");
			final TArgument vtrue = new TDataArg(true);
			final TArgument vfalse = new TDataArg(false);
			final TArgument vDate = new TDataArg(newDate("10.02.2020 17:30:00"));
			final TArgument vDate2 = new TDataArg(newDate("10.02.2020 18:35:00"));

			{ // Integer minus Integer
				final TArgument r = Operate.MINUS.exe(v12, v7);
				assertEquals(Integer.valueOf(5), r.get(Integer.class));
			}
			{ // Date minus Integer
				final TArgument r = Operate.MINUS.exe(vDate, v12);
				assertEquals(newDate("10.02.2020 17:18:00"), r.get(Date.class));
			}
			{ // Date minus Date
				final TArgument r = Operate.MINUS.exe(vDate2, vDate);
				assertEquals(Integer.valueOf(65), r.get(Integer.class));
			}
			{ // Integer minus Boolean
				try {
					Operate.MINUS.exe(v12, vtrue);
					fail();
				} catch (final ExprException i) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, i.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}
			{ // Boolean minus Boolean
				try {
					Operate.MINUS.exe(vfalse, vtrue);
					fail();
				} catch (final ExprException i) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, i.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void operateAndTest() {
		try {
			final TArgument v7 = new TDataArg(7);
			final TArgument vABCD = new TDataArg("ABCD");
			final TArgument vtrue = new TDataArg(true);
			final TArgument vfalse = new TDataArg(false);
			final TArgument vnull = new TDataArg(null);

			{ // Boolean and Boolean
				final TArgument r = Operate.AND.exe(vtrue, vfalse);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // Boolean and Boolean
				final TArgument r = Operate.AND.exe(vtrue, vtrue);
				assertEquals(true, r.get(Boolean.class));
			}
			{ // Boolean and Boolean
				final TArgument r = Operate.AND.exe(vfalse, vfalse);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // Boolean and null
				final TArgument r = Operate.AND.exe(vtrue, vnull);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // null and boolean
				final TArgument r = Operate.AND.exe(vnull, vfalse);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // null and null
				final TArgument r = Operate.AND.exe(vnull, vnull);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // Boolean and String
				try {
					Operate.AND.exe(vfalse, vABCD);
					fail();
				} catch (final ExprException c) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, c.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}
			{ // Integer and Boolean
				try {
					Operate.AND.exe(v7, vABCD);
					fail();
				} catch (final ExprException c) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, c.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}
		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void operateOrTest() {

		try {

			final TArgument v7 = new TDataArg(7);
			final TArgument vABCD = new TDataArg("ABCD");
			final TArgument vtrue = new TDataArg(true);
			final TArgument vfalse = new TDataArg(false);
			final TArgument vnull = new TDataArg(null);

			{ // Boolean or Boolean
				final TArgument r = Operate.OR.exe(vtrue, vfalse);
				assertEquals(true, r.get(Boolean.class));
			}
			{ // Boolean or Boolean
				final TArgument r = Operate.OR.exe(vfalse, vtrue);
				assertEquals(true, r.get(Boolean.class));
			}
			{ // Boolean or Boolean
				final TArgument r = Operate.OR.exe(vtrue, vtrue);
				assertEquals(true, r.get(Boolean.class));
			}
			{ // Boolean and Boolean
				final TArgument r = Operate.OR.exe(vfalse, vfalse);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // Boolean and null
				final TArgument r = Operate.OR.exe(vtrue, vnull);
				assertEquals(true, r.get(Boolean.class));
			}
			{ // null and boolean
				final TArgument r = Operate.OR.exe(vnull, vfalse);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // null and null
				final TArgument r = Operate.OR.exe(vnull, vnull);
				assertEquals(false, r.get(Boolean.class));
			}
			{ // Boolean and String
				try {
					Operate.OR.exe(vfalse, vABCD);
					fail();
				} catch (final ExprException c) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, c.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}
			{ // Integer and Boolean
				try {
					Operate.OR.exe(v7, vABCD);
					fail();
				} catch (final ExprException c) {
					/** OK ***/
					assertEquals(ExprInfo.Code.OPERATION_NOT_DEFINED, c.getExpressionInfo().getCode());
				} catch (final Throwable t) {
					fail();
				}
			}

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void operateEqualsTest() {

		try {

			final TArgument a12 = new TDataArg(12);
			final TArgument b12 = new TDataArg(12);
			final TArgument c32 = new TDataArg(32);

			final TArgument aABCD = new TDataArg("ABCD");
			final TArgument bABCD = new TDataArg("ABCD");
			final TArgument cGAGA = new TDataArg("gaga");

			final TArgument afalse = new TDataArg(false);
			final TArgument bfalse = new TDataArg(false);
			final TArgument atrue = new TDataArg(true);
			final TArgument btrue = new TDataArg(true);

			final TArgument aNul = new TDataArg(null);
			final TArgument bNul = new TDataArg(null);

			{ // 12 equals 12
				final TArgument r = Operate.EQUAL.exe(a12, b12);
				assertTrue(r.get(Boolean.class));
			}
			{ // 12 equals 32
				final TArgument r = Operate.EQUAL.exe(a12, c32);
				assertFalse(r.get(Boolean.class));
			}
			{ // ABCD equals 12
				final TArgument r = Operate.EQUAL.exe(aABCD, b12);
				System.out.println(r);
				assertFalse(r.get(Boolean.class));
			}
			{ // 12 equals null
				final TArgument r = Operate.EQUAL.exe(a12, aNul);
				assertFalse(r.get(Boolean.class));
			}
			{ // null equals null
				final TArgument r = Operate.EQUAL.exe(aNul, bNul);
				assertTrue(r.get(Boolean.class));
			}

			{ // ABCD equals ABCD
				final TArgument r = Operate.EQUAL.exe(aABCD, bABCD);
				assertTrue(r.get(Boolean.class));
			}
			{ // ABCD equals gaga
				final TArgument r = Operate.EQUAL.exe(aABCD, cGAGA);
				assertFalse(r.get(Boolean.class));
			}
			{ // ABCD equals false
				final TArgument r = Operate.EQUAL.exe(aABCD, afalse);
				assertFalse(r.get(Boolean.class));
			}
			{ // false equals false
				final TArgument r = Operate.EQUAL.exe(afalse, bfalse);
				assertTrue(r.get(Boolean.class));
			}
			{ // true equals true
				final TArgument r = Operate.EQUAL.exe(atrue, btrue);
				assertTrue(r.get(Boolean.class));
			}
			{ // false equals true
				final TArgument r = Operate.EQUAL.exe(afalse, atrue);
				assertFalse(r.get(Boolean.class));
			}

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void operateNotEqualsTest() {

		try {

			final TArgument a12 = new TDataArg(12);
			final TArgument b12 = new TDataArg(12);
			final TArgument c32 = new TDataArg(32);

			final TArgument aABCD = new TDataArg("ABCD");
			final TArgument bABCD = new TDataArg("ABCD");
			final TArgument cGAGA = new TDataArg("gaga");

			final TArgument afalse = new TDataArg(false);
			final TArgument bfalse = new TDataArg(false);
			final TArgument atrue = new TDataArg(true);
			final TArgument btrue = new TDataArg(true);

			final TArgument aNul = new TDataArg(null);
			final TArgument bNul = new TDataArg(null);

			{ // 12 not equals 12
				final TArgument r = Operate.NOT_EQUAL.exe(a12, b12);
				assertFalse(r.get(Boolean.class));
			}
			{ // 12 not equals 32
				final TArgument r = Operate.NOT_EQUAL.exe(a12, c32);
				assertTrue(r.get(Boolean.class));
			}
			{ // ABCD not equals 12
				final TArgument r = Operate.NOT_EQUAL.exe(aABCD, b12);
				System.out.println(r);
				assertTrue(r.get(Boolean.class));
			}
			{ // 12 not equals null
				final TArgument r = Operate.NOT_EQUAL.exe(a12, aNul);
				assertTrue(r.get(Boolean.class));
			}
			{ // null not equals null
				final TArgument r = Operate.NOT_EQUAL.exe(aNul, bNul);
				assertFalse(r.get(Boolean.class));
			}

			{ // ABCD not equals ABCD
				final TArgument r = Operate.NOT_EQUAL.exe(aABCD, bABCD);
				assertFalse(r.get(Boolean.class));
			}
			{ // ABCD not equals gaga
				final TArgument r = Operate.NOT_EQUAL.exe(aABCD, cGAGA);
				assertTrue(r.get(Boolean.class));
			}
			{ // ABCD not equals false
				final TArgument r = Operate.NOT_EQUAL.exe(aABCD, afalse);
				assertTrue(r.get(Boolean.class));
			}
			{ // false not equals false
				final TArgument r = Operate.NOT_EQUAL.exe(afalse, bfalse);
				assertFalse(r.get(Boolean.class));
			}
			{ // true not equals true
				final TArgument r = Operate.NOT_EQUAL.exe(atrue, btrue);
				assertFalse(r.get(Boolean.class));
			}
			{ // false not equals true
				final TArgument r = Operate.NOT_EQUAL.exe(afalse, atrue);
				assertTrue(r.get(Boolean.class));
			}

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void operateAssignmentTest() {

		try {

			// Data Source:
			final TDataArg data = new TDataArg();

			data.put("Name", "Hans");
			data.put("Second Name", "Plast");
			data.put("Age", 42);
			data.put("Smoker", true);

			data.put("Address.Street", "Feldweg");
			data.put("Address.Number", 17);
			data.put("Address.City", "Staenkelfeld");
			data.put("Address.Zip", 12345);

			// Result = 3 + Age :
			final TArgument c3 = new TDataArg(3);
			final TArgument vAge = data.createByKey("Age");

			final TArgument vResult = data.createByKey("Result");

			final TArgument arg1 = Operate.PLUS.exe(c3, vAge);
			System.out.println(arg1);

			final TArgument arg2 = Operate.ASSIGN.exe(vResult, arg1);

			System.out.println(arg2);
			System.out.println(arg2.getClass().getSimpleName());

			// New Substruct: Substruct.Sub = Adress
			final TArgument vSub = data.createByKey("Substruct.Sub");
			final TArgument vAdd = data.createByKey("Address");

			final TArgument arg3 = Operate.ASSIGN.exe(vSub, vAdd);

			System.out.println(arg3);
			System.out.println(arg3.getClass().getSimpleName());

			// Change Existing Address:
			// Address.Street = "Babbelgass";
			// Address.Number = 735;
			// Address.Country = "Lummerland";

			Operate.ASSIGN.exe(data.createByKey("Address.Street"), new TDataArg("Babbelgass"));
			Operate.ASSIGN.exe(data.createByKey("Address.Number"), new TDataArg(735));
			Operate.ASSIGN.exe(data.createByKey("Address.Country"), new TDataArg("Lummerland"));
			System.out.println(data);

		} catch (final ExprException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testLbsCalc() {

		// final Key<String> key1 = new Key<>("str", String.class);
		// final Key<String> key3 = new Key<>("nul", String.class);
		// final Key<Integer> key2 = new Key<>("num", Integer.class);
		// final Key<Val> key4 = new Key<>("key4", Val.class);

		final double LBS_FACTOR = 2.20462262d;

		// 453.59237

		final int lbsi = Math.toIntExact(Math.round(1000 / LBS_FACTOR * 10));
		final double lbsd = (1000 / LBS_FACTOR);

		System.out.println(lbsd);
		System.out.println(lbsi);
		System.out.println(lbsd * LBS_FACTOR);
		System.out.println(Math.round(lbsi * LBS_FACTOR / 10));

		for (int i = 0; i < 1000; i++) {

			final int lbs = Math.toIntExact(Math.round(i / LBS_FACTOR * 10));
			System.out.println(lbs + " : " + Math.round(lbs * LBS_FACTOR / 10));

			assertEquals(i, Math.round(lbs * LBS_FACTOR / 10));
		}

	}

	// Helper - test:
	static Date newDate(String set) {
		Date dt = new Date();
		// Festlegung des Formats:
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			// Einlesen vom String:
			dt = df.parse(set);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}
}
