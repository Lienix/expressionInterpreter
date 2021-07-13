package com.lienisoft.mtr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JsonSerializerTest {

	public static class TestData {
		public String aString;

		// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
		// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SZ")
		public Date aDate;
		public int anInt;

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("TestData [\n  aString=");
			builder.append(aString);
			builder.append("\n  aDate=");
			builder.append(aDate);
			builder.append("\n  anInt=");
			builder.append(anInt);
			builder.append("\n]");
			return builder.toString();
		}

	}

	@Test
	public void test() {

		final TestData ts = new TestData();

		// "yyyy-MM-dd'T'HH:mm:ss.SSSX"

		ts.aString = "test";
		try {
			ts.aDate = JsonSerializer.getDateWithMillis().parse("2019-06-23T09:57:23.123Z");
		} catch (final ParseException e) {
			e.printStackTrace();
			fail();
		}
		ts.anInt = 127;

		final String input = JsonSerializer.toJson(ts);

		System.out.println(input);

		// String input = ReadResourceFile.read("JsonWithTimestamp.json");

		final TestData map = JsonSerializer.toClass(input, TestData.class);

		System.out.println(map);

		System.out.println("-------------------------------------------------------");

		System.out.println(JsonSerializer.toJson(map));
	}

	@Test
	public void testTolerantDateSerialization() {

		String input = null;
		TestData testData = null;

		Date expectedDateMillis = null;
		Date expectedDate = null;
		try {
			expectedDateMillis = JsonSerializer.getDateWithMillis().parse("2019-06-23T09:57:23.123Z");
			expectedDate = JsonSerializer.getDateWithMillis().parse("2019-06-23T09:57:23.000Z");
		} catch (final ParseException e) {
			e.printStackTrace();
			fail();
		}

		// DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SZ":
		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23.123+0000\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDateMillis, testData.aDate);

		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23.0+0000\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDate, testData.aDate);

		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23+0000\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDate, testData.aDate);

		// DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSX":
		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23.123Z\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDateMillis, testData.aDate);

		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23.000Z\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDate, testData.aDate);

		// DateFormat = "yyyy-MM-dd'T'HH:mm:ssX": >>> NO MILLISECONDS! <<<
		input = "{\"aString\" : \"test\",\"aDate\" : \"2019-06-23T09:57:23Z\",\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDate, testData.aDate);

		// DateFormat = LONG (default):
		input = "{\"aString\" : \"test\",\"aDate\" : 1561283843123,\"anInt\" : 127}";
		testData = JsonSerializer.toClass(input, TestData.class);
		assertEquals(expectedDateMillis, testData.aDate);

	}

	// @Test
	// public void testTestX() {
	//
	// TestX td = new TestX();
	//
	// String json = JsonSerializer.toJson(td);
	//
	// System.out.println(">>>>\n" + json);
	//
	// td = JsonSerializer.toClass(json, TestX.class);
	// System.out.println(">>>>\n" + td);
	//
	// json = "{}";
	//
	// td = JsonSerializer.toClass(json, TestX.class);
	// System.out.println(">>>>\n" + td);
	//
	// }

	// For this test configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) must be enabled in
	// JsonSerializer!

	// @Test
	// public void testStringSerialization() {
	// String test = "abc\"\ngaga";
	//
	// String json = JsonSerializer.toJson(test);
	//
	// System.out.println(json);
	//
	// System.out.println(JsonSerializer.toClass(json, String.class));
	//
	// json = "\">test\\nblubb<\"";
	//
	// System.out.println(JsonSerializer.toClass(json, String.class));
	//
	// json = "\">test\nblubb<\"";
	// System.out.println(JsonSerializer.toClass(json, String.class));
	//
	// json = ReadResourceFile.read("JsonStringEscaped.json");
	//
	//
	// System.out.println(JsonSerializer.toClass(json, LinkedHashMap.class));
	//
	//
	//
	// json = ReadResourceFile.read("JsonStringNotEscaped.txt");
	//
	//
	// LinkedHashMap<String, Object> map = JsonSerializer.toClass(json, LinkedHashMap.class);
	//
	// System.out.println(map);
	//
	//
	// String str = (String) map.get("payload");
	//
	// System.out.println(str);
	//
	// // cut header
	// String payload = str.substring(6);
	// System.out.println("---------------------------------------------------------------");
	// System.out.println(JsonSerializer.toClass(payload, LinkedHashMap.class));
	//
	// }

}
