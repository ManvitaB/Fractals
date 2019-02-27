package com.fractals;

/**
 * Helper --- Non-instantiable class containing general static helper methods.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   02/26/2019
 */
public class Helper 
{
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
}
