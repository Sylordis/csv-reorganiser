package com.github.sylordis.tools.csvreorganiser.model.hyde.operations;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.exceptions.OperationBuildingException;
import com.github.sylordis.tools.csvreorganiser.model.hyde.HydeFilter;

/**
 * A constant operation is an operation which cannot have children or filters and returns a constant
 * single value, which is represented by the source {@link #getSource()}. Constant operations should
 * not be at the root of the tree and can only be a leaf node. {@link #apply(CSVRecord)} returns
 * this value.<br/>
 * <br/>
 * Unauthorised operations like adding or setting children or filters will result into an
 * {@link OperationBuildingException} being thrown.
 * 
 * @author sylordis
 *
 */
public class HydeConstantOperation extends HydeOperation {

	/**
	 * Creates a Constant operation with a single value.
	 * 
	 * @param value Constant value of the operation.
	 */
	public HydeConstantOperation(String value) {
		this(null, value);
	}

	/**
	 * Creates a Constant operation with a given name and value.
	 * 
	 * @param name  Name of the operation (for identification purposes)
	 * @param value Constant value of the operation.
	 */
	public HydeConstantOperation(String name, String value) {
		super(name, value);
	}

	@Override
	public String apply(CSVRecord t) {
		return getSource();
	}

	@Override
	public void addChild(HydeOperation e) {
		throw new OperationBuildingException("Constant operation cannot have children");
	}

	@Override
	public void setChildren(List<HydeOperation> children) {
		throw new OperationBuildingException("Constant operation cannot have children");
	}

	@Override
	public List<HydeOperation> getChildren() {
		return List.of();
	}

	@Override
	public List<HydeFilter> getFilters() {
		return List.of();
	}

	@Override
	public void addFilter(HydeFilter filter) {
		throw new OperationBuildingException("Constant operation cannot have children");
	}

	@Override
	public void setFilters(List<HydeFilter> filters) {
		throw new OperationBuildingException("Constant operation cannot have children");
	}

}
