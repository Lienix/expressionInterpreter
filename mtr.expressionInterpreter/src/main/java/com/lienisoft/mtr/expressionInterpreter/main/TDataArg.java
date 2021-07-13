package com.lienisoft.mtr.expressionInterpreter.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class TDataArg extends TData implements TArgument {

	private final String key;

	private final Object rootStructure;

	/**
	 * create constant value
	 *
	 * @param value
	 * @param rootStructure
	 */
	public TDataArg(Object value, Object rootStructure) {
		super(value);
		this.rootStructure = rootStructure;
		this.key = null;
	}

	/**
	 * create variable value
	 *
	 * @param key
	 * @param rootStructure
	 */
	public TDataArg(String key, Object rootStructure) {
		super(rootStructure);
		this.rootStructure = rootStructure;
		this.key = key;
	}

	/**
	 * only for testing!!!
	 *
	 * @param value
	 */
	public TDataArg(Object value) {
		super(value);
		this.rootStructure = null;
		this.key = null;
	}

	public TDataArg() {
		super();
		rootStructure = value;
		this.key = null;
	}

	@Override
	public TDataArg createByKey(Object key) {
		Objects.requireNonNull(key);
		return new TDataArg((String) key, rootStructure);
	}

	@Override
	public TDataArg create(Object obj) {
		return new TDataArg(obj, rootStructure);
	}

	// @Override
	// public TDataArg create(Object key) {
	// return new TDataArg((String) key, rootStructure);
	// }

	// @Override
	// public void set(Object value) {
	// if (key == null) {
	// throw new IllegalArgumentException("NOT AN L-VALUE!");
	// }
	// put(key, value);
	// }

	@Override
	public List<Object> getKeyList() {
		final Object value = get();
		if (value instanceof List<?>) {
			final List<?> list = (List<?>) value;
			final List<Object> retList = new ArrayList<>(list.size());
			for (int i = 0; i < list.size(); i++) {
				retList.add(i);
			}
			return retList;
		}
		if (value instanceof Map<?, ?>) {
			final Map<?, ?> map = (Map<?, ?>) value;
			final List<Object> retList = new ArrayList<>(map.size());
			for (final Object key : map.keySet()) {
				retList.add(key);
			}
			return retList;
		}
		return null;
	}

	@Override
	public Object get() {
		if (key != null) {
			return get(key);
		}
		return value;
	}

	@Override
	public Object get4Iter() {
		return value;
	}

	@Override
	public Integer listSize() {
		if (value instanceof List<?>) {
			return ((List<?>) value).size();
		}
		return null;
	}

	@Override
	public String createKey(String key) {
		return key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append("TDataArg\n");
		s.append("  key=");
		s.append(key);
		s.append("\n");
		s.append(super.toString());
		return s.toString();
	}

}
