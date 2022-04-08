package com.github.sylordis.tools.csvreorganiser.model.config.dictionary;

import java.util.Map;

import com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.OperationInstantiator;

/**
 * Interface for all configuration suppliers for {@link ReorgConfiguration}.
 *
 * @author sylordis
 *
 */
public interface ConfigurationSupplier {

	/**
	 * Provides the dictionary of operations. This method should be only called when setting or
	 * replacing the dictionary is needed.
	 *
	 * @return
	 */
	Map<String, Class<? extends AbstractReorgOperation>> getOperationsDictionary();

	/**
	 * Provides the dictionary of shortcuts for operations along with a way to instantiate such
	 * operations from the shortcut, if provided the name and the YAML data.
	 *
	 * Mapping of first map is Shortcut key => instantiator
	 *
	 * @return a map of shortcuts and suppliers of such operations
	 * @throws ConfigurationException if configuration is not correctly done
	 */
	Map<String, OperationInstantiator> getShortcutDictionary();
}
