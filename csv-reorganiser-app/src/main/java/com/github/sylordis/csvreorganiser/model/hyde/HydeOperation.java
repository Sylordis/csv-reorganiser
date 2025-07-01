package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;

/**
 * A Hyde operation is represented by a tree, the root operation holding the name while the other
 * nodes along the branches hold their own functions to perform with their parameters.
 * 
 * @author sylordis
 *
 */
public class HydeOperation implements ReorganiserOperation {

	/**
	 * Name of the operation, which will also be the CSV header. Only the root operation should have a
	 * name.
	 */
	private String name;
	/**
	 * Children of the operation. This list should never be null.
	 */
	private List<HydeOperationPart> children;

	/**
	 * Constructs a Hyde operation with just a name to itself.
	 * 
	 * @param name
	 */
	public HydeOperation(String name) {
		this.name = name;
		this.children = new ArrayList<>();
	}

	@Override
	public String apply(CSVRecord t) {
		String result = null;
		result = String.join("", this.children.stream().map(c -> c.apply(t)).toArray(String[]::new));
		return result;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Adds an operation to the end of the list of children of this operation.
	 * 
	 * @param e Operation to add
	 */
	public void addChild(HydeOperationPart e) {
		this.children.add(e);
	}

	/**
	 * @return the children
	 */
	public List<HydeOperationPart> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<HydeOperationPart> children) {
		this.children.clear();
		this.children.addAll(children);
	}

	/**
	 * Checks if this operation has any children.
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
