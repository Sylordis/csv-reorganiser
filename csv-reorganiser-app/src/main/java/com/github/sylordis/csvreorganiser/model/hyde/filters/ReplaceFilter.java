package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * <em>Replace</em> filter takes the content of a column from the source file and performs a regular
 * expression replacement. This regular expression must be have a Java regex syntax based on Pattern
 * Java class. The base string will be returned if the pattern cannot be found.
 *
 * @author sylordis
 *
 */
@ReorgOperation(name = "replace")
@ReorgOperationProperty(name = "pattern", field = "pattern", position = 0, required = true)
@ReorgOperationProperty(name = "replacement", field = "replacement", position = 1)
public class ReplaceFilter extends HydeAbstractFilter {

	/**
	 * Pattern to find and replace.
	 */
	private String pattern;
	/**
	 * Replacement string to replace the patterns with. Use "$X" to replace the X-th captured group.
	 */
	private String replacement = "";

	@Override
	public String apply(String t) {
		return t.replaceAll(pattern, replacement);
	}

	/**
	 * @return the pattern
	 */
	protected String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	protected void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the replacement
	 */
	protected String getReplacement() {
		return replacement;
	}

	/**
	 * @param replacement the replacement to set
	 */
	protected void setReplacement(String replacement) {
		this.replacement = replacement;
	}

}
