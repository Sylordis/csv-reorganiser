package com.github.sylordis.csvreorganiser.test.chess.defs;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;

/**
 * Dummy <em>operation</em> with a shortcut for the generation.
 */
@Operation(name = "dummyshort")
@OperationProperty(name = "important", field = "importance", required = true)
@OperationShortcut(keyword = "dumdum", property = "important")
public class DummyOperationWithShortcut extends ChessAbstractReorgOperation {

	/**
	 * Priority number 1.
	 */
	private int importance;

	public DummyOperationWithShortcut(String name) {
		super(name);
	}

	public DummyOperationWithShortcut(String name, int important) {
		super(name);
		this.importance = important;
	}

	@Override
	public String apply(CSVRecord record) {
		return null;
	}

}
