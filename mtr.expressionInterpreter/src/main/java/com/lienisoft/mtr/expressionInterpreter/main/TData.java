package com.lienisoft.mtr.expressionInterpreter.main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lienisoft.mtr.typeSafeContainer.TStructDefault;

public class TData implements TStructDefault {

	protected final Object value;

	/**
	 * most common needed version for testing:
	 */
	public TData() {
		value = createMap();
	}

	public TData(Object value) {
		this.value = value;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public Object get4Iter() {
		return value;
	}

	@Override
	public Map<?, ?> createMap() {
		return new LinkedHashMap<>();
	}

	@Override
	public List<?> createList() {
		return new ArrayList<>();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TData");
		builder.append(" [\n  value=");
		builder.append(value);
		builder.append("\n]");
		return builder.toString();
	}

	@Override
	public Object copyStruct(Object entry) {
		if (entry instanceof Map<?, ?>) {
			return copyMap((Map<?, ?>) entry);
		} else if (entry instanceof List<?>) {
			return copyList((List<?>) entry);
		} else {
			return entry;
		}
	}

	// @Override
	// public TContainer create(Object obj) {
	// return new TData(obj);
	// }

	// @Override
	// public TData getContainerStructureCopy() {
	// return (TData) create(copyStruct(getObj()));
	// }
}
