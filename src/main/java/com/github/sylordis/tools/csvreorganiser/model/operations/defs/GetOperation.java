package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * "Get" is the trivial basic operation which consists of getting the value of another column.
 *
 * @author sylordis
 *
 */
@Operation(name = "Get")
@OperationProperty(name = "source", field = "srcColumn", required = true, description = "Name of the column to get the value from")
@OperationShortcut(keyword = "source", property = "source")
public class GetOperation extends AbstractReorgOperation {

	/**
	 * Required properties for source column specification.
	 */
	public static final String OPDATA_FIELD_SOURCE = "srcColumn";

	/**
	 * Required properties for value specification in shortcut should match
	 * {@link OperationShortcut#keyword()}.
	 */
	public static final String SHORTCUT_KEY = "source";

	/**
	 * Index of the source column.
	 */
	private String srcColumn;

	/**
	 * Constructs a fully setup get operation.
	 *
	 * @param name      Name of the column it represents
	 * @param srcColumn Column to take its content from
	 */
	public GetOperation(String name, String srcColumn) {
		super(name);
		this.srcColumn = srcColumn;
	}

	/**
	 * Constructs a bare get operation.
	 *
	 * @param name Name of the column it represents
	 */
	public GetOperation(String name) {
		this(name, null);
	}

	@Override
	public String apply(CSVRecord record) {
		// Check configuration
		if (srcColumn == null)
			throw createMissingPropertyException(SHORTCUT_KEY);
		return record.get(srcColumn);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getName() + ", source=" + srcColumn + "]";
	}

	/**
	 * @return the srcColumn
	 */
	public String getSrcColumn() {
		return srcColumn;
	}

	/**
	 * @param srcColumn the srcColumn to set
	 */
	public void setSrcColumn(String srcColumn) {
		this.srcColumn = srcColumn;
	}

}
