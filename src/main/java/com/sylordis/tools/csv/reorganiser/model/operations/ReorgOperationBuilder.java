package com.sylordis.tools.csv.reorganiser.model.operations;

import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDATA_TYPE_KEY;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sylordis.tools.csv.reorganiser.model.YAMLtags;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.SelfFillingException;
import com.sylordis.tools.csv.reorganiser.utils.YAMLUtils;

/**
 * Auto builder for all reorganisation operations, basing its values and configuration upon an
 * operation dictionary that can be set via the following methods:
 * <ul>
 * <li>{@link #withDefaultConfiguration()}: takes all entries from {@link ReorgOperationType}</li>
 * <li>{@link #withCustomConfiguration(Map)}</li>
 * <li>{@link #addCustomConfiguration(Map)}</li>
 * <li>{@link #withReflectedConfiguration()} (future release)</li>
 * <li>{@link #withReflectedConfiguration(String)} (future release)</li>
 * </ul>
 *
 * Default configuration values are taken from {@link ReorgOperationType} values.
 *
 * @author sylordis
 *
 */
public class ReorgOperationBuilder {

	/**
	 * Dictionary of all operations to be performed.
	 */
	private final Map<String, Class<? extends AbstractReorgOperation>> operationDictionary;
	/**
	 * Pattern to filter operations classes with when loading a reflected configuration. Default value
	 * is {@link #DEFAULT_CLASS_FILTER}.
	 *
	 * @see #withReflectedConfiguration()
	 * @see #withReflectedConfiguration(String)
	 */
	private String classFilter;
	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Constructs a new builder.
	 */
	public ReorgOperationBuilder() {
		this.logger = LogManager.getLogger();
		this.operationDictionary = new HashMap<>();
		this.classFilter = null;
	}

	/**
	 * Builds the full operation from YAML syntax in one go, using
	 * {@link AbstractReorgOperation#fill(Map)}.
	 *
	 * @param yaml YAML data of the operation
	 * @return the operation or null if data is missing
	 */
	public AbstractReorgOperation fromData(String name, Map<String, Object> yaml) {
		logger.debug("data={}", yaml);
		AbstractReorgOperation op = null;
		try {
			// Check for type specification
			final String dataType = YAMLUtils.strValue(YAMLtags.OPDATA_TYPE_KEY, yaml);
			if (dataType == null)
				throw new OperationBuildingException("No type specified for operation (key=" + OPDATA_TYPE_KEY + ")");
			final Class<? extends AbstractReorgOperation> type = getClassFromType(dataType);
			if (type == null)
				throw new OperationBuildingException("Unknown type '" + dataType + "' (key=" + OPDATA_TYPE_KEY + ")");
			op = type.getDeclaredConstructor(String.class).newInstance(name);
			op.fill(yaml);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | SelfFillingException e) {
			throw new ConfigurationException(e);
		}
		return op;
	}

	/**
	 * Retrieves the type of an operation from its YAML definition. The check against the type is case
	 * insensitive.
	 *
	 * @param type name of the operation
	 * @return the class of the operation or null if it does not exist or if provided type is null
	 */
	public Class<? extends AbstractReorgOperation> getClassFromType(String type) {
		Class<? extends AbstractReorgOperation> result = null;
		if (type != null)
			result = this.operationDictionary.get(type.toLowerCase());
		return result;
	}

	/**
	 * Replaces the configuration with default values taken from {@link ReorgOperationType}.
	 *
	 * @see ReorgOperationType#getDefaultDictionary()
	 */
	public ReorgOperationBuilder withDefaultConfiguration() {
		operationDictionary.clear();
		operationDictionary.putAll(ReorgOperationType.getDefaultDictionary());
		return this;
	}

	/**
	 * Replaces the configuration via reflection. It will take all classes extending
	 * {@link AbstractReorgOperation}.
	 *
	 * @return itself
	 */
	public ReorgOperationBuilder withReflectedConfiguration() {
		throw new NotImplementedException("This feature will be implemented in future release.");
	}

	/**
	 * Replaces the configuration via reflection. It will take all classes extending
	 * {@link AbstractReorgOperation} and filter all results with pattern.
	 *
	 * @param filter only classes found by reflection with name matching this filter will be added to
	 *               the dictionary, null means no filter.
	 * @see #setClassFilter(String)
	 * @return itself
	 */
	public ReorgOperationBuilder withReflectedConfiguration(String filter) {
		throw new NotImplementedException("This feature will be implemented in future release.");
	}

	/**
	 * Replaces the current configuration with another dictionary.
	 *
	 * @param dictionary Dictionary to replace the current one
	 * @return itself
	 * @throws IllegalArgumentException if the provided dictionary is empty or null
	 */
	public ReorgOperationBuilder withCustomConfiguration(
			Map<String, Class<? extends AbstractReorgOperation>> dictionary) {
		if (dictionary == null || dictionary.isEmpty())
			throw new IllegalArgumentException("Provided dictionary cannot be null or empty");
		this.operationDictionary.clear();
		this.operationDictionary.putAll(dictionary);
		return this;
	}

	/**
	 * Adds custom configuration to current operation dictionary.
	 *
	 * @param dictionary Entries to add to the current dictionary
	 */
	public ReorgOperationBuilder addCustomConfiguration(
			Map<String, Class<? extends AbstractReorgOperation>> dictionary) {
		if (dictionary != null)
			this.operationDictionary.putAll(dictionary);
		return this;
	}

	/**
	 * @return the classFilter
	 */
	public String getClassFilter() {
		return classFilter;
	}

	/**
	 * @param filter the classPattern to set, null means no filter
	 */
	public void setClassFilter(String filter) {
		this.classFilter = filter;
	}

	/**
	 * Gets the dictionary for all operations as an unmodifiable map. If no configuration has been done,
	 * the result will be an empty map. This method never returns a null value.
	 *
	 * @return the operation dictionary
	 */
	public Map<String, Class<? extends AbstractReorgOperation>> getOperationDictionary() {
		return Collections.unmodifiableMap(operationDictionary);
	}

}
