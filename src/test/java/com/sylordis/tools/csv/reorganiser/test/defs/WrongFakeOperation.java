package com.sylordis.tools.csv.reorganiser.test.defs;

import org.apache.commons.csv.CSVRecord;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * Class for testing not respecting the basic constructor rule.
 *
 * @author sylordis
 *
 */
public class WrongFakeOperation extends AbstractReorgOperation {

	/**
	 * Base constructor
	 *
	 * @param name
	 */
	public WrongFakeOperation(String name, String arg) {
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