package com.lienisoft.mtr.expressionInterpreter.basic;

import java.io.Serializable;

public class LgEntry implements Serializable {

	private static final long serialVersionUID = -3695104020885850756L;

	final Integer elapsedMs;
	final String name;
	final Integer id;
	final String error;

	public static StringBuilder add(StringBuilder concat, String error) {
		if (concat == null) {
			concat = new StringBuilder();
		}
		concat.append("\n  . ");
		concat.append(error);
		return concat;
	}

	/**
	 * log processing steps or errors
	 *
	 * @param name name of the logged object
	 * @param id id of the logged object
	 * @param elapsedMs (optional) time consumed for this processing step
	 * @param error (optional) set this field only if an error occured
	 */
	public LgEntry(String name, Integer id, Integer elapsedMs, String error) {
		super();
		this.elapsedMs = elapsedMs;
		this.name = name;
		this.id = id;
		this.error = error;
	}

	public Integer getElapsedMs() {
		return elapsedMs;
	}

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public String getError() {
		return error;
	}

	@Override
	public String toString() {
		if (error == null) {
			return id + "\t(" + elapsedMs + "ms)" + "\t" + name;
		} else {
			return "\t" + id + " ERROR:" + error;
		}
	}

}
