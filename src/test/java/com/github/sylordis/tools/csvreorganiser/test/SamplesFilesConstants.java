package com.github.sylordis.tools.csvreorganiser.test;

/**
 * Constants class for test files.
 *
 * @author sylordis
 *
 */
public final class SamplesFilesConstants {

	/**
	 * Base directory for samples files.
	 */
	private static final String SAMPLES_DIR = "samples/";

	/**
	 * Path to test source file.
	 */
	public static final String SOURCE_CONTENT = SAMPLES_DIR + "people.csv";
	/**
	 * Path to test source file.
	 */
	public static final String SOURCE_CONTENT_2 = SAMPLES_DIR + "people2.csv";
	/**
	 * Path to test target file.
	 */
	public static final String TARGET_CONTENT = SAMPLES_DIR + "expected.csv";
	/**
	 * Path to test target file.
	 */
	public static final String TARGET_CONTENT_2 = SAMPLES_DIR + "expected2.csv";
	/**
	 * Path to test configuration file for Chess engine.
	 */
	public static final String CONFIG_CONTENT_CHESS = SAMPLES_DIR + "config-chess.yaml";
	/**
	 * Path to test configuration file for Hyde engine.
	 */
	public static final String CONFIG_CONTENT_HYDE = SAMPLES_DIR + "config-hyde.yaml";

}
