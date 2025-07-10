package com.github.sylordis.csvreorganiser.doc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.sylordis.csvreorganiser.doc.elements.Element;

/**
 * Handles the documentation for a given engine in order to be rendered.
 */
public interface EngineDocumentationHandler<T> {

	/**
	 * Generates the abstract documentation structure for a given operation.
	 * 
	 * @param type
	 * @return
	 */
	Element generateOperationDocumentation(Class<? extends T> type, CompilationUnit compilationUnit);

}
