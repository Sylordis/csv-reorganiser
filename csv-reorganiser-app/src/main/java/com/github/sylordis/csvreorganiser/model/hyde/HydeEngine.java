package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.hyde.operations.HydeOperation;

/**
 * The Hyde engine uses a more simple declaration than the Chess Engine, where each operation is
 * declared from one string and using a syntax similar to the
 * <a href="https://www.djangoproject.com/">Django template language</a>. Each column is defined by
 * a name and a string composed of column names, constants, modified by filters in order to modify
 * and produce a result.
 * 
 * @author sylordis
 *
 */
public class HydeEngine implements ReorganiserEngine {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	@Override
	public ReorganiserOperation createOperation(Map<String, Object> node) {
		logger.debug("yamlToOp[in]: ({}){}", node.getClass(), node);
		// Root node
		HydeOperation op = new HydeOperation("root");
		
		// TODO Auto-generated method stub
		return op;
	}

}
