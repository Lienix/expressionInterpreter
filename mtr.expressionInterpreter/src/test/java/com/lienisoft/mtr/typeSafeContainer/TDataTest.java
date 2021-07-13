package com.lienisoft.mtr.typeSafeContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.lienisoft.mtr.expressionInterpreter.main.TData;
import com.lienisoft.mtr.expressionInterpreter.main.TDataArg;
import com.lienisoft.mtr.utils.DateHelperMTR;

public class TDataTest {

	@Test
	public void testAccess() {

		/**
		 * default TVal is ValStruct:
		 */
		final TData tv = new TData();

		/**
		 * fill with some data:
		 */
		tv.put("Address.Street", "Station Street");
		tv.put("Address.HouseNr", 127);
		tv.put("Address.Zip", 63477);
		tv.put("Address.Town", "Little Creek Town");
		tv.put("IsValid", true);
		tv.put("MyList.0", "Banana");
		tv.put("MyList.1", "Apple");
		tv.put("MyList.3", 17);
		final Date testDate = DateHelperMTR.createDate("22.12.2020 11:01:17");
		tv.put("MyList.4", testDate);

		// final TData tAddress = (TData) tv.getContainer("Address");
		// final TData tMyList = (TData) tv.getContainer("MyList");

		/**
		 * access data "type safe container way"
		 */
		assertEquals(Integer.valueOf(127), tv.get("Address.HouseNr", Integer.class));
		assertEquals("Station Street", tv.get("Address.Street", String.class));
		assertEquals(Integer.valueOf(63477), tv.get("Address.Zip", Integer.class));
		assertEquals("Little Creek Town", tv.get("Address.Town", String.class));
		assertTrue(tv.get("IsValid", Boolean.class));
		assertEquals("Banana", tv.get("MyList.0", String.class));
		assertEquals("Apple", tv.get("MyList.1", String.class));
		assertNull(tv.get("MyList.2"));
		assertEquals(Integer.valueOf(17), tv.get("MyList.3", Integer.class));
		assertEquals(testDate, tv.get("MyList.4", Date.class));

		/**
		 * Access Sub Structures
		 */
		// assertEquals(Integer.valueOf(127), tAddress.get("HouseNr", Integer.class));
		// assertEquals("Station Street", tAddress.get("Street", String.class));
		// assertEquals(Integer.valueOf(63477), tAddress.get("Zip", Integer.class));
		// assertEquals("Little Creek Town", tAddress.get("Town", String.class));
		// assertEquals("Banana", tMyList.get(0, String.class));
		// assertEquals("Apple", tMyList.get(1, String.class));
		// assertNull(tMyList.getObj(2));
		// assertEquals(Integer.valueOf(17), tMyList.get(3, Integer.class));
		// assertEquals(testDate, tMyList.get(4, Date.class));
	}

	/**
	 * Test access by following Key Types:
	 * <ul>
	 * <li>String
	 * <li>String with wild card %
	 * </ul>
	 */
	@Test
	public void testKeyTypes() {

		/**
		 * default TVal is ValStruct:
		 */
		final TData tv = new TData();

		/**
		 * fill with some data:
		 */
		tv.put("Address.Street", "Station Street");
		tv.put("Address.HouseNr", 127);
		tv.put("Address.Zip", 63477);
		tv.put("Address.Town", "Little Creek Town");
		tv.put("IsValid", true);

		/**
		 * test: Key, KeyStruct, String and usage of wild card (%)
		 */
		assertEquals("Station Street", tv.get("Address.Street", String.class));
		assertEquals("Station Street", tv.get("%.Street", String.class));
		assertTrue(tv.get("IsValid", Boolean.class));
	}

