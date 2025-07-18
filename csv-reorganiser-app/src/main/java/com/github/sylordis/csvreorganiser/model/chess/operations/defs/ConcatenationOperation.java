package com.github.sylordis.csvreorganiser.model.chess.operations.defs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * <em>Concat</em> takes sources in multiple columns and constants to concatenate them together.
 * Every provided value will be first searched for as a column, then taken as constant if this last
 * one doesn't exist.
 *
 * @author sylordis
 *
 */
@Operation(name = "concat")
@OperationProperty(name = "values", field = "values", required = true)
@OperationShortcut(keyword = "concat", property = "values")
public class ConcatenationOperation extends ChessAbstractReorgOperation {

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
	 * List of columns to take values from and/or constants strings.
	 */
	private List<String> values;

	/**
	 * Constructs a new basic value extraction operation.
	 *
	 * @param name  Name of the column
	 * @param value constant value of the column
	 */
	public ConcatenationOperation(String name, List<String> values) {
		super(name);
		this.values = new ArrayList<>();
		this.values = values;
	}

	/**
	 * Constructs a bare value extraction operation.
	 *
	 * @param name Name of the column
	 */
	public ConcatenationOperation(String name) {
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
