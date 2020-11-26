package com.sylordis.tools.csv.reorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * "Value" (type = value, constant) operation returns a constant value.
 *
 * Required properties:
 * <dl>
 * <dt>value</dt>
 * <dd>Constant value to output</dd>
 * </dl>
 *
 * @author sylordis
 * @since 0.1
 *
 */
public class ValueOperation extends AbstractReorgOperation {

	/**
	 * Required properties for value specification.
	 */
	public static final String OPDATA_VALUE = "value";
	/**
	 * Fixed configuration for requirement properties names.
	 */
	public static final String[] DATA_REQUIRED_PROPS = { OPDATA_VALUE };

	/**
	 * Index of the source column.
	 */
	private String value;

	@Override
	protected void setup() {
		addProperty(OPDATA_VALUE, OPDATA_VALUE);
	}

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
			throw createMissingPropertyException(OPDATA_VALUE);
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
