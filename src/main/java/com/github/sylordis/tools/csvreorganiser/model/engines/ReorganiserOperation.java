package com.github.sylordis.tools.csvreorganiser.model.engines;

import java.util.function.Function;

import org.apache.commons.csv.CSVRecord;

/**
 * Represents an operation that could ingest a CSV Record and returns a value for a CSV file. 
 * 
 * @author sylordis
 *
 */
public interface ReorganiserOperation extends Function<CSVRecord, String> {

	/**
	 * Gets the header name of the column, a.k.a. the operation name.
	 *
	 * @return
	 */
	String getName();

}
