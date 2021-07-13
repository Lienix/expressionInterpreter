package com.lienisoft.mtr.typeSafeContainer;

import java.util.List;
import java.util.Map;

/**
 * create a Copy of a Structure
 *
 * @author U083950
 */
public interface TStructCopy {

	/**
	 * create structural copy from given value
	 *
	 * @param value
	 * @return
	 */
	Object copyStruct(Object value);

	/**
	 * create List of required type
	 *
	 * @return
	 */
	List<?> createList();

	/**
	 * creates Map of required type
	 *
	 * @return
	 */
	Map<?, ?> createMap();

	/*******************************************************************************************************
	 * default methods to create structural copy
	 */

	default Map<?, ?> copyMap(Map<?, ?> map) {
		@SuppressWarnings("unchecked")
		final Map<Object, Object> newMap = (Map<Object, Object>) createMap();
		// for (final Entry<?, ?> entry : map.entrySet()) {
		// newMap.put(entry.getKey(), copyStruct(entry.getValue()));
		// }
		map.forEach((key, value) -> newMap.put(key, copyStruct(value)));
		return newMap;
	}

	default List<?> copyList(List<?> list) {
		@SuppressWarnings("unchecked")
		final List<Object> newList = (List<Object>) createList();
		// for (final Object entry : list) {
		// newList.add(copyStruct(entry));
		// }
		list.forEach(entry -> newList.add(copyStruct(entry)));
		return newList;
	}

}
