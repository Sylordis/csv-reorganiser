package com.github.sylordis.csvreorganiser.model.chess.config;

import java.util.Map;

import com.github.sylordis.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.engines.ConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;

/**
 * Interface for all configuration suppliers for {@link ReorgConfiguration}.
 *
 * @author sylordis
 *
 */
public interface ChessConfigurationSupplier extends ConfigurationSupplier<ChessAbstractReorgOperation> {

	/**
	 * Provides the dictionary of shortcuts for operations along with a way to instantiate such
	 * operations from the shortcut, if provided the name and the YAML data.
	 *
	 * Mapping of first map is Shortcut key => instantiator
	 *
	 * @return a map of shortcuts and suppliers of such operations
	 * @throws ConfigurationException if configuration is not correctly done
	 */
	Map<String, ChessOperationInstantiator> getShortcutDictionary();
}
