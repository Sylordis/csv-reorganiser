package com.github.sylordis.csvreorganiser.model.chess.config;

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

import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation;

/**
 * Test suite for {@link ChessDefaultConfigurationSupplier}. Unfortunately, since this class is using
 * reflection, one cannot test the misconfiguration paths via unit tests.
 *
 * @author sylordis
 *
 */
class DefaultConfigurationSupplierTest {

	/**
	 * Default object under test.
	 */
	private ChessDefaultConfigurationSupplier dcs;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		dcs = new ChessDefaultConfigurationSupplier();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.config.ChessDefaultConfigurationSupplier#DefaultConfigurationSupplier()}
	 * to check that the constructor does create the object properly.
	 */
	@Test
	@Tag("Constructor")
	void testConstructor() {
		assertNotNull(dcs);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.chess.config.ChessDefaultConfigurationSupplier#getConfigurationDictionary()}.
	 */
	@Test
	void testGetOperationsDictionary() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> m = dcs.getConfigurationDictionary();
		assertNotNull(m);
		assertFalse(m.isEmpty());
	}

	@Test
	@Tag("Integration")
	void testGetShortcutDictionary() {
		Map<String, ChessOperationInstantiator> m = dcs.getShortcutDictionary();
		assertNotNull(m);
		Set<String> expected = Set.of("dumdum", ConcatenationOperation.SHORTCUT_KEY, GetOperation.SHORTCUT_KEY,
				ValueOperation.SHORTCUT_KEY);
		assertEquals(expected, m.keySet());
	}

	@ParameterizedTest
	@Tag("Integration")
	@MethodSource("provideShortcutArguments")
	void testGetShortcutDictionary_Operations(Class<? extends ChessAbstractReorgOperation> opType, String shortcutId,
			String opName, Map<String, Object> yamlData,
			Map<Object, Function<ChessAbstractReorgOperation, Object>> opEquals) {
		Map<String, ChessOperationInstantiator> shortcutDictionary = dcs.getShortcutDictionary();
		ChessAbstractReorgOperation operation;
		operation = shortcutDictionary.get(shortcutId).apply(opName, yamlData);
		assertNotNull(operation);
		assertEquals(opType, operation.getClass());
		assertEquals(opName, operation.getName());
		for (Entry<Object, Function<ChessAbstractReorgOperation, Object>> entry : opEquals.entrySet())
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
						Map.of("gold", new Function<ChessAbstractReorgOperation, Object>() {

							@Override
							public Object apply(ChessAbstractReorgOperation t) {
								return ((ValueOperation) t).getValue();
							}
						})),
				//
				Arguments.of(GetOperation.class, GetOperation.SHORTCUT_KEY, "Goldfish",
						Map.of(GetOperation.SHORTCUT_KEY, "red"),
						Map.of("red", new Function<ChessAbstractReorgOperation, Object>() {

							@Override
							public Object apply(ChessAbstractReorgOperation t) {
								return ((GetOperation) t).getSrcColumn();
							}
						})),
				//
				Arguments.of(ConcatenationOperation.class, ConcatenationOperation.SHORTCUT_KEY, "Together",
						Map.of(ConcatenationOperation.SHORTCUT_KEY, List.of("column", ".")),
						Map.of(List.of("column", "."), new Function<ChessAbstractReorgOperation, Object>() {

							@Override
							public Object apply(ChessAbstractReorgOperation t) {
								return ((ConcatenationOperation) t).getValues();
							}
						})));
	}

}
