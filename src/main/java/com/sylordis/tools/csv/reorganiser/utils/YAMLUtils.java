package com.sylordis.tools.csv.reorganiser.utils;

import java.util.List;
import java.util.Map;

/**
 * Utility class for YAML operations adapting to SnakeYAML. It mostly provides a wrapper for typical
 * operations without having to check for types (all warnings will be contained in this class).
 * <br/>
 * Methods in this class do not offer any kind of security checking or data validation checking
 * before accessing it and are to be used at the developer's own risk.
 *
 * @author sylordis
 *
 */
public final class YAMLUtils {

	/**
	 * Extracts a YAML set of properties from a key from dictionary of YAML keys. It is expected that
	 * this key holds nested keys.
	 *
	 * @param key  key to extract
	 * @param yaml YAML to extract the key's value from
	 * @return a dictionary of values or null if the requested key is not present
	 * @throws ClassCastException if the values of the key are not nested keys
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> get(String key, Map<String, Object> yaml) {
		return (Map<String, Object>) yaml.get(key);
	}

	/**
	 * Extracts a list of entries from the value of a key. It is expected that this key holds a list of
	 * entries.
	 *
	 * @param key  key to extract
	 * @param yaml YAML to extract the key's value from
	 * @return a list of values or null if the requested key is not present
	 * @throws ClassCastException if the key's value is not a list of entries
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> list(String key, Map<String, Object> yaml) {
		return (List<Object>) yaml.get(key);
	}

	/**
	 * Extract the value from a list of entries to a String format. It is expected that this key holds a
	 * single String value.
	 *
	 * @param key  key to extract
	 * @param yaml YAML to extract the key's value from
	 * @return the value of the key as a String or null if the requested key is not present
	 * @throws ClassCastException if the key's value is not a single value
	 */
	public static String strValue(String key, Map<String, Object> yaml) {
		return (String) yaml.get(key);
	}
}
