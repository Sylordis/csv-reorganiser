package com.github.sylordis.tools.csvreorganiser.model;

import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.OPDEF_COLUMN_KEY;
import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.OPDEF_OPERATIONS_KEY;
import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.OPDEF_OPERATION_KEY;
import static com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags.OPDEF_ROOT_KEY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.yaml.snakeyaml.Yaml;

import com.github.sylordis.tools.csvreorganiser.model.config.dictionary.ConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.OperationInstantiator;
import com.github.sylordis.tools.csvreorganiser.model.operations.ReorgOperationBuilder;
import com.github.sylordis.tools.csvreorganiser.utils.yaml.YAMLType;
import com.github.sylordis.tools.csvreorganiser.utils.yaml.YAMLUtils;

/**
 *
 * Configuration of the operations to perform to transform the source CSV file to the target. Each
 * entry of this list represents a column in the target CSV, in order.
 *
 * This also holds the configuration of all possible operations that can be passed on to other
 * objects, like {@link ReorgOperationBuilder}.
 *
 *
 * @author sylordis
 *
 */
public class ReorgConfiguration {

	/**
	 * Class logger.
	 */
	private final Logger logger;
	/**
	 * List of operations to perform for reorganisation. It can be empty, but never null.
	 */
	private final List<AbstractReorgOperation> operations;
	/**
	 * Dictionary of all existing operations.
	 */
	private final Map<String, Class<? extends AbstractReorgOperation>> operationsDictionary;
	/**
	 * Dictionary of all existing shortcuts to operations.
	 */
	private final Map<String, OperationInstantiator> operationsShortcutsDictionary;

	/**
	 * Constructs a new configuration without any operation dictionary.
	 */
	public ReorgConfiguration() {
		this.logger = LogManager.getLogger();
		this.operationsDictionary = new HashMap<>();
		this.operationsShortcutsDictionary = new HashMap<>();
		this.operations = new ArrayList<>();
	}

	/**
	 *
	 * @param dictionarySupplier
	 */
	public ReorgConfiguration(ConfigurationSupplier dictionarySupplier) {
		this();
		setOperationsDictionary(dictionarySupplier.getOperationsDictionary());
		setOperationsShortcutsDictionary(dictionarySupplier.getShortcutDictionary());
	}

	/**
	 * Loads a new configuration from a file. Old configuration will be cleared.
	 *
	 * @param cfgFile yaml configuration file to load
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException if the configuration is wrong or empty
	 */
	public void loadFromFile(File cfgFile) throws FileNotFoundException, IOException, ConfigurationImportException {
		operations.clear();
		Yaml yamlFile = new Yaml();
		try (FileInputStream yamlStream = new FileInputStream(cfgFile)) {
			Map<String, Object> root = yamlFile.load(yamlStream);
			if (root == null)
				throw new ConfigurationImportException("Configuration file is empty");
			logger.debug("Loading yaml file {}: {}", cfgFile.getAbsolutePath(), root);
			// Check that structure tag is present at root
			if (root.containsKey(YAMLTags.OPDEF_ROOT_KEY)) {
				logger.debug("Checking '{}' tag: ({}){}", OPDEF_ROOT_KEY,
						root.get(OPDEF_ROOT_KEY).getClass(),
						root.get(OPDEF_ROOT_KEY));
				// Check that structure tag contains a usable list
				if (YAMLUtils.checkChildType(root, OPDEF_ROOT_KEY, YAMLType.LIST)) {
					YAMLUtils.toList(root.get(OPDEF_ROOT_KEY)).stream().map(o -> createOperation(YAMLUtils.toNode(o)))
					.forEach(operations::add);
				} else {
					throw new ConfigurationImportException(
							"Error in configuration file: '" + OPDEF_ROOT_KEY
							+ "' tag should contain a list of operations.");
				}
			} else {
				throw new ConfigurationImportException(
						"Error in configuration file: no '" + OPDEF_ROOT_KEY + "' tag was found at the root.");
			}
			logger.debug("{}", this);
			logger.info("Configuration imported.");
		} catch (ClassCastException e) {
			throw new ConfigurationImportException("Provided file is not a yaml file", e);
		}
	}

