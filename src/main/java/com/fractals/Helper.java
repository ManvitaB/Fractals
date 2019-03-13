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
	
	public static int parseInt(int min, int max, String toParse) throws NumberFormatException
	{
		final String msg = "Int parsed was outside accepted range [" + min + ", " + max + "].";
		int value = Integer.parseInt(toParse);
		if(value < min) throw new NumberFormatException(msg);
		else if(value > max) throw new NumberFormatException(msg);
		return value;
	}
	
	public static long parseLong(long min, long max, String toParse) throws NumberFormatException
	{
		final String msg = "Long parsed was outside accepted range [" + min + ", " + max + "].";
		long value = Long.parseLong(toParse);
		if(value < min) throw new NumberFormatException(msg);
		else if(value > max) throw new NumberFormatException(msg);
		return value;
	}
	
	public static double parseDouble(double min, double max, String toParse) throws Exception
	{
		final String msg = "Double parsed was outside accepted range [" + min + ", " + max + "].";
		double value = Double.parseDouble(toParse);
		if(value < min) throw new NumberFormatException(msg);
		else if(value > max) throw new NumberFormatException(msg);
		return value;
	}
	
	public static int parseIntParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		return parseIntParam(Integer.MIN_VALUE, Integer.MAX_VALUE, paramName, toParse, parseFailedMessage);
	}
	
	public static int parseNonNegativeIntParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		return parseIntParam(0, Integer.MAX_VALUE, paramName, toParse, parseFailedMessage);
	}
	
	public static int parseIntParam(int min, int max, String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		try 
		{
			int value = parseInt(min, max, toParse);
			return value;
		} 
		catch (NumberFormatException e) 
		{
			parseFailedMessage.value = makeParseFailedMessage_int(min, max, paramName);
			throw e;
		}
	}
	
	public static long parseLongParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		return parseLongParam(Long.MIN_VALUE, Long.MAX_VALUE, paramName, toParse, parseFailedMessage);
	}
	
	public static long parseLongParam(long min, long max, String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws NumberFormatException
	{
		try 
		{
			long value = parseLong(min, max, toParse);
			return value;
		}
		catch (NumberFormatException e)
		{
			parseFailedMessage.value = makeParseFailedMessage_long(min, max, paramName);
			throw e;
		}
	}
	
	public static double parseDoubleParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws Exception
	{
		return parseDoubleParam(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, paramName, toParse, parseFailedMessage);
	}
	
	public static double parseNonNegativeDoubleParam(String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws Exception
	{
		return parseDoubleParam(0.0, Double.POSITIVE_INFINITY, paramName, toParse, parseFailedMessage);
	}

	public static double parseDoubleParam(double min, double max, String paramName, @NonNull String toParse, 
			@NonNull Wrapper<String> parseFailedMessage) throws Exception
	{
		try 
		{
			double value = parseDouble(min, max, toParse);
			return value;
		}
		catch (Exception e)
		{
			parseFailedMessage.value = makeParseFailedMessage_double(min, max, paramName);
			throw e;
		}
	}
	
	public static String makeParseFailedMessage_nonNegativeInt(String paramName)
	{
		return makeParseFailedMessage_int(0, Integer.MAX_VALUE, paramName);
	}
	
	public static String makeParseFailedMessage_int(String paramName)
	{
		return makeParseFailedMessage_int(Integer.MIN_VALUE, Integer.MAX_VALUE, paramName);
	}
	
	public static String makeParseFailedMessage_int(int min, int max, String paramName)
	{
		return makeParseFailedMessage(min, max, paramName, "integer");
	}
	
	public static String makeParseFailedMessage_long(String paramName)
	{
		return makeParseFailedMessage_long(Long.MIN_VALUE, Long.MAX_VALUE, paramName);
	}
	
	public static String makeParseFailedMessage_long(long min, long max, String paramName)
	{
		return makeParseFailedMessage(min, max, paramName, "integer (long supported)");
	}
	
	public static String makeParseFailedMessage_double(String paramName)
	{
		return makeParseFailedMessage_double(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, paramName);
	}
	
	public static String makeParseFailedMessage_double(double min, double max, String paramName)
	{
		return makeParseFailedMessage(min, max, paramName, "real number (double precision)");
	}
	
	public static String makeParseFailedMessage(Object min, Object max, String paramName, String paramType)
	{
		return "Failed to parse " + paramName + " parameter: please input "
			     + paramName + " as a " + paramType + " in range [" + min + ", " + max + "].";
	}
}
