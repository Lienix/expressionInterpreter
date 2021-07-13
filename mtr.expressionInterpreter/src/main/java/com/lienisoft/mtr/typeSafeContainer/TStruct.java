package com.lienisoft.mtr.typeSafeContainer;

public interface TStruct extends TContainer {

	/**
	 * get object from data structure by given key structure
	 *
	 * @param key key structure
	 * @return object if exists or null if does not exist
	 */
	Object get(Object key);

	/**
	 * get object of given type from data structure by given key structure
	 *
	 * @param key key structure
	 * @param type class of expected object
	 * @return object of given type if exists or null if does not exist
	 * @throws class cast exception if object is not of expected type
	 */
	default <T> T get(Object key, Class<T> type) {
		return type.cast(get(key));
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
	default <T> T tryGet(Object key, Class<T> type) {
		return TContainer.tryCast(type, get(key));
	}

	/**
	 * put object to data structure using given key structure
	 *
	 * @param key key structure
	 * @param value object that shall be set
	 * @return former value if existed or null
	 */
	Object put(Object key, Object value);

	/**
	 * remove object from data structure using given key structure
	 *
	 * @param key key structure
	 * @return former value if existed or null
	 */
	Object remove(Object key);

}
