package com.sylordis.tools.csv.reorganiser.model;

import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_COLUMN_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_MAIN_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_OPERATIONS_KEY;
import static com.sylordis.tools.csv.reorganiser.model.YAMLtags.OPDEF_OPERATION_KEY;
import static com.sylordis.tools.csv.reorganiser.utils.YAMLUtils.get;
import static com.sylordis.tools.csv.reorganiser.utils.YAMLUtils.strValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.yaml.snakeyaml.Yaml;

import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.ReorgOperationBuilder;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.GetOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation;

/**
 *
 * Configuration of the operations to perform to transform the source CSV file to the target. Each
 * entry of this list represents a column in the target CSV, in order.
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
	 * List of operations to perform for reorganisation.
	 */
	private final List<AbstractReorgOperation> operations;

	/**
	 * Constructs a new configuration.
	 */
	public ReorgConfiguration() {
		logger = LogManager.getLogger();
		operations = new ArrayList<>();
	}

	/**
	 * Loads a new configuration from a file. Old configuration will be cleared.
	 *
	 * @param cfgFile yaml configuration file to load
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException if the configuration is wrong
	 */
	@SuppressWarnings("unchecked")
	public void loadFromFile(File cfgFile) throws FileNotFoundException, IOException, ConfigurationImportException {
		operations.clear();
		Yaml yamlFile = new Yaml();
		try (FileInputStream yamlStream = new FileInputStream(cfgFile)) {
			Map<String, Object> root = yamlFile.load(yamlStream);
			logger.debug("Loading yaml file {}: {}", cfgFile.getAbsolutePath(), root);
			// Check that structure tag is present at root
			if (root.containsKey(YAMLtags.OPDEF_MAIN_KEY)) {
				logger.debug("Checking '{}' tag: ({}){}", OPDEF_MAIN_KEY,
						root.get(OPDEF_MAIN_KEY).getClass(),
						root.get(OPDEF_MAIN_KEY));
				// Check that structure tag contains a usable list
				if (yamlCheckIfChildIsClass(root, OPDEF_MAIN_KEY, ArrayList.class)) {
					((ArrayList<Object>) root.get(OPDEF_MAIN_KEY)).stream()
					.map(o -> createOperation((Map<String, Object>) o)).forEach(operations::add);
				} else {
					throw new ConfigurationImportException(
							"Error in configuration file: 'structure' tag should contain a list of operations.");
				}
			} else {
				throw new ConfigurationImportException(
						"Error in configuration file: no 'structure' tag was found at the root.");
			}
			logger.debug("{}", this);
			logger.info("Configuration imported.");
		}
	}

	/**
	 * Checks if the key's values of a node is of given class.
	 *
	 * @param node Reference node where to get the tag from
	 * @param tag  Tag in the node to check values for
	 * @param type Type of the values to check against
	 * @return true if the child exists and its values matches the given class, false otherwise.
	 */
	private boolean yamlCheckIfChildIsClass(Map<String, Object> node, String tag, Class<?> type) {
		return node.containsKey(tag) && type.equals(node.get(tag).getClass());
	}

	/**
	 * Transforms a Yaml entry into a usable operation by the software.
	 *
	 * @param yaml
	 * @return
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
			} else if (yaml.containsKey(OPDEF_OPERATION_KEY)) {
				// Single operation
				op = new ReorgOperationBuilder().fromData(columnName,
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
		ReorgConfiguration config = new ReorgConfiguration();
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

}
