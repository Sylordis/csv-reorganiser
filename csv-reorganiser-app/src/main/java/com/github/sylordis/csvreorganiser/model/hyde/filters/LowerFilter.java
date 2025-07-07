package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Changes all characters to the lower case.
 */
@ReorgOperation(name = "lower")
public class LowerFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		return t.toLowerCase();
	}

}
