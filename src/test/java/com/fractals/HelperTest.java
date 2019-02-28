package com.fractals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * HelperTest --- Contains tests for Helper class static methods.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   02/27/2019
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
	public void parseNonNegativeIntTest()
	{
		//1. Throws exception for invalid type (null):
		boolean exceptionThrown = false;
		try {
			Helper.parseNonNegativeInt(null);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//2. Throws exception for invalid type (not an int):
		exceptionThrown = false;
		try {
			Helper.parseNonNegativeInt("not an int");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//3. Throws exception for invalid input bounds (int, but negative):
		exceptionThrown = false;
		try {
			Helper.parseNonNegativeInt("-46");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		//4. Does not throw exception for valid input (non-negative int):
		exceptionThrown = false;
		int parsed = -1;
		try {
			parsed = Helper.parseNonNegativeInt("274");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(274, parsed);
	}
	
	@Test
	public void parseNonNegativeIntParamTest()
	{
		// 1. Throws exception for invalid type (not an int):
		boolean exceptionThrown = false;
		Helper.Wrapper<String> parseFailedMsg = new Helper.Wrapper<String>("");
		try {
			Helper.parseNonNegativeIntParam("p", "not an int", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_nonNegativeInt("p"), parseFailedMsg.value);

		// 2. Throws exception for invalid input bounds (int, but negative):
		exceptionThrown = false;
		parseFailedMsg.value = "";
		try {
			Helper.parseNonNegativeIntParam("p", "-46", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_nonNegativeInt("p"), parseFailedMsg.value);

		// 3. Does not throw exception for valid input (non-negative int):
		exceptionThrown = false;
		parseFailedMsg.value = "";
		int parsed = -1;
		try {
			parsed = Helper.parseNonNegativeIntParam("p", "274", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(274, parsed);
		assertEquals("", parseFailedMsg.value); //assert that parseFailedMsg is unchanged
	}
	
	@Test
	public void parseDoubleParamTest()
	{
		// 1. Throws exception for invalid type (not an int):
		boolean exceptionThrown = false;
		Helper.Wrapper<String> parseFailedMsg = new Helper.Wrapper<String>("");
		try {
			Helper.parseDoubleParam("p", "not a double", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		assertEquals(Helper.makeParseFailedMessage_double("p"), parseFailedMsg.value);

		// 2. Does not throw exception for valid input (double):
		exceptionThrown = false;
		parseFailedMsg.value = "";
		double parsed = -1;
		try {
			parsed = Helper.parseDoubleParam("p", "-274.456", parseFailedMsg);
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		assertEquals(-274.456, parsed, 0.0); 	// assert exact match
		assertEquals("", parseFailedMsg.value); // assert that parseFailedMsg is unchanged
	}
}
