package com.github.sylordis.tools.csvreorganiser.model.chess;

import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.Chess.OPDEF_COLUMN_KEY;
import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.Chess.OPDEF_OPERATION_KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import com.github.sylordis.tools.csvreorganiser.model.chess.annotations.ChessOperation;
import com.github.sylordis.tools.csvreorganiser.model.chess.config.dictionary.ChessConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.chess.config.dictionary.ChessDefaultConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.chess.operations.ChessOperationBuilder;
import com.github.sylordis.tools.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.tools.csvreorganiser.model.constants.ConfigConstants.Chess;
import com.github.sylordis.tools.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.tools.csvreorganiser.utils.yaml.YAMLUtils;

/**
 * The Chess engine functions on clear syntax of simple operations, based on pre-approved
 * operations from a dictionary. Operations declarations can be simplified via the setting of
 * "shortcuts" which allow a single keyword to define a value, instead of specifying all parameters.
 * Shortcut operations can only be applied to operations that need only one parameter to be
 * provided.<br/>
 * <br/>
 * 
 * Chess Engine can be extended or even replaced by introducing new operations and a new dictionary.
 * The default Chess Engine can be instantiated via {@link ChessEngine#createDefaultEngine()} and
 * contains all operations that meet the following conditions:
 * <ul>
 * <li>Class is located into or under the package
 * ConfigConstants#{@link Chess#OPERATIONS_PACKAGE}.</li>
 * <li>Class is annotated with {@link ChessOperation}.</li>
 * </ul>
 * Operations will be collected via reflection if they meet these criteria. Each operation is in
 * charge of describing via other annotations their structure and parameters.
 * 
 * @author sylordis

 *
 */
public class ChessEngine implements ReorganiserEngine {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();
	/**
	 * Dictionary of all existing operations.
	 */
	private final Map<String, Class<? extends ChessAbstractReorgOperation>> operationsDictionary;
	/**
	 * Dictionary of all existing shortcuts to operations.
	 */
	private final Map<String, ChessOperationInstantiator> operationsShortcutsDictionary;

	/**
	 * Constructs a chess engine without a dictionary supplier.
	 */
	public ChessEngine() {
		this.operationsDictionary = new HashMap<>();
		this.operationsShortcutsDictionary = new HashMap<>();
	}

	/**
	 * Constructs a chess engine with a given dictionary supplier.
	 * 
	 * @param dictionarySupplier
	 */
	public ChessEngine(ChessConfigurationSupplier dictionarySupplier) {
		this();
		setOperationsDictionary(dictionarySupplier.getOperationsDictionary());
		setOperationsShortcutsDictionary(dictionarySupplier.getShortcutDictionary());
	}

	/**
	 * Constructs a Chess engine with default configuration supplier.
	 * 
	 * @return a chess engine
	 */
	public static ChessEngine createDefaultEngine() {
		return new ChessEngine(new ChessDefaultConfigurationSupplier());
	}

	/**
	 * Transforms a Yaml entry into a usable operation by the software.
	 *
	 * @param yaml yaml definition of the operation
	 * @throws ConfigurationImportException if a yaml configuration object is malformed
	 * @return an operation
	 */
	public ChessAbstractReorgOperation createOperation(Map<String, Object> yaml) {
		ChessAbstractReorgOperation op = null;
		logger.debug("yamlToOp[in]: ({}){}", yaml.getClass(), yaml);
		// Check that column tag is present (all)
		if (yaml.containsKey(OPDEF_COLUMN_KEY)) {
			String columnName = (String) yaml.get(OPDEF_COLUMN_KEY);
			// Checking specification: shortcuts, single operation or nested
			if (yaml.containsKey(OPDEF_OPERATION_KEY)) {
				// Single operation
				logger.debug("Single operation");
				op = new ChessOperationBuilder(operationsDictionary).fromData(columnName,
				        YAMLUtils.get(OPDEF_OPERATION_KEY, yaml));
			} else if (checkIfOneIsShortcut(yaml.keySet())) {
				// Shortcut
				logger.debug("Shortcut operation: {}", findShortcut(yaml.keySet()));
				// This cannot fail as we checked before that the key exists
				op = operationsShortcutsDictionary.get(findShortcut(yaml.keySet())).apply(columnName, yaml);
				logger.debug("Built: {}", op.getClass().getSimpleName());
			} else {
				Message msg = logger.getMessageFactory().newMessage(
				        "No proper configuration of operation detected for {}, requires one of: {}", yaml,
				        operationsShortcutsDictionary.keySet().toArray());
				throw new ConfigurationImportException(msg.getFormattedMessage());
			}
		} else {
			Message msg = logger.getMessageFactory().newMessage("mandatory '{}' tag not present in {}",
			        OPDEF_COLUMN_KEY, yaml);
			throw new ConfigurationImportException(msg.getFormattedMessage());
		}
		logger.debug("yamlToOp[out]: {}", op);
		return op;
	}

	/**
	 * Finds a shortcut in a set of entries.
	 *
	 * @param set set of keys from YAML
	 * @return the shortcut name if it exists in the shortcuts dictionary
	 */
	private String findShortcut(Set<String> set) {
		Optional<String> entry = set.stream().filter(e -> operationsShortcutsDictionary.containsKey(e)).findFirst();
		return entry.get();
	}

	/**
	 * Checks if a shortcut appears in the given set of YAML properties.
	 *
	 * @param set set of keys from YAML
	 * @return true if one entry of the set is a shortcut from the shortcuts dictionary
	 */
	public boolean checkIfOneIsShortcut(Set<String> set) {
		Optional<String> entry = set.stream().filter(e -> operationsShortcutsDictionary.containsKey(e)).findFirst();
		return entry.isPresent();
	}

	/**
	 * Sets dictionary of all existing operations.
	 *
	 * @param operationDictionary a map of all operations indexed by their names
	 * @throws ConfigurationException when supplying a null map
	 */
	public void setOperationsDictionary(Map<String, Class<? extends ChessAbstractReorgOperation>> operationDictionary) {
		this.operationsDictionary.clear();
		if (operationDictionary != null)
			this.operationsDictionary.putAll(operationDictionary);
		else
			throw new ConfigurationException("Can't supply null operations dictionary");
	}

	/**
	 * Get the dictionary of all possible operations.
	 *
	 * @return the operationsDictionary
	 */
	public Map<String, Class<? extends ChessAbstractReorgOperation>> getOperationsDictionary() {
		return operationsDictionary;
	}

	/**
	 * Sets dictionary of all shortcuts to operations.
	 *
	 * @param operationsShortcutsDictionary map of all shortcuts to operations indexed by their names
	 * @throws ConfigurationException when supplying a null map
	 */
	public void setOperationsShortcutsDictionary(
	        Map<String, ChessOperationInstantiator> operationsShortcutsDictionary) {
		this.operationsShortcutsDictionary.clear();
		if (operationsShortcutsDictionary != null)
			this.operationsShortcutsDictionary.putAll(operationsShortcutsDictionary);
		else
			throw new ConfigurationException("Can't supply null operations shortcuts dictionary");
	}

	/**
	 * Get the dictionary of all possible shortcuts.
	 *
	 * @return the operationsDictionary
	 */
	public Map<String, ChessOperationInstantiator> getOperationsShortcutsDictionary() {
		return operationsShortcutsDictionary;
	}

}
