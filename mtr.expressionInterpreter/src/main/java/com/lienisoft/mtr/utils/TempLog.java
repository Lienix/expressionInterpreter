package com.lienisoft.mtr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Saves logs/errors which occured e.g. during a cache reset
 * REMARK: Actually this class was also intended as a logger for message processing (which steps
 * have been used). The info Level should be used for that. Currently, this is not yet the case!
 *
 * @author U083950
 */
public class TempLog {

	public enum Severity {
		ERROR, WARNING, INFO
	}

	/**
	 * inner class for identification of log entry
	 *
	 * @author U083950
	 */
	public static class LogEntry {

		private final String type;

		private final Severity severity;

		// identification id where the log event occured:
		private final Integer id;
		// identification string where the log event occured:
		private final String name;
		// the actual log text:
		private final String text;

		private final Integer elapsedMs;

		public LogEntry(String type, Severity severity, Integer id, String name, String text, Integer elapsedMs) {
			super();
			this.type = type;
			this.severity = severity;
			this.id = id;
			this.name = name;
			this.text = text;
			this.elapsedMs = elapsedMs;
		}

		@Override
		public String toString() {
			final StringBuilder s = new StringBuilder();

			if (type != null) {
				s.append(type);
				s.append(' ');
			}

			if (severity != null) {
				s.append(severity);
				s.append(' ');
			}

			if (id != null) {
				s.append(id);
				s.append(' ');
			}
			if (elapsedMs != null) {
				s.append('(');
				s.append(elapsedMs);
				s.append("ms) ");
			}

			if (name != null) {
				s.append(name);
				s.append(' ');
			}

			if (text != null) {
				s.append(text);
			}
			return s.toString();
		}

		public String toStringShort() {
			final StringBuilder s = new StringBuilder();

			if (id != null) {
				s.append(id);
			}
			while (s.length() < 8) {
				s.append(' ');
			}

			if (elapsedMs != null) {
				s.append('(');
				s.append(elapsedMs);
				s.append("ms) ");
			}
			while (s.length() < 17) {
				s.append(' ');
			}

			// if (type != null) {
			// s.append(type);
			// s.append(' ');
			// }

			if (severity == null || Severity.INFO.equals(severity)) {
				if (name != null) {
					s.append(name);
					s.append(' ');
				}
			} else {
				s.append("   ");
				s.append(severity);
				if (text != null) {
					s.append(':');
				}
			}

			if (text != null) {
				s.append(text);
			}
			return s.toString();
		}

		/**
		 * @return the id
		 */
		public Integer getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @return the severity
		 */
		public Severity getSeverity() {
			return severity;
		}

		public boolean isError() {
			return getSeverity().equals(Severity.ERROR);
		}

		public boolean isWarning() {
			return getSeverity().equals(Severity.WARNING);
		}

		public boolean isInfo() {
			return getSeverity().equals(Severity.INFO);
		}

		public boolean filterType(Predicate<String> p) {
			return p.test(type);
		}

		public boolean filterName(Predicate<String> p) {
			return p.test(name);
		}

		public boolean filterText(Predicate<String> p) {
			return p.test(text);
		}

		public final static Predicate<LogEntry> IS_ERR = LogEntry::isError;
		public final static Predicate<LogEntry> IS_WARN = LogEntry::isWarning;
		public final static Predicate<LogEntry> IS_INFO = LogEntry::isInfo;
	}

	private List<LogEntry> logList = null;

	/**
	 * inner storage Map of Log entries
	 *
	 * @return
	 */
	public List<LogEntry> getLog() {
		return logList;
	}

	/**
	 * append list to existing or add new if not existing
	 *
	 * @param type
	 * @param severity
	 * @param list
	 */
	public void addList(List<LogEntry> list) {
		if (list != null) {
			evaluateLog().addAll(list);
		}
	}

	/**
	 * log text in ERROR severity level
	 *
	 * @param type
	 * @param inText Log String with {} as placeholders for following objects (comma separated, as
	 *            String)
	 */
	public void err(String type, Object... inText) {
		log(type, Severity.ERROR, null, null, null, inText);
	}

	public void err(String type, String name, Integer id, Object... inText) {
		log(type, Severity.ERROR, null, name, id, inText);
	}

	public void err(String type, Integer elapsed, String name, Integer id, Object... inText) {
		log(type, Severity.ERROR, elapsed, name, id, inText);
	}

	/**
	 * log text in WARNING severity level
	 *
	 * @param type
	 * @param inText Log String with {} as placeholders for following objects (comma separated, as
	 *            String)
	 */
	public void warn(String type, Object... inText) {
		log(type, Severity.WARNING, null, null, null, inText);
	}

	public void warn(String type, String name, Integer id, Object... inText) {
		log(type, Severity.WARNING, null, name, id, inText);
	}

	public void warn(String type, Integer elapsed, String name, Integer id, Object... inText) {
		log(type, Severity.WARNING, elapsed, name, id, inText);
	}

	/**
	 * log text in INFO severity level
	 *
	 * @param type
	 * @param inText Log String with {} as placeholders for following objects (comma separated, as
	 *            String)
	 */
	public void info(String type, Object... inText) {
		log(type, Severity.INFO, null, null, null, inText);
	}

	public void info(String type, String name, Integer id, Object... inText) {
		log(type, Severity.INFO, null, name, id, inText);
	}

	public void info(String type, Integer elapsed, String name, Integer id, Object... inText) {
		log(type, Severity.INFO, elapsed, name, id, inText);
	}

	/**
	 * inner log routine
	 *
	 * @param type
	 * @param severity
	 * @param inText
	 */
	public void log(String type, Severity severity, Integer elapsed, String name, Integer id, Object... inText) {
		if (severity != null && type != null) {
			final List<LogEntry> list = evaluateLog();
			String text = null;

			/**
			 * evaluate text:
			 */
			if (inText != null && inText.length > 0 && inText[0] != null) {
				text = inText[0].toString();
				int i = 1;
				while (i < inText.length && text.contains("{}")) {
					text = text.replaceFirst("\\{\\}", inText[i] != null ? inText[i].toString() : "null");
					i++;
				}
			}

			list.add(new LogEntry(type, severity, id, name, text, elapsed));
		}
	}

	/**
	 * find existing list or create new list for given type and severity
	 *
	 * @param type
	 * @param severity
	 * @return list
	 */
	private List<LogEntry> evaluateLog() {
		if (logList == null) {
			logList = new ArrayList<>();
		}
		return logList;
	}

	/**
	 * concatenated formatted String output of the whole content of stored log entries
	 *
	 * @return
	 */
	public String debug() {
		final StringBuilder s = new StringBuilder();
		if (logList != null) {
			for (final LogEntry entry : logList) {
				s.append(entry);
				s.append('\n');
			}
		}

		return s.toString();
	}

	/**
	 * merge another log to this
	 *
	 * @param log
	 */
	public void add(TempLog log) {
		if (log != null && log.getLog() != null) {
			evaluateLog().addAll(log.getLog());
		}
	}

	@Override
	public String toString() {
		return debug();
	}

	/**
	 * amount of errors
	 *
	 * @return
	 */
	public int size() {
		return logList == null ? 0 : logList.size();
	}

	public String toStringShort() {
		final StringBuilder s = new StringBuilder();
		if (logList != null) {
			for (final LogEntry entry : logList) {
				s.append(entry.toStringShort());
				s.append('\n');
			}
		}

		return s.toString();
	}
}
