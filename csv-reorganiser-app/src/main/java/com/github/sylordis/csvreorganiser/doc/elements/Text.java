package com.github.sylordis.csvreorganiser.doc.elements;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.CSS;

public class Text extends Element {

	private String content;
	private final Map<CSS.Attribute, String> styles;

	public Text(String content) {
		this.content = content;
		styles = new HashMap<>();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	public Text bold() {
		this.styles.put(CSS.Attribute.FONT_WEIGHT, "bold");
		return this;
	}

	public Text italic() {
		this.styles.put(CSS.Attribute.FONT_STYLE, "italic");
		return this;
	}

	public Text code() {
		this.styles.put(CSS.Attribute.FONT_FAMILY, "code");
		return this;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the styles
	 */
	public Map<CSS.Attribute, String> getStyles() {
		return styles;
	}

}
