package com.github.sylordis.csvreorganiser.model.engines;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.github.sylordis.csvreorganiser.model.annotations.Operation;

/**
 * Implement this interface to supply a given configuration of operations. Defaults are taking all
 * classes that are in a specific package provided by {@link #getBasePackage()} and that inherit
 * from a given {@link #getBaseType()} and that possess the {@link Operation} annotation.
 * 
 * @param <T> type of the operation
 */
public interface ConfigurationSupplier<T> {

	public String getBasePackage();

	public Class<T> getBaseType();

	/**
	 * Provides the dictionary of operations. This method should be only called when setting or
	 * replacing the dictionary is needed.
	 *
	 * @return
	 */
	default Map<String, Class<? extends T>> getConfigurationDictionary() {
		Map<String, Class<? extends T>> map = new HashMap<>();
		Set<Class<? extends T>> types = getConfigurationByReflection(getBasePackage(), getBaseType());
		types.forEach(t -> map.put(t.getAnnotation(Operation.class).name(), t));
		return map;
	}

	/**
	 * Retrieves all possible operations by reflection.
	 * 
	 * @param pack Package of the operations
	 * @param type Root type that the operations inherit from the package
	 *
	 * @return a set of all operations
	 */
	default Set<Class<? extends T>> getConfigurationByReflection(String pack, Class<T> type) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage(pack));
		Set<Class<? extends T>> types = reflections.getSubTypesOf(type);
		return types.stream().filter(t -> t.isAnnotationPresent(Operation.class)).collect(Collectors.toSet());
	}
}
