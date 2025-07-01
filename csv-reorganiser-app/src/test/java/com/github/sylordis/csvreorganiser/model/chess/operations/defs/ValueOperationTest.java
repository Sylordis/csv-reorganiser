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
 * Test suite for {@link ValueOperation} class.
 *
 * @author sylordis
 *
 */
class ValueOperationTest {

	/**
	 * Column name used for the operation.
	 */
	private final static String OP_NAME = "valueop";

	/**
	 * Default operation value setting.
	 */
	private final static String OP_VALUE = "kwak";
	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Name", "Id" };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).get();
	/**
	 * CSV parser to be used to provide data in tests.
	 */
	private CSVParser parser;

	/**
	 * Fully constructed operation.
	 */
	private ValueOperation fop;
	/**
	 * Bare operation,
	 */
	private ValueOperation bop;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		fop = new ValueOperation(OP_NAME, OP_VALUE);
		bop = new ValueOperation(OP_NAME);
	}

	@AfterEach
	void tearDown() throws Exception {
		parser = null;
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#setup()}.
	 */
	@Test
	void testSetup() {
		final Collection<String> keys = bop.getRequiredProperties();
		assertTrue(!keys.isEmpty(), "List of required properties should not be empty");
		assertIterableEquals(List.of(ValueOperation.SHORTCUT_KEY), keys,
				"Setup should configure all required properties.");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertEquals(OP_VALUE, fop.apply(parser.getRecords().get(0)), "Apply should return the configured value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_Empty() throws IOException {
		final String value = "";
		fop.setValue(value);
		parser = CSVParser.parse("Testy,5", format);
		assertEquals(value, fop.apply(parser.getRecords().get(0)), "Apply should return the configured value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when a null record is provided. This one is an edge case as Value operation does not refer to any
	 * column, but a null record should not happen.
	 *
	 * @throws IOException
	 */
	@Test
	@Tag("Null")
	void testApply_Null() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertEquals(OP_VALUE, fop.apply(null), "Apply should return the configured value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation has not been filled.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NotFilled() throws IOException {
		parser = CSVParser.parse("Testy,5", format);
		assertThrows(IllegalArgumentException.class, () -> bop.apply(parser.getRecords().get(0)),
				"Apply should return in error if mandatory property value was not set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#ValueOperation(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testValueOperationStringString() {
		assertNotNull(fop, "Constructor should provide an instance of the object");
		assertEquals(OP_NAME, fop.getName(), "Name should be set to provided value");
		assertEquals(OP_VALUE, fop.getValue(), "Value property should be set to provided value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#ValueOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testValueOperationString() {
		assertNotNull(bop, "Constructor should provide an instance of the object");
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
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#getValue()}.
	 */
	@Test
	void testGetValue() {
		assertNull(bop.getValue(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation#setValue()}.
	 */
	@Test
	void testSetValue() {
		final String value = "Universe";
		bop.setValue(value);
		assertEquals(value, bop.getValue(), "Value property should be set to provided value");
	}

}
