package com.github.sylordis.csvreorganiser.model;

import java.lang.reflect.Field;

import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;
import com.github.sylordis.csvreorganiser.utils.TypeConverter;

/**
 * Classes implementing this interface will contain a method allowing for self filling of data from
 * any source, {@link #fill(Object)}.
 *
 * @author sylordis
 *
 * @param V type of data needed to fill itself
 *
 */
public interface SelfFiller<V> {

	/**
	 * Instructs the object to fill all its properties from provided data.
	 *
	 * @param data Data to fill the object with
	 * @throws SelfFillingException              if a filling error occurs
	 * @throws SelfFillingConfigurationException if a filling error occurs due to configuration of the
	 *                                           type/object
	 */
	void fill(V data) throws SelfFillingException;

	/**
	 * Sets a field of this class to the given value via reflection. If the field cannot be accessed, it
	 * will become accessible during this operation and then made inaccessible again.
	 * 
	 * @param name  Name of the field
	 * @param value Value to set it to
	 * @throws SelfFillingException if something goes wrong.
	 */
	default void setField(String name, Object value) throws SelfFillingException {
		boolean madeAccessible = false;
		Field field = null;
		try {
			field = this.getClass().getDeclaredField(name);
			// Accessibility check
			if (!field.canAccess(this)) {
				madeAccessible = true;
				field.setAccessible(true);
			}
			// Field type check
			field.set(this, TypeConverter.to(value, field.getType()));
		} catch (IllegalArgumentException e) {
			// Error in provided data
			throw new SelfFillingException(e);
		} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// If this is thrown, then the Operation configuration has not been done properly by the developer
			throw new SelfFillingConfigurationException(e);
		} finally {
			// If was made accessible, return it to non accessible
			if (field != null && madeAccessible)
				field.setAccessible(false);
		}
	}
}
