package com.github.sylordis.csvreorganiser.model.chess.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.SelfFiller;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * Abstract class for all operations. Each operation represents the data which will be processed to
 * form the values of every columns, included the name.
 *
 * When defining a new operation, it is vital that they possess a constructor with the same
 * signature as {@link #ReorgOperation(String)} as it is invoking the
 * {@link SelfFiller#fill(Object)} method to set all its internal fields to the proper values.
 *
 * @author sylordis
 *
 */
public abstract class ChessAbstractReorgOperation implements ReorganiserOperation, SelfFiller<Map<String, Object>> {

	/**
	 * Name of the column it will represent.
	 */
	private String name;
	/**
	 * Children operations in case of nested operations. For later uses when this will be implemented.
	 */
	private List<ChessAbstractReorgOperation> children;
	/**
	 * All properties to be defined for this operation.
	 */
	private final Map<String, String> properties;
	/**
	 * Name of all required properties for this operation.
	 */
	private final List<String> requiredProperties;
	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Constructs a new operation.
	 *
	 * @param name
	 */
	public ChessAbstractReorgOperation(String name) {
		this.logger = LogManager.getLogger();
		if (name == null || name.isEmpty())
			throw new ConfigurationImportException("Name of operation cannot be null or empty");
		this.name = name;
		this.children = new ArrayList<>();
		this.properties = new HashMap<>();
		this.requiredProperties = new ArrayList<>();
		setup();
	}

	/**
	 * Hidden setup of the operation, setting all properties to be given using
	 * {@link #addProperty(String, String)}. If not overridden, this method will get all annotations
	 * {@link OperationProperty} to automatically fill the properties map.
	 */
	protected void setup() {
		logger.debug("Setting up");
		OperationProperty[] properties = this.getClass().getAnnotationsByType(OperationProperty.class);
		logger.debug("class={} annotations={}", this.getClass(), Arrays.toString(properties));
		for (OperationProperty prop : properties) {
			logger.debug("Setting property {} linking to field {}", prop.name(), prop.field());
			addProperty(prop.name(), prop.field());
			if (prop.required())
				addRequiredProperty(prop.name());
		}
		logger.debug("setup complete: {}", this.properties);
	}

	/**
	 * Adds a property to the innate configuration of this operation.
	 *
	 * @param name  Name of the property to collect from output
	 * @param field Name of the field in the class to fill with this output
	 */
	protected void addProperty(String name, String field) {
		logger.debug("{} name={}, field={}", this.getClass(), name, field);
		properties.put(name, field);
	}

	/**
	 * Flags a property as required for this operation. This does not add it to the list of properties
	 * and does not check either if the property actually exists.
	 * 
	 * @param name Name of the required property
	 * @see #addProperty(String, String)
	 */
	protected void addRequiredProperty(String name) {
		requiredProperties.add(name);
	}

	@Override
	public void fill(Map<String, Object> data) throws SelfFillingException {
		logger.debug("Filling {}", this);
		for (Map.Entry<String, String> property : this.properties.entrySet()) {
			final String key = property.getKey();
			logger.debug("Filling {} with {} / value={}", property.getValue(), key, data.get(key));
			// Check if property exists
			if (data.containsKey(key)) {
				try {
					setField(property.getValue(), data.get(key));
				} catch (NumberFormatException e) {
					throw new SelfFillingException(
					        "Cannot convert given value [" + data.get(key) + "] for '" + key + "' to integer");
				}
			} else
				throw new SelfFillingException("Mandatory property '" + key + "' not provided.");
		}
		logger.debug("Finished filling: {}", this);
	}

	/**
	 * Creates a new harmonised exception if a property has not been filled. To be used in
	 * {@link #apply(CSVRecord)}.
	 *
	 * @param property Name of the unfilled property
	 * @return an {@link IllegalArgumentException}
	 */
	public final IllegalArgumentException createMissingPropertyException(String property) {
		return new IllegalArgumentException("'" + property + "' property not provided for column '" + getName() + "'");
	}

	/**
	 * Applies the operation to the record to return the desired value.
	 *
	 * @param record CSVRecord to get the value from
	 * @return the desired value
	 * @throws IllegalArgumentException if the requested record column does not exist
	 * @see #createMissingPropertyException(String)
	 */
	@Override
	public abstract String apply(CSVRecord record);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChessAbstractReorgOperation other = (ChessAbstractReorgOperation) obj;
		if (!name.equals(other.name))
			return false;
		if (!properties.equals(other.properties))
			return false;
		return true;
	}

	/**
	 * Gets the header name of the column.
	 *
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the column.
	 *
	 * @param name new name for the column
	 * @throws ConfigurationImportException if name is null or empty
	 */
	public void setName(String name) {
		if (name == null || name.isEmpty())
			throw new ConfigurationImportException("Name of operation cannot be null or empty");
		this.name = name;
	}

	/**
	 * Gets a list of mandatory properties. This never returns a null object.
	 *
	 * @return a list of properties names
	 */
	public List<String> getRequiredProperties() {
		return requiredProperties;
	}

	/**
	 * Gets the map of mandatory properties.
	 *
	 * @return a map of property name => field name
	 */
	protected Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Adds a new sub-operation.
	 *
	 * @param op child operation to add
	 */
	public void addChild(ChessAbstractReorgOperation op) {
		this.children.add(op);
	}

	/**
	 * Checks if this operation has sub-operations/children.
	 *
	 * @return true if this operation has children operations, false otherwise
	 */
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	/**
	 * Returns the list of children. This method never returns null but the list can be empty.
	 *
	 * @return the list children
	 */
	public List<ChessAbstractReorgOperation> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ChessAbstractReorgOperation> children) {
		this.children = children;
	}

}
