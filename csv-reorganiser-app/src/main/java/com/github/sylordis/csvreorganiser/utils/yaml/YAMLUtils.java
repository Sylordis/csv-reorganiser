package com.github.sylordis.csvreorganiser.utils.yaml;

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
	 * Private constructor to prevent instantiation.
	 */
	private YAMLUtils() {
		// Nothing to do here
	}

	/**
	 * Checks if the key's values of a node exists and is of given type according to types defined in
	 * {@link YAMLType}. Be reminded that using this method does not fully check that the resulting
	 * object can be safely used but gives a soft type check.
	 *
	 * @param node Reference node where to get the tag from
	 * @param tag  Tag in the node to check values for
	 * @param type Type of the values to check against
	 * @return true if the child exists and its values matches the given class, false otherwise.
	 */
	public static boolean checkChildType(Map<String, Object> node, String tag, YAMLType type) {
		return node.containsKey(tag) && type.getJavaType().isInstance(node.get(tag));
	}

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

	/**
	 * Converts an object to a YAML node representation in Java.
	 *
	 * @param o the object to convert
	 * @return a map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toNode(Object o) {
		return (Map<String, Object>) o;
	}

	/**
	 * Converts an object to a YAML list representation in Java.
	 *
	 * @param o the object to convert
	 * @return a list
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> toList(Object o) {
		return (List<Object>) o;
	}

}
