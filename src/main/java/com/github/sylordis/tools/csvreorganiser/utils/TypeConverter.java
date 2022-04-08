package com.github.sylordis.tools.csvreorganiser.utils;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Used for type conversions. This class is quite unsafe, prefer to use safe type
 * checking/conversion instead.
 *
 * @author sylordis
 * @since 1.1
 *
 */
@SuppressWarnings("unchecked")
public final class TypeConverter {

	/**
	 * Universal type conversion method. It can only convert from native to native and some basic
	 * classes. Use wisely.
	 *
	 * @param <T>  Wanted type for the object after conversion
	 * @param o    Object to convert type
	 * @param type Type to convert the object to
	 * @return The object converted
	 * @throws NotImplementedException if such conversion is not implemented yet. Good luck.
	 */
	public static <T> T to(Object o, Class<? extends T> type) {
		T desired = null;
		if (o == null || o.getClass().equals(type) || type.isAssignableFrom(o.getClass())) {
			// Same type or null
			desired = (T) o;
		} else if (String.class.equals(o.getClass())) {
			// Convert from String
			String os = (String) o;
			if (Integer.class.equals(type))
				desired = (T) Integer.valueOf(os);
		}
		if (o != null && desired == null)
			throw new NotImplementedException(
					"Conversion from " + o.getClass() + " to " + type + " is not implemented yet.");
		return desired;
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private TypeConverter() {
		// Nothing to see here
	}
}
