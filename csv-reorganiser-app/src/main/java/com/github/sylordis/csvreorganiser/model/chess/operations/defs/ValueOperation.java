package com.github.sylordis.csvreorganiser.model.chess.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * <em>Value</em> operation outputs a static value. This operation does not need any content from
 * source file in operation to output its content.
 *
 * @author sylordis
 *
 */
@ReorgOperation(name = "value")
@ReorgOperationProperty(name = "value", field = "value", required = true, description = "Constant value to output")
@ReorgOperationShortcut(keyword = "value", property = "value")
public class ValueOperation extends ChessAbstractReorgOperation {

	/**
	 * Required property for value specification.
	 */
	public static final String OPDATA_FIELD_VALUE = "value";

	/**
	 * Required properties for value specification in shortcut should match
	 * {@link ReorgOperationShortcut#keyword()}.
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
