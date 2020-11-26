package com.sylordis.tools.csv.reorganiser.test.defs;

import org.apache.commons.csv.CSVRecord;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

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
	protected void setup() {
		// Nothing to do here
	}

	@Override
	public String apply(CSVRecord record) {
		return null;
	}

}