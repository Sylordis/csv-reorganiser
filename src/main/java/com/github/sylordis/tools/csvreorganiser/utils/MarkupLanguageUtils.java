package com.github.sylordis.tools.csvreorganiser.utils;

import org.apache.logging.log4j.util.Strings;

/**
 * Util tool for markup languages.
 * 
 * @author sylordis
 *
 */
public final class MarkupLanguageUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private MarkupLanguageUtils() {
		// Nothing to do here
	}
	
	/**
	 * Splits a camel case string to a spaced case string by splitting the words before uppercase characters. Case will be maintained.
	 * @param s String to transform from camel case to normal case
	 * @return
	 */
	public static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}

	/**
	 * Transforms html syntax into markdown syntax.
	 * 
	 * @param s text to transform
	 * @return
	 */
	public static String htmlToMarkdown(String s) {
		String md = s;
		// Emphasis
		md = md.replaceAll("<em>([^<]*)</em>", "*$1*");
		// Lists
		md = md.replaceAll("</?ul>\r?\n", "").replaceAll("<li>(.*)</li>",
		        "* $1");
		// Headers
		for (int i = 1; i < 6; i++) {
			md = md.replaceAll("<h"+i+">(.*)</h"+i+">", Strings.repeat("#", i)+" $1");
		}
		return md;
	}

}
