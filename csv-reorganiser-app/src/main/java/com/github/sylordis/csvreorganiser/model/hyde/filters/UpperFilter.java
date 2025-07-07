package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Changes all characters to the upper case.
 */
@ReorgOperation(name = "upper")
public class UpperFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		return t.toUpperCase();
	}

}
