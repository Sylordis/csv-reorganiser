package com.github.sylordis.csvreorganiser.model.chess.config.dictionary;

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

import com.github.sylordis.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperation;
import com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperationProperty;
import com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.utils.TypeConverter;

/**
 * Default configuration supplier for {@link ReorgConfiguration}, building a dictionary out of all
 * operations contained in {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs},
 * extending {@link ChessAbstractReorgOperation} and possessing the {@link ChessOperation} annotation.
 * Shortcuts are created from {@link ChessAbstractReorgOperation} that satisfy previous condition and are
 * also annotated with {@link ChessOperationShortcut}.
 *
 * @author sylordis
 * @Since 1.0
 *
 */
public class ChessDefaultConfigurationSupplier implements ChessConfigurationSupplier {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	@Override
	public Map<String, Class<? extends ChessAbstractReorgOperation>> getOperationsDictionary() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> map = new HashMap<>();
		Set<Class<? extends ChessAbstractReorgOperation>> types = getOperationsByReflection();
		types.forEach(t -> map.put(t.getAnnotation(ChessOperation.class).name(), t));
		return map;
	}

	@Override
	public Map<String, ChessOperationInstantiator> getShortcutDictionary() {
		Map<String, ChessOperationInstantiator> shortcuts = new HashMap<>();
		logger.trace("Building shortcuts dictionary");
		Set<Class<? extends ChessAbstractReorgOperation>> types = getOperationsByReflection();
		for (Class<? extends ChessAbstractReorgOperation> type : types) {
			ChessOperationShortcut opShortAnn = type.getAnnotation(ChessOperationShortcut.class);
			if (opShortAnn != null) {
				// Check if definition of the operation was properly done, i.e.: the shortcut name must match one of
				// the property
				Optional<ChessOperationProperty> shortcutProp = Arrays
						.stream(type.getAnnotationsByType(ChessOperationProperty.class))
						.filter(a -> a.name().equals(opShortAnn.property())).findFirst();
				if (shortcutProp.isEmpty()) {
					throw new ConfigurationException(
							String.format("Shortcut configuration '%s' for %s did not match any property '%s'",
									opShortAnn.keyword(), type.getSimpleName(), opShortAnn.property()));
				}
				try {
					ChessOperationProperty opProperty = shortcutProp.get();
					// Get operation property field for typing
					Field opField = type.getDeclaredField(opProperty.field());
					// Retrieve constructor
					Constructor<? extends ChessAbstractReorgOperation> opBuilder = type.getDeclaredConstructor(String.class,
							opField.getType());
					// Build dynamic constructor
					ChessOperationInstantiator creator = (name, data) -> {
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
	 * Retrieves all operations by reflection.
	 *
	 * @return a set of all operations
	 */
	public Set<Class<? extends ChessAbstractReorgOperation>> getOperationsByReflection() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().forPackage(ConfigConstants.Chess.OPERATIONS_PACKAGE));
		Set<Class<? extends ChessAbstractReorgOperation>> types = reflections.getSubTypesOf(ChessAbstractReorgOperation.class);
		return types.stream().filter(t -> t.isAnnotationPresent(ChessOperation.class)).collect(Collectors.toSet());
	}
}
