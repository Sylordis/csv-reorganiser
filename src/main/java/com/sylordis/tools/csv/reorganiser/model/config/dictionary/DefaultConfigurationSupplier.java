package com.sylordis.tools.csv.reorganiser.model.config.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration;
import com.sylordis.tools.csv.reorganiser.model.annotations.Operation;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * Default configuration supplier for {@link ReorgConfiguration}, building a dictionary out of all
 * operations contained in {@link com.sylordis.tools.csv.reorganiser.model.operations.defs},
 * extending {@link AbstractReorgOperation} and possessing the {@link Operation} annotation.
 *
 * @author sylordis
 * @Since 1.0
 *
 */
public class DefaultConfigurationSupplier implements ConfigurationSupplier {

	@Override
	public Map<String, Class<? extends AbstractReorgOperation>> get() {
		Map<String, Class<? extends AbstractReorgOperation>> map = new HashMap<>();
		Reflections reflections = new Reflections("com.sylordis.tools.csv.reorganiser.model.operations.defs");
		Set<Class<? extends AbstractReorgOperation>> types = reflections.getSubTypesOf(AbstractReorgOperation.class);
		for (Class<? extends AbstractReorgOperation> type : types) {
			if (type.isAnnotationPresent(Operation.class)) {
				map.put(type.getAnnotation(Operation.class).name(), type);
			}
		}
		return map;
	}

}
