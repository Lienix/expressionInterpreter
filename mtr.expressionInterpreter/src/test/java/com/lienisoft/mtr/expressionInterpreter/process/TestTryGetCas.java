package com.lienisoft.mtr.expressionInterpreter.process;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestTryGetCas {

	@Test
	public void test() {

		{
			final Object al = new ArrayList<>();
			assertTrue(List.class.isAssignableFrom(al.getClass()));
			assertTrue(al instanceof List);
		}
		{
			final Object al = new Integer(123);
			assertFalse(List.class.isAssignableFrom(al.getClass()));
			assertFalse(al instanceof List);
		}
	}

}
