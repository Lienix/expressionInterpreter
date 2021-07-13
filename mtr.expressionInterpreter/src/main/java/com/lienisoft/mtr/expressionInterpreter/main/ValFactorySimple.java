package com.lienisoft.mtr.expressionInterpreter.main;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;
import com.lienisoft.mtr.expressionInterpreter.basic.ValFactoryBase;
import com.lienisoft.mtr.typeSafeContainer.TContainer;
import com.lienisoft.mtr.utils.DateHelperMTR;

public class ValFactorySimple extends ValFactoryBase {

	private static final Logger LOG = LoggerFactory.getLogger(ValFactorySimple.class);

	public static final String HEX = "HEX";
	public static final String XML = "XML";

	public static final String YYMMDD = "YYMMDD";

	private final TArgument container;

	public ValFactorySimple(TArgument container) {
		this.container = container;
	}

	@Override
	public TArgument createValSimple(String rawValue, String type, String format, Date referenceDate) {
		try {
			final TArgument val = super.createValSimple(rawValue, type, format, referenceDate);
			return val;
		} catch (final IllegalArgumentException e) {
			// final ErrorType error = ErrorType.valueOf(e.getMessage());
			// switch(error) {
			// break;
			// case InvalidNumber:
			// break;
			// default:
			// break;
			// }
			// TODO!
			// return new ValError(error, type, rawValue);
			return null;
		}
	}

	/**
	 * Creates a DateTime argument representing the point of time based on the reference time plus
	 * the duration.
	 *
	 * @param value the String representation of the duration
	 * @param format the formatting alias that facilitates parsing
	 * @param referenceDate reference time as base for calculation
	 * @return the DateTime argument marking the point of time denoted by the reference date and
	 *         the duration together
	 */
	@Override
	protected TArgument createValDateTimeAsDuration(String value, String format, Date referenceDate) {
		try {
			final Date completedValue = DateHelperMTR.completeDateTimeFromDuration(value, format, referenceDate);
			return container.create(completedValue);
		} catch (final Exception e) {
			// result.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			// TODO : error handling !
			LOG.warn("ParserError: {}", e.toString());
		}

		return null;
	}

	@Override
	protected TArgument createValBoolean(String value) {
		if (value == null || value.isEmpty()) {
			return container.create(null);
		} else if (value.equals("1") || value.equals("Y")) {
			return container.create(true);
		} else if (value.equals("0") || value.equals("N")) {
			return container.create(false);
		} else {
			return container.create(null);
		}
	}

	@Override
	protected TArgument createValString(String value) {
		return container.create(value);
	}

	@Override
	protected TArgument createValInteger(String value, String format) {
		if (value == null || value.isEmpty()) {
			return container.create(null);
		}
		try {
			if (format != null && format.equals(HEX)) {
				return container.create(Integer.parseInt(value.trim(), 16));
			} else {
				return container.create(Integer.valueOf(value.trim()));
			}
		} catch (final NumberFormatException e) {
			// final ValInteger vi = new ValInteger(null);
			// vi.setErrorCode(errorManager.getCode(ErrorId.InvalidNumber));
			// return vi;
			// TODO : error handling !
			LOG.warn("ParserError: {}", e.toString());
			// throw new IllegalArgumentException(ErrorType.InvalidNumber.name());
			throw new IllegalArgumentException("InvalidNumber");
		}
	}

	@Override
	public TArgument createValAltitude(String value, String format) {
		// TODO:
		// if (value == null || value.isEmpty()) {
		// return new ValAltitude();
		// }
		//
		// try {
		// // no format -> parse as cm
		// if (format == null) {
		// return new ValAltitude(value);
		// }
		// return new ValAltitude(value, format);
		// } catch (final NumberFormatException e) {
		// }
		//
		// // if all failed:
		// final ValAltitude v = new ValAltitude();
		// v.setErrorCode(errorManager.getCode(ErrorId.InvalidNumber));
		// return v;
		return null;
	}

	@Override
	public TArgument createValLatitude(String value, String format) {
		// TODO:
		// if (value == null || value.isEmpty()) {
		// return new ValLatitude();
		// }
		//
		// try {
		// // no format -> parse as KG
		// if (format == null) {
		// return new ValLatitude(Integer.valueOf(value.trim()));
		// }
		// return new ValLatitude(value, format);
		// } catch (final NumberFormatException e) {
		// } catch (final Exception e) {
		// }
		//
		// // if all failed:
		// final ValLatitude v = new ValLatitude();
		// v.setErrorCode(errorManager.getCode(ErrorId.InvalidNumber));
		// return v;
		return null;
	}

