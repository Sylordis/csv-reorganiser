package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Performs a regular expression replacement on the source string.
 */
@ReorgOperation(name = "replace")
@ReorgOperationProperty(name = "pattern", field = "pattern", position = 0, required = true)
@ReorgOperationProperty(name = "replace", field = "replacement", position = 1, required = true)
public class ReplaceFilter extends HydeAbstractFilter {

	/**
	 * Pattern to look for in the content as defined by <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html>Java Pattern</a>.
	 */
	private String pattern;
	/**
	 * Replacement to replace the identified patterns in the source string. Use `$X` to replace by capture group number X.
	 */
	private String replacement;

	@Override
	public String apply(String t) {
		return t.replaceAll(pattern, replacement);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + pattern + " => "
		        + replacement + "]";
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the replacement
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * @param replacement the replacement to set
	 */
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	
}
