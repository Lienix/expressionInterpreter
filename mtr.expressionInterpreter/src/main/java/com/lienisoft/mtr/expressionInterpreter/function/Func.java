package com.lienisoft.mtr.expressionInterpreter.function;

import java.util.List;

import com.lienisoft.mtr.expressionInterpreter.basic.ExprException;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoTypeMismatch;
import com.lienisoft.mtr.expressionInterpreter.basic.ExprInfoWrongParameterCount;
import com.lienisoft.mtr.expressionInterpreter.basic.TArgument;

public abstract class Func {

	public enum Type {
		IF, // System function IF
		LOOP, // System function LOOP
		GENERIC // Standard Function
	}

	/**
	 * calulate the result of a function using the argument or argument list from given result
	 *
	 * @param result argument or argument list of the function
	 * @return Result
	 * @throws ExprException if arguments do not match function
	 */
	// public abstract ResultBase<K, V> getResult(ResultBase<K,V> result) throws
	// ExpressionException;

	private final int minParams;
	private final Integer maxParams;

	public Func(int minParams, Integer maxParams) {
		super();

		if (maxParams != null && minParams > maxParams) {
			throw new IllegalArgumentException("minParams (" + minParams + ") > maxParams (" + maxParams + ")");
		}

		this.minParams = minParams;
		this.maxParams = maxParams;
	}

	public boolean isLValue() {
		return false;
	};

	// @SuppressWarnings("unchecked")
	// @Override
	public TArgument getResult(TArgument arg) throws ExprException {

		final TFuncArg funcArg = new TFuncArg(arg);

		checkSize(funcArg.size());

		final TArgument resultGeneric = getResultChecked(funcArg);

		if (resultGeneric != null) {
			return resultGeneric;
		}

		throw expressionException(funcArg);
	}

	public Type getType() {
		return Type.GENERIC;
	}

	//
	// @SuppressWarnings("unchecked")
	// public Result getResultNoArgs(Result dataProvider) throws ExpressionException {
	//
	// checkSize(0);
	//
	// List<ResultBase<K, V>> resultList = new ArrayList<>();
	// resultList.add((ResultBase<K, V>) dataProvider);
	//
	// ResultBaseList<K, V> resultGenericList = new ResultBaseList<>(resultList);
	//
	// ResultBase<K, V> resultGeneric = getResult(resultGenericList);
	//
	// if(resultGeneric != null) {
	// return resultGeneric;
	// }
	// throw expressionException(resultGenericList);
	// }

	protected ExprException expressionException(TFuncArg funcArg) {
		if (funcArg == null) {
			return new ExprException(getClass(), new ExprInfoTypeMismatch(-1, null, null));
		}
		return new ExprException(getClass(), new ExprInfoTypeMismatch(funcArg.getCurrentIndex(),
				getErrorClassName(funcArg), getTryClassNames(funcArg)));
	}

	private String getTryClassNames(TFuncArg funcArg) {
		final List<Class<?>> list = funcArg.getTryList();

		if (list != null && list.size() > 0) {
			StringBuilder s = null;
			for (final Class<?> clazz : list) {
				if (s == null) {
					s = new StringBuilder();
				} else {
					s.append(" or ");
				}
				if (clazz != null) {
					s.append(clazz.getSimpleName());
				} else {
					s.append("null");
				}
			}
			return s.toString();
		}
		return null;
	}

	private String getErrorClassName(TFuncArg funcArg) {

		// final V val = resultGenericList.getVal(resultGenericList.getCurrentIndex());
		// if (val != null) {
		// return val.getClass().getSimpleName();
		// }

		final Object obj = funcArg.getObj(funcArg.getCurrentIndex());
		return obj != null ? obj.getClass().getSimpleName() : null;
	}

	protected TArgument getResultChecked(TFuncArg funcArg) throws ExprException {
		throw new IllegalArgumentException("getResultChecked not implemented!");
	}

	// @SuppressWarnings("unchecked")
	// protected ResultBaseList<K, V> getResultList(Result result) throws ExpressionException {
	//
	// /**
	// * parameter list given > 1
	// */
	// if (result instanceof ResultBaseList) {
	// checkSize(((ResultBaseList<K, V>) result).size());
	// return (ResultBaseList<K, V>) result;
	// /**
	// * exactly one parameter given
	// */
	// } else if (result instanceof ResultBase) {
	// List<ResultBase<K, V>> resultList = new ArrayList<>();
	// resultList.add((ResultBase<K, V>) result);
	// checkSize(1);
	// return new ResultBaseList<>(resultList);
	// }
	//
	// return null;
	// }

	public void checkSize(int size) throws ExprException {

		if (size < minParams || (maxParams != null && size > maxParams)) {
			final ExprInfoWrongParameterCount info = new ExprInfoWrongParameterCount();
			info.setReceivedCount(size);
			info.setMinimum(minParams);
			info.setMaximum(maxParams);

			throw new ExprException(getClass(), info);

			// throw new WrongParameterCountException(getClass(), size, minParams, maxParams);
			// if(maxParams == null) {
			// throw new WrongParameterCountException(ExpressionInfo.Code.InvalidExpressionValue,
			// getClass(),
			// "invalid parameter count=" + size + " (expected>=" + minParams + ")");
			// } else if(minParams != maxParams) {
			// throw new WrongParameterCountException(ExpressionInfo.Code.InvalidExpressionValue,
			// getClass(),
			// "invalid parameter count=" + size + " (expected=" + minParams + "-" + maxParams +
			// ")");
			// } else {
			// throw new WrongParameterCountException(ExpressionInfo.Code.InvalidExpressionValue,
			// getClass(),
			// "invalid parameter count=" + size + " (expected=" + minParams + ")" + ")");
			// }
		}
	}

}
