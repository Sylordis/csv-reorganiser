package com.github.sylordis.csvreorganiser.doc;

import com.github.sylordis.csvreorganiser.doc.elements.Element;

/**
 * Contract for classes supposed to language in a specific language.
 */
public interface LanguageRenderer {

	/**
	 * Increases the base title level from 1 (Heading 1).
	 * 
	 * @param level
	 */
	void increaseBaseTitleLevel(int level);

	/**
	 * Renders the provided documentation.
	 * 
	 * @param operationDocumentation
	 * @return the documentation rendered
	 */
	String render(Element operationDocumentation);

}
