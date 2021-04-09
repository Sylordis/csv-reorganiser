package com.sylordis.tools.csv.reorganiser.model;

import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_COLUMN_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_OPERATIONS_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_OPERATION_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_ROOT_KEY;
import static com.sylordis.tools.csv.reorganiser.utils.yaml.YAMLUtils.get;
import static com.sylordis.tools.csv.reorganiser.utils.yaml.YAMLUtils.strValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.yaml.snakeyaml.Yaml;

import com.sylordis.tools.csv.reorganiser.model.config.dictionary.ConfigurationSupplier;
import com.sylordis.tools.csv.reorganiser.model.config.dictionary.DefaultConfigurationSupplier;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.GetOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation;
import com.sylordis.tools.csv.reorganiser.utils.yaml.YAMLType;
import com.sylordis.tools.csv.reorganiser.utils.yaml.YAMLUtils;

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
	// TODO dynamic operation shortcuts configuration

	/**
	 * Constructs a new configuration without any operation dictionary.
	 */
	public ReorgConfiguration() {
		this.logger = LogManager.getLogger();
		this.operationsDictionary = new HashMap<>();
		this.operations = new ArrayList<>();
	}

	/**
	 *
	 * @param dictionarySupplier
	 */
	public ReorgConfiguration(ConfigurationSupplier dictionarySupplier) {
		this();
		setOperationsDictionary(dictionarySupplier.get());
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
			if (root.containsKey(YAMLtags.OPDEF_ROOT_KEY)) {
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
				op = new ReorgOperationBuilder(operationsDictionary).fromData(columnName,
						get(OPDEF_OPERATION_KEY, yaml));
			} else if (yaml.containsKey(GetOperation.OPDATA_SOURCE_ID)) {
				// Shortcut to get
				op = new GetOperation(columnName, strValue(GetOperation.OPDATA_SOURCE_ID, yaml));
			} else if (yaml.containsKey(ValueOperation.OPDATA_VALUE)) {
				// Shortcut to value
				op = new ValueOperation(columnName, strValue(ValueOperation.OPDATA_VALUE, yaml));
			} else {
				Message msg = logger.getMessageFactory().newMessage(
						"No proper configuration of operation detected for {}, requires one of: {}",
						yaml,
						Arrays.toString(new String[] { OPDEF_OPERATIONS_KEY, OPDEF_OPERATION_KEY,
								GetOperation.OPDATA_SOURCE_ID, ValueOperation.OPDATA_VALUE }));
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
	public static ReorgConfiguration fromFile(File cfgFile)
			throws FileNotFoundException, IOException, ConfigurationImportException {
		ReorgConfiguration config = new ReorgConfiguration(new DefaultConfigurationSupplier());
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
	 * Sets dictionary of all operations provided by given supplier.
	 *
	 * @param dictionarySupplier
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

}
