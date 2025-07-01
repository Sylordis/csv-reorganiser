package com.github.sylordis.csvreorganiser.model.engines;

import java.util.List;
import java.util.Map;

/**
 * Defines engine classes that are supposed to manipulate a given type of operations.
 * 
 * @author sylordis
 *
 */
public interface ReorganiserEngine {

	/**
	 * Transforms a Yaml structure into a list of operations that can be called by the model.
	 * 
	 * @param node
	 * @return
	 */
	List<ReorganiserOperation> createOperations(Map<String, Object> root);
}
