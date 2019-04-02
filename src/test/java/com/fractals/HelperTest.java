package com.fractals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * HelperTest --- Contains tests for Helper class static methods.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   03/12/2019
 */
public class HelperTest 
{
	@Test
	public void getFilenameFromPathTest()
	{
		//1. No change for path without directories
		final String filename = "test.xyz";
		assertEquals(filename, Helper.getFilenameFromPath(filename));
		
		//2. Extract filename from path with multiple directories
		assertEquals(filename, Helper.getFilenameFromPath("/static/images/" + filename));
		
		//3. Remove query string from path without directories
		assertEquals(filename, Helper.getFilenameFromPath(filename + "?  112233"));
		
		//4. Full: extract filename and remove query string from path with multiple directories
		assertEquals(filename, Helper.getFilenameFromPath("temp/test/" + filename + "?  112233"));		
	}
	
	@Test
	public void getDirectoriesFromPathTest()
	{
		//1. Return "" for path with no directories
		final String filename = "noDirs.txt";
		assertEquals("", Helper.getDirectoriesFromPath(filename));
		
		//2. No change for path without filename
		final String dirs = "static/images/";
		assertEquals(dirs, Helper.getDirectoriesFromPath(dirs));
		
		//3. Full: extract directories from path with filename
		assertEquals(dirs, Helper.getDirectoriesFromPath(dirs + filename));
	}
	
