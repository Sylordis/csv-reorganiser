package com.github.sylordis.csvreorganiser.doc.elements;

public class Heading extends Element {

	private String content;

	/**
	 * @param content
	 */
	public Heading(String content) {
		super();
		this.content = content;
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

	@Override
	public boolean hasChildren() {
		return false;
	}
	
}
