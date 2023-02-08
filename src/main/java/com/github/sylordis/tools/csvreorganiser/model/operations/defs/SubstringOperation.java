package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * Substring operation does a literal substring on a value. If the end index is too long compared to
 * the word or not provided, the substring will be done from the start index up the end of the word.<br/>
 * This operation does not fail and only returns an empty string if:
 * <ul>
 * <li>The source is empty</li>
 * <li>The start index is out of bounds</li>
 * </ul>
 * 
 * @author sylordis
 *
 */
@Operation(name = "Substring")
@OperationProperty(name = "source", field = "srcColumn", required = true, description = "Column of the source file to take the content from")
@OperationProperty(name = "start", field = "indexStart", required = true, description = "Start index of the substring")
@OperationProperty(name = "end", field = "indexEnd", description = "End index of the substring, leave not set for until the end of the text")
public class SubstringOperation extends AbstractReorgOperation {

	/**
	 * Required property for source column specification.
	 */
	public static final String OPDATA_FIELD_SOURCE = "source";
	/**
	 * Required property for start index specification.
	 */
	public static final String OPDATA_FIELD_START = "start";
	/**
	 * Constant to specify that no end index is used.
	 */
	public static final int NO_END_INDEX = -1;
	
	/**
	 * Index of the source column.
	 */
	private String srcColumn;
	/**
	 * Start index.
	 */
	private int indexStart;
	/**
	 * End index.
	 */
	private int indexEnd;

	/**
	 * Constructs a bare substring operation to be filled later.
	 * @param name  Name of the column
	 */
	public SubstringOperation(String name) {
		super(name);
		this.indexStart = -1;
		this.indexEnd = NO_END_INDEX;
	}

	/**
	 * Creates a new operation with a start index until the end of an entry.
	 * @param name  Name of the column
	 * @param source	Source column where to take the content from
	 * @param from	Index where to start the substring from
	 */
	public SubstringOperation(String name, String source, int from) {
		this(name, source, from, NO_END_INDEX);
	}

	/**
	 * Creates a new operation with a start and end index.
	 * @param name  Name of the column
	 * @param source	Source column where to take the content from
	 * @param from	Index where to start the substring from
	 * @param to	Index where to end the substring (excluded)
	 */
	public SubstringOperation(String name, String source, int from, int to) {
		super(name);
		this.srcColumn = source;
		this.indexStart = Math.max(0, from);
		// Prevents putting index inferior to NO_END_INDEX
		this.indexEnd = Math.max(NO_END_INDEX, to);
	}

	@Override
	public String apply(CSVRecord record) {
		String result = record.get(this.srcColumn);
		int start = Math.min(indexStart, result.length());
		if (indexEnd == NO_END_INDEX) {
			result = result.substring(start);
		} else {
			int end = Math.min(indexEnd, result.length());
			result = result.substring(start, end);
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s [%s, start=%i, end=%i]", this.getClass().getSimpleName(), getName(), indexStart, indexEnd);
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

	/**
	 * @return the start index
	 */
	public int getStartIndex() {
		return indexStart;
	}

	/**
	 * @param start the start index to set
	 */
	public void setIndexStart(int start) {
		this.indexStart = start;
	}

	/**
	 * @return the end index
	 */
	public int getEndIndex() {
		return indexEnd;
	}

	/**
	 * @param end the end index to set (exclusive)
	 */
	public void setEndIndex(int end) {
		this.indexEnd = end;
	}
	
}
