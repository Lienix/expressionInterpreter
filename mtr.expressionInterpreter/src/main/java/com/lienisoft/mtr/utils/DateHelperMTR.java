package com.lienisoft.mtr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelperMTR {

	public static final TimeZone timeZone = TimeZone.getTimeZone("UTC");

	private static final Calendar calendar() {
		return Calendar.getInstance(timeZone);
		// return Calendar.getInstance();
	}

	private static final SimpleDateFormat simpleDateFormat(String format, Locale locale) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
		dateFormat.setTimeZone(timeZone);
		return dateFormat;
	}

	public static final String XML = "XML";

	public static enum MonthName {
		JAN(0), FEB(1), MAR(2), APR(3), MAY(4), JUN(5), JUL(6), AUG(7), SEP(8), OCT(9), NOV(10), DEC(11);

		MonthName(int index) {
			this.index = index;
		}

		public int index;
	};

	public static enum DateRangeType {
		PlusMinus12H(12, 12), Plus24H(0, 24), Minus24H(24, 0), NoCheck(0, 0), Minus23Plus1(23, 1), Minus1Plus23(1, 23);

		public int minusHours = 0;
		public int plusHours = 0;

		private DateRangeType(int minusHours, int plusHours) {
			this.minusHours = minusHours;
			this.plusHours = plusHours;
		}

		public static DateRangeType getEnumForValue(String searchValue) {
			if (searchValue == null) {
				return NoCheck;
			}

			for (final DateRangeType enm : EnumSet.allOf(DateRangeType.class)) {
				if (enm.toString().equals(searchValue)) {
					return enm;
				}
			}
			return NoCheck;
		}
	}

	/**
	 * For strings that contain a blank or a dash, we assume the blank or dash as separator and
	 * parse date and time separately.
	 * Here are the patterns for them
	 */
	static final List<String> ALLOWED_FORMATS_DEFAULT_DATE = Arrays.asList("d", "dd", "ddMMM", "dMMMyyyy", "ddMMMyy",
			"ddMMMyyyy", "dd.MM.", "dd.MM.yy", "dd.MM.yyyy", "dd.M.yyyy", "ddMM", "ddMMyy", "ddMMyyyy", "yyyy-MM-dd",
			"dd/MM/yyyy");
	static final List<String> ALLOWED_FORMATS_DEFAULT_TIME = Arrays.asList("HH.mm.ss", "HHmmss", "HH:mm", "HH:mm:ss",
			"HHmm", "HH/mm", "Hmm");

	/**
	 * This array defines the standard formats as well as the order in which they are checked.
	 * Only supports y, M, d, H, m and s in the patterns to be recognized as dynamic values!
	 * Note that the patterns here may not be contradictory! (e.g. ddMM would conflict with HHmm!)
	 */
	static final List<String> ALLOWED_FORMATS_DEFAULT_DATETIME = Arrays.asList("ddHHmm", "ddMMyyHHmm",
			"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyyMMdd", "d", "dd", "ddMMM", "dMMMyyyy", "ddMMMyy", "ddMMMyyyy", "HHmm",
			"HH:mm", "HH:mm:ss", "yyyy-MM-dd", "dd.M.yyyy", "dd.MM.yyyy", "dd.MM.yy", "dd.MM.", "Hmm");

	/**
	 * List of XML date and dateTime formats. First format will be used if formatting a date as
	 * "XML"
	 */
	public static final List<String> ALLOWED_FORMATS_XML = Arrays.asList("yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSX",
			"yyyy-MM-dd'T'HH:mm:ssX", "yyyy-MM-dd'Z'", "yyyy-MM-ddX", "yyyy-MM-dd");

	static int getMonth(String mon) {
		for (final MonthName month : MonthName.values()) {
			if (mon.equals(month.toString())) {
				return month.index;
			}
		}
		return -1; // not a valid month.
	}

	/**
	 * Optional negative prefix of XML duration format.
	 */
	static final String XML_DURATION_NEGATIVE_INDICATION = "-";
	/**
	 * Mandatory prefix of XML duration format.
	 */
	static final String XML_DURATION_PREFIX = "P";
	/**
	 * Optional "year" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_YEAR = "Y";
	/**
	 * Optional "month" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_MONTH = "M";
	/**
	 * Optional "day" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_DAY = "D";
	/**
	 * Conditional delimiter between date and time components in XML duration format.
	 */
	static final String XML_DURATION_DELIMITER_DATE_TIME = "T";
	/**
	 * Optional "hour" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_HOUR = "H";
	/**
	 * Optional "minute" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_MINUTE = "M";
	/**
	 * Optional "second" token's suffix in XML duration format.
	 */
	static final String XML_DURATION_TOKEN_SUFFIX_SECOND = "S";
	/**
	 * Optional delimiter between the seconds and milliseconds in XML duration format.
	 */
	static final String XML_DURATION_DELIMITER_SECOND_MILLIS = ".";

	/**
	 * Offers automatic completion of date/time values, aligned to a reference date/time with a
	 * timeframe/range where the desired result should be searched in.
	 *
	 * @param datetime = String to be evaluated to a Date
	 * @param setTimeToZero = if true, the time values will be overridden by zeroes even if the
	 *            parsed string contained hours/minutes/seconds!
	 * @param referenceDate = optional reference date if datetime includes no complete date
	 *            information (default=now!)
	 * @param dateRangeType = type of search range to consider when adjusting the time into a frame.
	 *            Dates are per default adjusted by checking +/- 14 days from reference date!
	 * @return = evaluated Date Object! null if parsing failed.
	 * @throws Exception
	 */

	public static Date completeDateTime(String datetime, boolean setTimeToZero, Date referenceDate,
			DateRangeType dateRangeType) throws Exception {
		return completeDateTime(datetime, setTimeToZero, referenceDate, dateRangeType, null);
	}

	private static class ProvidedElements {
		boolean year = false;
		boolean month = false;
		boolean day = false;
		boolean hours = false;
		boolean minutes = false;
		boolean seconds = false;
	}

	public static Date completeDateTime(String datetime, boolean setTimeToZero, Date referenceDate,
			DateRangeType dateRangeType, List<String> allowedFormatOverride) throws Exception {

		if (datetime == null) {
			return null;
		}

		if (referenceDate == null) {
			referenceDate = new Date();
		}

		final ProvidedElements provided = new ProvidedElements();

		List<String> allowedFormats = null;
		if (allowedFormatOverride != null) {
			// Replace Y by y to avoid unintentional handling of Week year!
			allowedFormats = new ArrayList<>();
			allowedFormats.addAll(allowedFormatOverride);
		} else {
			// use all available patterns for the regular logic
			allowedFormats = ALLOWED_FORMATS_DEFAULT_DATETIME;
		}

		Date parsed = null;

		int i = datetime.indexOf(" ");
		if (i == -1) {
			i = datetime.indexOf("/");
		}

		// If no override is given and the split of the input string was possible
		if (allowedFormatOverride == null && i >= 0 && i < datetime.length()) {
			String time = null;
			String date = null;
			// only check date if provided before the separator!
			if (i > 0) {
				date = datetime.substring(0, i);
			}
			time = datetime.substring(i + 1);

			// now parse the values in two steps!
			parsed = referenceDate;
			if (date != null) {
				parsed = parseDateWithReference(parsed, date, ALLOWED_FORMATS_DEFAULT_DATE, provided);
				// if we had a date token but it did not result in a valid parsed date, we failed
				if (parsed == null) {
					return null;
				}
			}
			parsed = parseDateWithReference(parsed, time, ALLOWED_FORMATS_DEFAULT_TIME, provided);
		} else {

			parsed = parseDateWithReference(referenceDate, datetime, allowedFormats, provided);
		}

		// if parsing failed, the parsed date/time is null
		if (parsed == null) {
			return null;
		}

		// boolean wasDaySpecified = dayProvided;
		// boolean wasMonthSpecified = monthProvided;
		// boolean wasYearSpecified = yearProvided;

		// Set date and time ///////////////////////////////////////////////////////////////
		final Calendar calculated = calendar();
		calculated.setTime(parsed);
		calculated.set(Calendar.MILLISECOND, 0);

		if ((provided.minutes || provided.hours) && !provided.seconds) {
			calculated.set(Calendar.SECOND, 0);
		}

		if (setTimeToZero) {
			calculated.set(Calendar.HOUR_OF_DAY, 0);
			calculated.set(Calendar.MINUTE, 0);
			calculated.set(Calendar.SECOND, 0);
		}

		/**
		 * DATEFRAME search (if no month or year was supplied, we need to find the according date!)
		 * Check is done +/-14 days from reference date.
		 * Only needed if at least day was specified and month and/or year are missing.
		 */

		if (provided.day) {
			if (!provided.month) {
				final Calendar checkDate = calendar();
				checkDate.setTime(calculated.getTime());
				// set time to 00:00:00.0 for checking!
				checkDate.set(Calendar.HOUR_OF_DAY, 0);
				checkDate.set(Calendar.MINUTE, 0);
				checkDate.set(Calendar.SECOND, 0);
				checkDate.set(Calendar.MILLISECOND, 0);

				final Calendar referencePlus14D = calendar();
				referencePlus14D.setTime(referenceDate);
				referencePlus14D.add(Calendar.DAY_OF_MONTH, 14);
				final Calendar referenceMinus14D = calendar();
				referenceMinus14D.setTime(referenceDate);
				referenceMinus14D.add(Calendar.DAY_OF_MONTH, -14);

				if (checkDate.getTime().before(referenceMinus14D.getTime())) {
					calculated.add(Calendar.MONTH, 1);
				} else if (checkDate.getTime().after(referencePlus14D.getTime())) {
					calculated.add(Calendar.MONTH, -1);
				}
			} else if (!provided.year) {

				final Calendar checkDate = calendar();
				checkDate.setTime(calculated.getTime());

				final Calendar referencePlus6Months = calendar();
				referencePlus6Months.setTime(referenceDate);
				referencePlus6Months.add(Calendar.MONTH, 6);
				final Calendar referenceMinus6Months = calendar();
				referenceMinus6Months.setTime(referenceDate);
				referenceMinus6Months.add(Calendar.MONTH, -6);

				if (checkDate.getTime().before(referenceMinus6Months.getTime())) {
					calculated.add(Calendar.YEAR, 1);
				} else if (checkDate.getTime().after(referencePlus6Months.getTime())) {
					calculated.add(Calendar.YEAR, -1);
				}
			}
		}

		/**
		 * TIMEFRAME search (if no day was supplied, we need to find the according date!)
		 */

		Date calculatedDateTime = calculated.getTime();

		if (!provided.day && referenceDate != null && dateRangeType != DateRangeType.NoCheck) {
			final Calendar refDateCal = calendar();
			refDateCal.setTime(referenceDate);
			// if supplied time has no seconds
			if (!provided.seconds) {
				refDateCal.set(Calendar.SECOND, 0);
			}

			refDateCal.add(Calendar.HOUR, -dateRangeType.minusHours);
			final Date rangeStart = refDateCal.getTime();

			refDateCal.add(Calendar.HOUR, dateRangeType.minusHours);
			refDateCal.add(Calendar.HOUR, dateRangeType.plusHours);
			final Date rangeEnd = refDateCal.getTime();

			if (calculatedDateTime.before(rangeStart)) {
				calculated.add(Calendar.DATE, 1);
			} else if (!calculatedDateTime.before(rangeEnd)) { // last second/minute does not count
				// into range to avoid same time on
				// multiple dates!
				calculated.add(Calendar.DATE, -1);
			}

			// If the corrected date is now in our range, use the new date. Otherwise
			if (!calculated.getTime().before(rangeStart) && calculated.getTime().before(rangeEnd)) {
				calculatedDateTime = calculated.getTime();
			} else {
				// log.warn("completeDateTime() could not find matching date in
				// range!\ncalculatedDateTime: "
				// + calculatedDateTime.toString() + "\nrangeFrom: " + rangeStart.toString() +
				// "\nrangeTo: "
				// + rangeEnd.toString());
			}
		}

		return calculatedDateTime; // return date time object.
	}

	public static String completeDateAsStr(String date) throws Exception {
		return formatDate(completeDateTime(date, true, null, DateRangeType.PlusMinus12H));
	}

	public static String formatDate(Date date) {
		final SimpleDateFormat dateFormat = simpleDateFormat("ddMMMyyyy", Locale.US);
		return dateFormat.format(date).toUpperCase();
	}

	public static String formatDateTime(Date date) {
		final SimpleDateFormat dateTimeFormat = simpleDateFormat("ddMMMyyyy HHmmss", Locale.US);
		return dateTimeFormat.format(date).toUpperCase();
	}

	/**
	 * This method tries to adjust the date of the targetTime to match the given rangeType according
	 * to the reference date/time.
	 * If no matching date was found (e.g. the targetTime has a date more than +/- 1 day of the
	 * reference), the targetTime is returned.
	 * Returns new Date object if the targetTime was modified.
	 *
	 * @param reference
	 * @param targetTime
	 * @param rangeType
	 * @return
	 */
	public static Date adjustDate(Date reference, Date targetTime, DateRangeType rangeType) {

		if (rangeType.equals(DateRangeType.NoCheck)) {
			return targetTime;
		}

		if (reference == null || targetTime == null) {
			return targetTime;
		}

		if (targetTime.equals(reference)) {
			return targetTime;
		}

		final Calendar refDateCal = calendar();
		refDateCal.setTime(reference);
		final Calendar result = calendar();
		result.setTime(targetTime);

		refDateCal.add(Calendar.HOUR, -rangeType.minusHours);
		final Date rangeStart = refDateCal.getTime();

		refDateCal.add(Calendar.HOUR, rangeType.minusHours);
		refDateCal.add(Calendar.HOUR, rangeType.plusHours);
		final Date rangeEnd = refDateCal.getTime();

		if (result.getTime().before(rangeStart)) {
			result.add(Calendar.DATE, 1);
		} else if (!result.getTime().before(rangeEnd)) { // last second/minute does not count
			// into range to avoid same time on
			// multiple dates!
			result.add(Calendar.DATE, -1);
		}

		// If the corrected date is now in our range, use the new date. Otherwise
		if (!result.getTime().before(rangeStart) && result.getTime().before(rangeEnd)) {
			return result.getTime();
		} else {
			return targetTime;
		}

	}

	/** Just a simple method to be used for test cases etc. to create values **/
	public static Date createDate(String set) {
		Date dt = new Date();
		// Festlegung des Formats:
		final SimpleDateFormat df = simpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);
		df.setTimeZone(timeZone);

		try {
			// Einlesen vom String:
			dt = df.parse(set);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

	/**
	 * converts a date object into a timestamp and sets milliseconds = 000
	 *
	 * @param date
	 * @return
	 */
	public static Date getTimeWithoutMillisec(Date date) {
		if (date == null) {
			return null;
		}
		final Calendar cal = calendar();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * returns current time as timestamp with milliseconds = 000
	 *
	 * @return
	 */
	public static Date newTimeWithoutMillisec() {
		return getTimeWithoutMillisec(new Date());
	}

	/**
	 * returns 01.01.1900 00:00:00 as timestamp with milliseconds = 000
	 *
	 * @return
	 */
	private static Date getDefaultFrom() {
		final Calendar cal = calendar();
		cal.set(1990, 0, 1, 0, 0, 0);
		final Date defaultFrom = cal.getTime();
		return getTimeWithoutMillisec(defaultFrom);
	}

	public static final Date defaultFrom = getDefaultFrom();

	public static Date dateWithoutTime(Date javaDate) {
		final Calendar cal = calendar();
		cal.setTime(javaDate);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		return cal.getTime();
	}

	public static Date modifyDate(Date date, int field, int amount) {
		final Calendar cal = calendar();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * Iterates through the given allowed formats. Updates the given reference date with the values
	 * included in the parsed string.
	 * Only supports y, M, d, H, m and s in the patterns to be recognized as dynamic values!
	 * Replaces dashes by temporary characters for parsing in the formats as well as the strToParse
	 * to avoid misrecognition of SDF which tries to to subtraction
	 */
	public static Date parseDateWithReference(Date referenceDate, String strToParse, List<String> allowedFormats,
			ProvidedElements provided) {
		final Calendar resultCal = calendar();
		resultCal.setTime(referenceDate);
		for (final String allowedFormat : allowedFormats) {

			// Check if the length of the strings are equal, otherwise it might be just a partial
			// match but we accept full matches only!
			// Only check this if there is more than one allowed Format (trying different formats)
			if (strToParse.length() != allowedFormat.replaceAll("'", "").replaceAll("X", "XXXXXX").length()) {
				continue;
			}

			// replace dashes from format as well as from the input string
			// avoids issues since SDF tries to recognize ranges and addition/subtraction!
			final String allowedFormatClean = allowedFormat.replaceAll("Y", "y");// .replaceAll("-",
																					// "*");
			final String toParseClean = strToParse;// .replaceAll("-", "*");

			try {
				final SimpleDateFormat sdf = simpleDateFormat(allowedFormatClean, Locale.US);
				sdf.setLenient(false);
				final Date parsedDate = sdf.parse(toParseClean);
				final Calendar parsedCal = calendar();
				parsedCal.setTime(parsedDate);

				// Now that we successfully parsed the date, we update the fields in the result
				// object for those fields that were contained in the pattern. All other fields
				// remain from the reference date
				if (allowedFormatClean.contains("y")) {
					resultCal.set(Calendar.YEAR, parsedCal.get(Calendar.YEAR));
					if (provided != null) {
						provided.year = true;
					}
				}
				if (allowedFormatClean.contains("M")) {
					resultCal.set(Calendar.MONTH, parsedCal.get(Calendar.MONTH));
					if (provided != null) {
						provided.month = true;
					}
				}
				if (allowedFormatClean.contains("d")) {
					// To avoid Linient handling (wrapping over the month) in case the current month
					// does not have the number of days that the date provided, we migh need to
					// subtract the month.
					final int daysInMonth = resultCal.getActualMaximum(Calendar.DAY_OF_MONTH);
					final int parsedDay = parsedCal.get(Calendar.DAY_OF_MONTH);
					if (daysInMonth < parsedDay) {
						// we use lenient, so if month gets to 0, it jumps back to december of
						// previous day!
						resultCal.add(Calendar.MONTH, -1);
					}

					resultCal.set(Calendar.DAY_OF_MONTH, parsedDay);
					if (provided != null) {
						provided.day = true;
					}
				}
				if (allowedFormatClean.contains("H")) {
					resultCal.set(Calendar.HOUR_OF_DAY, parsedCal.get(Calendar.HOUR_OF_DAY));
					if (provided != null) {
						provided.hours = true;
					}
				}
				if (allowedFormatClean.contains("m")) {
					resultCal.set(Calendar.MINUTE, parsedCal.get(Calendar.MINUTE));
					if (provided != null) {
						provided.minutes = true;
					}
				}
				if (allowedFormatClean.contains("s")) {
					resultCal.set(Calendar.SECOND, parsedCal.get(Calendar.SECOND));
					if (provided != null) {
						provided.seconds = true;
					}
				}

				return resultCal.getTime();
			} catch (final ParseException e) {
			}
		}

		return null;
	}

	/**
	 * Creates a {@link Date} object based on the given value, format and reference time.
	 * <br>
	 * Should the value be null or empty, null is returned.
	 * <br>
	 * Should the reference time be null, the current system time is taken instead.
	 * <br>
	 * Format can be either {@link ValDateTime#XML} or something else.
	 * <br>
	 * When the XML format is explicitly requested, then the standard XML duration parsing will
	 * occur.
	 * <br>
	 * Otherwise the value will be interpreted as an integer denoting minutes.<br>
	 *
	 * @see <a href="http://www.datypic.com/sc/xsd/t-xsd_duration.html">implementation basis</a>
	 * @param value the value
	 * @param format the format
	 * @param referenceDate the reference time
	 * @return the Date representing the point of time based on the reference time and duration
	 *         together, not null
	 * @throws Exception in case of parsing failure
	 */
	public static Date completeDateTimeFromDuration(String value, String format, Date referenceDate) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		if (referenceDate == null) {
			referenceDate = new Date();
		}

		final Calendar cal = calendar();
		cal.setTime(referenceDate);

		if (XML.equals(format)) {
			// assume XML duration input
			int signum;

			if (value.startsWith(XML_DURATION_NEGATIVE_INDICATION)) {
				signum = -1;
				value = value.substring(XML_DURATION_NEGATIVE_INDICATION.length());
			} else {
				signum = 1;
			}

			if (!value.startsWith(XML_DURATION_PREFIX)) {
				throw new Exception("duration prefix is missing");
			}

			value = value.substring(XML_DURATION_PREFIX.length());

			int year = 0;
			int month = 0;
			int day = 0;
			int hour = 0;
			int minute = 0;
			int second = 0;
			int milliseconds = 0;

			int index;

			index = value.indexOf(XML_DURATION_DELIMITER_DATE_TIME);
			String datePart;
			String timePart;

			if (index != -1) {
				datePart = value.substring(0, index);
				timePart = value.substring(index + XML_DURATION_DELIMITER_DATE_TIME.length(), value.length());

				if (timePart.isEmpty()) {
					throw new Exception(
							"when time delimiter is present, at least one time component must be specified");
				}
			} else {
				datePart = value;
				timePart = "";
			}

			value = "";
			int collectedComponents = 0;

			if (!datePart.isEmpty()) {
				// parse year
				index = datePart.indexOf(XML_DURATION_TOKEN_SUFFIX_YEAR);

				if (index != -1) {
					year = Integer.parseInt(datePart.substring(0, index));
					collectedComponents++;
					datePart = datePart.substring(index + XML_DURATION_TOKEN_SUFFIX_YEAR.length());
				}

				// parse month
				index = datePart.indexOf(XML_DURATION_TOKEN_SUFFIX_MONTH);

				if (index != -1) {
					month = Integer.parseInt(datePart.substring(0, index));
					collectedComponents++;
					datePart = datePart.substring(index + XML_DURATION_TOKEN_SUFFIX_MONTH.length());
				}

				// parse day
				index = datePart.indexOf(XML_DURATION_TOKEN_SUFFIX_DAY);

				if (index != -1) {
					day = Integer.parseInt(datePart.substring(0, index));
					collectedComponents++;
					datePart = datePart.substring(index + XML_DURATION_TOKEN_SUFFIX_DAY.length());
				}

				if (!datePart.isEmpty()) {
					throw new Exception("Unexpected remaining date part: " + datePart);
				}
			}

			if (!timePart.isEmpty()) {
				// parse hour
				index = timePart.indexOf(XML_DURATION_TOKEN_SUFFIX_HOUR);

				if (index != -1) {
					hour = Integer.parseInt(timePart.substring(0, index));
					collectedComponents++;
					timePart = timePart.substring(index + XML_DURATION_TOKEN_SUFFIX_HOUR.length());
				}

				// parse minute
				index = timePart.indexOf(XML_DURATION_TOKEN_SUFFIX_MINUTE);

				if (index != -1) {
					minute = Integer.parseInt(timePart.substring(0, index));
					collectedComponents++;
					timePart = timePart.substring(index + XML_DURATION_TOKEN_SUFFIX_MINUTE.length());
				}

				// parse second
				index = timePart.indexOf(XML_DURATION_TOKEN_SUFFIX_SECOND);

				if (index != -1) {
					final String secondsAndHundredMillis = timePart.substring(0, index);
					collectedComponents++;
					timePart = timePart.substring(index + XML_DURATION_TOKEN_SUFFIX_SECOND.length());
					final int indexOfMilliDelimiter = secondsAndHundredMillis
							.indexOf(XML_DURATION_DELIMITER_SECOND_MILLIS);

					if (indexOfMilliDelimiter != -1) {
						// parse seconds and milliseconds separately
						second = Integer.parseInt(secondsAndHundredMillis.substring(0, indexOfMilliDelimiter));
						milliseconds = Integer.parseInt(secondsAndHundredMillis.substring(
								indexOfMilliDelimiter + XML_DURATION_DELIMITER_SECOND_MILLIS.length(),
								secondsAndHundredMillis.length()));

						// .5 means 500 milliseconds, multiply by 10 until desired range is reached
						if (milliseconds > 0) {
							while (milliseconds < 100) {
								milliseconds *= 10;
							}
						}
					} else {
						// parse seconds only
						second = Integer.parseInt(secondsAndHundredMillis);
					}
				}
			}

			if (collectedComponents < 1) {
				throw new Exception("at least one component must be specified");
			}
			if (year < 0 || month < 0 || day < 0 || hour < 0 || minute < 0 || second < 0 || milliseconds < 0) {
				throw new Exception("no signum is allowed within components");
			}

			cal.add(Calendar.YEAR, signum * year);
			cal.add(Calendar.MONTH, signum * month);
			cal.add(Calendar.DAY_OF_YEAR, signum * day);
			cal.add(Calendar.HOUR_OF_DAY, signum * hour);
			cal.add(Calendar.MINUTE, signum * minute);
			cal.add(Calendar.SECOND, signum * second);
			cal.add(Calendar.MILLISECOND, signum * milliseconds);
		} else {
			// assume integer input denoting minutes
			final int durationMinutes = Integer.parseInt(value);
			cal.add(Calendar.MINUTE, durationMinutes);
		}

		return cal.getTime();
	}
}
