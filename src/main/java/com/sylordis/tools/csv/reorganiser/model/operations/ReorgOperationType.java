package com.sylordis.tools.csv.reorganiser.model.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sylordis.tools.csv.reorganiser.model.operations.defs.GetOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation;

/**
 * All different reorganisation operation types. Each enumeration value possess a field "javaClass"
 * made to provide a way to call the constructor via
 * {@link Class#getDeclaredConstructors()}.newInstance().
 *
 * @author sylordis
 *
 */
public enum ReorgOperationType {

	// Please keep them ordered alphabetically

	/**
	 * Type for {@link GetOperation}.
	 */
	GET(GetOperation.class),
	/**
	 * Type for {@link RegReplaceOperation}.
	 */
	REGREPLACE(RegReplaceOperation.class),
	/**
	 * Type for {@link ValueOperation}.
	 */
	VALUE(ValueOperation.class);

	/**
	 * Class linked to the enum value.
	 */
	private final Class<? extends AbstractReorgOperation> javaClass;

	/**
	 * Constructor of each enum value.
	 *
	 * @param javaClass java class linked to this operation
	 */
	private ReorgOperationType(Class<? extends AbstractReorgOperation> javaClass) {
		this.javaClass = javaClass;
	}

	/**
	 * @return the java class linked to this operation
	 */
	public Class<? extends AbstractReorgOperation> getJavaClass() {
		return javaClass;
	}

	/**
	 * Gets the default dictionary from its own values.
	 *
	 * @return a map with
	 */
	public static Map<String, Class<? extends AbstractReorgOperation>> getDefaultDictionary() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		Arrays.asList(ReorgOperationType.values())
		.forEach(t -> dictionary.put(toKey(t.toString()), t.getJavaClass()));
		return dictionary;
	}

	/**
	 * Formats a string to a new string that matches default key format.
	 *
	 * @param raw string to format as a key
	 * @return a formated string
	 */
	public static String toKey(String raw) {
		return raw.toLowerCase();
	}

}
