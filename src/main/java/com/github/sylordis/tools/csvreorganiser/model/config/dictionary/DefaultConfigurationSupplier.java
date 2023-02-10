package com.github.sylordis.tools.csvreorganiser.model.config.dictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.tools.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.tools.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.OperationInstantiator;
import com.github.sylordis.tools.csvreorganiser.utils.TypeConverter;

/**
 * Default configuration supplier for {@link ReorgConfiguration}, building a dictionary out of all
 * operations contained in {@link com.github.sylordis.tools.csvreorganiser.model.operations.defs},
 * extending {@link AbstractReorgOperation} and possessing the {@link Operation} annotation.
 * Shortcuts are created from {@link AbstractReorgOperation} that satisfy previous condition and are
 * also annotated with {@link OperationShortcut}.
 *
 * @author sylordis
 * @Since 1.0
 *
 */
public class DefaultConfigurationSupplier implements ConfigurationSupplier {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	@Override
	public Map<String, Class<? extends AbstractReorgOperation>> getOperationsDictionary() {
		Map<String, Class<? extends AbstractReorgOperation>> map = new HashMap<>();
		Set<Class<? extends AbstractReorgOperation>> types = getOperationsByReflection();
		types.forEach(t -> map.put(t.getAnnotation(Operation.class).name(), t));
		return map;
	}

	@Override
	public Map<String, OperationInstantiator> getShortcutDictionary() {
		Map<String, OperationInstantiator> shortcuts = new HashMap<>();
		logger.trace("Building shortcuts dictionary");
		Set<Class<? extends AbstractReorgOperation>> types = getOperationsByReflection();
		for (Class<? extends AbstractReorgOperation> type : types) {
			OperationShortcut opShortAnn = type.getAnnotation(OperationShortcut.class);
			if (opShortAnn != null) {
				// Check if definition of the operation was properly done, i.e.: the shortcut name must match one of
				// the property
				Optional<OperationProperty> shortcutProp = Arrays
						.stream(type.getAnnotationsByType(OperationProperty.class))
						.filter(a -> a.name().equals(opShortAnn.property())).findFirst();
				if (shortcutProp.isEmpty()) {
					throw new ConfigurationException(
							String.format("Shortcut configuration '%s' for %s did not match any property '%s'",
									opShortAnn.keyword(), type.getSimpleName(), opShortAnn.property()));
				}
				try {
					OperationProperty opProperty = shortcutProp.get();
					// Get operation property field for typing
					Field opField = type.getDeclaredField(opProperty.field());
					// Retrieve constructor
					Constructor<? extends AbstractReorgOperation> opBuilder = type.getDeclaredConstructor(String.class,
							opField.getType());
					// Build dynamic constructor
					OperationInstantiator creator = (name, data) -> {
						try {
							return opBuilder.newInstance(name,
									TypeConverter.to(data.get(opShortAnn.keyword()), opField.getType()));
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException e) {
							throw new ConfigurationException(e);
						}
					};
					shortcuts.put(opShortAnn.keyword(), creator);
				} catch (NoSuchMethodException | SecurityException | NoSuchFieldException e) {
					throw new ConfigurationException(e);
				}
			}
		}
		return shortcuts;
	}

	/**
	 * Retrieves all operations by reflection:
	 *
	 * @return a set of all operations
	 */
	public Set<Class<? extends AbstractReorgOperation>> getOperationsByReflection() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().forPackage(ConfigConstants.OPERATIONS_PACKAGE));
		Set<Class<? extends AbstractReorgOperation>> types = reflections.getSubTypesOf(AbstractReorgOperation.class);
		return types.stream().filter(t -> t.isAnnotationPresent(Operation.class)).collect(Collectors.toSet());
	}
}
