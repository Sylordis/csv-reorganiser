package com.github.sylordis.csvreorganiser.doc.renderers;

import com.github.sylordis.csvreorganiser.doc.LanguageRenderer;
import com.github.sylordis.csvreorganiser.doc.elements.Element;

/**
 * Renderer for Markdown.
 */
public class MarkdownRenderer implements LanguageRenderer {

	private int baseTitleLevel = 1;

	@Override
	public String render(Element operationDocumentation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void increaseBaseTitleLevel(int level) {
		this.baseTitleLevel += level;
	}

	/**
	 * @return the baseTitleLevel
	 */
	public int getBaseTitleLevel() {
		return baseTitleLevel;
	}

	/**
	 * @param baseTitleLevel the baseTitleLevel to set
	 */
	public void setBaseTitleLevel(int baseTitleLevel) {
		this.baseTitleLevel = baseTitleLevel;
	}

	
}
