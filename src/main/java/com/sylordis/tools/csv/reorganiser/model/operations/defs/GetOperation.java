package com.sylordis.tools.csv.reorganiser.model.operations.defs;

import org.apache.commons.csv.CSVRecord;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * "Get" is the trivial basic operation which consists of getting the value of another column.
 *
 * Required properties:
 * <dl>
 * <dt>source</dt>
 * <dd>Name of the column to get the value from.</dd>
 * </dl>
 *
 * @author sylordis
 * @since 0.1
 *
 */
public class GetOperation extends AbstractReorgOperation {

	/**
	 * Required property for source specification.
	 */
	public static final String OPDATA_SOURCE_ID = "source";
	/**
	 * Matching internal field of {@link #OPDATA_SOURCE_ID}.
	 */
	public static final String OPDATA_SOURCE_FIELD = "srcColumn";
	/**
	 * Fixed configuration for requirement properties names.
	 */
	public static final String[] DATA_REQUIRED_PROPS = { OPDATA_SOURCE_ID };

	/**
	 * Index of the source column.
	 */
	private String srcColumn;

	@Override
	protected void setup() {
		addProperty(OPDATA_SOURCE_ID, OPDATA_SOURCE_FIELD);
	}

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
			throw createMissingPropertyException(OPDATA_SOURCE_ID);
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