	@Override
	public TArgument createValLongitude(String value, String format) {
		// if (value == null || value.isEmpty()) {
		// return new ValLongitude();
		// }
		//
		// try {
		// if (format == null) {
		// return new ValLongitude(Integer.valueOf(value.trim()));
		// }
		// return new ValLongitude(value, format);
		// } catch (final NumberFormatException e) {
		// } catch (final Exception e) {
		// }
		//
		// // if all failed:
		// final ValLongitude v = new ValLongitude();
		// v.setErrorCode(errorManager.getCode(ErrorId.InvalidNumber));
		// return v;
		return null;
	}

	@Override
	public TArgument createValUnitWeight(String value, String format) {
		//
		// if (value == null || value.isEmpty()) {
		// return new ValUnitWeight();
		// }
		//
		// try {
		// final Integer parsedValue = Integer.valueOf(value.trim());
		// // no format -> parse as KG
		// if (format == null) {
		// return new ValUnitWeight(parsedValue);
		// }
		// return new ValUnitWeight(parsedValue, format);
		// } catch (final NumberFormatException e) {
		// }
		//
		// // If Integer parsing failed, try float
		// try {
		// final Double parsedValue = Double.valueOf(value.trim());
		// // no format -> parse as KG
		// if (format == null) {
		// return new ValUnitWeight(parsedValue.intValue());
		// }
		// return new ValUnitWeight(parsedValue, format);
		// } catch (final NumberFormatException e) {
		// } catch (final Exception e) {
		// }
		//
		// // if all failed:
		// final ValUnitWeight vUW = new ValUnitWeight();
		// vUW.setErrorCode(errorManager.getCode(ErrorId.InvalidNumber));
		// return vUW;
		return null;
	}

	// public ValInteger createValInteger(Integer value) {
	// return new ValInteger(value);
	// }

	@Override
	public TArgument createValDateTime(String value, Date referenceDate, String format) {
		if (value == null || value.isEmpty()) {
			// ValDateTime val = new ValDateTime(null);
			// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			return container.create(null);
		}

		Date date = null;

		try {
			date = DateHelperMTR.completeDateTime(value, false, referenceDate, DateHelperMTR.DateRangeType.NoCheck,
					evaluateFormatList(format));
			return container.create(date);
		} catch (final Exception e) {
		}

		// ValDateTime val = new ValDateTime(date);
		// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
		return container.create(null);
	}

	@Override
	public TArgument createValDateTimeAsDate(String value, String format, Date referenceDate) {
		if (value == null || value.isEmpty()) {
			// ValDateTime val = new ValDateTime(null);
			// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			return container.create(null);
		}

		if (format != null && format.equals(YYMMDD)) {
			if (value.length() == 6) {
				value = value.substring(4, 6) + "." + value.substring(2, 4) + "." + value.substring(0, 2) + " 00:00:00";
			}
		}

		/**
		 * This is for values without time-part. Therefore we force time to 000000 here!
		 */
		try {
			final Date date = DateHelperMTR.completeDateTime(value, true, referenceDate,
					DateHelperMTR.DateRangeType.NoCheck, evaluateFormatList(format));
			return container.create(date);
		} catch (final Exception e) {
		}

		// ValDateTime val = new ValDateTime(date);
		// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
		return container.create(null);
	}

	/**
	 * Creates a date/time object receiving only the time (HHMM or HHMMSS)
	 * TODO: Departure Time may be 0000 - check this!
	 *
	 * @param format
	 */
	@Override
	public TArgument createValTime(String value, Date referenceDate, DateHelperMTR.DateRangeType dateRangeType,
			String format) {

		LOG.debug("value:         {}", value);
		LOG.debug("referenceDate: {}", referenceDate);
		LOG.debug("dateRangeType: {}", dateRangeType);
		LOG.debug("format:        {}", format);

		if (value == null || value.isEmpty()) {
			// ValDateTime val = new ValDateTime(null);
			// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			return container.create(null);
		}

		try {
			final Date date = DateHelperMTR.completeDateTime(value, false, referenceDate, dateRangeType,
					evaluateFormatList(format));
			return container.create(date);
		} catch (final Exception e) {
			LOG.warn("ParserError: {}", e.toString());
		}

		// TODO:
		// ValDateTime val = new ValDateTime(date);
		// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
		return null;
	}

	/**
	 * Creates a date/time object receiving only the time (HHMM), lookup the date from -23
	 * to +1 hours
	 *
	 * @param format
	 */
	@Override
	public TArgument createValTimePast(String value, Date referenceDate, String format) {
		if (value == null || value.isEmpty()) {
			// ValDateTime val = new ValDateTime(null);
			// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			return container.create(null);
		}

		try {
			final Date date = DateHelperMTR.completeDateTime(value, false, referenceDate,
					DateHelperMTR.DateRangeType.Minus23Plus1, evaluateFormatList(format));
			return container.create(date);
		} catch (final Exception e) {
			LOG.warn("ParserError: {}", e.toString());
		}

		// ValDateTime val = new ValDateTime(date);
		// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
		return null;
	}

