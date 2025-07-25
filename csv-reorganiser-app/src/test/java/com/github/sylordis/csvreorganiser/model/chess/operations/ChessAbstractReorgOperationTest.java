package com.github.sylordis.csvreorganiser.model.chess.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * Test suite for {@link ChessAbstractReorgOperation} class.
 *
 * @author sylordis
 *
 */
class ChessAbstractReorgOperationTest {

	/**
	 * Default operation name.
	 */
	private static final String NAME = "Fudge";

	/**
	 * Default test object.
	 */
	private ReorgOperationImpl op;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new ReorgOperationImpl(NAME);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#AbstractReorgOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testChessAbstractReorgOperation() {
		assertNotNull(op, "Constructor should provide an instance of the object");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#AbstractReorgOperation(java.lang.String)}
	 * when name is null.
	 */
	@Test
	@Tag("Constructor")
	void testChessAbstractReorgOperationName_Null() {
		assertThrows(ConfigurationImportException.class, () -> new ReorgOperationImpl(null),
		        "Constructor should throw an error if provided name is null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#AbstractReorgOperation(java.lang.String)}
	 * when name is empty.
	 */
	@Test
	@Tag("Constructor")
	void testChessAbstractReorgOperationName_Empty() {
		assertThrows(ConfigurationImportException.class, () -> new ReorgOperationImpl(""),
		        "Constructor should throw an error if provided name is empty");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#addProperty(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddProperty() {
		final String propName = "Light";
		final String propField = "Dark";
		op.addProperty(propName, propField);
		assertFalse(op.getProperties().isEmpty(), "Properties should not be empty after having set at least one");
		Collection<String> expected = List.of(propName);
		assertIterableEquals(expected, op.getProperties().keySet(),
		        "Properties should only contain all properties introduced via addProperty()");
		Map<String, String> props = Map.of(propName, propField);
		assertEquals(props, op.getProperties(), "Properties should match expected");
	}

	@Test
	void testAddRequiredProperty() {
		final String propName = "Matter";
		op.addRequiredProperty(propName);
		assertFalse(op.getRequiredProperties().isEmpty(), "Required properties should not be empty after adding one");
		Collection<String> expected = List.of(propName);
		assertEquals(expected, op.getRequiredProperties(),
		        "Properties should only contain all properties introduced via addRequiredProperty()");
		assertTrue(op.getProperties().isEmpty(), "Properties should not be modified when setting a required property");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}.
	 *
	 * @throws SelfFillingException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@Test
	void testFill_String() throws SelfFillingException, NoSuchFieldException, SecurityException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final String value = "DaValue";
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		op.fill(Map.of(prop, value));
		assertEquals(value, op.getStringField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when fill is called multiple times with proper data and configuration.
	 *
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_Twice() throws SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "Doctor";
		final String value = "11";
		final String value2 = "12";
		op.addProperty(prop, fieldName);
		op.fill(Map.of(prop, value));
		op.fill(Map.of(prop, value2));
		assertEquals(value2, op.getStringField(), "Field should be set according to last given value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when auto-filling an integer.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_Int() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "intField";
		final String prop = "DaProp";
		final int value = 42;
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		op.fill(Map.of(prop, value));
		assertEquals(value, op.getIntField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when auto-filling an integer from an accessible field.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_AccessibleField() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "accessibleIntField";
		final String prop = "Toasty";
		final int value = 111;
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		op.fill(Map.of(prop, value));
		assertEquals(value, op.accessibleIntField, "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when auto-filling an integer.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_IntFromString() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "intField";
		final String prop = "DaProp";
		final int value = 42;
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		op.fill(Map.of(prop, Integer.toString(value)));
		assertEquals(value, op.getIntField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when auto-filling an integer.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_IntFromEmptyString() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "intField";
		final String prop = "DaProp";
		final String value = "";
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		assertThrows(SelfFillingException.class, () -> op.fill(Map.of(prop, value)),
		        "Giving wrong data value to convert to int should result in an error");
		assertEquals(0, op.getIntField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when auto-filling an integer.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	@Tag("Null")
	void testFill_IntFromNullString() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "intField";
		final String prop = "DaProp";
		final String value = null;
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		Map<String, Object> map = new HashMap<>();
		map.put(prop, value);
		assertThrows(SelfFillingException.class, () -> op.fill(map),
		        "Giving wrong data value to convert to int should result in an error");
		assertEquals(0, op.getIntField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when property was not configured.
	 *
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_PropertyNotExisting() throws SelfFillingException {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "Uther";
		final String value = "Pendragon";
		op.addProperty(prop, fieldName);
		assertThrows(SelfFillingException.class, () -> op.fill(Map.of("Gorlois", value)),
		        "Unconfigured mandatory properties setting should throw an error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when configured field reflecting property does not exist.
	 */
	@Test
	void testFill_FieldNotExisting() {
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "nonExistingField";
		final String prop = "Merlin";
		final String value = "Myrddin";
		op.addProperty(prop, fieldName);
		assertThrows(SelfFillingConfigurationException.class, () -> op.fill(Map.of(prop, value)),
		        "Filling a non-existant field should throw an error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * when filling value is null.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	@Tag("Null")
	void testFill_Null() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// This field name should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final Field field = op.getClass().getDeclaredField(fieldName);
		final boolean accessible = field.canAccess(op);
		op.addProperty(prop, fieldName);
		final String value = null;
		final Map<String, Object> data = new HashMap<>();
		data.put(prop, value);
		op.fill(data);
		assertEquals(value, op.getStringField(), "Field should be set according to given value");
		assertEquals(accessible, field.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * with multiple fields to fill (combination of other functional tests)
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws SelfFillingException
	 */
	@Test
	void testFill_Multiple() throws NoSuchFieldException, SecurityException, SelfFillingException {
		// These fields names should match the field's name of the concrete implementation below
		final String stringFieldName = "stringField";
		final String intFieldName = "intField";
		final String stringProp = "Clan";
		final String intProp = "Knights";
		final String stringValue = "Round table";
		final int intValue = 12;
		final Field stringField = op.getClass().getDeclaredField(stringFieldName);
		final Field intField = op.getClass().getDeclaredField(intFieldName);
		final boolean stringAccessible = stringField.canAccess(op);
		final boolean intAccessible = stringField.canAccess(op);
		op.addProperty(stringProp, stringFieldName);
		op.addProperty(intProp, intFieldName);
		op.fill(Map.of(stringProp, stringValue, intProp, intValue));
		assertEquals(stringValue, op.getStringField(), "Field should be set according to given value (String)");
		assertEquals(intValue, op.getIntField(), "Field should be set according to given value (int)");
		assertEquals(stringAccessible, stringField.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling (String)");
		assertEquals(intAccessible, intField.canAccess(op),
		        "Accessiblity of touched fields should be the same after filling (int)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * with multiple fields to fill (combination of other functional tests) with one error.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@Test
	void testFill_MultipleWithErrorAtBeginning() throws NoSuchFieldException, SecurityException {
		// These fields names should match the field's name of the concrete implementation below
		final String stringFieldName = "stringField";
		final String intFieldName = "intField";
		final String stringProp = "Clan";
		final String intProp = "Knights";
		final Field stringField = op.getClass().getDeclaredField(stringFieldName);
		final Field intField = op.getClass().getDeclaredField(intFieldName);
		final boolean stringAccessible = stringField.canAccess(op);
		final boolean intAccessible = stringField.canAccess(op);
		op.addProperty(stringProp, stringFieldName);
		op.addProperty(intProp, intFieldName);
		assertThrows(SelfFillingException.class, () -> op.fill(new HashMap<>()),
		        "Unconfigured mandatory properties setting should throw an error");
		assertNull(op.getStringField(), "Field should not be set if encountered an error while setting (String)");
		assertEquals(0, op.getIntField(), "Field should not be set as error should have happened previously (int)");
		assertEquals(stringAccessible, stringField.canAccess(op),
		        "Accessiblity of touched fields should be the same after an error has been triggered during filling (String)");
		assertEquals(intAccessible, intField.canAccess(op),
		        "Accessiblity of touched fields should be the same after an error has been triggered during filling (int)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#fill(java.util.Map)}
	 * with multiple fields to fill (combination of other functional tests) with one error.
	 *
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@Test
	void testFill_MultipleWithErrorAtLeastOneSet() throws NoSuchFieldException, SecurityException {
		// These fields names should match the field's name of the concrete implementation below
		final String stringFieldName = "stringField";
		final String intFieldName = "intField";
		final String stringProp = "Clan";
		final String intProp = "Knights";
		final String stringValue = "Round table";
		final String intValue = "";
		final Field stringField = op.getClass().getDeclaredField(stringFieldName);
		final Field intField = op.getClass().getDeclaredField(intFieldName);
		final boolean stringAccessible = stringField.canAccess(op);
		final boolean intAccessible = stringField.canAccess(op);
		op.addProperty(stringProp, stringFieldName);
		op.addProperty(intProp, intFieldName);
		assertThrows(SelfFillingException.class, () -> op.fill(Map.of(stringProp, stringValue, intProp, intValue)),
		        "Unconfigured mandatory properties setting should throw an error");
		assertEquals(stringValue, op.getStringField(),
		        "Field should be set to provided value since error happens afterwards (String)");
		assertEquals(0, op.getIntField(), "Field should not be set as error should have happened (int)");
		assertEquals(stringAccessible, stringField.canAccess(op),
		        "Accessiblity of touched fields should be the same after an error has been triggered during filling (String)");
		assertEquals(intAccessible, intField.canAccess(op),
		        "Accessiblity of touched fields should be the same after an error has been triggered during filling (int)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#createMissingPropertyException(java.lang.String)}.
	 */
	@Test
	void testCreateMissingPropertyException() {
		assertNotNull(op.createMissingPropertyException(NAME), "Exception object should be created properly");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#getName()}.
	 */
	@Test
	void testGetName() {
		assertEquals(NAME, op.getName(), "Name of the operation should return the set value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#setName(java.lang.String)}.
	 */
	@Test
	void testSetName() {
		final String name = "Nobody";
		op.setName(name);
		assertEquals(name, op.getName(), "Name should be changed to given value");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#setName(java.lang.String)}.
	 */
	@Test
	@Tag("Null")
	void testSetName_Null() {
		assertThrows(ConfigurationImportException.class, () -> op.setName(null),
		        "Setting a null value as name should return in error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#setName(java.lang.String)}.
	 */
	@Test
	void testSetName_Empty() {
		assertThrows(ConfigurationImportException.class, () -> op.setName(""),
		        "Setting a null value as name should return in error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#getRequiredProperties()}.
	 */
	@Test
	void testGetRequiredProperties() {
		assertNotNull(op.getRequiredProperties(), "Required properties should never be null");
		assertTrue(op.getRequiredProperties().isEmpty(), "Required properties should be empty by default");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#getRequiredProperties()}.
	 */
	@Test
	void testGetProperties() {
		assertNotNull(op.getProperties(), "Required properties should never be null");
		assertTrue(op.getProperties().isEmpty(), "Required properties should be empty by default");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#addChild(com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}.
	 */
	@Test
	void testAddChild() {
		final ReorgOperationImpl child = new ReorgOperationImpl("flubber");
		op.addChild(child);
		assertNotNull(op.getChildren(), "Children list should never be null");
		assertEquals(List.of(child), op.getChildren());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#hasChildren()}
	 * with no children.
	 */
	@Test
	void testHasChildren_Empty() {
		assertFalse(op.hasChildren(), "Children list should be empty by default");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#hasChildren()}.
	 */
	@Test
	void testHasChildren() {
		op.addChild(new ReorgOperationImpl("gizmo"));
		assertTrue(op.hasChildren(), "Children list should not be empty");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#hasChildren()}.
	 */
	@Test
	void testGetChildren() {
		assertNotNull(op.getChildren(), "Children list should never be null");
		assertTrue(op.getChildren().isEmpty(), "Children list should be empty by default");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation#setChildren(java.util.List)}.
	 */
	@Test
	void testSetChildren() {
		List<ChessAbstractReorgOperation> children = new ArrayList<>();
		children.add(new ReorgOperationImpl("op"));
		op.setChildren(children);
		assertNotNull(op.getChildren(), "Children list should never be null");
		assertIterableEquals(op.getChildren(), children, "Children list should be empty by default");
	}

	@Test
	void testEquals() throws NoSuchFieldException, SecurityException, SelfFillingException {
		ReorgOperationImpl op1, op2;
		final String name = "Op";
		op1 = new ReorgOperationImpl(name);
		op2 = new ReorgOperationImpl(name);
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		op1.addProperty(prop, fieldName);
		op2.addProperty(prop, fieldName);
		assertEquals(op1, op2, "Both objects should be equal");
	}

	@Test
	void testEquals_Same() throws NoSuchFieldException, SecurityException, SelfFillingException {
		ReorgOperationImpl op1;
		final String name = "Op";
		op1 = new ReorgOperationImpl(name);
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final String value = "DaValue";
		op1.addProperty(prop, fieldName);
		final Map<String, Object> data = new HashMap<>();
		data.put(prop, value);
		op1.fill(data);
		assertEquals(op1, op1, "Same object should be equal to itself");
	}

	@Test
	@Tag("Null")
	void testEquals_Null() throws NoSuchFieldException, SecurityException, SelfFillingException {
		assertNotEquals(op, null, "Object should not be equal to null if not null");
	}

	@Test
	void testEquals_NotClass() throws NoSuchFieldException, SecurityException, SelfFillingException {
		ReorgOperationImpl op;
		Object obj;
		final String name = "Op";
		op = new ReorgOperationImpl(name);
		obj = new Object();
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final String value = "DaValue";
		op.addProperty(prop, fieldName);
		final Map<String, Object> data = new HashMap<>();
		data.put(prop, value);
		op.fill(data);
		assertNotEquals(op, obj, "Equals should not return true if not the same class");
	}

	@Test
	void testEquals_NotOnName() throws NoSuchFieldException, SecurityException, SelfFillingException {
		ReorgOperationImpl op1, op2;
		op1 = new ReorgOperationImpl("Op");
		op2 = new ReorgOperationImpl("Nop");
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final String value = "DaValue";
		op1.addProperty(prop, fieldName);
		final Map<String, Object> data = new HashMap<>();
		data.put(prop, value);
		op1.fill(data);
		op2.fill(data);
		assertNotEquals(op1, op2, "Both objects should not be equal as their names are different");
	}

	@Test
	void testEquals_NotOnProperties() throws NoSuchFieldException, SecurityException, SelfFillingException {
		ReorgOperationImpl op1, op2;
		final String name = "Op";
		op1 = new ReorgOperationImpl(name);
		op2 = new ReorgOperationImpl(name);
		// This field name value should match the field's name of the concrete implementation below
		final String fieldName = "stringField";
		final String prop = "DaProp";
		final String value = "DaValue";
		op1.addProperty(prop, fieldName);
		final Map<String, Object> data = new HashMap<>();
		data.put(prop, value);
		op1.fill(data);
		assertNotEquals(op1, op2, "Both objects should not be equal as their properties are different");
	}

	/**
	 * Concrete implementation for test.
	 *
	 * @author sylordis
	 *
	 */
	private class ReorgOperationImpl extends ChessAbstractReorgOperation {

		/**
		 * String field for filling test.
		 */
		private String stringField;
		/**
		 * Int field for filling test.
		 */
		private int intField;
		/**
		 * Int accessible field for filling test.
		 */
		public int accessibleIntField;

		/**
		 * Constructs a new instance.
		 *
		 * @param name
		 */
		public ReorgOperationImpl(String name) {
			super(name);
		}

		/**
		 * Doesn't perform anything.
		 */
		@Override
		protected void setup() {
		}

		/**
		 * Only returns null.
		 */
		@Override
		public String apply(CSVRecord record) {
			return null;
		}

		/**
		 * @return the stringField
		 */
		public String getStringField() {
			return stringField;
		}

		/**
		 * @return the intField
		 */
		public int getIntField() {
			return intField;
		}

	}
}
