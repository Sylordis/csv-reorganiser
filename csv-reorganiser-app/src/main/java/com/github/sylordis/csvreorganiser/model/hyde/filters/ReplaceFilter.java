package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Performs a regular expression replacement on the source string.<br/>
 * <br/>
 * Only the first argument is mandatory. If the second is not provided, the replacement will be
 * considered as an empty string, e.g. to remove the pattern.
 */
@Operation(name = "replace")
@OperationProperty(name = "pattern", field = "pattern", position = 0, required = true)
@OperationProperty(name = "replace", field = "replacement", position = 1)
public class ReplaceFilter extends HydeAbstractFilter {

	/**
	 * Pattern to look for in the content as defined by <a
	 * href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html>Java
	 * Pattern</a>.
	 */
	private String pattern;
	/**
	 * Replacement to replace the identified patterns in the source string. Use `$X` to replace by
	 * capture group number X. If not specified, will be considered as empty string.
	 */
	private String replacement = "";

	@Override
	public String apply(String t) {
		return t.replaceAll(pattern, replacement);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + pattern + " => " + replacement + "]";
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
