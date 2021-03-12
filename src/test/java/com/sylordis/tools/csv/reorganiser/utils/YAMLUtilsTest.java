package com.sylordis.tools.csv.reorganiser.utils;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link YAMLUtils} class.
 *
 * @author sylordis
 *
 */
class YAMLUtilsTest {

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
	void testGetWrongValueType() {
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
	void testGetKeyNotExist() {
		final String key = "mapValueKey";
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.get(key, yaml), "Null should be returned (not exist)");
	}

	/**
	 * Test for {@link YAMLUtils#get(String, java.util.Map)} when passing a null value as key.
	 */
	@Test
	void testGetKeyNull() {
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
	void testListWrongValueType() {
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
	void testListNotExist() {
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
	void testStrValueWrongValueType() {
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
	void testStrValueNotExist() {
		final String key = "strValueKey";
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.strValue(key, yaml), "Null should be returned (not exist)");
	}

	/**
	 * Test for {@link YAMLUtils#strValue(String, java.util.Map)} when passing a null value as key.
	 */
	@Test
	void testStrValueKeyNull() {
		Map<String, Object> yaml = new HashMap<>();
		assertNull(YAMLUtils.strValue(null, yaml), "Null should be returned (null)");
	}

}
