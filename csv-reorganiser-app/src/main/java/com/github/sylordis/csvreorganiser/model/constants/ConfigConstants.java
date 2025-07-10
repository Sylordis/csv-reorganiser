package com.github.sylordis.csvreorganiser.model.constants;

/**
 * This class contains all constants related to configuration.
 *
 * @author sylordis
 *
 */
public final class ConfigConstants {

	/**
	 * Default string ellipsis.
	 */
	public final static String DEFAULT_ELLIPSIS = "...";

	/**
	 * Path of the source files from the module root.
	 */
	public final static String SOURCE_ROOT = "src/main/java/";

	/**
	 * Constants for Chess engine.
	 *
	 */
	public final class Chess {

		/**
		 * Name of the package with all operations definitions.
		 */
		public final static String OPERATIONS_PACKAGE = "com.github.sylordis.csvreorganiser.model.chess.operations.defs";

	}

	/**
	 * Constants for Hyde engine.
	 */
	public final class Hyde {
		/**
		 * Pattern for template opening.
		 */
		public final static String TEMPLATE_START = "{{";
		/**
		 * Pattern for template closing.
		 */
		public final static String TEMPLATE_END = "}}";
		/**
		 * Symbol for modifier delimiter.
		 */
		public final static String TEMPLATE_FILTER_DELIMITER = "|";
		/**
		 * Symbol for modifier parameter delimiter..
		 */
		public final static String TEMPLATE_FILTER_PARAM_DELIMITER = ":";
		/**
		 * Name of the package with all operations definitions.
		 */
		public final static String FILTERS_PACKAGE = "com.github.sylordis.csvreorganiser.model.hyde.filters";
	}

	/**
	 * Private constructor
	 */
	private ConfigConstants() {
		// Nothing to do here
	}
}
