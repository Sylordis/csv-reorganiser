package com.github.sylordis.csvreorganiser.utils.yaml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link YAMLUtils} class.
 *
 * @author sylordis
 *
 */
class YAMLUtilsTest {

	/**
	 * Test for {@link YAMLUtils} to check if the class cannot be instantiated.
	 *
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Test
	@Tag("Constructor")
	void testConstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	InvocationTargetException, NoSuchMethodException, SecurityException {
		assertThrows(IllegalAccessException.class, () -> YAMLUtils.class.getDeclaredConstructor().newInstance());
	}

	/**
	 * Test for {@link YAMLUtils#checkChildType(Map, String, Class)}.
	 */
	@Test
	void testCheckChildType_Node() {
		Map<String, Object> yaml = new HashMap<>();
		final String key = "MyKey";
		yaml.put(key, yaml);
		assertTrue(YAMLUtils.checkChildType(yaml, key, YAMLType.NODE));
	}

	/**
	 * Test for {@link YAMLUtils#checkChildType(Map, String, Class)}.
	 */
	@Test
	void testCheckChildType_List() {
		Map<String, Object> yaml = new HashMap<>();
		List<Object> list = new ArrayList<>();
		final String key = "MyKey";
		yaml.put(key, list);
		assertTrue(YAMLUtils.checkChildType(yaml, key, YAMLType.LIST));
	}

	/**
	 * Test for {@link YAMLUtils#checkChildType(Map, String, Class)}.
	 */
	@Test
	void testCheckChildType_String() {
		Map<String, Object> yaml = new HashMap<>();
		final String key = "MyKey";
		yaml.put(key, "Hello");
		assertTrue(YAMLUtils.checkChildType(yaml, key, YAMLType.STRING));
	}

	/**
	 * Test for {@link YAMLUtils#checkChildType(Map, String, Class)}.
	 */
	@Test
	void testCheckChildType_NotPresent() {
		Map<String, Object> yaml = new HashMap<>();
		assertFalse(YAMLUtils.checkChildType(yaml, "SomeTag", YAMLType.STRING));
	}

	/**
	 * Test for {@link YAMLUtils#checkChildType(Map, String, Class)}.
	 */
	@Test
	void testCheckChildType_NotClass() {
		Map<String, Object> yaml = new HashMap<>();
		final String key = "NotKey";
		yaml.put(key, key);
		assertFalse(YAMLUtils.checkChildType(yaml, key, YAMLType.NODE));
	}

	/**
	 * Test for {@link YAMLUtils#get(String, java.util.Map)}.
	 */
	@Test
	void testGet() {
		final String key = "mapValueKey";
		Map<String, Object> yaml = new HashMap<>();
		Map<String, Object> expected = new HashMap<>();
		expected.put("someOtherKey", new Object());
		yaml.put(key, expected);
		yaml.put("noise", new Object());
		Map<String, Object> actual = YAMLUtils.get(key, yaml);
		assertSame(expected, actual, "The correct map should be returned");
	}

	/**
	 * Test for {@link YAMLUtils#get(String, java.util.Map)} when the key's value is not of the correct
	 * type.
	 */
	@Test
	void testGet_WrongValueType() {
		final String key = "mapValueKey";
		Map<String, Object> yaml = new HashMap<>();
		String value = "uhoh";
		yaml.put(key, value);
		yaml.put("noise", new Object());
		assertThrows(ClassCastException.class, () -> YAMLUtils.get(key, yaml),
				"Get should return in error (ClassCast)");
	}

	/**
	 * Test for {@link YAMLUtils#get(String, java.util.Map)} when the key cannot be found.
	 */
	@Test
	void testGet_KeyNotExist() {
		final String key = "mapValueKey";
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.get(key, yaml), "Null should be returned (not exist)");
	}

	/**
	 * Test for {@link YAMLUtils#get(String, java.util.Map)} when passing a null value as key.
	 */
	@Test
	@Tag("Null")
	void testGet_KeyNull() {
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.get(null, yaml), "Null should be returned (null)");
	}

	/**
	 * Test for {@link YAMLUtils#list(String, java.util.Map)}.
	 */
	@Test
	void testList() {
		final String key = "listValueKey";
		Map<String, Object> yaml = new HashMap<>();
		List<Object> expected = new ArrayList<>();
		yaml.put(key, expected);
		yaml.put("noise", new Object());
		List<Object> actual = YAMLUtils.list(key, yaml);
		assertSame(expected, actual, "The correct list should be returned");
	}

	/**
	 * Test for {@link YAMLUtils#list(String, java.util.Map)} when the key's value is not of the correct
	 * type.
	 */
	@Test
	void testList_WrongValueType() {
		final String key = "listValueKey";
		Map<String, Object> yaml = new HashMap<>();
		String value = "uhoh";
		yaml.put(key, value);
		yaml.put("noise", new Object());
		assertThrows(ClassCastException.class, () -> YAMLUtils.list(key, yaml),
				"List should return in error (ClassCast)");
	}

	/**
	 * Test for {@link YAMLUtils#list(String, java.util.Map)} when the key cannot be found.
	 */
	@Test
	void testList_NotExist() {
		final String key = "listValueKey";
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.list(key, yaml), "Null should be returned (not exist)");
	}

	/**
	 * Test for {@link YAMLUtils#list(String, java.util.Map)} when passing a null value as key.
	 */
	@Test
	void testListKeyNull() {
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.list(null, yaml), "Null should be returned (null)");
	}

	/**
	 * Test for {@link YAMLUtils#strValue(String, java.util.Map)}.
	 */
	@Test
	void testStrValue() {
		final String key = "strValueKey";
		Map<String, Object> yaml = new HashMap<>();
		final String expected = "Ducky";
		yaml.put(key, expected);
		yaml.put("noise", new Object());
		String actual = YAMLUtils.strValue(key, yaml);
		assertSame(expected, actual, "The correct string should be returned");
	}

	/**
	 * Test for {@link YAMLUtils#strValue(String, java.util.Map)} when the key's value is not of the
	 * correct type.
	 */
	@Test
	void testStrValue_WrongValueType() {
		final String key = "strValueKey";
		Map<String, Object> yaml = new HashMap<>();
		List<Object> expected = new ArrayList<>();
		yaml.put(key, expected);
		yaml.put("noise", new Object());
		assertThrows(ClassCastException.class, () -> YAMLUtils.strValue(key, yaml),
				"strValue should return in error (ClassCast)");
	}

	/**
	 * Test for {@link YAMLUtils#strValue(String, java.util.Map)} when the key cannot be found.
	 */
	@Test
	void testStrValue_NotExist() {
		final String key = "strValueKey";
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.strValue(key, yaml), "Null should be returned (not exist)");
	}

	/**
	 * Test for {@link YAMLUtils#strValue(String, java.util.Map)} when passing a null value as key.
	 */
	@Test
	@Tag("Null")
	void testStrValueKey_Null() {
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.strValue(null, yaml), "Null should be returned (null)");
	}

}
