package com.lienisoft.mtr.expressionInterpreter.basic;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.lienisoft.mtr.typeSafeContainer.TStruct;

public interface TArgument extends TStruct, Comparable<TArgument> {

	/**
	 * special constructor for ExprVar: create a variable expression
	 *
	 * @param key
	 * @param arg
	 * @return
	 */
	TArgument createByKey(Object key);

	Object createKey(String key);

	/**
	 * get inner object for calculations
	 *
	 * @return
	 */
	@Override
	Object get();

	/***********************************************************************************************
	 * basic functions for Expression Processing
	 */

	/**
	 * create new TContainer instance
	 *
	 * @param obj
	 * @return
	 */
	TArgument create(Object obj);

	/**
	 * implement assignment if necessary
	 *
	 * @param arg
	 * @throws IllegalArgumentException if Container is not an L-Value!
	 * @throws ExprException if assignment not possible
	 */
	default void assign(TArgument right) throws ExprException {
		Objects.requireNonNull(right);
		final Object key = getKey();
		final Object existingObj = get();
		final Object newObj = right.get();
		if (key == null) {
			throw new ExprException(getClass(), new ExprInfoOperationNotDefined(ExprInfo.Code.NOT_AN_LVALUE,
					"assign not allowed", existingObj, newObj, Operate.ASSIGN));
		}
		if (existingObj == null || newObj == null || existingObj.getClass() == newObj.getClass()) {
			if (newObj != null || existingObj != null) {
				try {
					put(key, right.getCopy());
				} catch (final Exception e) {
					throw new ExprException(getClass(), new ExprInfoOperationNotDefined(ExprInfo.Code.ASSIGNMENT_FAILED,
							"Cannot create substructure on ValSimple", existingObj, newObj, Operate.ASSIGN));
				}
			}
			/**
			 * if newObj == null AND existingObj == null : -> intentionally just do nothing!
			 */
		} else {
			throw new ExprException(getClass(), new ExprInfoOperationNotDefined(ExprInfo.Code.TYPE_MISMATCH,
					"assign between different types", existingObj, newObj, Operate.ASSIGN));
		}
	}

	/**
	 * get own key
	 *
	 * @return key or null if this is a constant value!
	 */
	Object getKey();

	/**
	 * get Object for assignment (used for LOOP function):
	 * return object in case of simple object
	 * return a copy of the inner structure in case of structure
	 *
	 * @param key Iterated Key
	 * @return VALUE which is iterated in LOOP
	 */
	default Object getCopy(Object key) {
		return copyStruct(get(key));
	}

	/**
	 * get Object for assignment:
	 * return object in case of simple object
	 * return a copy of the inner structure in case of structure
	 *
	 * @return
	 */
	default Object getCopy() {
		return copyStruct(get());
	}

	Object copyStruct(Object obj);

	default boolean equals(TArgument right) {
		final Object leftObj = this.get();
		final Object rightObj = right.get();

		if (leftObj == null || rightObj == null) {
			return leftObj == null && rightObj == null;
		}
		return leftObj.equals(rightObj);
	}

	/*****************************************************************************************************
	 * implement Comparable Interface
	 */

	/**
	 * some basic types of objects can be compared
	 *
	 * @param right
	 * @return
	 */
	default boolean isComparable(TArgument right) {
		if (tryGet(Integer.class) != null && right.tryGet(Integer.class) != null) {
			return true;
		}
		if (tryGet(String.class) != null && right.tryGet(String.class) != null) {
			return true;
		}
		if (tryGet(Date.class) != null && right.tryGet(Date.class) != null) {
			return true;
		}
		return false;
	}

	@Override
	default int compareTo(TArgument right) {
		if (tryGet(Integer.class) != null && right.tryGet(Integer.class) != null) {
			return (tryGet(Integer.class)).compareTo(right.tryGet(Integer.class));
		}
		if (tryGet(String.class) != null && right.tryGet(String.class) != null) {
			return (tryGet(String.class)).compareTo(right.tryGet(String.class));
		}
		if (tryGet(Date.class) != null && right.tryGet(Date.class) != null) {
			return (tryGet(Date.class)).compareTo(right.tryGet(Date.class));
		}
		return 0;
	}

	/******************************************************************************************************
	 * methods for processing LOOP function:
	 */

	/**
	 * get list of Keys if self is list or map container.
	 * Keys are either indices or Key Objects
	 *
	 * @return list or null if self is neither a list nor a map
	 */
	List<Object> getKeyList();

	/*********************************************************************************************************
	 * methods for the COMMA-Operator (= List)
	 */

	/**
	 * create a List Argument
	 *
	 * @return
	 */
	List<?> createList();

	/**
	 * get size of the inner List argument
	 *
	 * @return size or null
	 */
	Integer listSize();

}
