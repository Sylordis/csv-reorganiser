package com.sylordis.tools.csv.reorganiser.model.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sylordis.tools.csv.reorganiser.model.YAMLtags;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException;
import com.sylordis.tools.csv.reorganiser.test.defs.FakeOperation;
import com.sylordis.tools.csv.reorganiser.test.defs.WrongFakeOperation;

/**
 * Test suite for {@link ReorgOperationBuilder}.
 *
 * @author sylordis
 *
 */
class ReorgOperationBuilderTest {

	/**
	 * Default object for tests.
	 */
	private ReorgOperationBuilder builder;
	/**
	 * Default object for data.
	 */
	private Map<String, Object> data;
	/**
	 * Default dictionary for operations.
	 */
	private Map<String, Class<? extends AbstractReorgOperation>> operationDictionary;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		builder = new ReorgOperationBuilder();
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#ReorgOperationBuilder()}.
	 */
	@Test
	@Tag("Constructor")
	void testReorgOperationBuilder() {
		assertNotNull(builder, "Constructor should provide an object");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when data is passed without configuring the builder.
	 */
	@Test
	void testFromDataNoConfiguration() {
		data.put(YAMLtags.OPDATA_TYPE_KEY, "something");
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}.
	 */
	@Test
	void testFromData() {
		final String opName = "fake";
		data.put(YAMLtags.OPDATA_TYPE_KEY, opName);
		operationDictionary.put(opName, FakeOperation.class);
		builder = new ReorgOperationBuilder(operationDictionary);
		AbstractReorgOperation op = builder.fromData("any", data);
		assertNotNull(op, "Result operation should not be null");
		assertEquals(FakeOperation.class, op.getClass());
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when null data is passed.
	 */
	@Test
	void testFromDataNull() {
		assertThrows(NullPointerException.class, () -> builder.fromData("any", null),
				"Providing null data should return in error");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when the type was not provided in data.
	 */
	@Test
	void testFromDataNoTypeProvided() {
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data),
				"Providing data without the type tag should return in error");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when an unexisting type is provided.
	 */
	@Test
	void testFromDataWrongType() {
		data.put(YAMLtags.OPDATA_TYPE_KEY, "Scary");
		assertThrows(OperationBuildingException.class, () -> builder.fromData("any", data),
				"Providing data without the type tag should return in error");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#fromData(java.lang.String, java.util.Map)}
	 * when the class was wrongly written and does not possess the basic constructor.
	 */
	@Test
	void testFromDataNoConstructor() {
		final String opName = "fake";
		data.put(YAMLtags.OPDATA_TYPE_KEY, opName);
		operationDictionary.put(opName, WrongFakeOperation.class);
		builder = new ReorgOperationBuilder(operationDictionary);
		assertThrows(ConfigurationException.class, () -> builder.fromData("any", data));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetType() throws OperationBuildingException {
		assertNull(builder.getClassFromType("anything"));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}
	 * to check that a defined operation can be retrieved regardless of the case asking.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetTypeCasseless() throws OperationBuildingException {
		Class<? extends AbstractReorgOperation> clazz = FakeOperation.class;
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}
	 * when providing null.
	 */
	@Test
	void testGetTypeNull() {
		assertNull(builder.getClassFromType(null), "Asking for null type should return null");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}
	 * when providing empty string type.
	 */
	@Test
	void testGetTypeEmpty() {
		assertNull(builder.getClassFromType(""), "Asking for no specifing type should return null");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}
	 * when asking for unknown type.
	 */
	@Test
	void testGetTypeUnknown() {
		assertNull(builder.getClassFromType("TheGreatUnknown"), "Asking for unknown type should return null");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getOperationDictionary()}.
	 */
	@Test
	void testGetOperationDictionary() {
		assertNotNull(builder.getOperationDictionary(), "Dictionary should never be null");
		assertTrue(builder.getOperationDictionary().isEmpty(),
				"Dictionary should be empty if no configuration has been done");
		assertThrows(UnsupportedOperationException.class,
				() -> builder.getOperationDictionary().put("Something", AbstractReorgOperation.class));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setOperationDictionary(Map)}
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setOperationDictionary(Map)}
	 * checking that providing an empty map does not trigger any error.
	 */
	@Test
	void testSetOperationsDictionaryEmpty() {
		builder.setOperationDictionary(operationDictionary);
		assertNotSame(operationDictionary, builder.getOperationDictionary());
		assertTrue(builder.getOperationDictionary().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setOperationDictionary(Map)}
	 * checking that providing a null map does not trigger any error.
	 */
	@Test
	void testSetOperationsDictionaryNull() {
		builder.setOperationDictionary(null);
		assertNotNull(builder.getOperationDictionary());
		assertTrue(builder.getOperationDictionary().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setOperationDictionary(Map)}
	 * checking that calling this setter multiple times clears and sets the dictionary.
	 */
	@Test
	void testSetOperationsDictionaryMultiple() {

	}
}
