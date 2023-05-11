package com.github.sylordis.tools.csvreorganiser.model.hyde.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.hyde.HydeFilter;

/**
 * @author sylordis
 *
 */
class HydeOperationTest {

	/**
	 * Basic name for root operation
	 */
	private final String OP_NAME = "MrHyde";

	/**
	 * Test CSV header.
	 */
	private final static String[] CSV_HEADERS = { "Item", "Id", "Counter" };
	/**
	 * CSV format to be used in tests.
	 */
	private final CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADERS).build();
	/**
	 * Operation under test.
	 */
	private HydeOperation op;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new HydeOperation(OP_NAME);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#HydeOperation()}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeOperation() {
		op = new HydeOperation();
		assertNotNull(op);
		assertNull(op.getName());
		assertNull(op.getSource());
		assertNotNull(op.getFilters());
		assertFalse(op.hasFilters());
		assertNotNull(op.getChildren());
		assertFalse(op.hasChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#HydeOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeOperationString() {
		final String name = "Welcome Mr";
		op = new HydeOperation(name);
		assertNotNull(op);
		assertEquals(name, op.getName());
		assertNull(op.getSource());
		assertNotNull(op.getFilters());
		assertFalse(op.hasFilters());
		assertNotNull(op.getChildren());
		assertFalse(op.hasChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#HydeOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeOperationStringString() {
		final String name = "Welcome Ms";
		final String source = "folly";
		op = new HydeOperation(name, source);
		assertNotNull(op);
		assertEquals(name, op.getName());
		assertEquals(source, op.getSource());
		assertNotNull(op.getFilters());
		assertFalse(op.hasFilters());
		assertNotNull(op.getChildren());
		assertFalse(op.hasChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#HydeOperation(java.lang.String, java.util.function.Function)}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeOperationStringStringListOfHydeFilter() {
		final String source = "weather";
		final String name = "Good afternoon";
		op = new HydeOperation(name, source, List.of(t -> t.repeat(3)));
		assertNotNull(op);
		assertEquals(name, op.getName());
		assertEquals(source, op.getSource());
		assertNotNull(op.getChildren());
		assertFalse(op.hasChildren());
		assertTrue(op.hasFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}.
	 */
	@Test
	void testApply_noSourceOrChildren() {
		assertThrows(ConfigurationException.class, () -> op.apply(mock(CSVRecord.class)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}.
	 * 
	 * @throws IOException
	 */
	@Test
	void testApply_source() throws IOException {
		op.setSource(CSV_HEADERS[1]);
		CSVParser parser = CSVParser.parse("Ball,BIT,20", format);
		assertEquals("BIT", op.apply(parser.getRecords().get(0)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}.
	 * 
	 * @throws IOException
	 */
	@Test
	void testApply_sourceNotFound() throws IOException {
		op.setSource("ScaryParam");
		CSVParser parser = CSVParser.parse("Yacht,YACHT,5", format);
		assertThrows(IllegalArgumentException.class, () -> op.apply(parser.getRecords().get(0)));
	}

	@ParameterizedTest
	@Tag("Null")
	@NullSource
	void testApply_null(CSVRecord rec) {
		op.setSource("something");
		assertThrows(NullPointerException.class, () -> op.apply(rec));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation under test has children.
	 * 
	 * @throws IOException
	 */
	@Test
	void testApply_withChildren() throws IOException {
		CSVParser parser = CSVParser.parse("Paper clips,clippa,109321", format);
		HydeOperation opc1 = new HydeOperation(OP_NAME + "/opc1");
		opc1.setSource(CSV_HEADERS[0]);
		HydeOperation opc2 = new HydeOperation(OP_NAME + "/opc2");
		opc2.setSource(CSV_HEADERS[2]);
		op.setChildren(List.of(opc1, opc2));
		assertEquals("Paper clips109321", op.apply(parser.getRecords().get(0)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation under test has filters set but no children.
	 * 
	 * @throws IOException
	 */
	@Test
	void testApply_withFilter() throws IOException {
		CSVParser parser = CSVParser.parse("Rainbows,R4IN,99999", format);
		op.setSource(CSV_HEADERS[1]);
		op.addFilter(t -> t.toLowerCase());
		assertEquals("r4in", op.apply(parser.getRecords().get(0)));

	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}
	 * when the operation under test has filters set but no children.
	 * 
	 * @throws IOException
	 */
	@Test
	void testApply_withFilters() throws IOException {
		CSVParser parser = CSVParser.parse("Rainbows,R4IN,99999", format);
		op.setSource(CSV_HEADERS[0]);
		op.addFilter(t -> t.toUpperCase());
		op.addFilter(t -> t.substring(0, 4));
		op.addFilter(t -> t.concat("man"));
		assertEquals("RAINman", op.apply(parser.getRecords().get(0)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#apply(org.apache.commons.csv.CSVRecord)}.
	 * 
	 * @throws IOException
	 */
	@Test
	@Tag("Integration")
	void testApply_withChildrenFilters() throws IOException {
		CSVParser parser = CSVParser.parse("Musketeers,ATHOS,1", format);
		HydeOperation opc1 = new HydeOperation(OP_NAME + "/opc1", CSV_HEADERS[2],
		        List.of(t -> Integer.toString(Integer.valueOf(t) * 5)));
		HydeOperation opc2 = new HydeOperation(OP_NAME + "/opc2", CSV_HEADERS[1],
		        List.of(t -> t.toLowerCase(), t -> " " + t));
		op.setChildren(List.of(opc1, opc2));
		assertEquals("5 athos", op.apply(parser.getRecords().get(0)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#getName()}.
	 */
	@Test
	void testGetName() {
		assertEquals(OP_NAME, op.getName());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#addChild(com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation)}.
	 */
	@Test
	void testAddChild() {
		final String childName = "Little blue";
		HydeOperation child = new HydeOperation(childName);
		op.addChild(child);
		assertTrue(op.hasChildren());
		assertEquals(List.of(child), op.getChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#addChild(com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation)}.
	 */
	@Test
	void testAddChild_conserveOrder() {
		final String c1n = "Little blue";
		final String c2n = "Little green";
		final String c3n = "Little red";
		HydeOperation c1 = new HydeOperation(c1n);
		HydeOperation c2 = new HydeOperation(c2n);
		HydeOperation c3 = new HydeOperation(c3n);
		op.addChild(c1);
		op.addChild(c2);
		op.addChild(c3);
		assertEquals(List.of(c1, c2, c3), op.getChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#getChildren()}.
	 */
	@Test
	void testGetChildren() {
		assertEquals(List.of(), op.getChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setChildren(java.util.List)}.
	 */
	@Test
	void testSetChildren() {
		HydeOperation c1 = new HydeOperation("Can");
		HydeOperation c2 = new HydeOperation("You");
		HydeOperation c3 = new HydeOperation("See");
		op.setChildren(List.of(c1, c2, c3));
		assertEquals(List.of(c1, c2, c3), op.getChildren());

	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setChildren(java.util.List)}.
	 */
	@Test
	void testSetChildren_replace() {
		HydeOperation c1 = new HydeOperation("Can");
		HydeOperation c2 = new HydeOperation("You");
		HydeOperation c3 = new HydeOperation("See");
		op.addChild(c1);
		op.setChildren(List.of(c2, c3));
		assertEquals(List.of(c2, c3), op.getChildren());

	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#hasChildren()}.
	 */
	@Test
	void testHasChildren() {
		assertFalse(op.hasChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setName(java.lang.String)}.
	 */
	@ParameterizedTest
	@Tag("Blank")
	@Tag("Null")
	@EmptySource
	@NullSource
	@ValueSource(strings = { "Jekyll", "The Bad Doctor" })
	void testSetName(String name) {
		op.setName(name);
		assertEquals(name, op.getName());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#getSource()}.
	 */
	@Test
	void testGetSource() {
		assertNull(op.getSource());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setSource(String)}.
	 */
	@ParameterizedTest
	@Tag("Blank")
	@Tag("Null")
	@EmptySource
	@ValueSource(strings = { "eternal youth", "blank" })
	void testSetSource(String arg) {
		op.setSource(arg);
		assertEquals(arg, op.getSource());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setSource(String)}
	 * if argument is null.
	 */
	@ParameterizedTest
	@Tag("Null")
	@NullSource
	void testSetSource_null(String arg) {
		op.setSource(arg);
		assertNull(op.getSource());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#getFilters()}.
	 */
	@Test
	void testGetFilters() {
		assertNotNull(op.getFilters());
		assertFalse(op.hasFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#addFilter(HydeOperation)}.
	 */
	@Test
	void testAddFilter() {
		HydeFilter f1 = t -> t;
		op.addFilter(f1);
		assertEquals(List.of(f1), op.getFilters());
		HydeFilter f2 = t -> t.toLowerCase();
		op.addFilter(f2);
		assertEquals(List.of(f1, f2), op.getFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#addFilter(HydeOperation)}
	 * if the provided filter is null.
	 */
	@Test
	@Tag("Null")
	void testAddFilter_null() {
		op.addFilter(null);
		assertFalse(op.hasFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setFilters(List)}.
	 */
	@Test
	void testSetFilters() {
		List<HydeFilter> filters = List.of(t -> t);
		op.setFilters(filters);
		assertEquals(filters, op.getFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setFilters(List)}
	 * to check that setting multiple filters at once conserve the order.
	 */
	@Test
	void testSetFilters_multi() {
		List<HydeFilter> filters = List.of(t -> t, t -> "a");
		op.setFilters(filters);
		assertEquals(filters, op.getFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setFilters(List)}
	 * to check that this method replaces any previous set filters.
	 */
	@Test
	void testSetFilters_replace() {
		List<HydeFilter> filters = List.of(t -> t);
		op.setFilters(filters);
		List<HydeFilter> filters2 = List.of(t -> "a", t -> t.substring(2));
		op.setFilters(filters2);
		assertEquals(filters2, op.getFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setFilters(List)}
	 * if argument is null.
	 */
	@Test
	@Tag("Null")
	void testSetFilters_null() {
		op.setFilters(null);
		assertFalse(op.hasFilters());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.hyde.operations.HydeOperation#setFilters(List)}
	 * if argument is null and filters were already present.
	 */
	@Test
	@Tag("Null")
	void testSetFilters_nullWithReplace() {
		List<HydeFilter> filters = List.of(t -> t);
		op.setFilters(filters);
		op.setFilters(null);
		assertFalse(op.hasFilters());
	}
}
