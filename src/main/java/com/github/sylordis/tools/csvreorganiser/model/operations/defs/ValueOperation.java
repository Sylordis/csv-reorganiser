package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * "Value" (type = value, constant) operation returns a constant value.
 *
 * @author sylordis
 *
 */
@Operation(name = "Value")
@OperationProperty(name = "value", field = "value", required = true, description = "Constant value to output")
@OperationShortcut(keyword = "value", property = "value")
public class ValueOperation extends AbstractReorgOperation {

	/**
	 * Required property for value specification.
	 */
	public static final String OPDATA_FIELD_VALUE = "value";

	/**
	 * Required properties for value specification in shortcut should match
	 * {@link OperationShortcut#keyword()}.
	 */
	public static final String SHORTCUT_KEY = OPDATA_FIELD_VALUE;

	/**
	 * Index of the source column.
	 */
	private String value;

	/**
	 * Constructs a new basic value extraction operation.
	 *
	 * @param name  Name of the column
	 * @param value constant value of the column
	 */
	public ValueOperation(String name, String value) {
		super(name);
		this.value = value;
	}

	/**
	 * Constructs a bare value extraction operation.
	 *
	 * @param name Name of the column
	 */
	public ValueOperation(String name) {
		this(name, null);
	}

	@Override
	public String apply(CSVRecord record) {
		if (value == null)
			throw createMissingPropertyException(SHORTCUT_KEY);
		return value;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getName() + ", value=" + value + "]";
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
