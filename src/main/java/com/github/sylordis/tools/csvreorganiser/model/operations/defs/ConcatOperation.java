package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * "Concat" takes sources in multiple columns and constants to concatenate them together.
 *
 * @author sylordis
 * @since 1.1
 *
 */
@Operation(name = "Concat")
@OperationProperty(name = "values", field = "values", required = true, description = "List of columns or constants strings to take values from")
@OperationShortcut(keyword = "concat", property = "values")
public class ConcatOperation extends AbstractReorgOperation {

	/**
	 * Required properties for values specification.
	 */
	public static final String OPDATA_FIELD_VALUES = "values";

	/**
	 * Required properties for value specification in shortcut should match
	 * {@link OperationShortcut#keyword()}.
	 */
	public static final String SHORTCUT_KEY = "concat";

	/**
	 * Index of the source column.
	 */
	private List<String> values;

	/**
	 * Constructs a new basic value extraction operation.
	 *
	 * @param name  Name of the column
	 * @param value constant value of the column
	 */
	public ConcatOperation(String name, List<String> values) {
		super(name);
		this.values = new ArrayList<>();
		this.values = values;
	}

	/**
	 * Constructs a bare value extraction operation.
	 *
	 * @param name Name of the column
	 */
	public ConcatOperation(String name) {
		this(name, null);
	}

	@Override
	public String apply(CSVRecord record) {
		if (values == null)
			throw createMissingPropertyException(OPDATA_FIELD_VALUES);
		StringBuilder rame = new StringBuilder();
		for (String v : values) {
			if (!record.isMapped(v))
				rame.append(v);
			else
				rame.append(record.get(v));
		}
		return rame.toString();
	}

	@Override
	public String toString() {
		return String.format("%s [%s, values=%s]", this.getClass().getSimpleName(), getName(), values);
	}

	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

}
