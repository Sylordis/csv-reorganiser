package com.github.sylordis.tools.csvreorganiser.model.config.dictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.OperationInstantiator;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.ConcatenationOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.GetOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.ValueOperation;

/**
 * Test suite for {@link DefaultConfigurationSupplier}. Unfortunately, since this class is using
 * reflection, one cannot test the misconfiguration paths via unit tests.
 *
 * @author sylordis
 *
 */
class DefaultConfigurationSupplierTest {

	/**
	 * Default object under test.
	 */
	private DefaultConfigurationSupplier dcs;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		dcs = new DefaultConfigurationSupplier();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.config.dictionary.DefaultConfigurationSupplier#DefaultConfigurationSupplier()}
	 * to check that the constructor does create the object properly.
	 */
	@Test
	@Tag("Constructor")
	void testConstructor() {
		assertNotNull(dcs);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.config.dictionary.DefaultConfigurationSupplier#getOperationsDictionary()}.
	 */
	@Test
	void testGetOperationsDictionary() {
		Map<String, Class<? extends AbstractReorgOperation>> m = dcs.getOperationsDictionary();
		assertNotNull(m);
		assertFalse(m.isEmpty());
	}

	@Test
	@Tag("Integration")
	void testGetShortcutDictionary() {
		Map<String, OperationInstantiator> m = dcs.getShortcutDictionary();
		assertNotNull(m);
		Set<String> expected = Set.of(ConcatenationOperation.SHORTCUT_KEY, GetOperation.SHORTCUT_KEY,
				ValueOperation.SHORTCUT_KEY);
		assertEquals(expected, m.keySet());
	}

	@ParameterizedTest
	@Tag("Integration")
	@MethodSource("provideShortcutArguments")
	void testGetShortcutDictionary_Operations(Class<? extends AbstractReorgOperation> opType, String shortcutId,
			String opName, Map<String, Object> yamlData,
			Map<Object, Function<AbstractReorgOperation, Object>> opEquals) {
		Map<String, OperationInstantiator> shortcutDictionary = dcs.getShortcutDictionary();
		AbstractReorgOperation operation;
		operation = shortcutDictionary.get(shortcutId).apply(opName, yamlData);
		assertNotNull(operation);
		assertEquals(opType, operation.getClass());
		assertEquals(opName, operation.getName());
		for (Entry<Object, Function<AbstractReorgOperation, Object>> entry : opEquals.entrySet())
			assertEquals(entry.getKey(), entry.getValue().apply(operation));
	}

	/**
	 * Provides all arguments for {@link #testGetShortcutDictionary_Operations(Class, String, Map)}.
	 *
	 * @return
	 */
	private static Stream<Arguments> provideShortcutArguments() {
		return Stream.of(//
				Arguments.of(ValueOperation.class, ValueOperation.SHORTCUT_KEY, "MyValueOp",
						Map.of(ValueOperation.SHORTCUT_KEY, "gold"),
						Map.of("gold", new Function<AbstractReorgOperation, Object>() {

							@Override
							public Object apply(AbstractReorgOperation t) {
								return ((ValueOperation) t).getValue();
							}
						})),
				//
				Arguments.of(GetOperation.class, GetOperation.SHORTCUT_KEY, "Goldfish",
						Map.of(GetOperation.SHORTCUT_KEY, "red"),
						Map.of("red", new Function<AbstractReorgOperation, Object>() {

							@Override
							public Object apply(AbstractReorgOperation t) {
								return ((GetOperation) t).getSrcColumn();
							}
						})),
				//
				Arguments.of(ConcatenationOperation.class, ConcatenationOperation.SHORTCUT_KEY, "Together",
						Map.of(ConcatenationOperation.SHORTCUT_KEY, List.of("column", ".")),
						Map.of(List.of("column", "."), new Function<AbstractReorgOperation, Object>() {

							@Override
							public Object apply(AbstractReorgOperation t) {
								return ((ConcatenationOperation) t).getValues();
							}
						})));
	}

}
