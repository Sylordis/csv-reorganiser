package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.function.Function;

import org.apache.commons.csv.CSVRecord;

public interface HydeOperationPart extends Function<CSVRecord, String> {

}
