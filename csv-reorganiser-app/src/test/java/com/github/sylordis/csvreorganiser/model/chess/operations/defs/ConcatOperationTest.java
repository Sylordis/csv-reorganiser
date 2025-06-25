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
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ConcatOperationTest {

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Item", "Id", "Counter" };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).build();
	/**
	 * Default name of the operation.
	 */
	private final String OP_NAME = "MyConcatOp";
	/**
	 * Object under test.
	 */
	private ConcatenationOperation op;

	@BeforeEach
	void setUp() throws Exception {
		op = new ConcatenationOperation(OP_NAME);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#setup()}.
	 */
	@Test
	void testSetup() {
		final Collection<String> keys = op.getRequiredProperties();
		assertTrue(!keys.isEmpty(), "List of required properties should not be empty");
		assertIterableEquals(List.of("values"), keys,
				"Setup should configure all required properties");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * in normal conditions.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply() throws IOException {
		op.setValues(List.of(CSV_HEADERS[1], "(", CSV_HEADERS[2], ") ", CSV_HEADERS[0]));
		CSVParser parser = CSVParser.parse("Ball,BIT,20", format);
		assertEquals("BIT(20) Ball", op.apply(parser.getRecords().get(0)),
				"Apply should return the configured value if existing");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the provided record is null. This is an edge case which should not happen.
	 *
	 * @throws IOException
	 */
	@Test
	@Tag("Null")
	void testApply_Null() throws IOException {
		assertThrows(IllegalArgumentException.class, () -> op.apply(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when a null record is provided but no configuration was made.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NullWhenConfigured() throws IOException {
		op.setValues(List.of("a", "Item"));
		assertThrows(NullPointerException.class, () -> op.apply(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when given values do not exist as fields in the CSV record. They should be output as normal text.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NotExisting() throws IOException {
		op.setValues(List.of("a", "b", "c"));
		CSVParser parser = CSVParser.parse("Ball,BIT,20", format);
		assertEquals("abc", op.apply(parser.getRecords().get(0)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation was not filled.
	 *
	 * @throws IOException
	 */
	@Test
	void testApply_NotFilled() throws IOException {
		CSVParser parser = CSVParser.parse("Testy,5", format);
		assertThrows(IllegalArgumentException.class, () -> op.apply(parser.getRecords().get(0)),
				"Apply should return in error if missing required properties");
	}

	/**
	 * Test method for {@link ConcatenationOperation#ConcatOperation(String, List)}.
	 */
	@Test
	@Tag("Constructor")
	void testConcatOperationStringListOfString() {
		final List<String> values = List.of("treat", "is", "a", "lie");
		op = new ConcatenationOperation(OP_NAME, values);
		assertEquals(OP_NAME, op.getName());
		assertEquals(values, op.getValues());
	}

	/**
	 * Test method for {@link ConcatenationOperation#ConcatOperation(String)}.
	 *
	 */
	@Test
	@Tag("Constructor")
	void testConcatOperationString() {
		assertNotNull(op);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#toString()}
	 *
	 */
	@Test
	void testToString() {
		assertNotNull(op.toString());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#getValues()}
	 */
	@Test
	void testGetValues() {
		assertNull(op.getValues());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#setValues(List)}.
	 *
	 * @param values
	 */
	@ParameterizedTest
	@MethodSource("provideForSetValues")
	void testSetValues(List<String> values) {
		op.setValues(values);
		assertEquals(values, op.getValues());
	}

	/**
	 * Argument provider for {@link #testSetValues(List)}.
	 *
	 * @return a list of arguments.
	 */
	private static Stream<Arguments> provideForSetValues() {
		return Stream.of(Arguments.of(List.of()), Arguments.of(List.of("Col1", "symbol", "col2")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation#setValues(List)}
	 * when null is provided.
	 */
	@Test
	void testSetValues_Null() {
		op.setValues(null);
		assertNull(op.getValues());
	}

}
