package com.github.sylordis.csvreorganiser.model.constants;

/**
 * This final class is used for constants regarding YAML specific tags used in this software.
 *
 * @author sylordis
 *
 */
public final class YAMLTags {

	/**
	 * YAML tags for the Chess engine.
	 *
	 */
	public final class Chess {

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

	}

	/**
	 * YAML tags for the common header.
	 *
	 */
	public final class Header {
		/**
		 * Tag for engine specification.
		 */
		public static final String CFG_ENGINE = "engine";
	}
	
	/**
	 * YAML tags for the Hyde engine.
	 *
	 */
	public final class Hyde {

	}

	/**
	 * Main tag at root for all operations
	 */
	public static final String OPDEF_ROOT_KEY = "structure";
	/**
	 * Main tag for the header definition.
	 */
	public static final String CFG_HEADER_KEY = "header";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private YAMLTags() {
		// Nothing to do here
	}

}
