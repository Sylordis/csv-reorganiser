package com.github.sylordis.csvreorganiser.doc.elements;

import java.util.ArrayList;
import java.util.List;

public class Table extends Element {

	private List<String> headers = new ArrayList<>();

	@Override
	public boolean hasChildren() {
		return false;
	}

	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	/**
	 * Sets the headers and return itself
	 * 
	 * @param headers
	 * @return
	 */
	public Table withHeaders(List<String> headers) {
		setHeaders(headers);
		return this;
	}

}