	/**
	 * Creates a date/time object receiving only the time (HHMM or HHMMSS)
	 * TODO: Arrival Time may be 2400 - check this!
	 *
	 * @param format
	 */
	@Override
	public TArgument createValArrivalTime(String value, Date referenceDate, DateHelperMTR.DateRangeType dateRangeType,
			String format) {
		if (value == null || value.isEmpty()) {
			// ValDateTime val = new ValDateTime(null);
			// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
			LOG.warn("ParserError: value is null or empty");
			return container.create(null);
		}

		Date date = null;
		try {
			// switch hour 24 to hour 00
			if (value.length() > 2 && value.startsWith("24")) {
				value = "00" + value.substring(2);
			}

			date = DateHelperMTR.completeDateTime(value, false, referenceDate, dateRangeType,
					evaluateFormatList(format));
			return container.create(date);
		} catch (final Exception e) {
			LOG.warn("ParserError: {}", e.toString());
		}

		// TODO:
		// ValDateTime val = new ValDateTime(date);
		// val.setErrorCode(errorManager.getCode(ErrorId.ParserError));
		return null;
	}

	// public ValDateTime createValDateTime(Date value) {
	// return new ValDateTime(value);
	// }

	@Override
	public TArgument createValStation(String value) {
		// TODO
		// if (value == null || value.isEmpty()) {
		// return new ValStation(null, null, null);
		// }
		//
		// StringPairId station = null;
		// if (StringHelper.isNumeric(value)) {
		// Integer stationId = Integer.valueOf(value);
		// station = airlineCodeManager.findStationObjectById(stationId);
		// if (station == null) {
		// ValStation vs;
		// vs = new ValStation(value, null, null, stationId);
		// vs.setErrorCode(errorManager.getCode(ErrorId.UnknownStation));
		// return vs;
		// }
		// } else if (value.length() == 3 || value.length() == 4) {
		// if (value.length() == 3) {
		// station = airlineCodeManager.findStationObjectByIATA(value);
		// } else {
		// station = airlineCodeManager.findStationObjectByICAO(value);
		// }
		// if (station == null) {
		// ValStation vs;
		// if (value.length() == 3) {
		// vs = new ValStation(value, value, null);
		// } else {
		// // case: length = 4
		// vs = new ValStation(value, null, value);
		// }
		// vs.setErrorCode(errorManager.getCode(ErrorId.UnknownStation));
		// return vs;
		// }
		// } else {
		// final ValStation vs = new ValStation(value, null, null);
		// vs.setErrorCode(errorManager.getCode(ErrorId.InvalidStation));
		// return vs;
		// }
		// if (station.getValue() == null || station.getValue().isEmpty()) {
		// return new ValStation(value, "XXX", station.getKey(), station.getId());
		// } else {
		// return new ValStation(value, station.getValue(), station.getKey(), station.getId());
		// }
		LOG.warn("ParserError: cannot create Station");
		return null;
	}

	@Override
	public TArgument createValAirline(String value) {
		// TODO
		// if (value == null || value.isEmpty()) {
		// return new ValAirline(null, null, null);
		// }
		//
		// StringPairId airline = null;
		//
		// if (StringHelper.isNumeric(value)) {
		// airline = airlineCodeManager.findAirlineObjectById(Integer.valueOf(value));
		// } else if (value.length() == 2 || value.length() == 3) {
		// if (value.length() == 2) {
		// airline = airlineCodeManager.findAirlineObjectByIATA(value);
		// } else {
		// airline = airlineCodeManager.findAirlineObjectByICAO(value);
		// }
		// if (airline == null) {
		// ValAirline vs;
		// if (value.length() == 2) {
		// vs = new ValAirline(value, value, null);
		// } else {
		// // case: length = 3
		// vs = new ValAirline(value, null, value);
		// }
		//
		// vs.setErrorCode(errorManager.getCode(ErrorId.UnknownAirline));
		// return vs;
		// }
		// } else {
		// final ValAirline vs = new ValAirline(value, null, null);
		// vs.setErrorCode(errorManager.getCode(ErrorId.InvalidAirline));
		// return vs;
		// }
		//
		// if (airline == null) {
		// final ValAirline vs = new ValAirline(value, null, null);
		// vs.setErrorCode(errorManager.getCode(ErrorId.UnknownAirline));
		// return vs;
		// }
		// return new ValAirline(value, airline.getValue(), airline.getKey(), airline.getId());
		LOG.warn("ParserError: cannot create Airline");
		return null;
	}

