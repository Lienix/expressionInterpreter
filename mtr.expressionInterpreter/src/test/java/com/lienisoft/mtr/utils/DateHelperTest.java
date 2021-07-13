package com.lienisoft.mtr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.lienisoft.mtr.utils.DateHelperMTR.DateRangeType;

public class DateHelperTest {

	@Test
	public void testSimple() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("ddHHmm");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			// final Date referenceDate = formatter.parse("23.02.2012 23:11:10");

			final Date date = formatter.parse("251759");

			System.out.println(date);

			// Date probe = DateHelperMTR.completeDateTime("1302", false, referenceDate,
			// DateRangeType.PlusMinus12H);

			// String probeAsString = formatter.format(probe);
			// assertEquals("23.02.2012 13:02:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormat() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 23:11:10");

			final Date probe = DateHelperMTR.completeDateTime("1302", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("23.02.2012 13:02:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormatHMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 23:11:10");

			final Date probe = DateHelperMTR.completeDateTime("302", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("24.02.2012 03:02:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDFormat() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 23:11:10");

			final Date probe = DateHelperMTR.completeDateTime("12", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("12.02.2012 23:11:10", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDFormat() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 23:11:10");

			final Date probe = DateHelperMTR.completeDateTime("2", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("02.03.2012 23:11:10", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testEachAutoRecognitionFormat() throws Exception {
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Date referenceDate = formatter.parse("23.01.2011 23:11:10");
		Date probe;

		// Date/Time combined:
		probe = DateHelperMTR.completeDateTime("132259", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.01.2011 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("1302122259", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("2012-02-13T22:59:59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("2012-02-13T22:59:59Z", false, referenceDate,
				DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("2012-02-13T22:59:59.123", false, referenceDate,
				DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("2012-02-13T22:59:59.123Z", false, referenceDate,
				DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:59", formatter.format(probe));
		// date only
		probe = DateHelperMTR.completeDateTime("20120213", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.01.2011 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2011 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("3FEB2012", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("03.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB12", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB2012", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		// time only
		probe = DateHelperMTR.completeDateTime("2259", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("23.01.2011 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("22:59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("23.01.2011 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("22:59:59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("23.01.2011 22:59:59", formatter.format(probe));
		// more date only
		probe = DateHelperMTR.completeDateTime("2012-02-13", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.2.2012", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02.2012", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02.12", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 23:11:10", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02.", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2011 23:11:10", formatter.format(probe));

		// Date/Time separated by blank or dash
		probe = DateHelperMTR.completeDateTime("13 22.59.59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.01.2011 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB/225959", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2011 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("3FEB2012 22:59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("03.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB12 22:59:59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:59", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13FEB2012 2259", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02. 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2011 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02.12 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.02.2012 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13.2.2012 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("1302 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2011 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("130212 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13022012 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("2012-02-13 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
		probe = DateHelperMTR.completeDateTime("13/02/2012 22/59", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("13.02.2012 22:59:00", formatter.format(probe));
	}

	@Test
	/**
	 * Tests for critical date values (month and year changes, short and long months etc.)
	 */
	public void testDDCriticalDates() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date probe = null;

			// test middle of february
			probe = DateHelperMTR.completeDateTime("01", false, formatter.parse("14.02.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("01.02.2013 23:11:10", formatter.format(probe));

			// test middle of february (month with 28 days, our range is +14d and -14d)
			probe = DateHelperMTR.completeDateTime("01", false, formatter.parse("15.02.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("01.03.2013 23:11:10", formatter.format(probe));

			// test middle of long month (month with 31 days, in the middle of the month you do not
			// reach a day 1 in both directions)
			probe = DateHelperMTR.completeDateTime("01", false, formatter.parse("16.03.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("01.04.2013 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("01", false, formatter.parse("31.12.2012 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("01.01.2013 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("31", false, formatter.parse("31.12.2012 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("31.12.2012 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("30", false, formatter.parse("31.12.2012 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("30.12.2012 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("30", false, formatter.parse("01.01.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("30.12.2012 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("31", false, formatter.parse("01.01.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("31.12.2012 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("01", false, formatter.parse("01.01.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("01.01.2013 23:11:10", formatter.format(probe));

			// test year switch
			probe = DateHelperMTR.completeDateTime("10", false, formatter.parse("01.01.2013 23:11:10"),
					DateRangeType.PlusMinus12H);
			assertEquals("10.01.2013 23:11:10", formatter.format(probe));

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.getMessage());

		}
	}

	@Test
	/**
	 * Tests for critical date values (month and year changes, short and long months etc.)
	 */
	public void testDDMonthSwitch() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date probe = null;

			// test longer month backreference from shorter month
			// Result should be 31. august.
			probe = DateHelperMTR.completeDateTime("31", false, formatter.parse("01.09.2013 01:00:00"),
					DateRangeType.PlusMinus12H);
			assertEquals("31.08.2013 01:00:00", formatter.format(probe));

			probe = DateHelperMTR.completeDateTime("31", false, formatter.parse("01.01.2013 01:00:00"),
					DateRangeType.PlusMinus12H);
			assertEquals("31.12.2012 01:00:00", formatter.format(probe));

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormatWithDayChangeFuture() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("31.12.2012 23:11:10");

			final Date probe = DateHelperMTR.completeDateTime("0010", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("01.01.2013 00:10:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormatWithDayChangePast() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:10");

			final Date probe = DateHelperMTR.completeDateTime("2125", false, referenceDate, DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("31.12.2012 21:25:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormatWithDayChangePlus24H() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("2125", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);
			assertEquals("01.01.2013 21:25:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testTimeFormatSameMinute() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 01:11:10");

			final Date probe = DateHelperMTR.completeDateTime("0111", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);
			assertEquals("23.02.2012 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testHHMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 00:11:10");

			final Date probe = DateHelperMTR.completeDateTime("0111", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);
			assertEquals("23.02.2012 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDHHMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.02.2012 00:11:10");

			final Date probe = DateHelperMTR.completeDateTime("230111", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);
			assertEquals("23.02.2012 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMYYHHMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.08.2013 01:20:00");

			final Date probe = DateHelperMTR.completeDateTime("2308130111", false, referenceDate,
					DateRangeType.PlusMinus12H);

			final String probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testXmlDate() throws Exception {
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Date referenceDate = formatter.parse("23.08.2013 01:20:10");

		Date probe = DateHelperMTR.completeDateTime("2013-08-23", false, referenceDate, DateRangeType.PlusMinus12H);
		assertEquals("23.08.2013 01:20:10", formatter.format(probe));

		probe = DateHelperMTR.completeDateTime("2013-08-23Z", false, referenceDate, DateRangeType.PlusMinus12H,
				DateHelperMTR.ALLOWED_FORMATS_XML);
		assertEquals("23.08.2013 01:20:10", formatter.format(probe));

		probe = DateHelperMTR.completeDateTime("2013-08-23+01:00", false, referenceDate, DateRangeType.PlusMinus12H,
				DateHelperMTR.ALLOWED_FORMATS_XML);
		assertEquals("22.08.2013 01:20:10", formatter.format(probe));
	}

	@Test
	public void testXmlDateTime() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("23.08.2013 01:20:10");

			Date probe = DateHelperMTR.completeDateTime("2013-08-23T01:11:10", false, referenceDate,
					DateRangeType.PlusMinus12H);
			String probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 01:11:10", probeAsString);

			probe = DateHelperMTR.completeDateTime("2013-08-23T01:11:10Z", false, referenceDate,
					DateRangeType.PlusMinus12H, DateHelperMTR.ALLOWED_FORMATS_XML);
			probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 01:11:10", probeAsString);

			probe = DateHelperMTR.completeDateTime("2013-08-23T01:11:10.123", false, referenceDate,
					DateRangeType.PlusMinus12H, DateHelperMTR.ALLOWED_FORMATS_XML);
			probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 01:11:10", probeAsString);

			probe = DateHelperMTR.completeDateTime("2013-08-23T01:11:10.123Z", false, referenceDate,
					DateRangeType.PlusMinus12H, DateHelperMTR.ALLOWED_FORMATS_XML);
			probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 01:11:10", probeAsString);

			probe = DateHelperMTR.completeDateTime("2013-08-23T01:11:10-01:00", false, referenceDate,
					DateRangeType.PlusMinus12H, DateHelperMTR.ALLOWED_FORMATS_XML);
			probeAsString = formatter.format(probe);
			assertEquals("23.08.2013 02:11:10", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("02MAY", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("02.05.2013 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMMYYYY() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("12APR2016", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("12.04.2016 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testYYYYMMDD() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("20160812", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("12.08.2016 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testSplitDateTime() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			Date probe = DateHelperMTR.completeDateTime("02MAY 001122", false, referenceDate, DateRangeType.Plus24H);
			final String probeAsString = formatter.format(probe);
			assertEquals("02.05.2013 00:11:22", probeAsString);

			probe = DateHelperMTR.completeDateTime("02MAY XX1122", false, referenceDate, DateRangeType.Plus24H);
			assertTrue(probe == null);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testInvalidDay() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.02.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("32OCT 001122", false, referenceDate,
					DateRangeType.Plus24H);
			assertTrue(probe == null);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void test31thWithFebruaryReference() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.02.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("31", true, referenceDate, DateRangeType.Plus24H);
			final String probeAsString = formatter.format(probe);
			assertEquals("31.01.2013 00:00:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testStrictFormat() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			// first try invalid format:
			Date probe = DateHelperMTR.completeDateTime("02MAY 001122", false, referenceDate, DateRangeType.Plus24H,
					Arrays.asList("ddMM HHmmss"));
			assertTrue(probe == null);

			// now try valid format:
			probe = DateHelperMTR.completeDateTime("02-MAY 001122", false, referenceDate, DateRangeType.Plus24H,
					Arrays.asList("dd-MMM HHmmss"));
			final String probeAsString = formatter.format(probe);
			assertEquals("02.05.2013 00:11:22", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testStrictISO() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");
			Date probe = null;

			probe = DateHelperMTR.completeDateTime("2018-12-31T12:00:00.123+05:00", false, referenceDate,
					DateRangeType.Plus24H, DateHelperMTR.ALLOWED_FORMATS_XML);
			assertEquals("31.12.2018 07:00:00", formatter.format(probe));

			probe = DateHelperMTR.completeDateTime("2018-12-31T12:00:00.123Z", false, referenceDate,
					DateRangeType.Plus24H, DateHelperMTR.ALLOWED_FORMATS_XML);
			assertEquals("31.12.2018 12:00:00", formatter.format(probe));

			probe = DateHelperMTR.completeDateTime("2018-12-31T12:00:00+05:00", false, referenceDate,
					DateRangeType.Plus24H, DateHelperMTR.ALLOWED_FORMATS_XML);
			assertEquals("31.12.2018 07:00:00", formatter.format(probe));

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMMYY() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("02JUL20", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("02.07.2020 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMMYYHHMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("02JUL20 1601", false, referenceDate,
					DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("02.07.2020 16:01:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDslashHHslashMM() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("12/02/03", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("12.01.2013 02:03:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testYYYYconversionToyyyy() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");
			Date probe = DateHelperMTR.completeDateTime("2008-12-30", false, referenceDate, DateRangeType.Plus24H,
					Arrays.asList("YYYY-MM-dd"));

			String probeAsString = formatter.format(probe);
			assertEquals("30.12.2008 01:11:00", probeAsString);

			// test encoding as well (YYYY would lead to 2009!):
			probe = formatter.parse("30.12.2008 01:11:00");

			// OPSDateFormat converts YYYY to yyyy automatically:
			// probeAsString = OPSDateFormat.format("YYYY-MM-dd", probe);

			final SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			probeAsString = sdm.format(probe);

			assertEquals("2008-12-30", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMMinusOneYear() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("01.01.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("02JUL", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("02.07.2012 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testDDMMMPlusOneYear() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("31.12.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("02MAY", false, referenceDate, DateRangeType.Plus24H);

			final String probeAsString = formatter.format(probe);

			assertEquals("02.05.2014 01:11:00", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testHHMMSS() {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			final Date referenceDate = formatter.parse("31.12.2013 01:11:00");

			final Date probe = DateHelperMTR.completeDateTime("/122511", false, referenceDate,
					DateRangeType.Minus23Plus1);

			final String probeAsString = formatter.format(probe);

			assertEquals("30.12.2013 12:25:11", probeAsString);

		} catch (final ParseException e) {
			fail("Exception: " + e.getMessage());
		} catch (final Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	/**
	 * Tests the operation of
	 * {@link DateHelperMTR#completeDateTimeFromDuration(String, String, Date)}.
	 */
	@Test
	public void testCompleteDateTimeFromDuration() {
		String value;
		String format;
		Date referenceDate;

		// case 1

		value = null;
		format = null;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 2

		value = "";
		format = null;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 3

		value = null;
		format = DateHelperMTR.XML;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 4

		value = "";
		format = DateHelperMTR.XML;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 5

		value = "10";
		format = null;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNotNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 6

		value = "-10";
		format = null;
		referenceDate = null;

		try {
			final Date result = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.assertNotNull(result);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got exception");
		}

		// case 7

		value = "10.0";
		format = null;
		referenceDate = null;

		try {
			DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}

		// case 8

		value = "10";
		format = DateHelperMTR.XML;
		referenceDate = null;

		try {
			DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}

		// XML examples follow. Format and reference date stays the same except for the first.

		format = DateHelperMTR.XML;
		referenceDate = new Date(1483146719000L); // 31.12.2016 01:11:59

		// case 9 - various incorrectly formatted inputs

		try {
			DateHelperMTR.completeDateTimeFromDuration("P-20M", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P20MT", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P1YM5D", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P15.5Y", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P1D2H", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("1Y2M", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P2M1Y", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("P", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}
		try {
			DateHelperMTR.completeDateTimeFromDuration("PT15.S", format, referenceDate);
			Assert.fail("Got no Exception");
		} catch (final Exception e) {
			// this is the expected result
		}

		// case 11 - various correctly formatted inputs

		try {
			Assert.assertEquals(new Date(1562334449000L),
					DateHelperMTR.completeDateTimeFromDuration("P2Y6M5DT12H35M30S", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1483240319000L),
					DateHelperMTR.completeDateTimeFromDuration("P1DT2H", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1535677919000L),
					DateHelperMTR.completeDateTimeFromDuration("P20M", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1483147919000L),
					DateHelperMTR.completeDateTimeFromDuration("PT20M", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1535677919000L),
					DateHelperMTR.completeDateTimeFromDuration("P0Y20M0D", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1483146719000L),
					DateHelperMTR.completeDateTimeFromDuration("P0Y", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1477962719000L),
					DateHelperMTR.completeDateTimeFromDuration("-P60D", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}

		try {
			Assert.assertEquals(new Date(1483146809500L),
					DateHelperMTR.completeDateTimeFromDuration("PT1M30.5S", format, referenceDate));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail("Got Exception");
		}
	}
}
