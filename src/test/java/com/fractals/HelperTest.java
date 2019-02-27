package com.fractals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * HelperTest --- Contains tests for Helper class static methods.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   02/26/2019
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
}
