package com.lienisoft.mtr.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonSerializer {

	/**
	 * ISO 8601 established date as string format:
	 * Remark: SimpleDateFormat is not thread save so no static object could be used here!
	 */
	public static SimpleDateFormat getDateWithMillis() {
		return createDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	}

	public static SimpleDateFormat getDateWithoutMillis() {
		return createDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	}

	private static final SimpleDateFormat createDateFormat(String format) {
		final SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf;
	}

	public static class DateDeserializer extends StdDeserializer<Date> {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private final SimpleDateFormat withMillis = getDateWithMillis();
		private final SimpleDateFormat withoutMillis = getDateWithoutMillis();

		public DateDeserializer() {
			this(null);
		}

		public DateDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			final String dateString = p.getText();
			if (dateString.isEmpty()) {
				// handle empty strings however you want,
				// but I am setting the Date objects null
				return null;
			}

			try {
				return withMillis.parse(dateString);
			} catch (final ParseException e) {
				try {
					return withoutMillis.parse(dateString);
				} catch (final ParseException e1) {
					try {
						return new Date(Long.valueOf(dateString));
					} catch (final Exception e2) {
						throw new RuntimeException("Unable to parse date", e2);
					}
				}
			}
		}
	}

	/**
	 * Use ISO 8601 Date conversion, forced to "UTC"
	 */
	private static final ObjectMapper mapper = createObjectMapper();

	private static ObjectMapper createObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();

		// ! No special default encode mapping => mapping as JSON Standard Long.
		// mapper.setDateFormat(withMillis);

		final SimpleModule dateModule = new SimpleModule();
		dateModule.addDeserializer(Date.class, new DateDeserializer());
		mapper.registerModule(dateModule);

		// allow unescaped /n in a string:
		// mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

		return mapper;
	}

	public static String toJson(Object obj) {
		final ObjectWriter prettyWriter = mapper.writerWithDefaultPrettyPrinter();
		try {
			return prettyWriter.writeValueAsString(obj);
			// return mapper.writeValueAsString(obj);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Class<Data> valueType)
	public static <T> T toClass(String json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (final JsonParseException e) {
			throw new IllegalArgumentException(
					"cannot serialize to: " + valueType.getSimpleName() + "\n(" + e.getMessage() + ")");
		} catch (final JsonMappingException e) {
			throw new IllegalArgumentException(
					"cannot serialize to: " + valueType.getSimpleName() + "\n(" + e.getMessage() + ")");
		} catch (final IOException e) {
			throw new IllegalArgumentException(
					"cannot serialize to: " + valueType.getSimpleName() + "\n(" + e.getMessage() + ")");
		}
	}
}