	/**
	 * Copy a Structure
	 * <ul>
	 * <li>if Value in Copy changes, Value in Original must remain unchanged!
	 * <li>if Value in Original changes, Value in Copy must remain unchanged!
	 * </ul>
	 */
	@Test
	public void testStructureCopy() {

		{ // TEST with shallow structure (only 1 level)
			/**
			 * default TData is ValStruct:
			 */
			final TDataArg tv = new TDataArg();

			/**
			 * fill with some data:
			 */
			tv.put("Address.Street", "Station Street");
			tv.put("Address.HouseNr", 127);
			tv.put("Address.Zip", 63477);
			tv.put("Address.Town", "Little Creek Town");
			tv.put("IsValid", true);
			tv.put("MyList.0", "Banana");
			tv.put("MyList.1", "Apple");
			tv.put("MyList.3", 17);
			final Date testDate = DateHelperMTR.createDate("22.12.2020 11:01:17");
			tv.put("MyList.4", testDate);

			assertEquals(Integer.valueOf(127), tv.get("Address.HouseNr", Integer.class));
			assertEquals("Station Street", tv.get("Address.Street", String.class));
			assertEquals(Integer.valueOf(63477), tv.get("Address.Zip", Integer.class));
			assertEquals("Little Creek Town", tv.get("Address.Town", String.class));
			assertTrue(tv.get("IsValid", Boolean.class));
			assertEquals("Banana", tv.get("MyList.0", String.class));
			assertEquals("Apple", tv.get("MyList.1", String.class));
			assertNull(tv.get("MyList.2"));
			assertEquals(Integer.valueOf(17), tv.get("MyList.3", Integer.class));
			assertEquals(testDate, tv.get("MyList.4", Date.class));

			final TDataArg tvCopy = tv.create(tv.getCopy());

			assertEquals(Integer.valueOf(127), tvCopy.get("Address.HouseNr", Integer.class));
			assertEquals("Station Street", tvCopy.get("Address.Street", String.class));
			assertEquals(Integer.valueOf(63477), tvCopy.get("Address.Zip", Integer.class));
			assertEquals("Little Creek Town", tvCopy.get("Address.Town", String.class));
			assertTrue(tvCopy.get("IsValid", Boolean.class));
			assertEquals("Banana", tvCopy.get("MyList.0", String.class));
			assertEquals("Apple", tvCopy.get("MyList.1", String.class));
			assertNull(tvCopy.get("MyList.2"));
			assertEquals(Integer.valueOf(17), tvCopy.get("MyList.3", Integer.class));
			assertEquals(testDate, tvCopy.get("MyList.4", Date.class));

			/**
			 * HouesNr changes in Copy but not in Original:
			 */
			tvCopy.put("Address.HouseNr", 128);
			assertEquals(Integer.valueOf(127), tv.get("Address.HouseNr", Integer.class));
			assertEquals(Integer.valueOf(128), tvCopy.get("Address.HouseNr", Integer.class));

			/**
			 * Street changes in Original but not in Copy:
			 */
			tv.put("Address.Street", "Dump Street");
			assertEquals("Dump Street", tv.get("Address.Street", String.class));
			assertEquals("Station Street", tvCopy.get("Address.Street", String.class));

			/**
			 * Delete Value from Copy
			 */
			tvCopy.remove("Address.HouseNr");
			assertEquals(Integer.valueOf(127), tv.get("Address.HouseNr", Integer.class));
			assertEquals(null, tvCopy.get("Address.HouseNr", Integer.class));
			assertNull(tvCopy.get("Address.HouseNr"));

			/**
			 * Add Value to Original
			 */
			tv.put("Address.Added", "TEST");
			assertEquals("TEST", tv.get("Address.Added", String.class));
			assertEquals(null, tvCopy.get("Address.Added", String.class));

			/**
			 * change value in Original List
			 */
			tv.put("MyList.0", "Potato");
			assertEquals("Potato", tv.get("MyList.0", String.class));
			assertEquals("Banana", tvCopy.get("MyList.0", String.class));

			/**
			 * Delete value from Copy List
			 */
			tvCopy.remove("MyList.3");
			assertEquals(Integer.valueOf(17), tv.get("MyList.3", Integer.class));
			// former index 4 is now index 3(!):
			assertEquals(testDate, tvCopy.get("MyList.3", Date.class));

			tvCopy.remove("MyList.3");
			assertEquals(Integer.valueOf(17), tv.get("MyList.3", Integer.class));
			assertEquals(null, tvCopy.get("MyList.3", Date.class));
		}

		{ // TEST with deeper structure (more than 1 level)
			/**
			 * default TData is ValStruct:
			 */
			final TDataArg tv = new TDataArg();

			/**
			 * fill with some data:
			 */
			tv.put("M.Address.Street", "Station Street");
			tv.put("M.Address.HouseNr", 127);
			tv.put("M.Address.Zip", 63477);
			tv.put("M.Address.Town", "Little Creek Town");
			tv.put("M.IsValid", true);
			tv.put("M.MyList.0", "Banana");
			tv.put("M.MyList.1", "Apple");
			tv.put("M.MyList.3", 17);
			final Date testDate = DateHelperMTR.createDate("22.12.2020 11:01:17");
			tv.put("M.MyList.4", testDate);

			assertEquals(Integer.valueOf(127), tv.get("M.Address.HouseNr", Integer.class));
			assertEquals("Station Street", tv.get("M.Address.Street", String.class));
			assertEquals(Integer.valueOf(63477), tv.get("M.Address.Zip", Integer.class));
			assertEquals("Little Creek Town", tv.get("M.Address.Town", String.class));
			assertTrue(tv.get("M.IsValid", Boolean.class));
			assertEquals("Banana", tv.get("M.MyList.0", String.class));
			assertEquals("Apple", tv.get("M.MyList.1", String.class));
			assertNull(tv.get("M.MyList.2"));
			assertEquals(Integer.valueOf(17), tv.get("M.MyList.3", Integer.class));
			assertEquals(testDate, tv.get("M.MyList.4", Date.class));

			final TDataArg tvCopy = tv.create(tv.getCopy());

			assertEquals(Integer.valueOf(127), tvCopy.get("M.Address.HouseNr", Integer.class));
			assertEquals("Station Street", tvCopy.get("M.Address.Street", String.class));
			assertEquals(Integer.valueOf(63477), tvCopy.get("M.Address.Zip", Integer.class));
			assertEquals("Little Creek Town", tvCopy.get("M.Address.Town", String.class));
			assertTrue(tvCopy.get("M.IsValid", Boolean.class));
			assertEquals("Banana", tvCopy.get("M.MyList.0", String.class));
			assertEquals("Apple", tvCopy.get("M.MyList.1", String.class));
			assertNull(tvCopy.get("M.MyList.2"));
			assertEquals(Integer.valueOf(17), tvCopy.get("M.MyList.3", Integer.class));
			assertEquals(testDate, tvCopy.get("M.MyList.4", Date.class));

			/**
			 * HouesNr changes in Copy but not in Original:
			 */
			tvCopy.put("M.Address.HouseNr", 128);
			assertEquals(Integer.valueOf(127), tv.get("M.Address.HouseNr", Integer.class));
			assertEquals(Integer.valueOf(128), tvCopy.get("M.Address.HouseNr", Integer.class));

			/**
			 * Street changes in Original but not in Copy:
			 */
			tv.put("M.Address.Street", "Dump Street");
			assertEquals("Dump Street", tv.get("M.%.Street", String.class));
			assertEquals("Station Street", tvCopy.get("M.%.Street", String.class));

			/**
			 * Delete Value from Copy
			 */
			tvCopy.remove("M.Address.HouseNr");
			assertEquals(Integer.valueOf(127), tv.get("M.Address.HouseNr", Integer.class));
			assertEquals(null, tvCopy.get("M.Address.HouseNr", Integer.class));
			assertNull(tvCopy.get("M.Address.HouseNr"));

			/**
			 * Add Value to Original
			 */
			tv.put("M.Address.Added", "TEST");
			assertEquals("TEST", tv.get("M.Address.Added", String.class));
			assertEquals(null, tvCopy.get("M.Address.Added", String.class));

			/**
			 * change value in Original List
			 */
			tv.put("M.MyList.0", "Potato");
			assertEquals("Potato", tv.get("%.MyList.0", String.class));
			assertEquals("Banana", tvCopy.get("%.MyList.0", String.class));

			/**
			 * Delete value from Copy List
			 */
			tvCopy.remove("%.MyList.3");
			assertEquals(Integer.valueOf(17), tv.get("M.MyList.3", Integer.class));
			// former index 4 is now index 3(!):
			assertEquals(testDate, tvCopy.get("M.MyList.3", Date.class));

			tvCopy.remove("M.MyList.3");
			assertEquals(Integer.valueOf(17), tv.get("M.MyList.3", Integer.class));
			assertEquals(null, tvCopy.get("M.MyList.3", Date.class));
		}
	}

