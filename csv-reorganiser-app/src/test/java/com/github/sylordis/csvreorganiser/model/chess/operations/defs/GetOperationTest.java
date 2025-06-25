package com.github.sylordis.csvreorganiser.model.chess.operations.defs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link GetOperation} class.
 *
 * @author sylordis
 *
 */
class GetOperationTest {

	/**
	 * Column name used for the operation.
	 */
	private final static String OP_NAME = "bar";
	/**
	 * Source column used for the operation.
	 */
	private final static String SRC_NAME = "sourcy";

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Name", "Id", SRC_NAME };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).build();
	/**
	 * CSV parser to be used to provide data in tests.
	 */
	private CSVParser parser;

	/**
	 * Fully constructed operation.
	 */
	private GetOperation fop;
	/**
	 * Bare operation,
	 */
	private GetOperation bop;

	@BeforeEach
	void setUp() throws Exception {
		fop = new GetOperation(OP_NAME, SRC_NAME);
		bop = new GetOperation(OP_NAME);
	}

	@AfterEach
	void tearDown() throws Exception {
		parser = null;
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#setup()}.
	 */
	@Test
	void testSetup() {
		final Collection<String> keys = bop.getRequiredProperties();
		assertTrue(!keys.isEmpty(), "List of required properties should not be empty");
		assertIterableEquals(List.of(GetOperation.SHORTCUT_KEY), keys,
				"Setup should configure all required properties");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply() throws IOException {
		parser = CSVParser.parse("Testy,5,polib", format);
		assertEquals("polib", fop.apply(parser.getRecords().get(0)),
				"Apply should return the configured value if existing");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the provided record is null. This is an edge case which should not happen.
	 *
	 * @throws IOException
	 */
	@Test
	@Tag("Null")
	void testApply_Null() throws IOException {
		parser = CSVParser.parse("Testy,5,polib", format);
		assertThrows(NullPointerException.class, () -> fop.apply(null),
				"Apply should return in error provided a null record");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the source column does not exists.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NotExisting() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertThrows(IllegalArgumentException.class, () -> fop.apply(parser.getRecords().get(0)),
				"Apply should return in error if provided column does not exist");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation is not completely set/filled.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NotFilled() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertThrows(IllegalArgumentException.class, () -> bop.apply(parser.getRecords().get(0)),
				"Apply should return in error if missing required properties");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#GetOperation(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testGetOperationStringString() {
		assertNotNull(fop, "Constructor should return an instance of the class");
		assertEquals(OP_NAME, fop.getName(), "Name should be set to provided value");
		assertEquals(SRC_NAME, fop.getSrcColumn(), "Source column should be set to provided value");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#GetOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testGetOperationString() {
		assertNotNull(bop, "Constructor should return an instance of the class");
		assertEquals(OP_NAME, bop.getName(), "Name should be set to provided value");
		// Other checks to be performed in classic getter tests
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(bop.toString(), "toString() should return a non null String describing the object (bare)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#toString()}.
	 */
	@Test
	void testToString_Configured() {
		assertNotNull(fop.toString(), "toString() should return a non null String describing the object (full)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#getSrcColumn()} when
	 * the property was not filled.
	 */
	@Test
	void testGetSrcColumn() {
		assertNull(bop.getSrcColumn(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation#setSrcColumn()}.
	 */
	@Test
	void testSetSrcColumn() {
		final String value = "SuperTrooper";
		bop.setSrcColumn(value);
		assertEquals(value, bop.getSrcColumn(), "Property should be set to provided value, even if null");
	}
}
