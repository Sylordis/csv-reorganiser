package com.github.sylordis.csvreorganiser.utils.yaml;

import java.util.ArrayList;
import java.util.Map;

/**
 * Supposed to represent different yaml types on a Java level. Be reminded that this type checking
 * is a soft checking without parameterised generic classes.
 *
 * @author sylordis
 *
 */
public enum YAMLType {

	/**
	 * A typical YAML node.
	 */
	NODE(Map.class),
	/**
	 * A YAML list.
	 */
	LIST(ArrayList.class),
	/**
	 * A YAML String value.
	 */
	STRING(String.class);

	private final Class<?> javaType;

	YAMLType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public Class<?> getJavaType() {
		return javaType;
	}
}
