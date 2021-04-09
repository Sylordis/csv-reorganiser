package com.sylordis.tools.csv.reorganiser.model.config.dictionary;

import java.util.Map;
import java.util.function.Supplier;

import com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * Interface for all configuration suppliers for {@link ReorgConfiguration}.
 *
 * @author sylordis
 *
 */
public interface ConfigurationSupplier extends Supplier<Map<String, Class<? extends AbstractReorgOperation>>> {

}
