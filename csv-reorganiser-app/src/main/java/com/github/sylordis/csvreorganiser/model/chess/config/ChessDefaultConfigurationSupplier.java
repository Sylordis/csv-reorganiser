package com.github.sylordis.csvreorganiser.model.chess.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperation;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.utils.TypeConverter;

/**
 * Default configuration supplier for {@link ReorgConfiguration}, building a dictionary out of all
 * operations contained in {@link com.github.sylordis.csvreorganiser.model.chess.operations.defs},
 * extending {@link ChessAbstractReorgOperation} and possessing the {@link ReorgOperation} annotation.
 * Shortcuts are created from {@link ChessAbstractReorgOperation} that satisfy previous condition and are
 * also annotated with {@link ReorgOperationShortcut}.
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
	public Map<String, ChessOperationInstantiator> getShortcutDictionary() {
		Map<String, ChessOperationInstantiator> shortcuts = new HashMap<>();
		logger.trace("Building shortcuts dictionary");
		Set<Class<? extends ChessAbstractReorgOperation>> types = getConfigurationByReflection(ConfigConstants.Chess.OPERATIONS_PACKAGE, ChessAbstractReorgOperation.class);
		for (Class<? extends ChessAbstractReorgOperation> type : types) {
			ReorgOperationShortcut opShortAnn = type.getAnnotation(ReorgOperationShortcut.class);
			if (opShortAnn != null) {
				// Check if definition of the operation was properly done, i.e.: the shortcut name must match one of
				// the property
				Optional<ReorgOperationProperty> shortcutProp = Arrays
						.stream(type.getAnnotationsByType(ReorgOperationProperty.class))
						.filter(a -> a.name().equals(opShortAnn.property())).findFirst();
				if (shortcutProp.isEmpty()) {
					throw new ConfigurationException(
							String.format("Shortcut configuration '%s' for %s did not match any property '%s'",
									opShortAnn.keyword(), type.getSimpleName(), opShortAnn.property()));
				}
				try {
					ReorgOperationProperty opProperty = shortcutProp.get();
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

	@Override
	public String getBasePackage() {
		return ConfigConstants.Chess.OPERATIONS_PACKAGE;
	}

	@Override
	public Class<ChessAbstractReorgOperation> getBaseType() {
		return ChessAbstractReorgOperation.class;
	}
}
