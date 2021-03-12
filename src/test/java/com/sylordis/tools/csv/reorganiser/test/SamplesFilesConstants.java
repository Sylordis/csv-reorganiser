package com.sylordis.tools.csv.reorganiser.test;

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
	 * Path to test target file.
	 */
	public static final String TARGET_CONTENT = SAMPLES_DIR + "expected.csv";
	/**
	 * Path to test configuration file.
	 */
	public static final String CONFIG_CONTENT = SAMPLES_DIR + "config.yaml";
}
