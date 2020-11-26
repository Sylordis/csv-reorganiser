package com.sylordis.tools.csv.reorganiser.model.operations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sylordis.tools.csv.reorganiser.model.YAMLtags;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation;
import com.sylordis.tools.csv.reorganiser.test.defs.FakeOperation;
import com.sylordis.tools.csv.reorganiser.test.defs.WrongFakeOperation;
import com.sylordis.tools.csv.reorganiser.test.matchers.MapContainsAllMatcher;

/**
 * Test suite for {@link ReorgOperationBuilder}
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
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		builder = new ReorgOperationBuilder();
		data = new HashMap<>();
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
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(opName, FakeOperation.class);
		builder.withCustomConfiguration(dictionary);
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
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(opName, WrongFakeOperation.class);
		builder.withCustomConfiguration(dictionary);
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetTypeDefaultConfiguration() throws OperationBuildingException {
		builder.withDefaultConfiguration();
		assertEquals(ValueOperation.class, builder.getClassFromType("VALUE"), "Correct type should be returned");
		assertEquals(ValueOperation.class, builder.getClassFromType("value"),
				"Correct type should be returned (lowercase)");
		assertEquals(ValueOperation.class, builder.getClassFromType("VaLue"),
				"Correct type should be returned (mixed case)");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFromType(java.util.Map)}.
	 *
	 * @throws OperationBuildingException
	 */
	@Test
	void testGetTypeReflectedConfiguration() throws OperationBuildingException {
		builder.withDefaultConfiguration();
		assertEquals(ValueOperation.class, builder.getClassFromType("VALUE"), "Correct type should be returned");
		assertEquals(ValueOperation.class, builder.getClassFromType("value"),
				"Correct type should be returned (lowercase)");
		assertEquals(ValueOperation.class, builder.getClassFromType("VaLue"),
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#getClassFilter()}.
	 */
	@Test
	void testGetClassFilter() {
		assertNull(builder.getClassFilter(), "Class filter should be null by default");
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setClassFilter(String)}.
	 */
	@Test
	void testSetClassFilter() {
		final String filter = "ElNewFiltero";
		builder.setClassFilter(filter);
		assertEquals(filter, builder.getClassFilter(),
				"Class filter should be set to provided value");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#setClassFilter(String)}
	 * with a null value provided.
	 */
	@Test
	void testSetClassFilterNull() {
		final String filter = "ElNewFiltero";
		builder.setClassFilter(filter);
		builder.setClassFilter(null);
		assertNull(builder.getClassFilter(),
				"Class filter should be set to provided value (null)");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#addCustomConfiguration(Map)}.
	 */
	@Test
	void testAddCustomConfiguration() {
		final String opName = "faker";
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(opName, FakeOperation.class);
		ReorgOperationBuilder builderCompare = new ReorgOperationBuilder().withDefaultConfiguration();
		builder.withDefaultConfiguration().addCustomConfiguration(dictionary);
		Map<String, Class<? extends AbstractReorgOperation>> expected = new HashMap<>(
				builderCompare.getOperationDictionary());
		expected.putAll(dictionary);
		assertEquals(ReorgOperationType.values().length + dictionary.size(), builder.getOperationDictionary().size(),
				"Builder should have more entries than the default configuration");
		assertThat("Dictionary should contain all entries, previous and added",
				builder.getOperationDictionary(), new MapContainsAllMatcher<>(expected));

	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#addCustomConfiguration(Map)}
	 * when passing an empty custom configuration.
	 */
	@Test
	void testAddCustomConfigurationEmpty() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		ReorgOperationBuilder builderCompare = new ReorgOperationBuilder().withDefaultConfiguration();
		builder.withDefaultConfiguration().addCustomConfiguration(dictionary);
		Map<String, Class<? extends AbstractReorgOperation>> expected = new HashMap<>(
				builderCompare.getOperationDictionary());
		expected.putAll(dictionary);
		assertEquals(ReorgOperationType.values().length, builder.getOperationDictionary().size(),
				"Builder should retain the original configuration");
		assertThat("Dictionary should contain all default entries", builder.getOperationDictionary(),
				new MapContainsAllMatcher<>(expected));

	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#addCustomConfiguration(Map)}
	 * when a null object is passed instead of a map.
	 */
	@Test
	void testAddCustomConfigurationNull() {
		ReorgOperationBuilder builderCompare = new ReorgOperationBuilder().withDefaultConfiguration();
		builder.withDefaultConfiguration().addCustomConfiguration(null);
		Map<String, Class<? extends AbstractReorgOperation>> expected = new HashMap<>(
				builderCompare.getOperationDictionary());
		assertEquals(ReorgOperationType.values().length, builder.getOperationDictionary().size(),
				"Builder should retain the original configuration");
		assertThat("Dictionary should contain all default entries",
				builder.getOperationDictionary(), new MapContainsAllMatcher<>(expected));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withCustomConfiguration(Map)}
	 */
	@Test
	void testWithCustomConfiguration() {
		final String opName = "noperation";
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(opName, FakeOperation.class);
		ReorgOperationBuilder builderConfigured = builder.withCustomConfiguration(dictionary);
		assertSame(builder, builderConfigured,
				"Setting custom configuration with the builder should return the same entity");
		assertIterableEquals(dictionary.entrySet(), builder.getOperationDictionary().entrySet(),
				"Dictionary should contain provided custom entries");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withCustomConfiguration(Map)}
	 * with an empty map provided.
	 */
	@Test
	void testWithCustomConfigurationEmpty() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		assertThrows(IllegalArgumentException.class, () -> builder.withCustomConfiguration(dictionary),
				"Providing an empty map should return in error");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withCustomConfiguration(Map)}
	 * with a null object provided.
	 */
	@Test
	void testWithCustomConfigurationNull() {
		assertThrows(IllegalArgumentException.class, () -> builder.withCustomConfiguration(null),
				"Providing a null map should return in error");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withCustomConfiguration(Map)}
	 * when a pre-existing configuration is already set. Default configuration is already tested by
	 * {@link #testWithDefaultConfiguration()}.
	 */
	@Test
	void testWithCustomConfigurationReplace() {
		builder.withDefaultConfiguration();
		final String opName = "noperation";
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(opName, FakeOperation.class);
		ReorgOperationBuilder builderConfigured = builder.withCustomConfiguration(dictionary);
		assertSame(builder, builderConfigured,
				"Setting custom configuration with the builder should return the same entity");
		assertIterableEquals(dictionary.entrySet(), builder.getOperationDictionary().entrySet(),
				"Dictionary should contain provided custom entries only");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withDefaultConfiguration()}
	 */
	@Test
	void testWithDefaultConfiguration() {
		builder = builder.withDefaultConfiguration();
		Map<String, Class<? extends AbstractReorgOperation>> expected = ReorgOperationType.getDefaultDictionary();
		assertThat("Dictionary should contain all default entries", builder.getOperationDictionary(),
				new MapContainsAllMatcher<>(expected));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withReflectedConfiguration()}
	 */
	@Test
	void testWithReflectedConfiguration() {
		assertThrows(NotImplementedException.class, () -> builder.withReflectedConfiguration(),
				"Non implemented features should return in error when used");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder#withReflectedConfiguration(String)}
	 */
	@Test
	void testWithReflectedConfigurationString() {
		assertThrows(NotImplementedException.class, () -> builder.withReflectedConfiguration("filter"),
				"Non implemented features should return in error when used");
	}
}