	/**
	 * create a ValRegistration by given DB Id
	 *
	 * @param id : DB AIRCRAFT.ID
	 * @return ValRegistration
	 */
	// public ValRegistration createValRegistrationById(Integer id) {
	// if (id == null) {
	// return new ValRegistration(null);
	// }
	// final StringPairId registration = registrationDataManager.findRegistrationObject(id);
	// if (registration == null) {
	// final ValRegistration valReg = new ValRegistration(String.valueOf(id));
	// setDashPosGeneric(valReg);
	// valReg.setErrorCode(errorManager.getCode(ErrorId.UnknownAircraft));
	// return valReg;
	// }
	// return new ValRegistration(String.valueOf(id), registration.key, registration.value,
	// registration.getId());
	// }

	/**
	 * create a ValRegistration by given String. Format can be with dash or without dash.
	 * Example: "D-ALFA" or "DALFA"
	 *
	 * @param value : aircraft registration (tailsign, aircraft number)
	 * @return ValRegistration
	 */
	public Object createValRegistration(String value) {
		// TODO
		// if (value == null || value.isEmpty()) {
		// return new ValRegistration(null);
		// }
		// int dashPos = -1;
		//
		// // convert to uppercase to catch lowercase input
		// String cleanedRegistration = value.toUpperCase();
		// dashPos = cleanedRegistration.indexOf('-');
		//
		// // clean from dash!
		// if (dashPos > -1) {
		// cleanedRegistration = cleanedRegistration.replace("-", "");
		// }
		//
		// // Check aircraft is known to system:
		// final StringPairId registration =
		// registrationDataManager.findRegistrationObject(cleanedRegistration);
		//
		// if (registration == null) {
		// final ValRegistration valReg = new ValRegistration(value);
		//
		// setDashPosGeneric(valReg);
		//
		// valReg.setErrorCode(errorManager.getCode(ErrorId.UnknownAircraft));
		// return valReg;
		// }
		//
		// // Check dash position if contained:
		// if (dashPos > -1) {
		// final ValRegistration valReg = new ValRegistration(value, registration.key, value,
		// registration.getId());
		// // Dash does not match between current registration and saved registration:
		// if (!registration.value.equals(value)) {
		// valReg.setErrorCode(errorManager.getCode(ErrorId.DashDoesNotMatch));
		// }
		// return valReg;
		// }
		// return new ValRegistration(value, registration.key, registration.value,
		// registration.getId());
		LOG.warn("ParserError: cannot create Registration");
		return null;
	}

	/**
	 * create a ValRegistration by given String and given format.
	 * If format is null a registration String format (with dash or without dash) is assumed
	 *
	 * @param value
	 * @param format ("ID" if value is a DB ID, null or any other if value is a String)
	 * @return
	 */
	@Override
	public TArgument createValRegistrationByFormat(String value, String format) {
		// TODO
		// /** DB ID cannot be detected as registration value may be a number too! */
		// if (value != null && format != null && format.equals(ValStringId.ID) &&
		// StringHelper.isNumeric(value)) {
		// return createValRegistrationById(Integer.valueOf(value));
		// } else {
		// /**
		// * actual format: ValRegistration.REG_CLEAN or ValRegistration.REG_DASH will be detected
		// * automatically!
		// */
		// return createValRegistration(value);
		// }
		// }
		//
		// private void setDashPosGeneric(ValRegistration valReg) {
		// final String reg = valReg.getValue();
		//
		// for (final Entry<String, Integer> regDashPosition : regDashPositions.entrySet()) {
		// if (reg.startsWith(regDashPosition.getKey())) {
		// valReg.setDashPos(regDashPosition.getValue());
		// return;
		// }
		// }
		LOG.warn("ParserError: cannot create Registration");
		return null;
	}

	@Override
	protected TArgument createValStringPosition(String value) {
		// return new ValCodeKey(value);
		LOG.warn("ParserError: cannot create StringPosition");
		return null;
	}

	@Override
	public String format(TContainer argument, String format) {
		if (argument != null) {
			if (format == null || format.isEmpty()) {
				return argument.toString();
			}
			switch (argument.getClass().getSimpleName()) {
			// case "Integer":
			// return ValIntegerFormat.toString((Integer) argument, format);
			// case "String":
			// return ValStringFormat.toString((Integer) argument, format);
			// case "Boolean":
			// return ValBooleanFormat.toString((Integer) argument, format);
			// case "Date":
			// return ValdateFormat.toString((Integer) argument, format);
			// default:
			// return argument.toString();
			}
		}
		return null;
	}

	protected static String formatInteger(Integer value, String format) {

		final Pattern pat = Pattern.compile("\\.[0-9]{1,}f");

		final Matcher matcher = pat.matcher(format);

		if (matcher.find()) {

			final String found = matcher.group();
			return found;
		}

		return null;
	}

	// "%.1f"
	//
	//
	// Pattern

}
