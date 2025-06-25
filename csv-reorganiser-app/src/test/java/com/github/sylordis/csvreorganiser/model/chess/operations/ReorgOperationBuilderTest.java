package com.github.sylordis.csvreorganiser.model.chess.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.OperationBuildingException;
import com.github.sylordis.csvreorganiser.test.chess.defs.FakeOperation;
import com.github.sylordis.csvreorganiser.test.chess.defs.WrongFakeOperation;

/**
 * Test suite for {@link ChessOperationBuilder}.
 *
 * @author sylordis
 *
 */
class ReorgOperationBuilderTest {

	/**
	 * Default object for tests.
	 */
	private ChessOperationBuilder builder;
	/**
	 * Default object for data.
	 */
	private Map<String, Object> data;
	/**
	 * Default dictionary for operations.
	 */
	private Map<String, Class<? extends ChessAbstractReorgOperation>> operationDictionary;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		builder = new ChessOperationBuilder();
		data = new HashMap<>();
		operationDictionary = new HashMap<>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#ReorgOperationBuilder()}.
	 */
	@Test
	@Tag("Constructor")
	void testReorgOperationBuilder() {
		assertNotNull(builder, "Constructor should provide an object");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when data is passed without configuring the builder.
	 */
	@Test
	void testFromData_NoConfiguration() {
		data.put(YAMLTags.Chess.OPDATA_TYPE_KEY, "something");
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}.
	 */
	@Test
	void testFromData() {
		final String opName = "fake";
		data.put(YAMLTags.Chess.OPDATA_TYPE_KEY, opName);
		operationDictionary.put(opName, FakeOperation.class);
		builder = new ChessOperationBuilder(operationDictionary);
		ChessAbstractReorgOperation op = builder.fromData("any", data);
		assertNotNull(op, "Result operation should not be null");
		assertEquals(FakeOperation.class, op.getClass());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when null data is passed.
	 */
	@Test
	@Tag("Null")
	void testFromData_Null() {
		assertThrows(NullPointerException.class, () -> builder.fromData("any", null),
				"Providing null data should return in error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when the type was not provided in data.
	 */
	@Test
	void testFromData_NoTypeProvided() {
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data),
				"Providing data without the type tag should return in error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when an unexisting type is provided.
	 */
	@Test
	void testFromData_WrongType() {
		data.put(YAMLTags.Chess.OPDATA_TYPE_KEY, "Scary");
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data),
				"Providing data without the type tag should return in error");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when the class was wrongly written and does not possess the basic constructor.
	 */
	@Test
	void testFromData_NoConstructor() {
		final String opName = "fake";
		data.put(YAMLTags.Chess.OPDATA_TYPE_KEY, opName);
		operationDictionary.put(opName, WrongFakeOperation.class);
		builder = new ChessOperationBuilder(operationDictionary);
		assertThrows(ConfigurationException.class, () -> builder.fromData("any", data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getClassFromType(java.util.Map)}.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetType() throws OperationBuildingException {
		assertNull(builder.getClassFromType("anything"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getClassFromType(java.util.Map)}
	 * to check that a defined operation can be retrieved regardless of the case asking.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetType_Casseless() throws OperationBuildingException {
		Class<? extends ChessAbstractReorgOperation> clazz = FakeOperation.class;
		operationDictionary.put("Fake", clazz);
		builder.setOperationDictionary(operationDictionary);
		assertEquals(clazz, builder.getClassFromType("FAKE"), "Correct type should be returned");
		assertEquals(clazz, builder.getClassFromType("fake"),
				"Correct type should be returned (lowercase)");
		assertEquals(clazz, builder.getClassFromType("FAKE"),
				"Correct type should be returned (mixed case)");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getClassFromType(java.util.Map)}
	 * when providing null.
	 */
	@Test
	@Tag("Null")
	void testGetType_Null() {
		assertNull(builder.getClassFromType(null), "Asking for null type should return null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getClassFromType(java.util.Map)}
	 * when providing empty string type.
	 */
	@Test
	void testGetType_Empty() {
		assertNull(builder.getClassFromType(""), "Asking for no specifing type should return null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getClassFromType(java.util.Map)}
	 * when asking for unknown type.
	 */
	@Test
	void testGetType_Unknown() {
		assertNull(builder.getClassFromType("TheGreatUnknown"), "Asking for unknown type should return null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#getOperationDictionary()}.
	 */
	@Test
	void testGetOperationDictionary() {
		assertNotNull(builder.getOperationDictionary(), "Dictionary should never be null");
		assertTrue(builder.getOperationDictionary().isEmpty(),
				"Dictionary should be empty if no configuration has been done");
		assertThrows(UnsupportedOperationException.class,
				() -> builder.getOperationDictionary().put("Something", ChessAbstractReorgOperation.class));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#setOperationDictionary(Map)}
	 * checking that the dictionary is setup but not taken directly as object.
	 */
	@Test
	void testSetOperationsDictionary() {
		operationDictionary.put("Fake", FakeOperation.class);
		operationDictionary.put("Wrong", WrongFakeOperation.class);
		builder.setOperationDictionary(operationDictionary);
		assertNotSame(operationDictionary, builder.getOperationDictionary());
		assertEquals(operationDictionary.size(), builder.getOperationDictionary().size());
		assertTrue(builder.getOperationDictionary().containsKey("fake"));
		assertEquals(FakeOperation.class, builder.getOperationDictionary().get("fake"));
		assertTrue(builder.getOperationDictionary().containsKey("wrong"));
		assertEquals(WrongFakeOperation.class, builder.getOperationDictionary().get("wrong"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#setOperationDictionary(Map)}
	 * checking that providing an empty map does not trigger any error.
	 */
	@Test
	void testSetOperationsDictionary_Empty() {
		builder.setOperationDictionary(operationDictionary);
		assertNotSame(operationDictionary, builder.getOperationDictionary());
		assertTrue(builder.getOperationDictionary().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#setOperationDictionary(Map)}
	 * checking that providing a null map does not trigger any error.
	 */
	@Test
	@Tag("Null")
	void testSetOperationsDictionary_Null() {
		builder.setOperationDictionary(null);
		assertNotNull(builder.getOperationDictionary());
		assertTrue(builder.getOperationDictionary().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder#setOperationDictionary(Map)}
	 * checking that calling this setter multiple times clears and sets the dictionary. Also checks that
	 * things get lower-cased.
	 */
	@Test
	void testSetOperationsDictionary_Multiple() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> dic1 = new HashMap<>();
		dic1.put("Op1", Operation1.class);
		dic1.put("Op2", Operation2.class);
		Map<String, Class<? extends ChessAbstractReorgOperation>> dic2 = new HashMap<>();
		builder.setOperationDictionary(operationDictionary);
		builder.setOperationDictionary(dic2);
		builder.setOperationDictionary(dic1);
		assertEquals(2, builder.getOperationDictionary().size());
		assertEquals(Operation1.class, builder.getOperationDictionary().get("op1"));
		assertEquals(Operation2.class, builder.getOperationDictionary().get("op2"));
	}

	/*
	 * TEST CLASSES
	 */

	/**
	 * Fake operation class for test.
	 *
	 * @author sylordis
	 *
	 */
	private final class Operation1 extends ChessAbstractReorgOperation {

		public Operation1(String name) {
			super(name);
		}

		@Override
		public String apply(CSVRecord record) {
			return null;
		}

	}

	private final class Operation2 extends ChessAbstractReorgOperation {

		public Operation2(String name) {
			super(name);
		}

		@Override
		public String apply(CSVRecord record) {
			return null;
		}

	}
}
