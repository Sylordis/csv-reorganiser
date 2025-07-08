package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Outputs the length of the provided string.
 */
@Operation(name = "length")
public class LengthFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		return Integer.toString(t.length());
	}

}
