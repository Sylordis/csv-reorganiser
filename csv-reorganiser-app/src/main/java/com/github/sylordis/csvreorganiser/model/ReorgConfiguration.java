package com.github.sylordis.csvreorganiser.model;

import static com.github.sylordis.csvreorganiser.model.constants.YAMLTags.OPDEF_ROOT_KEY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationBuilder;
import com.github.sylordis.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.csvreorganiser.model.engines.EngineFactory;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.EngineException;
import com.github.sylordis.csvreorganiser.utils.yaml.YAMLUtils;

/**
 *
 * Configuration of the operations to perform to transform the source CSV file to the target. Each
 * entry of this list represents a column in the target CSV, in order.
 *
 * This also holds the configuration of all possible operations that can be passed on to other
 * objects, like {@link ChessOperationBuilder}.
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
	private final List<ReorganiserOperation> operations;
	/**
	 * Engine to be used for the reorganisation.
	 */
	private ReorganiserEngine engine;

	/**
	 * Constructs a new configuration without an Engine.
	 */
	public ReorgConfiguration() {
		this.logger = LogManager.getLogger();
		this.operations = new ArrayList<>();
	}

	/**
	 * Constructs a new configuration with an Engine.
	 */
	public ReorgConfiguration(ReorganiserEngine engine) {
		this();
		this.engine = engine;
	}

	/**
	 * Loads a new configuration from a file. Old configuration will be cleared.
	 *
	 * @param cfgFile yaml configuration file to load
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException if the configuration is wrong or empty
	 * @throws EngineException
	 */
	public void loadFromFile(File cfgFile)
	        throws FileNotFoundException, IOException, ConfigurationImportException, EngineException {
		operations.clear();
		Yaml yamlFile = new Yaml();
		try (FileInputStream yamlStream = new FileInputStream(cfgFile)) {
			// Set specified engine if provided
			logger.info("Loading YAML file");
			Map<String, Object> fileRoot = yamlFile.load(yamlStream);
			if (fileRoot == null)
				throw new ConfigurationImportException("Configuration file is empty");
			if (!fileRoot.containsKey(YAMLTags.CFG_ROOT))
				throw new ConfigurationImportException("Error in configuration file: no '" + YAMLTags.CFG_ROOT + "' tag was found as root.");
			Map<String,Object> cfgRoot = YAMLUtils.toNode(fileRoot.get(YAMLTags.CFG_ROOT));
			logger.debug("Loaded YAML root");
			logger.debug("Loaded yaml file {}:\n{}", cfgFile.getAbsolutePath(), fileRoot);
			// Header analysis
			if (cfgRoot.containsKey(YAMLTags.CFG_HEADER_KEY)) {
				Map<String, Object> header = YAMLUtils.toNode(cfgRoot.get(YAMLTags.CFG_HEADER_KEY));
				logger.debug("Analysing header: {}", header);
				checkHeaderConfiguration(header);
			}
			// Check if engine is present before loading operations
			if (this.engine == null) {
				// If not, set default chess engine
				logger.info("No engine specified, getting default engine");
				this.engine = EngineFactory.getDefaultEngine();
			}
			// Check that structure tag is present at root
			if (!cfgRoot.containsKey(YAMLTags.OPDEF_ROOT_KEY))
				throw new ConfigurationImportException(
				        "Error in configuration file: no '" + OPDEF_ROOT_KEY + "' tag was found in the configuration.");
			logger.debug("Checking '{}' tag: ({}){}", OPDEF_ROOT_KEY, cfgRoot.get(OPDEF_ROOT_KEY).getClass(),
					cfgRoot.get(OPDEF_ROOT_KEY));
			this.operations.addAll(engine.createOperations(cfgRoot));
			logger.debug("{}", this);
			logger.info("Configuration imported.");
		} catch (ClassCastException e) {
			throw new ConfigurationImportException("Provided file is not a yaml file", e);
		}
	}

	/**
	 * Checks the configuration specified in the header.
	 * 
	 * @param header
	 * @throws EngineException 
	 */
	public void checkHeaderConfiguration(Map<String, Object> header) throws EngineException {
		logger.debug("Checking engine configuration and compatibility with header {}", header);
		String engineId = YAMLUtils.strValue(YAMLTags.Header.CFG_ENGINE, header);
		logger.debug("EngineId = '{}'", engineId);
		ReorganiserEngine fileEngine = new EngineFactory().getEngineFromId(engineId);
		logger.debug("Local engine is {}, header declared engine is '{}'",
		        this.engine != null ? this.engine.getClass().getSimpleName() : null,
		        fileEngine != null ? fileEngine.getClass().getSimpleName() : null);
		// Local engine is not specified, set it to file's
		if (this.engine == null) {
				this.engine = fileEngine;
		} else if (fileEngine != null && fileEngine.equals(engine)) {
			// Local engine is specified, check if they are different
			throw new EngineException("Configuration file specifies different engine that the one used");
		}
		// If they are the same, nothing to do, just keep it
	}

	/**
	 * Adds an operation to the end of the list of operations to perform.
	 *
	 * @param op operation to be inserted
	 */
	public void addOperation(ChessAbstractReorgOperation op) {
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
	public void addOperation(int index, ChessAbstractReorgOperation op) {
		if (op == null)
			throw new IllegalArgumentException("Added operations cannot be null");
		operations.add(Math.min(index, operations.size()), op);
	}

	/**
	 * Static builder for the class to be used when importing configuration from a file. A new
	 * configuration will be created without an engine.
	 *
	 * @see #loadFromFile(File)
	 *
	 * @param cfgFile YAML configuration file to load
	 * @return A new configuration
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException
	 */
	public static ReorgConfiguration fromFile(File cfgFile)
	        throws FileNotFoundException, IOException, ConfigurationImportException, EngineException {
		ReorgConfiguration config = new ReorgConfiguration();
		config.loadFromFile(cfgFile);
		return config;
	}

	/**
	 * Static builder for the class specifying an engine.
	 * 
	 * @see #loadFromFile(File)
	 * 
	 * @param cfgFile YAML configuration file to load
	 * @param engine
	 * @return A new configuration
	 * @throws FileNotFoundException        if no file is found
	 * @throws IOException                  if something goes wrong while reading the configuration file
	 * @throws ConfigurationImportException
	 * @throws EngineException if no engine is set once the file is loaded
	 */
	public static ReorgConfiguration fromFile(File cfgFile, ReorganiserEngine engine)
	        throws FileNotFoundException, IOException, ConfigurationImportException, EngineException {
		ReorgConfiguration config = new ReorgConfiguration(engine);
		config.loadFromFile(cfgFile);
		return config;
	}

	/**
	 * Gets the unmodifiable list of all operations in configuration. This list can be empty but never
	 * null.
	 *
	 * @return an unmodifiable list of operations
	 */
	public List<ReorganiserOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

	/**
	 * @return the engine
	 */
	public ReorganiserEngine getEngine() {
		return engine;
	}

	/**
	 * Checks that an engine is present in the configuration.
	 * 
	 * @return true if an engine is set, false otherwise.
	 */
	public boolean hasEngine() {
		return null != null;
	}

	/**
	 * @param engine the engine to set
	 */
	public void setEngine(ReorganiserEngine engine) {
		this.engine = engine;
	}

}
