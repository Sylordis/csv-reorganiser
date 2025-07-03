package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class HydeReorgOperationTemplatePart implements HydeReorgOperationPart {
	
	private List<HydeFilter> filters;
	private String field;

	public HydeReorgOperationTemplatePart() {
		filters = new ArrayList<>();
	}

	@Override
	public String apply(CSVRecord t) {
		String content = t.get(field);
		for (HydeFilter filter : filters)
			content = filter.apply(content); 
		return content;
	}
	
	/**
	 * @return the filters
	 */
	public List<HydeFilter> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(List<HydeFilter> filters) {
		this.filters = filters;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

}
