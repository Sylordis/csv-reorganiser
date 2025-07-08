package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Changes all characters to the lower case.
 */
@Operation(name = "lower")
public class LowerFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		return t.toLowerCase();
	}

}
