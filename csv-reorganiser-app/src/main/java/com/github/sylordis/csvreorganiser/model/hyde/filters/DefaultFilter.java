package com.github.sylordis.csvreorganiser.model.hyde.filters;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

/**
 * Outputs a new default value if the previous output is null or empty.
 */
@Operation(name = "default")
@OperationProperty(name = "value", position = 0, field = "defaultValue", required = true)
public class DefaultFilter extends HydeAbstractFilter {

	/**
	 * Default value to provide.
	 */
	private String defaultValue;

	@Override
	public String apply(String t) {
		String ret = t;
		if (t == null || t.isBlank())
			ret = defaultValue;
		return ret;
	}

	/**
	 * @return the default value
	 */
	protected String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param value the default value to set
	 */
	protected void setDefaultValue(String value) {
		this.defaultValue = value;
	}

}
