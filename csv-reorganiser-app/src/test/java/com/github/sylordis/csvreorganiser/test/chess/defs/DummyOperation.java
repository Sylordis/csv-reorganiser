package com.github.sylordis.csvreorganiser.test.chess.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * <em>Dummy</em> operation for the generation.
 */
@Operation(name = "dummy")
@OperationProperty(name = "field1", field = "one", required = true)
@OperationProperty(name = "field2", field = "two")
public class DummyOperation extends ChessAbstractReorgOperation {

	/**
	 * This is field 1.
	 */
	private String one;
	/**
	 * This is field 2.
	 */
	private int two;

	public DummyOperation(String name) {
		super(name);
	}

	@Override
	public String apply(CSVRecord record) {
		return null;
	}
	
}