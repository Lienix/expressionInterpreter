package com.lienisoft.mtr.expressionInterpreter.basic;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.utils.DateHelperMTR;
import com.lienisoft.mtr.utils.DateHelperMTR.DateRangeType;

public abstract class ValFactoryBase implements IValFactory {

	private static final Logger log = LoggerFactory.getLogger(ValFactoryBase.class);

	public static final String HEX = "HEX";
	public static final String XML = "XML";

	public static final String YYMMDD = "YYMMDD";

	@Override
	public TArgument createValSimple(String str, String type, String format, Date referenceDate) {

		log.debug("str:           {}", str);
		log.debug("type:          {}", type);
		log.debug("format:        {}", format);
		log.debug("referenceDate: {}", referenceDate);

		switch (type) {
		case ValTypeBase.STRING:
			return createValString(str);
		case ValTypeBase.TIMEDIFF:
		case ValTypeBase.NUMBER:
		case ValTypeBase.FLIGHTNUMBER:
			return createValInteger(str, format);
		case ValTypeBase.TIMESTAMP:
			return createValDateTime(str, referenceDate, format);
		case ValTypeBase.DATE:
			return createValDateTimeAsDate(str, format, referenceDate);
		case ValTypeBase.AIRLINE:
			return createValAirline(str);
		case ValTypeBase.STATION:
			return createValStation(str);
		case ValTypeBase.REGISTRATION:
			return createValRegistrationByFormat(str, format);
		case ValTypeBase.DEPARTURETIME:
			return createValTime(str, referenceDate, DateRangeType.PlusMinus12H, format);
		case ValTypeBase.DEPARTURETIMEPAST:
			return createValTime(str, referenceDate, DateRangeType.Minus23Plus1, format);
		case ValTypeBase.ARRIVALTIMEPAST:
			return createValArrivalTime(str, referenceDate, DateRangeType.Minus23Plus1, format);
		case ValTypeBase.ARRIVALTIME:
			return createValArrivalTime(str, referenceDate, DateRangeType.PlusMinus12H, format);
		case ValTypeBase.ARRIVALTIMEFUTURE:
			return createValArrivalTime(str, referenceDate, DateRangeType.Minus1Plus23, format);
		case ValTypeBase.TIMEPAST:
			return createValTimePast(str, referenceDate, format);
		case ValTypeBase.CODEKEY:
			return createValStringPosition(str);
		case ValTypeBase.BOOLEAN:
			return createValBoolean(str);
		case ValTypeBase.UNIT_WEIGHT:
			return createValUnitWeight(str, format);
		// case ValTypeBase.WAY_POINT:
		// return createValWayPoint(str, format);
		case ValTypeBase.COMMENT:
			return null;
		case ValTypeBase.ALTITUDE:
			return createValAltitude(str, format);
		case ValTypeBase.LATITUDE:
			return createValLatitude(str, format);
		case ValTypeBase.LONGITUDE:
			return createValLongitude(str, format);
		case ValTypeBase.DURATION:
			return createValDateTimeAsDuration(str, format, referenceDate);
		case ValTypeBase.UNKNOWN:
		default:
			return createValString(str);
		}
	}

	protected abstract TArgument createValDateTimeAsDuration(String str, String format, Date referenceDate);

	protected abstract TArgument createValLongitude(String str, String format);

	protected abstract TArgument createValLatitude(String str, String format);

	protected abstract TArgument createValAltitude(String str, String format);

	protected abstract TArgument createValUnitWeight(String str, String format);

	protected abstract TArgument createValBoolean(String str);

	protected abstract TArgument createValStringPosition(String str);

	protected abstract TArgument createValTimePast(String str, Date referenceDate, String format);

	protected abstract TArgument createValArrivalTime(String str, Date referenceDate, DateRangeType minus23plus1,
			String format);

	protected abstract TArgument createValTime(String str, Date referenceDate, DateRangeType plusminus12h,
			String format);

	protected abstract TArgument createValRegistrationByFormat(String str, String format);

	protected abstract TArgument createValStation(String str);

	protected abstract TArgument createValAirline(String str);

	protected abstract TArgument createValDateTimeAsDate(String str, String format, Date referenceDate);

	protected abstract TArgument createValDateTime(String str, Date referenceDate, String format);

	protected abstract TArgument createValInteger(String str, String format);

	protected abstract TArgument createValString(String str);

	// protected abstract String format(V argument, String formate);

	protected List<String> evaluateFormatList(String format) {
		if (format != null && !format.isEmpty()) {
			if (format.startsWith("!")) {
				return Arrays.asList(format.substring("!".length()));
			}
			switch (format) {
			case YYMMDD:
				return null;
			case XML:
				return DateHelperMTR.ALLOWED_FORMATS_XML;
			default:
				return Arrays.asList(format);
			}
		}
		return null;
	}
}
