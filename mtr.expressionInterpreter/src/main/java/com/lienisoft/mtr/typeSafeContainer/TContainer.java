package com.lienisoft.mtr.typeSafeContainer;

/**
 * interface for 'Typesafe Container'.
 * T stands for Typesafe.
 *
 * @author U083950
 */
public interface TContainer {

	/**
	 * must be implemented to get the concrete object
	 *
	 * @return
	 */
	public Object get();

	/**
	 * type safe access internal Object as required type
	 * if Object is of required type: return specific class
	 * if Object has different type: throw class cast exception
	 * if Object is null: return null (independent of required type!)
	 *
	 * @param <T> returned object
	 * @param type required type
	 * @return specific class of required type or null
	 */
	default public <T> T get(Class<T> type) {
		return type.cast(get());
	}

	/**
	 * try to cast provided object to required type
	 * if Object is of required type: return specific class
	 * if Object has different type: return null
	 * if Object is null: return null
	 *
	 * @param <T>
	 * @param type required type
	 * @param obj provided object to check
	 * @return specific class of required type or null
	 */
	public static <T> T tryCast(Class<T> type, Object obj) {
		if (obj == null || !type.isAssignableFrom(obj.getClass())) {
			return null;
		}
		return type.cast(obj);
	}

	/**
	 * type safe try-access internal Object as required type
	 * if Object is of required type: return specific class
	 * if Object has different type: return null
	 * if Object is null: return null
	 *
	 * @param <T> returned object
	 * @param type required type
	 * @return specific class of required type or null
	 */
	default public <T> T tryGet(Class<T> type) {
		return tryCast(type, get());
	}

	default public String asString() {
		return get() != null ? get().toString() : null;
	}
}
