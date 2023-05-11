package com.github.sylordis.tools.csvreorganiser.test.chess.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * Class for testing with basic implementation of {@link ChessAbstractReorgOperation}.
 *
 * @author sylordis
 */
public class FakeOperation extends ChessAbstractReorgOperation {

	/**
	 * Fake value
	 */
	private String theValue;
	
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
		return theValue;
	}

}