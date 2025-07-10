package com.github.sylordis.csvreorganiser.doc.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base for elements that are light-weight constrained.
 */
public abstract class Element {

	/**
	 * Children elements under this one.
	 */
	private List<Element> children = new ArrayList<>();

	/**
	 * Add a child element as a last child.
	 * 
	 * @param child
	 */
	public void addChild(Element child) {
		children.add(child);
	}

	/**
	 * Adds all provided children after all existing children.
	 * 
	 * @param children
	 */
	public void addChildren(Collection<? extends Element> children) {
		this.children.addAll(children);
	}

	/**
	 * Adds all provided children after all existing children.
	 * 
	 * @param children
	 */
	public void addChildren(Element... children) {
		this.children.addAll(Arrays.asList(children));
	}

	/**
	 * Adds the provided children after all existing children. Call this method last in a chain.
	 * 
	 * @param children
	 * @return itself
	 * @see Element#addChildren(Collection)
	 */
	public Element with(Collection<? extends Element> children) {
		addChildren(children);
		return this;
	}

	/**
	 * Adds the provided children after all existing children. Call this method last in a chain.
	 * 
	 * @param children
	 * @return itself
	 * @see Element#addChildren(Element...)
	 */
	public Element with(Element... children) {
		addChildren(children);
		return this;
	}

	/**
	 * @return the children
	 */
	public List<Element> getChildren() {
		return children;
	}

	/**
	 * Checks if this element has children, i.e. the children's list is not null and is not empty.
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [children=" + children + "]";
	}

}
