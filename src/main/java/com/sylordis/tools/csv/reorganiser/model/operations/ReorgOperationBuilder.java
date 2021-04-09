package com.sylordis.tools.csv.reorganiser.model.operations;

import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDATA_TYPE_KEY;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sylordis.tools.csv.reorganiser.model.YAMLtags;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.SelfFillingException;
import com.sylordis.tools.csv.reorganiser.utils.yaml.YAMLUtils;

/**
 * Auto builder for all reorganisation operations, basing its values and configuration upon an
 * operation dictionary that is provided upon construction.
 *
 * @author sylordis
 *
 */
public class ReorgOperationBuilder {

	/**
	 * Dictionary of all operations to be performed.
	 */
	private Map<String, Class<? extends AbstractReorgOperation>> operationDictionary;
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
	}

	/**
	 * Constructs a new builder with provided dictionary.
	 *
	 * @param operationDictionary
	 */
	public ReorgOperationBuilder(Map<String, Class<? extends AbstractReorgOperation>> operationDictionary) {
		this();
		setOperationDictionary(operationDictionary);
	}

	/**
	 * Builds the full operation from YAML syntax in one go, using
	 * {@link AbstractReorgOperation#fill(Map)}. This method does not support inner classes as it is
	 * using {@link Class#getConstructor(Class...)} and {@link Constructor#newInstance(Object...)}.
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
	 * Gets the dictionary for all operations as an unmodifiable map. If no configuration has been done,
	 * the result will be an empty map. This method never returns a null value.
	 *
	 * @return the operation dictionary
	 */
	public Map<String, Class<? extends AbstractReorgOperation>> getOperationDictionary() {
		return Collections.unmodifiableMap(operationDictionary);
	}

	/**
	 * @param operationDictionary the operationDictionary to set
	 */
	public void setOperationDictionary(Map<String, Class<? extends AbstractReorgOperation>> operationDictionary) {
		this.operationDictionary.clear();
		if (operationDictionary != null)
			operationDictionary.entrySet().stream()
			.forEach(e -> this.operationDictionary.put(e.getKey().toLowerCase(), e.getValue()));
	}

}
