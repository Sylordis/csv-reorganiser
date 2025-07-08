package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Truncates the provided string if it is strictly longer than a certain length.
 */
@Operation(name = "truncate")
@OperationProperty(name = "length", position = 0, field = "length", required = true)
@OperationProperty(name = "ellipsis", position = 1, field = "ellipsis")
public class TruncateFilter extends HydeAbstractFilter {

	/**
	 * Length to which to truncate the provided entry.
	 */
	private int length;
	/**
	 * Ellipsis to put when truncated.
	 */
	private String ellipsis = ConfigConstants.DEFAULT_ELLIPSIS;

	@Override
	public String apply(String t) {
		String ret = t;
		if (t.length() > length)
			ret = t.substring(0, length) + ellipsis;
		return ret;
	}

	/**
	 * @return the length
	 */
	protected int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	protected void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the ellipsis
	 */
	protected String getEllipsis() {
		return ellipsis;
	}

	/**
	 * @param ellipsis the ellipsis to set
	 */
	protected void setEllipsis(String ellipsis) {
		this.ellipsis = ellipsis;
	}

}
