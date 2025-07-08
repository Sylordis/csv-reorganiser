package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Changes all characters to the upper case.
 */
@Operation(name = "upper")
public class UpperFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		return t.toUpperCase();
	}

}
