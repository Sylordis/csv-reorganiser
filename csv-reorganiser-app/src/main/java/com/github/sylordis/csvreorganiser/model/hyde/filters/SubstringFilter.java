package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

@ReorgOperation(name = "substring")
@ReorgOperationProperty(name = "start", field = "startIndex", position = 0, required = true)
@ReorgOperationProperty(name = "end", field = "endIndex", position = 1)
public class SubstringFilter extends HydeAbstractFilter {

	/**
	 * Start index of the substring.
	 */
	private int startIndex;
	/**
	 * End index of the substring (excluded) or until the end of the string if not specified.
	 */
	private int endIndex = -1;

	@Override
	public String apply(String t) {
		String res = t;
		if (endIndex != -1)
			res = t.substring(startIndex, endIndex);
		else
			res = t.substring(startIndex);
		return res;
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
