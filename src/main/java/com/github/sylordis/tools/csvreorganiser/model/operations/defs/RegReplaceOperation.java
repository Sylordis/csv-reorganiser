package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationRequiredProperty;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * Regular expression Replacement (type = "RegReplace") applies a regular expression replacement to
 * the value of a column. The base string will be returned if the pattern cannot be found.
 *
 * @author sylordis
 *
 */
@Operation(name = "RegReplace")
@OperationRequiredProperty(name = "source", field = "srcColumn", description = "Column of the source file to take the content from")
@OperationRequiredProperty(name = "pattern", field = "pattern", description = "Pattern to look for in the content")
@OperationRequiredProperty(name = "replace", field = "replacement", description = "Replacement for the pattern")
public class RegReplaceOperation extends AbstractReorgOperation {

	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_ID_SOURCE = "source";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_FIELD_SOURCE = "srcColumn";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_ID_PATTERN = "pattern";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_ID_REPLACEMENT = "replace";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_FIELD_REPLACEMENT = "replacement";

	/**
	 * Pattern to search for.
	 */
	private String pattern;
	/**
	 * Regex to replace the searched pattern with.
	 */
	private String replacement;
	/**
	 * Index of the source column.
	 */
	private String srcColumn;

	/**
	 * Constructs a fully configured new Regular Expression Replacement operation.
	 *
	 * @param name
	 */
	public RegReplaceOperation(String name, String srcColumn, String pattern, String replacement) {
		super(name);
		this.srcColumn = srcColumn;
		this.pattern = pattern;
		this.replacement = replacement;
	}

	/**
	 * Constructs an empty new Regular Expression Replacement operation.
	 *
	 * @param name
	 */
	public RegReplaceOperation(String name) {
		this(name, null, null, null);
	}

	@Override
	public String apply(CSVRecord record) {
		// Check configuration
		if (this.pattern == null)
			throw createMissingPropertyException(OPDATA_ID_PATTERN);
		if (this.srcColumn == null)
			throw createMissingPropertyException(OPDATA_ID_PATTERN);
		if (this.replacement == null)
			throw createMissingPropertyException(OPDATA_ID_REPLACEMENT);
		String result = record.get(this.srcColumn).replaceAll(pattern, replacement);
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getName() + ", " + srcColumn + ": "
				+ pattern
				+ " => "
				+ replacement + "]";
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the replacement
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * @param replacement the replacement to set
	 */
	public void setReplacement(String replacement) {
		this.replacement = replacement;
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