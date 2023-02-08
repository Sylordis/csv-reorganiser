package com.github.sylordis.tools.csvreorganiser.model.operations.defs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubstringOperationTest {

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Name", "Surname" };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).build();
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
	void testSubstringOperation() {
		assertNotNull(op);
	}

	/**
	 * Test method for {@link SubstringOperation#SubstringOperation(String, String, int)}.
	 */
	@Test
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

	@Test
	void testGetStartIndex() {
		assertEquals(-1, op.getStartIndex(), "Bare operation should not have required fields set");
	}

	@Test
	void testGetEndIndex() {
		assertEquals(SubstringOperation.NO_END_INDEX, op.getEndIndex(), "Bare operation should not have required fields set");
	}
}