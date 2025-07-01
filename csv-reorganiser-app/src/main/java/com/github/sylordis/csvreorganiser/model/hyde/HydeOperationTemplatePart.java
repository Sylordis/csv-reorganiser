package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class HydeOperationTemplatePart implements HydeOperationPart {
	
	private List<HydeModifier> modifiers;
	private String field;

	public HydeOperationTemplatePart() {
		modifiers = new ArrayList<>();
	}

	@Override
	public String apply(CSVRecord t) {
		String content = t.get(field);
		for (HydeModifier modifier : modifiers)
			content = modifier.apply(content); 
		return content;
	}
	
	/**
	 * @return the modifiers
	 */
	public List<HydeModifier> getModifiers() {
		return modifiers;
	}

	/**
	 * @param modifiers the modifiers to set
	 */
	public void setModifiers(List<HydeModifier> modifiers) {
		this.modifiers = modifiers;
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
