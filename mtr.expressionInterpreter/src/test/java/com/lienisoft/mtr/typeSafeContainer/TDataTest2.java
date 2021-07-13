package com.lienisoft.mtr.typeSafeContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.main.TData;

public class TDataTest2 {

	@Test
	public void testRead() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Structures", true);
		val.put("X.Text", "other");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val.getObj()));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));
		assertEquals("Plast", val.get("Address.Name", String.class));
		assertEquals("Hintergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 127, val.get("Address.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other", val.get("X.Text", String.class));
	}

	@Test
	public void testReadWildcard() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Address.Info.Additional.Texxt", "ABCDE");
		val.put("Structures", true);
		val.put("X.Text", "XHoleridudoedeldi");
		val.put("Texxt", "XABCDE");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val.getObj()));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("%.FirstName", String.class));
		assertEquals("Albert", val.get("%.SecondName", String.class));
		assertEquals("Plast", val.get("%.Name", String.class));
		assertEquals("Hintergasse", val.get("%.Street", String.class));
		assertEquals((Integer) 127, val.get("%.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("%.Additional.Text", String.class));
		assertEquals("Holeridudoedeldi", val.get("%.Text", String.class));
		assertEquals("ABCDE", val.get("%.Additional.Texxt", String.class));
		assertEquals("XABCDE", val.get("%.Texxt", String.class));
		assertEquals("Holeridudoedeldi", val.get("Address.%.Text", String.class));
		assertEquals("ABCDE", val.get("Address.%.Texxt", String.class));
	}

	@Test
	public void testUpdate() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Structures", true);
		val.put("X.Text", "other");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val.getObj()));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));
		assertEquals("Plast", val.get("Address.Name", String.class));
		assertEquals("Hintergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 127, val.get("Address.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other", val.get("X.Text", String.class));

		val.put("Address.FirstName", "Willy");
		val.put("Address.SecondName", "Schorsch");
		val.put("Address.Name", "Simbel");
		val.put("Address.Street", "Vordergasse");
		val.put("Address.Number", 128);
		val.put("Address.Info.Additional.Text", "Bla!");
		val.put("Structures", false);
		val.put("X.Text", "other!?");
		val.put("Null", "not null");

		assertEquals(false, val.get("Structures", Boolean.class));
		assertEquals("not null", val.get("Null", String.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Willy", val.get("Address.FirstName", String.class));
		assertEquals("Schorsch", val.get("Address.SecondName", String.class));
		assertEquals("Simbel", val.get("Address.Name", String.class));
		assertEquals("Vordergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 128, val.get("Address.Number", Integer.class));
		assertEquals("Bla!", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other!?", val.get("X.Text", String.class));
	}

	@Test
	public void testUpdateWildcards() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Address.Info.Additional.Texxt", "ABCDE");
		val.put("Address.Info.Additional.Texxxt", "asdf");
		val.put("Address.Info.Additional.Gaga.Blubb.Test.Text", "Rembrand");
		val.put("Structures", true);
		val.put("X.Text", "other");
		val.put("Texxt", "XABCDE");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));
		assertEquals("Plast", val.get("Address.Name", String.class));
		assertEquals("Hintergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 127, val.get("Address.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("ABCDE", val.get("Address.Info.Additional.Texxt", String.class));
		assertEquals("asdf", val.get("Address.Info.Additional.Texxxt", String.class));
		assertEquals("other", val.get("X.Text", String.class));
		assertEquals("XABCDE", val.get("Texxt", String.class));
		assertEquals("Rembrand", val.get("Address.Info.Additional.Gaga.Blubb.Test.Text", String.class));

		val.put("%.FirstName", "Willy");
		val.put("%.SecondName", "Schorsch");
		val.put("%.Name", "Simbel");
		val.put("%.Street", "Vordergasse");
		val.put("%.Number", 128);
		val.put("%.Text", "Bla!");
		val.put("Structures", false);
		val.put("X.Text", "other!?");
		val.put("Null", "not null");
		val.put("%.Texxt", "xxx");
		val.put("%.Additional.Texxt", "yyy");
		val.put("Address.%.Texxxt", "zzz");
		val.put("%.Texxxt", "zzz");
		val.put("Address.%.Gaga.%.Text", "Aegypten");

		// System.out.println(TJson.toJson(val));

		assertEquals(false, val.get("Structures", Boolean.class));
		assertEquals("not null", val.get("Null", String.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Willy", val.get("Address.FirstName", String.class));
		assertEquals("Schorsch", val.get("Address.SecondName", String.class));
		assertEquals("Simbel", val.get("Address.Name", String.class));
		assertEquals("Vordergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 128, val.get("Address.Number", Integer.class));
		assertEquals("Bla!", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other!?", val.get("X.Text", String.class));
		assertEquals("xxx", val.get("Texxt", String.class));
		assertEquals("yyy", val.get("Address.Info.Additional.Texxt", String.class));
		assertEquals("zzz", val.get("Address.Info.Additional.Texxxt", String.class));
		assertEquals("Aegypten", val.get("Address.Info.Additional.Gaga.Blubb.Test.Text", String.class));
	}

	@Test
	public void testFallbackInsertWildcards() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Address.Info.Additional.Texxt", "ABCDE");
		val.put("Address.Info.Additional.Texxxt", "asdf");
		val.put("Address.Info.Additional.Gaga.Blubb.Test.Text", "Rembrand");
		val.put("Structures", true);
		val.put("X.Text", "other");
		val.put("Texxt", "XABCDE");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));
		assertEquals("Plast", val.get("Address.Name", String.class));
		assertEquals("Hintergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 127, val.get("Address.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("ABCDE", val.get("Address.Info.Additional.Texxt", String.class));
		assertEquals("asdf", val.get("Address.Info.Additional.Texxxt", String.class));
		assertEquals("other", val.get("X.Text", String.class));
		assertEquals("XABCDE", val.get("Texxt", String.class));
		assertEquals("Rembrand", val.get("Address.Info.Additional.Gaga.Blubb.Test.Text", String.class));

		val.put("%.OtherName", "Willy");
		val.put("Address.Info.%.Something", "Some thing");
		val.put("Address.%.Gaga.%.SUB1.%.SUB2.Text", "something else");

		// System.out.println(TJson.toJson(val));
		assertEquals("Willy", val.get("%.OtherName", String.class));
		assertEquals("Some thing", val.get("Address.Info.%.Something", String.class));
		assertEquals("something else", val.get("Address.Info.Additional.Gaga.%.SUB1.%.SUB2.Text", String.class));

	}

	@Test
	public void testRemove() {

		final TData val = new TData(new LinkedHashMap<String, TContainer>());

		val.put("Address.FirstName", "Hans");
		val.put("Address.SecondName", "Albert");
		val.put("Address.Name", "Plast");
		val.put("Address.Street", "Hintergasse");
		val.put("Address.Number", 127);
		val.put("Address.Info.Additional.Text", "Holeridudoedeldi");
		val.put("Structures", true);
		val.put("X.Text", "other");
		val.put("Null", null);

		// System.out.println(TJson.toJson(val.getObj()));

		assertEquals(true, val.get("Structures", Boolean.class));
		assertEquals(null, val.get("Null", Boolean.class));
		assertEquals(null, val.get("NotContained", Boolean.class));
		assertEquals("Hans", val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));
		assertEquals("Plast", val.get("Address.Name", String.class));
		assertEquals("Hintergasse", val.get("Address.Street", String.class));
		assertEquals((Integer) 127, val.get("Address.Number", Integer.class));
		assertEquals("Holeridudoedeldi", val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other", val.get("X.Text", String.class));

		// Remove:
		val.remove("Address.FirstName");
		assertNull(val.get("Address.FirstName", String.class));
		assertEquals("Albert", val.get("Address.SecondName", String.class));

		val.remove("Address.%.Text");
		assertNull(val.get("Address.Info.Additional.Text", String.class));
		assertEquals("other", val.get("X.Text", String.class));

		val.remove("Address");
		assertNull(val.get("Address", Map.class));
		assertNull(val.get("Address.SecondName", String.class));

		// System.out.println(TJson.toJson(val.getObj()));
		val.remove("Null");
		assertNull(val.get("Null", Object.class));
		// System.out.println(TJson.toJson(val.getObj()));
	}

	@Test
	public void testInteger() {

		final TContainer c = new TData(123);

		// test get
		assertEquals((Integer) 123, c.get(Integer.class));

		try {
			c.get(String.class);
			fail();
		} catch (final Throwable t) {
			assertEquals(ClassCastException.class, t.getClass());
		}

		// test tryGet
		assertEquals((Integer) 123, c.tryGet(Integer.class));
		assertNull(c.tryGet(String.class));
		assertNull(c.tryGet(Boolean.class));
	}

	@Test
	public void testBoolean() {

		final TContainer c = new TData(false);

		// Test get
		assertEquals(false, c.get(Boolean.class));

		try {
			c.get(Integer.class);
			fail();
		} catch (final Throwable t) {
			assertEquals(ClassCastException.class, t.getClass());
		}

		// test tryGet
		assertEquals(false, c.tryGet(Boolean.class));
		assertNull(c.tryGet(String.class));
		assertNull(c.tryGet(Integer.class));
	}

	@Test
	public void testNull() {
		final TContainer c = new TData(null);

		// Test get
		assertNull(c.get(Integer.class));
		assertNull(c.get(Boolean.class));
		assertNull(c.get(String.class));

		// Test tryGet
		assertNull(c.tryGet(Integer.class));
		assertNull(c.tryGet(Boolean.class));
		assertNull(c.tryGet(String.class));
	}

}
