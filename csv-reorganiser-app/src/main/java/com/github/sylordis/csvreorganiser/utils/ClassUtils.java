package com.github.sylordis.csvreorganiser.utils;

import java.lang.reflect.Field;

public class ClassUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ClassUtils() {
		// Nothing to do here
	}
	
	/**
	 * Gets the type of a given field declared in a property of an operation.
	 * 
	 * @param type The operation under analysis
	 * @param prop The property to check for
	 * @return
	 */
	public static Class<?> getFieldType(Class<?> type, String fieldName) {
		Class<?> fieldType = null;
		try {
			Field field = type.getDeclaredField(fieldName);
			fieldType = field.getType();
		} catch (NoSuchFieldException | SecurityException e) {
			// Do nothing, null will be returned
		}
		return fieldType;
	}

}
