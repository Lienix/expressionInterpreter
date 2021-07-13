package com.lienisoft.mtr.typeSafeContainer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * some general useful default method implementations for TStruct Typesafe Containers
 *
 * @author U083950
 */
public interface TStructDefault extends TStruct, TStructCopy {

	/**
	 * an iterator for key structures, given as dot separated list of key strings. key structure may
	 * contain "%" as wild card
	 *
	 * @author U083950
	 */
	final static class KeyIter {
		final String key;
		final String rest;
		final Object newVal;

		enum Mode {
			GET, PUT, REMOVE
		}

		final Mode mode;

		static KeyIter iterGet(Object keyObj) {
			return new KeyIter(keyObj, null, Mode.GET);
		}

		static KeyIter iterRemove(Object keyObj) {
			return new KeyIter(keyObj, null, Mode.REMOVE);
		}

		static KeyIter iterPut(Object keyObj, Object newVal) {
			return new KeyIter(keyObj, newVal, Mode.PUT);
		}

		KeyIter next() {
			return new KeyIter(rest, newVal, mode);
		}

		private KeyIter(Object keyObj, Object newVal, Mode mode) {

			final String keyStruct = Objects.requireNonNull(keyObj).toString();
			final int dot = keyStruct.indexOf('.');
			if (dot < 0) {
				key = keyStruct;
				rest = null;
			} else {
				key = keyStruct.substring(0, dot);
				rest = keyStruct.substring(dot + 1);
			}
			this.newVal = newVal;
			this.mode = mode;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("KeyIter [\n  key=");
			builder.append(key);
			builder.append(",\n  rest=");
			builder.append(rest);
			builder.append(",\n  newVal=");
			builder.append(newVal);
			builder.append(",\n  mode=");
			builder.append(mode);
			builder.append("\n]");
			return builder.toString();
		}

	}

	/**
	 * get object from data structure by given key structure
	 *
	 * @param key key structure
	 * @return object if exists or null if does not exist
	 */
	@Override
	default public Object get(Object key) {
		return find(get4Iter(), KeyIter.iterGet(key));
	}

	Object get4Iter();

	/**
	 * get object of given type from data structure by given key structure
	 *
	 * @param key key structure
	 * @param type class of expected object
	 * @return object of given type if exists or null if does not exist
	 * @throws class cast exception if object is not of expected type
	 */
	@Override
	default public <T> T get(Object key, Class<T> type) {
		final Object val = find(get4Iter(), KeyIter.iterGet(key));
		return val != null ? type.cast(val) : null;
	}

	/**
	 * put object to data structure using given key structure
	 *
	 * @param key key structure
	 * @param value object that shall be set
	 * @return former value if existing or null
	 */
	@Override
	default public Object put(Object key, Object value) {
		return find(get4Iter(), KeyIter.iterPut(key, value));
	}

	/**
	 * remove object from data structure using given key structure
	 *
	 * @param key key structure
	 * @return former value if existing or null
	 */
	@Override
	default public Object remove(Object key) {
		return find(get4Iter(), KeyIter.iterRemove(key));
	}

	/**
	 * find or set TypeSafe value in the structure of this by using a Key iterator
	 *
	 * @param struct
	 * @param currentKey
	 * @param newVal if given, this value will replace existing value or will be just set if value
	 *            did not yet exist.
	 * @return found TypeSafe value or null if not exists
	 */
	default Object find(Object struct, KeyIter currentKey) {

		/** last layer of key structure **/
		if (currentKey.rest == null) {
			return getPutRemove(struct, currentKey);
		}
		/** inside key structure **/
		else {
			final KeyIter nextKey = currentKey.next();
			if ("%".equals(currentKey.key)) {
				Object newStruct = findFirst(struct, nextKey);

				/**
				 * fallback case for insert via wildcard (%):
				 */
				if (newStruct == null && nextKey.mode == KeyIter.Mode.PUT) {
					newStruct = createStruct(struct, currentKey.key, nextKey.key);

				}

				return newStruct != null ? find(newStruct, nextKey) : null;
			} else {
				/** check if currenKey matches, if yes return next substructure **/
				final Object value = getInner(struct, currentKey.key);
				/** next substructure exists **/
				if (value != null) {
					return find(value, nextKey);
				}
				/** in put mode must insert missing substructure ! **/
				else if (nextKey.mode == KeyIter.Mode.PUT) {
					final Object newStruct = createStruct(struct, currentKey.key, nextKey.key);
					return find(newStruct, nextKey);
				}
			}
		}

		return null;
	}

	/**
	 * checks and puts value to given data structure (Map or List).
	 * If list has not enough members to fit to given key (=index) it will be resized automatically
	 *
	 * @param struct given structure to put new value
	 * @param key given key
	 * @param newVal given new value
	 * @return the former value or null if it did not yet exist
	 */
	default Object getPutRemove(Object struct, KeyIter key) {
		switch (key.mode) {
		case GET:
			return getInner(struct, key.key);
		case PUT:
			return putInner(struct, key.key, key.newVal);
		case REMOVE:
			return removeInner(struct, key.key);
		default:
			return null;
		}
	}

