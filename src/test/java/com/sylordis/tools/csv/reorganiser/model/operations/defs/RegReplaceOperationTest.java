package com.sylordis.tools.csv.reorganiser.model.operations.defs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link RegReplaceOperation} class.
 *
 * @author sylordis
 *
 */
class RegReplaceOperationTest {

	/**
	 * Column name used for the operation.
	 */
	private final static String OP_NAME = "REPLAAAACE";
	/**
	 * Default operation value setting.
	 */
	private final static String SRC_VALUE = "Doctor";
	/**
	 * Default pattern value.
	 */
	private final static String PATT_VALUE = "([^ ]*)( -> (.*))?";
	/**
	 * Default replacement value.
	 */
	private final static String REPL_VALUE = "$3";

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Name", "Id", SRC_VALUE };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.DEFAULT.withHeader(CSV_HEADERS);
	/**
	 * CSV parser to be used to provide data in tests.
	 */
	private CSVParser parser;

	/**
	 * Fully constructed operation.
	 */
	private RegReplaceOperation fop;
	/**
	 * Bare operation,
	 */
	private RegReplaceOperation bop;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		fop = new RegReplaceOperation(OP_NAME, SRC_VALUE, PATT_VALUE, REPL_VALUE);
		bop = new RegReplaceOperation(OP_NAME);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		parser = null;
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#setup()}.
	 */
	@Test
	void testSetup() {
		final List<String> keys = new ArrayList<>(bop.getRequiredProperties());
		assertTrue(!keys.isEmpty(), "List of required properties should not be empty");
		final List<String> expected = new ArrayList<>(
				Arrays.asList(new String[] { RegReplaceOperation.OPDATA_SOURCE_ID,
		                RegReplaceOperation.OPDATA_PATTERN_ID, RegReplaceOperation.OPDATA_REPLACEMENT_ID }));
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(expected);
		assertIterableEquals(expected, keys, "Setup should configure all required properties.");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply() throws IOException {
		parser = CSVParser.parse("Testy,5,SOMETHING -> ISwrong", format);
		assertEquals("ISwrong", fop.apply(parser.getRecords().get(0)), "Apply should return the proper result");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the pattern cannot be matched.
	 *
	 * @throws IOException
	 */
	@Test
	void testApplyPatternPartiallyFound() throws IOException {
		parser = CSVParser.parse("Testy,5, hello", format);
		// The string leaves the first space but matches on "hello" afterwards, and removes it because
		// there's no third group
		assertEquals(" ", fop.apply(parser.getRecords().get(0)),
				"Apply should return something even if part of the pattern is not found");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the pattern cannot be matched.
	 *
	 * @throws IOException
	 */
	@Test
	void testApplyPatternNotFound() throws IOException {
		fop.setPattern("--4");
		parser = CSVParser.parse("Testy,5, hello", format);
		// The string leaves the first space but matches on "hello" afterwards, and removes it because
		// there's no third group
		assertEquals(" hello", fop.apply(parser.getRecords().get(0)),
				"Apply should return base string value if the pattern is not found");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApplyConstantReplacement() throws IOException {
		final String replacement = "ducky!";
		fop.setReplacement(replacement);
		parser = CSVParser.parse("Testy,5,SOMETHING -> ISwrong", format);
		// It replaces all patterns it can find, so 2 here
		assertEquals(replacement + replacement, fop.apply(parser.getRecords().get(0)),
				"Apply should properly replace with constant values");

	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the provided record is null. This is an edge case which should not happen.
	 */
	@Test
	void testApplyNull() throws IOException {
		assertThrows(NullPointerException.class, () -> fop.apply(null),
				"Apply should return in error provided a null record");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when not all required properties have been provided.
	 */
	@Test
	void testApplyNotFilledSource() throws IOException {
		parser = CSVParser.parse("Testy,5,SOMETHING -> ISwrong", format);
		fop.setSrcColumn(null);
		assertThrows(IllegalArgumentException.class, () -> fop.apply(parser.getRecords().get(0)),
				"Apply should return in error if the mandatory property source column is not set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the source column does not exist.
	 */
	@Test
	void testApplyNotExisting() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertThrows(IllegalArgumentException.class, () -> fop.apply(parser.getRecords().get(0)),
				"Apply should return in error if the provided source column does not exist");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when not all required properties have been provided.
	 */
	@Test
	void testApplyNotFilledPattern() throws IOException {
		parser = CSVParser.parse("Testy,5,SOMETHING -> ISwrong", format);
		fop.setPattern(null);
		assertThrows(IllegalArgumentException.class, () -> fop.apply(parser.getRecords().get(0)),
				"Apply should return in error if the mandatory property pattern is not set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when not all required properties have been provided.
	 */
	@Test
	void testApplyNotFilledReplacement() throws IOException {
		parser = CSVParser.parse("Testy,5,SOMETHING -> ISwrong", format);
		fop.setReplacement(null);
		assertThrows(IllegalArgumentException.class, () -> fop.apply(parser.getRecords().get(0)),
				"Apply should return in error if the mandatory property replacement is not set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#RegReplaceOperation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testRegReplaceOperationStringStringStringString() {
		assertNotNull(fop, "Constructor should provide an instance of the object");
		assertEquals(OP_NAME, fop.getName(), "Name should be set to provided value");
		assertEquals(SRC_VALUE, fop.getSrcColumn(), "Source column should be set to provided value");
		assertEquals(PATT_VALUE, fop.getPattern(), "Pattern should be set to provided value");
		assertEquals(REPL_VALUE, fop.getReplacement(), "Replacement should be set to provided value");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#RegReplaceOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testRegReplaceOperationString() {
		assertNotNull(bop);
		assertEquals(OP_NAME, fop.getName(), "Name should be set to provided value");
		// Other checks to be performed in classic getter tests
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(bop.toString(), "toString() should return a non null String describing the object (bare)");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation#toString()}.
	 */
	@Test
	void testToStringConfigured() {
		assertNotNull(fop.toString(), "toString() should return a non null String describing the object (full)");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#getPattern()}.
	 */
	@Test
	void testGetPattern() {
		assertNull(bop.getPattern(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#setPattern(java.lang.String)}.
	 */
	@Test
	void testSetPattern() {
		final String pattern = "patchwork";
		bop.setPattern(pattern);
		assertEquals(pattern, bop.getPattern(), "Pattern should be set to provided value");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#getReplacement()}.
	 */
	@Test
	void testGetReplacement() {
		assertNull(bop.getReplacement(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#setReplacement(java.lang.String)}.
	 */
	@Test
	void testSetReplacement() {
		final String replacement = "borg";
		bop.setReplacement(replacement);
		assertEquals(replacement, bop.getReplacement(), "Replacement should be set to provided value");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#getSrcColumn()}.
	 */
	@Test
	void testGetSrcColumn() {
		assertNull(bop.getSrcColumn(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation#setSrcColumn(java.lang.String)}.
	 */
	@Test
	void testSetSrcColumn() {
		final String srcColumn = "fountain of life";
		bop.setSrcColumn(srcColumn);
		assertEquals(srcColumn, bop.getSrcColumn(), "Source column should be set to provided value");
	}

}
