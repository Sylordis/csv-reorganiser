package com.github.sylordis.tools.csvreorganiser.test.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * Class for testing with basic implementation of {@link AbstractReorgOperation}.
 *
 * @author sylordis
 */
public class FakeOperation extends AbstractReorgOperation {

	/**
	 * Base constructor
	 *
	 * @param name
	 */
	public FakeOperation(String name) {
		super(name);
	}

	@Override
	public String apply(CSVRecord record) {
		return null;
	}

}