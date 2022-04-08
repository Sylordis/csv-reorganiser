package com.github.sylordis.tools.csvreorganiser.model.operations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.tools.csvreorganiser.model.SelfFiller;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationRequiredProperty;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException;

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
public abstract class AbstractReorgOperation implements Function<CSVRecord, String>, SelfFiller<Map<String, Object>> {

	/**
	 * Name of the column it will represent.
	 */
	private String name;
	/**
	 * Children operations in case of nested operations. For later uses when this will be implemented.
	 */
	private List<AbstractReorgOperation> children;
	/**
	 * All properties to be defined for this operation.
	 */
	private final Map<String, String> properties;
	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Constructs a new operation.
	 *
	 * @param name
	 */
	public AbstractReorgOperation(String name) {
		this.logger = LogManager.getLogger();
		if (name == null || name.isEmpty())
			throw new ConfigurationImportException("Name of operation cannot be null or empty");
		this.name = name;
		this.children = new ArrayList<>();
		this.properties = new HashMap<>();
		setup();
	}

	/**
	 * Hidden setup of the operation, setting all properties to be given using
	 * {@link #addProperty(String, String)}. If not overridden, this method will get all annotations
	 * {@link OperationRequiredProperty} to automatically fill the properties map.
	 */
	protected void setup() {
		logger.debug("Setting up");
		OperationRequiredProperty[] properties = this.getClass().getAnnotationsByType(OperationRequiredProperty.class);
		logger.debug("class={} annotations={}", this.getClass(), Arrays.toString(properties));
		for (OperationRequiredProperty prop : properties) {
			logger.debug("Setting property {} linking to field {}", prop.name(), prop.field());
			addProperty(prop.name(), prop.field());
		}
		logger.debug("setup complete: {}", this.properties);
	}

	/**
	 * Adds a mandatory property to the innate configuration of this operation.
	 *
	 * @param name  Name of the property to collect from output
	 * @param field Name of the field in the class to fill with this output
	 */
	protected void addProperty(String name, String field) {
		logger.debug("{} name={}, field={}", this.getClass(), name, field);
		properties.put(name, field);
	}

	@Override
	public void fill(Map<String, Object> data) throws SelfFillingException {
		logger.debug("Filling {}", this);
		for (Map.Entry<String, String> property : this.properties.entrySet()) {
			final String key = property.getKey();
			logger.debug("Filling {} with {} / value={}", property.getValue(), key,
					data.get(key));
			// Check if property exists
			if (data.containsKey(key)) {
				boolean madeAccessible = false;
				Field field = null;
				try {
					field = this.getClass().getDeclaredField(property.getValue());
					// Accessibility check
					if (!field.canAccess(this)) {
						madeAccessible = true;
						field.setAccessible(true);
					}
					// Field type check
					if (Integer.TYPE.equals(field.getType())) {
						// Data type check
						if (data.get(key) != null && Integer.class.equals(data.get(key).getClass()))
							field.set(this, data.get(key));
						else
							field.set(this, Integer.valueOf((String) data.get(key)));
					} else
						field.set(this, data.get(key));

				} catch (NumberFormatException e) {
					throw new SelfFillingException(
							"Cannot convert given value [" + data.get(key) + "] for '" + key + "' to integer");
				} catch (IllegalArgumentException e) {
					// Error in provided data
					throw new SelfFillingException(e);
				} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// If this is thrown, then the Operation configuration has not been done properly by the developer
					throw new SelfFillingConfigurationException(e);
				} finally {
					// If was made accessible, return it to non accessible
					if (field != null && madeAccessible)
						field.setAccessible(false);
				}
			} else
				throw new SelfFillingException(
						"Mandatory property '" + key + "' not provided.");
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
		return new IllegalArgumentException(
				"'" + property + "' property not provided for column '" + getName() + "'");
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
		AbstractReorgOperation other = (AbstractReorgOperation) obj;
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
	public Collection<String> getRequiredProperties() {
		return properties.keySet();
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
	public void addChild(AbstractReorgOperation op) {
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
	public List<AbstractReorgOperation> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<AbstractReorgOperation> children) {
		this.children = children;
	}

}
