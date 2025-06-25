package com.github.sylordis.csvreorganiser.test.chess.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * Class for testing not respecting the basic constructor rule.
 *
 * @author sylordis
 *
 */
public class WrongFakeOperation extends ChessAbstractReorgOperation {
	
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