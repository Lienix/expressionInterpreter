package com.lienisoft.mtr.expressionInterpreter.stack;

import java.util.ArrayList;
import java.util.List;

public class ExprDebug {
	
	private final List<StringBuilder> lines = new ArrayList<> ();
	private int maxLen = 0;
	private int currentLine = 0;
	private final int depth;
	private final char indent;
	
	public ExprDebug(boolean singleLineMode, int depth, char indent) {
		set(0);
		if(singleLineMode) {
			maxLen = -1;
		} else {
			maxLen = 0;
		}
		this.depth = depth;
		this.indent = indent;
	}
	
	public void incrementCurrentLine() {
		if(maxLen >= 0) {
			set(currentLine + 1);
		}
	}

	public void decrementCurrentLine() {
		if(maxLen >= 0) {
			set(currentLine - 1);
		}
	}
	
	private void set(int currentLine) {
		while(lines.size() <= currentLine) {
			lines.add(new StringBuilder());
		}
		if(this.currentLine != currentLine) {
			if(lines.get(this.currentLine).length() > maxLen) {
				maxLen = lines.get(this.currentLine).length();
			}
			int curLen = lines.get(currentLine).length();
			lines.get(currentLine).append(makeIndent(maxLen - curLen));
			this.currentLine = currentLine;
		}
	}
	
	public StringBuilder get() {
		return lines.get(currentLine);
	}

	public String makeIndent(int size) {
		if(indent == 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		for(int i = 0; i<size; i++) {
			s.append(indent);
		}
		return s.toString();
	}

	public String toString() {
		if(lines.size() > 1) {
			StringBuilder s = new StringBuilder();
			for(int i = 0; i<lines.size(); i++) {
				if(depth != 0 && i > depth) {
					break;
				}
				if(i > 0) {
					if(i > 1) {
						s.append('\n');
					}
					s.append(lines.get(i));
				}
			}
			return s.toString();
		} else {
			return lines.get(0).toString();
		}
	}
	
	public boolean isMaxDepth() {
		if(depth != 0) {
			return currentLine >= depth;
		} else {
			return false;
		}
	}
	
}