	/**
	 * Transforms a Yaml entry into a usable operation by the software.
	 *
	 * @param yaml yaml definition of the operation
	 * @throws ConfigurationImportException if a yaml configuration object is malformed
	 * @return an operation
	 */
	public AbstractReorgOperation createOperation(Map<String, Object> yaml) {
		AbstractReorgOperation op = null;
		logger.debug("yamlToOp[in]: ({}){}", yaml.getClass(), yaml);
		// Check that column tag is present (all)
		if (yaml.containsKey(OPDEF_COLUMN_KEY)) {
			String columnName = (String) yaml.get(OPDEF_COLUMN_KEY);
			// Checking specification: shortcuts, single operation or nested
			if (yaml.containsKey(OPDEF_OPERATIONS_KEY)) {
				// TODO Nested operations
				throw new NotImplementedException("Nested operations is not implemented yet");
			} else if (yaml.containsKey(OPDEF_OPERATION_KEY)) {
				// Single operation
				logger.debug("Single operation");
				op = new ReorgOperationBuilder(operationsDictionary).fromData(columnName,
						YAMLUtils.get(OPDEF_OPERATION_KEY, yaml));
			} else if (checkIfShortcut(yaml.keySet())) {
				// Shortcut
				logger.debug("Shortcut operation: {}", findShortcut(yaml.keySet()));
				// This cannot fail as we checked before that the key exists
				op = operationsShortcutsDictionary.get(findShortcut(yaml.keySet())).apply(columnName, yaml);
				logger.debug("Built: {}", op.getClass().getSimpleName());
			} else {
				Message msg = logger.getMessageFactory().newMessage(
						"No proper configuration of operation detected for {}, requires one of: {}",
						yaml, operationsShortcutsDictionary.keySet().toArray());
				throw new ConfigurationImportException(msg.getFormattedMessage());
			}
		} else {
			Message msg = logger.getMessageFactory().newMessage("mandatory '{}' tag not present in {}",
					OPDEF_COLUMN_KEY,
					yaml);
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
	public boolean checkIfShortcut(Set<String> set) {
		Optional<String> entry = set.stream().filter(e -> operationsShortcutsDictionary.containsKey(e)).findFirst();
		return entry.isPresent();
	}

	/**
	 * Adds an operation to the end of the list of operations to perform.
	 *
	 * @param op operation to be inserted
	 */
	public void addOperation(AbstractReorgOperation op) {
		if (op == null)
			throw new IllegalArgumentException("Added operations cannot be null");
		operations.add(op);
	}

	/**
	 * Adds an operation at the specified position in the list of operations to perform.
	 *
	 * @param index index at which the operation is to be inserted
	 * @param op    operation to be inserted, if bigger than actual operation size, puts it as last in
	 *              the list.
	 */
	public void addOperation(int index, AbstractReorgOperation op) {
		if (op == null)
			throw new IllegalArgumentException("Added operations cannot be null");
		operations.add(Math.min(index, operations.size()), op);
	}

	/**
	 * Static builder for the class to be used when importing configuration from a file.
	 *
	 * @see #loadFromFile(File)
	 *
	 * @param cfgFile yaml configuration file to load
	 * @return A new configuration
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException
	 */
	public static ReorgConfiguration fromFile(File cfgFile, ConfigurationSupplier cfg)
			throws FileNotFoundException, IOException, ConfigurationImportException {
		ReorgConfiguration config = new ReorgConfiguration(cfg);
		config.loadFromFile(cfgFile);
		return config;
	}

	/**
	 * Gets the unmodifiable list of all operations in configuration. This list can be empty but never
	 * null.
	 *
	 * @return an unmodifiable list of operations
	 */
	public List<AbstractReorgOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

	/**
	 * Sets dictionary of all existing operations.
	 *
	 * @param operationDictionary a map of all operations indexed by their names
	 * @throws ConfigurationException when supplying a null map
	 */
	public void setOperationsDictionary(Map<String, Class<? extends AbstractReorgOperation>> operationDictionary) {
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
	public Map<String, Class<? extends AbstractReorgOperation>> getOperationsDictionary() {
		return operationsDictionary;
	}

	/**
	 * Sets dictionary of all shortcuts to operations.
	 *
	 * @param operationsShortcutsDictionary map of all shortcuts to operations indexed by their names
	 * @throws ConfigurationException when supplying a null map
	 */
	public void setOperationsShortcutsDictionary(Map<String, OperationInstantiator> operationsShortcutsDictionary) {
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
	public Map<String, OperationInstantiator> getOperationsShortcutsDictionary() {
		return operationsShortcutsDictionary;
	}

}