	/**
	 * Test Val.isValueEqual
	 */

	// @Test
	// public void testSpecialIsValueEqualImplementation() {

	/**
	 * not relevant here!
	 */

	// final TData tv = new TData();
	// tv.putVal("M.Flight.DepStation", new ValStation("FRA", "FRA", "EDDF", 19));
	// tv.putVal("M.Flight.ArrStation", new ValStation("EDDH", "HAM", "EDDH", 21));
	//
	// tv.put("DepIATA", "FRA");
	// tv.put("ArrIATA", "HAM");
	// tv.put("DepICAO", "EDDF");
	// tv.put("ArrICAO", "EDDH");
	// tv.put("DepID", 19);
	// tv.put("ArrID", 21);
	//
	// // ValStation equals ValString IATA Code:
	// assertTrue(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("DepIATA")));
	// assertTrue(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("ArrIATA")));
	// // KO Test
	// assertFalse(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("DepIATA")));
	// assertFalse(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("ArrIATA")));
	// assertFalse(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("Unknown")));
	// assertFalse(tv.getContainer("M.Flight.Unknown").equals(tv.getContainer("ArrIATA")));
	// // null = null Test
	// assertTrue(tv.getContainer("M.Flight.Unknown").equals(tv.getContainer("Unknown")));
	//
	// // ValStation equals ValString ICAO Code:
	// assertTrue(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("DepICAO")));
	// assertTrue(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("ArrICAO")));
	// // KO test
	// assertFalse(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("DepICAO")));
	// assertFalse(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("ArrICAO")));
	//
	// // ValStation equals ValInteger ID Code:
	// assertTrue(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("DepID")));
	// assertTrue(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("ArrID")));
	// // KO test
	// assertFalse(tv.getContainer("M.Flight.ArrStation").equals(tv.getContainer("DepID")));
	// assertFalse(tv.getContainer("M.Flight.DepStation").equals(tv.getContainer("ArrID")));
	// }

}
