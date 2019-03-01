package com.fractals;

import org.springframework.lang.NonNull;

/**
 * Helper --- Non-instantiable class containing general static helper methods.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   02/28/2019
 */
public class Helper 
{
	/**
	 * Wrapper<T>: Simple class that is a wrapper for another type, which can be 
	 * 			   used to implement pass-by-reference for immutable types such as String.
	 * @param <T>  Class to wrap.
	 */
	public static class Wrapper<T>
	{
		public T value;
		
		public Wrapper() 
		{
			this.value = null;
		}
		
		public Wrapper(T value)
		{
			this.value = value;
		}
	}
	
	private Helper() {}
	
	/**
	 * Extracts a filename from a string path, excluding a query string following the filename if present.
	 * @param path To extract filename from.
	 * @return filename at path.
	 */
	public static String getFilenameFromPath(String path)
	{
		int start = 0;
		int end = path.length();
		char[] chars = path.toCharArray();
		boolean foundQueryChar = false;
		for(int i = end - 1; i >= 0; i--)
		{
			if(chars[i] == '/') {
				start = i + 1;
				break;
			} else if(!foundQueryChar && chars[i] == '?') { 
				// Don't include query part of filename (if present)
				// e.g. exclude "?112334" in "images/fractal-tree.png?112334"
				end = i;
				foundQueryChar = true;
			}
		}
		String filename = path.substring(start, end);
		return filename;
	}
	
	/**
	 * Extracts the full directory list from a string path, excluding the filename (if any).
	 * (e.g. if path="static/images/a.png", return "static/images/")
	 * @param path To extract directories from.
	 * @return directory hierarchy, or "" if no directories in path. 
	 */
	public static String getDirectoriesFromPath(String path)
	{
		//if no "/" is present, want to return "".
		int start = 0;
		int end = 0;
		char[] chars = path.toCharArray();
		for(int i = path.length() - 1; i >= 0; i--)
		{
			if(chars[i] == '/') {
				end = i + 1;
				break;
			}
		}
		String directories = path.substring(start, end);
		return directories;
	}
	
	public static int parseNonNegativeInt(String toParse) throws NumberFormatException
	{
		int value = Integer.parseInt(toParse);
		if(value < 0) throw new NumberFormatException("Was parsed as a negative int (" + value + ").");
		return value;
	}
	
	public static int parseNonNegativeIntParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		try 
		{
			int value = parseNonNegativeInt(toParse);
			return value;
		} 
		catch (NumberFormatException e) 
		{
			parseFailedMessage.value = makeParseFailedMessage_nonNegativeInt(paramName);
			throw e;
		}
	}
	
	public static long parseLongParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		try 
		{
			long value = Long.parseLong(toParse);
			return value;
		}
		catch (NumberFormatException e)
		{
			parseFailedMessage.value = makeParseFailedMessage_long(paramName);
			throw e;
		}
	}

	public static double parseDoubleParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		try 
		{
			double value = Double.parseDouble(toParse);
			return value;
		}
		catch (NumberFormatException e)
		{
			parseFailedMessage.value = makeParseFailedMessage_double(paramName);
			throw e;
		}
	}
	
	public static String makeParseFailedMessage_nonNegativeInt(String paramName)
	{
		return makeParseFailedMessage(paramName, "non-negative integer");
	}
	
	public static String makeParseFailedMessage_long(String paramName)
	{
		return makeParseFailedMessage(paramName, "integer (long supported)");
	}
	
	public static String makeParseFailedMessage_double(String paramName)
	{
		return makeParseFailedMessage(paramName, "real number (double precision)");
	}
	
	public static String makeParseFailedMessage(String paramName, String paramType)
	{
		return "Failed to parse " + paramName + " parameter: please input "
			     + paramName + " as a " + paramType + ".";
	}
}
