package com.sylordis.tools.csv.reorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * Regular expression Replacement (type = "RegReplace") applies a regular expression replacement to
 * the value of a column. The base string will be returned if the pattern cannot be found.
 *
 * Required properties:
 * <dl>
 * <dt>source</dt>
 * <dd>Column of the source to take the data from.</dd>
 * <dt>pattern</dt>
 * <dd>Pattern to replace in the value.</dd>
 * <dt>replacement</dt>
 * <dd>Replacement for the pattern.</dd>
 * </dl>
 *
 * @author sylordis
 * @since 0.1
 *
 */
public class RegReplaceOperation extends AbstractReorgOperation {

	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_SOURCE_ID = "source";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_SOURCE_FIELD = "srcColumn";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_PATTERN_ID = "pattern";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_REPLACEMENT_ID = "replace";
	/**
	 * Required properties for source specification.
	 */
	public static final String OPDATA_REPLACEMENT_FIELD = "replacement";
	/**
	 * Fixed configuration for requirement properties names.
	 */
	public static final String[] DATA_REQUIRED_PROPS = { OPDATA_SOURCE_ID, OPDATA_PATTERN_ID, OPDATA_REPLACEMENT_ID };

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

	@Override
	protected void setup() {
		addProperty(OPDATA_SOURCE_ID, OPDATA_SOURCE_FIELD);
		addProperty(OPDATA_PATTERN_ID, OPDATA_PATTERN_ID);
		addProperty(OPDATA_REPLACEMENT_ID, OPDATA_REPLACEMENT_FIELD);
	}

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
			throw createMissingPropertyException(OPDATA_PATTERN_ID);
		if (this.srcColumn == null)
			throw createMissingPropertyException(OPDATA_PATTERN_ID);
		if (this.replacement == null)
			throw createMissingPropertyException(OPDATA_REPLACEMENT_ID);
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
