package com.github.sylordis.tools.csvreorganiser.model.hyde;

import java.util.Map;

import com.github.sylordis.tools.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.tools.csvreorganiser.model.engines.ReorganiserOperation;

/**
 * The Hyde engine uses a more simple declaration than the Chess Engine, where each operation is
 * declared from one string and using a syntax similar to the
 * <a href="https://www.djangoproject.com/">Django template language</a>. Each column is defined by
 * a name and a string composed of column names, constants, modified by filters in order to modify and produce
 * a result.
 * 
 * @author sylordis
 * @since 1.1
 *
 */
public class HydeEngine implements ReorganiserEngine {

	@Override
	public ReorganiserOperation createOperation(Map<String, Object> node) {
		// TODO Auto-generated method stub
		return null;
	}

}
