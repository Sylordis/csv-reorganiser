package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Extracts a portion of the source string.
 */
@Operation(name = "substring")
@OperationProperty(name = "start", field = "startIndex", position = 0, required = true)
@OperationProperty(name = "end", field = "endIndex", position = 1)
public class SubstringFilter extends HydeAbstractFilter {

	/**
	 * Default end index value if not set.
	 */
	public static final int DEFAULT_END_INDEX = -1;
	/**
	 * Start index of the substring.
	 */
	private int startIndex;
	/**
	 * End index of the substring (excluded) or until the end of the string if not specified.
	 */
	private int endIndex = DEFAULT_END_INDEX;

	@Override
	public String apply(String t) {
		String res = t;
		if (endIndex != DEFAULT_END_INDEX)
			res = t.substring(startIndex, endIndex);
		else
			res = t.substring(startIndex);
		return res;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + startIndex + " => "
		        + (endIndex == DEFAULT_END_INDEX ? "" : endIndex) + "]";
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

}
