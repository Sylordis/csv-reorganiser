package com.github.sylordis.csvreorganiser.model.chess.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * <em>Get</em> operation simply takes the full content of a column. If the source column does not exist, an error will be raised.
 *
 * @author sylordis
 *
 */
@ReorgOperation(name = "get")
@ReorgOperationProperty(name = "source", field = "srcColumn", required = true)
@ReorgOperationShortcut(keyword = "source", property = "source")
public class GetOperation extends ChessAbstractReorgOperation {

	/**
	 * Required properties for value specification in shortcut should match
	 * {@link ReorgOperationShortcut#keyword()}.
	 */
	public static final String SHORTCUT_KEY = "source";

	/**
	 * Name of the column to get the value from.
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
