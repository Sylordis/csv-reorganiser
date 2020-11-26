package com.sylordis.tools.csv.reorganiser.model;

/**
 * This final class is used for constants regarding YAML specific tags used in this software.
 *
 * @author sylordis
 *
 */
public final class YAMLtags {

	/**
	 * Main tag at root for all operations
	 */
	public static final String OPDEF_MAIN_KEY = "structure";
	/**
	 * Necessary tag for all operations
	 */
	public static final String OPDEF_COLUMN_KEY = "column";
	/**
	 * YAML tag for single operation.
	 */
	public static final String OPDEF_OPERATION_KEY = "operation";
	/**
	 * YAML tag for nested operations.
	 */
	public static final String OPDEF_OPERATIONS_KEY = "operations";
	/**
	 * YAML tag for operation type.
	 */
	public static final String OPDATA_TYPE_KEY = "type";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private YAMLtags() {
		// Nothing to do here
	}

}
