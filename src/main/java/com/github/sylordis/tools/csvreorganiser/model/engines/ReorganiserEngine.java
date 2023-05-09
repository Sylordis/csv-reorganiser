package com.github.sylordis.tools.csvreorganiser.model.engines;

import java.util.Map;

import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationImportException;

/**
 * Defines engine classes that are supposed to manipulate a given type of operations.
 * 
 * @author sylordis
 *
 */
public interface ReorganiserEngine {

	/**
	 * Transforms a Yaml entry into a usable operation by the software.
	 *
	 * @param yaml yaml definition of the operation
	 * @throws ConfigurationImportException if a yaml configuration object is malformed
	 * @return an operation
	 */
	ReorganiserOperation createOperation(Map<String,Object> node);
	
}
