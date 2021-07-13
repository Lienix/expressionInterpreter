package com.lienisoft.mtr.expressionInterpreter.parse;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public class KeyAndTContainer {
	public final Object key;
	public final TArgument container;

	public KeyAndTContainer(Object key, TArgument container) {
		this.key = key;
		this.container = container;
	}
}