	@Test
	public void parseIntTest()
	{
		//1. Throws exception for invalid type (null):
		boolean exceptionThrown = false;
		try {
			Helper.parseInt(Integer.MIN_VALUE, Integer.MAX_VALUE, null);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//2. Throws exception for invalid type (not an int):
		exceptionThrown = false;
		try {
			Helper.parseInt(Integer.MIN_VALUE, Integer.MAX_VALUE, "not an int");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//3. Out-of-range, below: Throws exception for invalid input below min (int, but negative):
		exceptionThrown = false;
		try {
			Helper.parseInt(0, Integer.MAX_VALUE, "-46");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//4. Out-of-range, above: Throws exception for invalid input above max (int, but positive):
		exceptionThrown = false;
		try {
			Helper.parseInt(Integer.MIN_VALUE, 0, "46");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//5. Does not throw exception for valid input within inclusive range
		exceptionThrown = false;
		int parsed = -1;
		try {
			parsed = Helper.parseInt(274, 274, "274");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(274, parsed);
	}
	
	@Test
	public void parseLongTest()
	{
		//1. Throws exception for invalid type (null):
		boolean exceptionThrown = false;
		try {
			Helper.parseLong(Long.MIN_VALUE, Long.MAX_VALUE, null);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//2. Throws exception for invalid type (not a long):
		exceptionThrown = false;
		try {
			Helper.parseLong(Long.MIN_VALUE, Long.MAX_VALUE, "not a long");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//3. Out-of-range, below: Throws exception for invalid input below min (long, but negative):
		exceptionThrown = false;
		try {
			Helper.parseLong(0L, Long.MAX_VALUE, "-46");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//4. Out-of-range, above: Throws exception for invalid input above max (long, but positive):
		exceptionThrown = false;
		try {
			Helper.parseLong(Long.MIN_VALUE, 0L, "46");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//5. Does not throw exception for valid input within inclusive range
		exceptionThrown = false;
		Long expected = 274L;
		Long parsed = -1L;
		try {
			parsed = Helper.parseLong(expected, expected, String.valueOf(expected));
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(expected, parsed);
	}
	
	@Test
	public void parseDoubleTest()
	{
		//1. Throws exception for invalid type (null):
		boolean exceptionThrown = false;
		try {
			Helper.parseDouble(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, null);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//2. Throws exception for invalid type (not a double):
		exceptionThrown = false;
		try {
			Helper.parseDouble(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "not a double");
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//3. Out-of-range, below: Throws exception for invalid input below min (double, but negative):
		exceptionThrown = false;
		try {
			Helper.parseDouble(0.0, Double.POSITIVE_INFINITY, "-46.2");
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//4. Out-of-range, above: Throws exception for invalid input above max (double, but positive):
		exceptionThrown = false;
		try {
			Helper.parseDouble(Double.NEGATIVE_INFINITY, 0.0, "46.2");
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//5. Does not throw exception for valid input within inclusive range
		exceptionThrown = false;
		double expected = 274.456;
		double parsed = -1.0;
		try {
			parsed = Helper.parseDouble(expected, expected, String.valueOf(expected));
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(expected, parsed, 0.0);
	}
	
	@Test
	public void parseIntParamTest()
	{	
		// 1. Throws exception for invalid type (not an int):
		final String paramName = "p";
		boolean exceptionThrown = false;
		Helper.Wrapper<String> parseFailedMsg = new Helper.Wrapper<String>("");
		try {
			Helper.parseIntParam(paramName, "not an int", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_int(paramName), parseFailedMsg.value);

		// 2. Out-of-range, below: Throws exception for invalid input below min (int, but negative):
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseNonNegativeIntParam("p", "-46", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_nonNegativeInt(paramName), parseFailedMsg.value);
		
		// 3. Out-of-range, above: Throws exception for invalid int input above max
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseIntParam(Integer.MIN_VALUE, 0, "p", "46", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_int(Integer.MIN_VALUE, 0, paramName), parseFailedMsg.value);

		// 4. Does not throw exception for valid input within inclusive range
		exceptionThrown = false;
		parseFailedMsg.value = "";
		int parsed = -1;
		try {
			parsed = Helper.parseIntParam(274, 274, paramName, "274", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(274, parsed);
		assertEquals("", parseFailedMsg.value); //assert that parseFailedMsg is unchanged
	}
	
	@Test
	public void parseLongParamTest()
	{
		// 1. Throws exception for invalid type (not a long):
		final String paramName = "p";
		boolean exceptionThrown = false;
		Helper.Wrapper<String> parseFailedMsg = new Helper.Wrapper<String>("");
		try {
			Helper.parseLongParam("p", "not a long", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_long(paramName), parseFailedMsg.value);
		
		// 2. Out-of-range, below: Throws exception for invalid long input below min
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseLongParam(0L, Long.MAX_VALUE, "p", "-46", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_long(0, Long.MAX_VALUE, paramName), parseFailedMsg.value);

		// 3. Out-of-range, above: Throws exception for invalid long input above max
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseLongParam(Long.MIN_VALUE, 0L, "p", "46", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_long(Long.MIN_VALUE, 0, paramName), parseFailedMsg.value);

		// 4. Does not throw exception for valid input (long):
		exceptionThrown = false;
		parseFailedMsg.value = "";
		double parsed = -1;
		try {
			parsed = Helper.parseLongParam(-274L, -274L, paramName, "-274", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(-274L, parsed, 0.0); 	// assert exact match
		assertEquals("", parseFailedMsg.value); // assert that parseFailedMsg is unchanged
	}
	
	@Test
	public void parseDoubleParamTest()
	{
		// 1. Throws exception for invalid type (not a double):
		final String paramName = "p";
		boolean exceptionThrown = false;
		Helper.Wrapper<String> parseFailedMsg = new Helper.Wrapper<String>("");
		try {
			Helper.parseDoubleParam(paramName, "not a double", parseFailedMsg);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_double(paramName), parseFailedMsg.value);
		
		// 2. Out-of-range, below: Throws exception for invalid double input below min
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseDoubleParam(0.0, Double.POSITIVE_INFINITY, paramName, "-46.2", parseFailedMsg);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_double(0.0, Double.POSITIVE_INFINITY, paramName), parseFailedMsg.value);

		// 3. Out-of-range, above: Throws exception for invalid long input above max
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseDoubleParam(Double.NEGATIVE_INFINITY, 0.0, paramName, "46.2", parseFailedMsg);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_double(Double.NEGATIVE_INFINITY, 0.0, paramName), parseFailedMsg.value);

		// 4. Does not throw exception for valid double input within inclusive range:
		exceptionThrown = false;
		parseFailedMsg.value = "";
		double expected = -274.456;
		double parsed = -1;
		try {
			parsed = Helper.parseDoubleParam(expected, expected, paramName, String.valueOf(expected), parseFailedMsg);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(expected, parsed, 0.0); 	// assert exact match
		assertEquals("", parseFailedMsg.value); // assert that parseFailedMsg is unchanged
	}
}
