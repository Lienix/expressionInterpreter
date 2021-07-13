package com.lienisoft.mtr.expressionInterpreter.basic;


/**
 * find a String marked by leading and trailing " inside a list of tokens.
 * inside the String the following escapes are resolved and substituted: \t \n \r \\ \"
 * 
 * @author U083950
 *
 */
public class BetterStringParser {
	
	private int begin;
	private int end;
	private final String text;
	
	public BetterStringParser(String text) {
		this.text = text;
		find(0);
	}
	
	
	public String parse() {
		if(begin < 0) {
			return null;
		}
		
		StringBuilder parsed = new StringBuilder();
		// boolean escape = false;
		String backslashes = "";
		for(end = begin; end < text.length(); end++) {
			
			char c = text.charAt(end);
			
			switch (c) {
			case '"':
				backslashes = processBackslashes(backslashes, parsed);
				if(! backslashes.isEmpty()) {
					parsed.append('"');
					backslashes = "";
				} else {
					// Stop condition !
					parsed.append('"');
					if(end > begin) return parsed.toString();
				}
				break;
			case '\\':
				backslashes += '\\';
				break;
			case 'n':
				backslashes = processBackslashes(backslashes, parsed);
				parsed.append(! backslashes.isEmpty() ? '\n' : c);
				backslashes = "";
				break;
			case 'r':
				backslashes = processBackslashes(backslashes, parsed);
				parsed.append(! backslashes.isEmpty() ? '\r' : c);
				backslashes = "";
				break;
			case 't':
				backslashes = processBackslashes(backslashes, parsed);
				parsed.append(! backslashes.isEmpty() ? '\t' : c);
				backslashes = "";
				break;
			case ';': // for historical reason, semicolons might be escaped!
//				if(! backslashes.isEmpty()) {
//					backslashes = backslashes.substring(0, backslashes.length() - 1);
//					parsed.append(backslashes);
//					backslashes = "";
//				}
				backslashes = processBackslashes(backslashes, parsed);
				parsed.append(';');
				backslashes = "";
				break;
			default:
				if(! backslashes.isEmpty()) {
					// unknown escaped char oder just a remaining unescaped backslash:
					parsed.append(backslashes);
					backslashes = "";
				}
				parsed.append(c);
				break;
			}
		}
		/**
		 * this is an error: String not properly closed!
		 */
		return parsed.toString();
	}
	
	
	private String processBackslashes(String backslashes, StringBuilder parsed) {
		while(backslashes.length() > 1) {
			parsed.append('\\');
			backslashes = backslashes.substring(2);
		}
		return backslashes;
	}
	
	

	public boolean find(int fromIndex) {
		begin = text.indexOf('"', fromIndex);
		return begin >= 0;
	}


	public int start() {
		return begin;
	}

	public int end() {
		return end;
	}
}
