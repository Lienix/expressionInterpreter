package com.lienisoft.mtr.expressionInterpreter.basic;

import java.util.Date;

import com.lienisoft.mtr.typeSafeContainer.TContainer;

public interface IValFactory {
	/**
	 * @param str
	 * @param type
	 * @param format optional - some types can use/need the format to parse correctly
	 * @param referenceDate
	 * @return
	 */
	public TContainer createValSimple(String str, String type, String format, Date referenceDate);

	public String format(TContainer argument, String formate);
}