	/**
	 * get from structure inner function
	 *
	 * @param struct
	 * @param key
	 * @return
	 */
	default Object getInner(Object struct, String key) {

		final boolean isIndex = isIndex(key);

		/** case structure is map **/
		if (struct instanceof Map<?, ?> && !isIndex) {
			return ((Map<?, ?>) struct).get(key);
		}
		/** case structure is list **/
		else if (struct instanceof List<?> && isIndex) {
			final int index = Integer.valueOf(key.toString());
			if (index >= 0 && index < ((List<?>) struct).size()) {
				return ((List<?>) struct).get(index);
			}
		}
		return null;
	}

	/**
	 * put to structure inner function
	 *
	 * @param struct
	 * @param key
	 * @param newVal
	 * @param ifExists perform update only if old value already exists
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Object putInner(Object struct, Object key, Object newVal) {

		/** case structure is map **/
		if (struct instanceof Map<?, ?>) {
			return ((Map<Object, Object>) struct).put(key, newVal);
		}
		/** case structure is list **/
		else if (struct instanceof List<?>) {
			final int index = Integer.valueOf(key.toString());
			if (index >= 0) {
				/** in put mode resize array if necessary! **/
				while (index >= ((List<?>) struct).size()) {
					((List<?>) struct).add(null);
				}
			}
			final Object val = ((List<Object>) struct).get(index);
			((List<Object>) struct).set(index, newVal);
			return val;
		}
		throw new IllegalArgumentException("ASSIGNMENT_FAILED");
	}

	/**
	 * remove from struct inner function
	 *
	 * @param struct
	 * @param key
	 * @return
	 */
	default Object removeInner(Object struct, Object key) {
		/** case structure is map **/
		if (struct instanceof Map<?, ?>) {
			return ((Map<?, ?>) struct).remove(key);
		}
		/** case structure is list **/
		else if (struct instanceof List<?>) {
			final int index = Integer.valueOf(key.toString());
			if (index >= 0 && index < ((List<?>) struct).size()) {
				return ((List<?>) struct).remove(index);
			}
		}
		throw new IllegalArgumentException("ASSIGNMENT_FAILED");
	}

	/**
	 * find first occurence of a given key and return its value
	 *
	 * @param struct given structure to search
	 * @param key given key
	 * @return returns substructure that contains key if exists or null
	 */
	default Object findFirst(Object struct, KeyIter key) {
		final Object val = getInner(struct, key.key);
		if (val != null) {
			return struct;
		} else {
			for (final Object iterVal : getValues(struct)) {
				if (isStruct(iterVal)) {
					final Object newStruct = findFirst(iterVal, key);
					if (newStruct != null) {
						return newStruct;
					}
				}
			}
			return null;
		}
	}

	/**
	 * creates new structure in existing structure using current key.
	 * List will be created and resized until index if next key is an index
	 * Map will be created if next key is a string
	 *
	 * @param struct existing structure
	 * @param currentKey current key
	 * @param nextKey key one level below current key
	 * @return the newly created structure (map or list)
	 */
	@SuppressWarnings("unchecked")
	default Object createStruct(Object struct, String currentKey, String nextKey) {

		final Object newStruct = isIndex(nextKey) ? createList() : createMap();

		if (struct instanceof Map<?, ?>) {
			((Map<Object, Object>) struct).put(currentKey, newStruct);
			return newStruct;
		} else if (struct instanceof List<?>) {
			final int index = Integer.valueOf(currentKey);
			if (index >= 0) {
				while (index >= ((List<?>) struct).size()) {
					((List<?>) struct).add(null);
				}
				((List<Object>) struct).set(index, newStruct);
			}
			return newStruct;
		}
		throw new IllegalArgumentException("ASSIGNMENT_FAILED");
	}

	/**
	 * checks if given Key is an index or a String
	 *
	 * @param s key to be checked
	 * @return true if key is index, false if key is string
	 */
	static boolean isIndex(String s) {
		if (s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (Character.digit(s.charAt(i), 10) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * check if given value is data structure or just simple object
	 *
	 * @param val value to be checked
	 * @return true if this is structure else false
	 */
	default boolean isStruct(Object val) {
		return (val != null && (val instanceof Map || val instanceof List));
	}

	/**
	 * retrieve all values of given structure as simple list
	 *
	 * @param struct
	 * @return list if this is structure (map or list) else null
	 */
	default List<Object> getValues(Object struct) {
		if (struct instanceof Map) {
			@SuppressWarnings("unchecked")
			final Map<Object, Object> map = (Map<Object, Object>) struct;
			if (map != null) {
				@SuppressWarnings("unchecked")
				final List<Object> list = (List<Object>) createList();
				for (final Object obj : map.values()) {
					list.add(obj);
				}
				return list;
			}
		}

		if (struct instanceof List) {
			@SuppressWarnings("unchecked")
			final List<Object> list = (List<Object>) struct;
			if (list != null) {
				return list;
			}
		}
		return null;
	}

	/**
	 * create key object from given String
	 *
	 * @param key
	 * @return
	 */
	// @Override
	// default Object createKey(String key) {
	// return key;
	// }

	// @Override
	// default TContainer getContainer(Object key) {
	// return create(getObj(key));
	// }
	//
	// @Override
	// default TContainer putContainer(Object key, TContainer val) {
	// return create(put(key, val != null ? val.getObj() : null));
	// }

}
