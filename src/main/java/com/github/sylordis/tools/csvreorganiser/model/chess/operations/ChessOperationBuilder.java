package com.github.sylordis.tools.csvreorganiser.model.chess.operations;

import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.Chess.OPDATA_TYPE_KEY;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.OperationBuildingException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException;
import com.github.sylordis.tools.csvreorganiser.utils.yaml.YAMLUtils;

/**
 * Auto builder for all reorganisation operations, basing its values and configuration upon an
 * operation dictionary that is provided upon construction.
 *
 * @author sylordis
 *
 */
public class ChessOperationBuilder {

	/**
	 * Dictionary of all operations to be performed.
	 */
	private Map<String, Class<? extends ChessAbstractReorgOperation>> operationDictionary;
	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Constructs a new builder.
	 */
	public ChessOperationBuilder() {
		this.logger = LogManager.getLogger();
		this.operationDictionary = new HashMap<>();
	}

	/**
	 * Constructs a new builder with provided dictionary.
	 *
	 * @param operationDictionary
	 */
	public ChessOperationBuilder(Map<String, Class<? extends ChessAbstractReorgOperation>> operationDictionary) {
		this();
		setOperationDictionary(operationDictionary);
	}

	/**
	 * Builds the full operation from YAML syntax in one go, using
	 * {@link ChessAbstractReorgOperation#fill(Map)}. This method does not support inner classes as it is
	 * using {@link Class#getConstructor(Class...)} and {@link Constructor#newInstance(Object...)}.
	 *
	 * @param yaml YAML data of the operation
	 * @return the operation or null if data is missing
	 */
	public ChessAbstractReorgOperation fromData(String name, Map<String, Object> yaml) {
		logger.debug("data={}", yaml);
		ChessAbstractReorgOperation op = null;
		try {
			// Check for type specification
			final String dataType = YAMLUtils.strValue(YAMLTags.Chess.OPDATA_TYPE_KEY, yaml);
			if (dataType == null)
				throw new OperationBuildingException("No type specified for operation (key=" + OPDATA_TYPE_KEY + ")");
			final Class<? extends ChessAbstractReorgOperation> type = getClassFromType(dataType);
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
	public Class<? extends ChessAbstractReorgOperation> getClassFromType(String type) {
		Class<? extends ChessAbstractReorgOperation> result = null;
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
	public Map<String, Class<? extends ChessAbstractReorgOperation>> getOperationDictionary() {
		return Collections.unmodifiableMap(operationDictionary);
	}

	/**
	 * @param operationDictionary the operationDictionary to set
	 */
	public void setOperationDictionary(Map<String, Class<? extends ChessAbstractReorgOperation>> operationDictionary) {
		this.operationDictionary.clear();
		if (operationDictionary != null)
			operationDictionary.entrySet().stream()
			.forEach(e -> this.operationDictionary.put(e.getKey().toLowerCase(), e.getValue()));
	}

}
