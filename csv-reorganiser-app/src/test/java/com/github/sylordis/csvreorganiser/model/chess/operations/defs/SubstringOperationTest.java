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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;

class SubstringOperationTest {

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Name", "Surname" };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).get();
	/**
	 * Typical name for the operation
	 */
	private final String OP_NAME = "MySubstOp";
	/**
	 * Object under test.
	 */
	private SubstringOperation op;
	
	@BeforeEach
	void setUp() throws Exception {
		op = new SubstringOperation(OP_NAME);
	}

	/**
	 * Test method for {@link SubstringOperation#SubstringOperation(String)}.
	 */
	@Test
	@Tag("Constructor")
	void testSubstringOperation() {
		assertNotNull(op);
	}

	/**
	 * Test method for {@link SubstringOperation#SubstringOperation(String, String, int)}.
	 */
	@Test
	@Tag("Constructor")
	void testSubstringOperationStringStringInt() {
		final String src = "SomeSource";
		final int start = 4;
		op = new SubstringOperation(OP_NAME, src, start);
		assertNotNull(op);
		assertEquals(OP_NAME, op.getName());
		assertEquals(src, op.getSrcColumn());
		assertEquals(start, op.getStartIndex());
		assertEquals(SubstringOperation.NO_END_INDEX, op.getEndIndex());
	}

	/**
	 * Test method for {@link SubstringOperation#SubstringOperation(String, String, int, int)}.
	 */
	@Test
	@Tag("Constructor")
	void testSubstringOperationStringStringIntInt() {
		final String src = "MySource";
		final int start = 2;
		final int end = 5;
		op = new SubstringOperation(OP_NAME, src, start, end);
		assertNotNull(op);
		assertEquals(OP_NAME, op.getName());
		assertEquals(src, op.getSrcColumn());
		assertEquals(start, op.getStartIndex());
		assertEquals(end, op.getEndIndex());
	}

	/**
	 * Test method for {@link SubstringOperation#SubstringOperation(String, String, int, int) when end index < start index.
	 */
	@Test
	@Tag("Constructor")
	void testSubstringOperationStringStringIntInt_CrossedIndexes() throws IOException {
		assertThrows(ConfigurationImportException.class, () -> new SubstringOperation(OP_NAME, "any", 10, 1));
	}
	
	/**
	 * Test method for {@link SubstringOperation#apply()} in normal conditions.
	 */
	@Test
	void testApply() throws IOException {
		final String src = CSV_HEADERS[1];
		op = new SubstringOperation(OP_NAME, src, 1, 3);
		CSVParser parser = CSVParser.parse("Henry,Popaloudos", format);
		assertEquals("op", op.apply(parser.getRecords().get(0)),
				"Apply should return the configured value substring");
	}

	/**
	 * Test method for {@link SubstringOperation#apply()} with an empty source to treat.
	 */
	@Test
	void testApply_EmptySource() throws IOException {
		final String src = CSV_HEADERS[1];
		op = new SubstringOperation(OP_NAME, src, 1, 3);
		CSVParser parser = CSVParser.parse("Daniel,", format);
		assertEquals("", op.apply(parser.getRecords().get(0)),
				"Apply should not fail if the source string is empty");
	}

	/**
	 * Test method for {@link SubstringOperation#apply()} with a word too short compared to end index.
	 */
	@Test
	void testApply_TooShort() throws IOException {
		final String src = CSV_HEADERS[0];
		op = new SubstringOperation(OP_NAME, src, 2, 10);
		CSVParser parser = CSVParser.parse("Mark,Gruduh", format);
		assertEquals("rk", op.apply(parser.getRecords().get(0)),
				"Apply should not fail if the source string is too short compared to end index");
	}

	/**
	 * Test method for {@link SubstringOperation#apply()} with a start index out of bounds.
	 */
	@Test
	void testApply_StartOutOfBounds() throws IOException {
		final String src = CSV_HEADERS[0];
		op = new SubstringOperation(OP_NAME, src, 15);
		CSVParser parser = CSVParser.parse("Abigail,Abernathy", format);
		assertEquals("", op.apply(parser.getRecords().get(0)),
				"Apply should not fail if the start index is out of bounds");
	}

	/**
	 * Test method for {@link SubstringOperation#apply()} with no end index.
	 */
	@Test
	void testApply_ToEnd() throws IOException {
		final String src = CSV_HEADERS[0];
		op = new SubstringOperation(OP_NAME, src, 2);
		CSVParser parser = CSVParser.parse("Purple,Bear", format);
		assertEquals("rple", op.apply(parser.getRecords().get(0)),
				"Apply should not fail and return the substring to the end of the word");
	}

	/**
	 * Test method for {@link SubstringOperation#apply()} with no end index.
	 */
	@Test
	void testApply_ToEnd_StartOutOfBounds() throws IOException {
		final String src = CSV_HEADERS[1];
		op = new SubstringOperation(OP_NAME, src, 10);
		CSVParser parser = CSVParser.parse("Smart,Wallaby", format);
		assertEquals("", op.apply(parser.getRecords().get(0)),
				"Apply should not fail if the start index is out of bounds");
	}

	/**
	 * Test method for {@link SubstringOperation#setup()}.
	 */
	@Test
	void testSetup() {
		final Collection<String> keys = op.getRequiredProperties();
		assertTrue(!keys.isEmpty(), "List of required properties should not be empty");
		assertIterableEquals(List.of(SubstringOperation.OPDATA_FIELD_SOURCE, SubstringOperation.OPDATA_FIELD_START), keys,
				"Setup should configure all required properties");
	}

	/**
	 * Test method for {@link SubstringOperation#getSrcColumn()}.
	 */
	@Test
	void testGetSrcColumn() {
		assertNull(op.getSrcColumn(), "Bare operation should not have required fields set");
	}

	/**
	 * Test method for {@link SubstringOperation#setSrcColumn()}.
	 */
	@Test
	void testSetSrcColumn() {
		final String column = "Serenade";
		op.setSrcColumn(column);
		assertEquals(column, op.getSrcColumn());
	}

	@Test
	void testGetStartIndex() {
		assertEquals(SubstringOperation.NO_START_INDEX, op.getStartIndex(), "Bare operation should not have required fields set");
	}

	@Test
	void testSetStartIndex() {
		final int index = 723;
		op.setStartIndex(index);
		assertEquals(index, op.getStartIndex());
	}

	@Test
	void testGetEndIndex() {
		assertEquals(SubstringOperation.NO_END_INDEX, op.getEndIndex(), "Bare operation should not have required fields set");
	}

	@Test
	void testSetEndIndex() {
		final int index = 928;
		op.setEndIndex(index);
		assertEquals(index, op.getEndIndex());
	}

	@Test
	void testToString() {
		assertNotNull(op.toString());
	}
}
