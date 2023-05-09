package com.github.sylordis.tools.csvreorganiser.model.hyde;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.github.sylordis.tools.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;

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
	private List<HydeOperation> children;
	/**
	 * Column to get the data from.
	 */
	private String source;
	/**
	 * List of filters to apply. This list should never be null.
	 */
	private List<HydeFilter> filters;

	/**
	 * Constructs a blank Hyde operation.
	 */
	public HydeOperation() {
		this(null, null, (List<HydeFilter>) null);
	}

	/**
	 * Constructs a Hyde operation with just a name to itself.
	 * 
	 * @param name
	 */
	public HydeOperation(String name) {
		this(name, null, (List<HydeFilter>) null);
	}

	/**
	 * Constructs a Hyde operation with a given name (CSV Header result if it is the root), a source
	 * column and an optional list of filters to apply to this source.
	 * 
	 * @param name    CSV Header name of the column
	 * @param source  Name of source column where to take its value from
	 * @param filters Filters to apply to the value
	 */
	public HydeOperation(String name, String source, HydeFilter... filters) {
		this(name);
		this.source = source;
		if (filters != null)
			setFilters(List.of(filters));
	}

	/**
	 * Constructs a new Hyde operation with a given name (CSV Header result if it is the root), a column
	 * where to takes its source from and a list of filters to apply.
	 * 
	 * @param name    CSV Header name of the column
	 * @param source  Name of source column where to take its value from
	 * @param filters Filters to apply to the value
	 */
	public HydeOperation(String name, String source, List<HydeFilter> filters) {
		this.children = new ArrayList<>();
		this.filters = new ArrayList<>();
		this.name = name;
		this.source = source;
		if (filters != null)
			this.filters.addAll(filters);
	}

	@Override
	public String apply(CSVRecord t) {
		String result = null;
		if (hasChildren())
			result = String.join("", this.children.stream().map(c -> c.apply(t)).toArray(String[]::new));
		else if (null == source)
			throw new ConfigurationException("No source was specified for " + getName());
		else {
			// Get the record of the given column
			result = t.get(source);
			// Apply all filters (if any)
			for (HydeFilter f : filters) {
				result = f.apply(result);
			}
		}
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
	public void addChild(HydeOperation e) {
		this.children.add(e);
	}

	/**
	 * @return the children
	 */
	public List<HydeOperation> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<HydeOperation> children) {
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

	/**
	 * @return the source column
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source column to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the filters
	 */
	public List<HydeFilter> getFilters() {
		return filters;
	}

	/**
	 * Checks if this operation has filters.
	 * 
	 * @return
	 */
	public boolean hasFilters() {
		return !filters.isEmpty();
	}

	/**
	 * Adds a new filter to this operation at the end of the filters list, if it is not null.
	 * 
	 * @param filter
	 */
	public void addFilter(HydeFilter filter) {
		if (filter != null)
			this.filters.add(filter);
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(List<HydeFilter> filters) {
		this.filters.clear();
		if (null != filters)
			this.filters.addAll(filters);
	}

}
