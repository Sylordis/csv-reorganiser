package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Capitalises the first character of the string.
 */
@Operation(name = "capfirst")
public class CapFirstFilter extends HydeAbstractFilter {

	@Override
	public String apply(String t) {
		String ret = t;
		if (t.length() > 0)
			ret = t.substring(0,1).toUpperCase() + t.substring(1);
		return ret;
	}

}
