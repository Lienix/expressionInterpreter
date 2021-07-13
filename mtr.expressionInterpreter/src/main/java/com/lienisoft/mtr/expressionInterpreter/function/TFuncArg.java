package com.lienisoft.mtr.expressionInterpreter.function;

import java.util.ArrayList;
import java.util.List;

import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

/**
 * @author U083950
 */
public class TFuncArg {

	/**
	 * allowed classes tried
	 */
	private List<Class<?>> tryList = null;

	private final TArgument argument;

	public TFuncArg(TArgument arg) {
		argument = arg;
	}

	/**
	 * index of current function argument
	 */
	private int currentIndex = -1;

	protected void addError(int i, Class<?> clazz) {
		if (i != currentIndex) {
			currentIndex = i;
			tryList = new ArrayList<>();
		}
		tryList.add(clazz);
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public List<Class<?>> getTryList() {
		return tryList;
	}

	/**
	 * method to get member of an argument list (for functions)
	 *
	 * @param i
	 * @return
	 */
	public Object getObj(int i) {
		if (argument != null) {
			if (argument.listSize() != null) {
				return (argument.get(i));
			}
			if (i == 0) {
				return argument.get();
			}
		}
		return null;
	}

	public Object getKey() {
		if (argument != null) {
			return argument.getKey();
		}
		return null;
	}

	/**
	 * type safe access to i-th member of an argument list
	 *
	 * @param <T>
	 * @param i
	 * @param type
	 * @return
	 */
	public <T> T get(int i, Class<T> type) {
		if (argument != null) {
			if (argument.listSize() != null) {
				return (argument.get(i, type));
			}
			if (i == 0) {
				return argument.get(type);
			}
		}
		return null;
	}

	/**
	 * try to cast safe access to i-th member of an argument list.
	 * each unsuccessful try will be registered as "Error"
	 *
	 * @param <T>
	 * @param i
	 * @param type
	 * @return
	 */
	public <T> T tryGet(int i, Class<T> type) {

		if (argument != null) {
			if (argument.listSize() != null) {
				final T result = argument.tryGet(i, type);
				if (result != null) {
					return result;
				}
			} else if (i == 0) {
				final T result = argument.tryGet(type);
				if (result != null) {
					return result;
				}
			}
		}
		addError(i, type);
		return null;
	}

	/**
	 * get size of argument list
	 *
	 * @return
	 */
	public int size() {
		if (argument != null) {
			return argument.listSize() != null ? argument.listSize() : 1;
		}
		return 0;
	}

	public TArgument create(Object obj) {
		return argument != null ? argument.create(obj) : null;
	}

	public Object createKey(String key) {
		return argument != null ? argument.createKey(key) : null;
	}

	public TArgument createByKey(Object key) {
		return argument != null ? argument.createByKey(key) : null;
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append("TFuncArg:\n");
		s.append(argument);

		return s.toString();
	}

	public void remove(Object key) {
		if (argument != null) {
			argument.remove(key);
		}
	}

}
