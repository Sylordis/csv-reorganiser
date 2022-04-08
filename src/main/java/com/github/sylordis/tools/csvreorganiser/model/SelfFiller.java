package com.github.sylordis.tools.csvreorganiser.model;

import com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException;

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

}
