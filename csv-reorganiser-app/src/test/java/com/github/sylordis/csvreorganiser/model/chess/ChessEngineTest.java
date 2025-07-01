package com.github.sylordis.csvreorganiser.model.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.github.sylordis.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.csvreorganiser.model.chess.config.dictionary.ChessConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.test.chess.defs.FakeOperation;

/**
 * @author sylordis
 *
 */
class ChessEngineTest {
	
	private ChessEngine engine; 

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		engine = new ChessEngine();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.ChessEngine#ChessEngine()}.
	 */
	@Test
	void testChessEngine() {
		assertNotNull(engine);
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.ChessEngine#ChessEngine(com.github.sylordis.csvreorganiser.model.chess.config.dictionary.ChessConfigurationSupplier)}.
	 */
	@Test
	void testChessEngineChessConfigurationSupplier() {
		assertNotNull(engine = new ChessEngine(new ChessConfigurationSupplier() {
			
			@Override
			public Map<String, ChessOperationInstantiator> getShortcutDictionary() {
				return new HashMap<String, ChessOperationInstantiator>();
			}
			
			@Override
			public Map<String, Class<? extends ChessAbstractReorgOperation>> getOperationsDictionary() {
				return new HashMap<String, Class<? extends ChessAbstractReorgOperation>>();
			}
		}));
		assertEquals(new HashMap<String, ChessOperationInstantiator>(), engine.getOperationsShortcutsDictionary());
		assertEquals(new HashMap<String, Class<? extends ChessAbstractReorgOperation>>(), engine.getOperationsDictionary());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}.
	 */
	@Test
	void testCreateOperation() {
		final String fakeOperationName = "fake";
		Map<String, Class<? extends ChessAbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(fakeOperationName, FakeOperation.class);
		engine.setOperationsDictionary(dictionary);
		Map<String, Object> data = new HashMap<>();
		data.put(YAMLTags.Chess.OPDEF_COLUMN_KEY, "FakeCol");
		Map<String, Object> opConfig = new HashMap<>();
		opConfig.put(YAMLTags.Chess.OPDATA_TYPE_KEY, fakeOperationName);
		data.put(YAMLTags.Chess.OPDEF_OPERATION_KEY, opConfig);
		ChessAbstractReorgOperation op = engine.createOperation(data);
		assertNotNull(op);
		assertTrue(op instanceof FakeOperation, "created operation should be a FakeOperation according to dictionary");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperations(java.util.Map)}.
	 * if root does not contain a list for values.
	 *
	 * @throws IOException
	 */
	@Test
	void testCreateOperations_NoListUnderRoot() throws IOException {
		String content = """
%s:
   %s:
      hello: there
		        """;
		Map<String,Object> yaml = new Yaml().load(String.format(content, YAMLTags.CFG_ROOT, YAMLTags.OPDEF_ROOT_KEY));
		assertThrows(ConfigurationImportException.class, () -> engine.createOperations(yaml));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * when null is provided.
	 */
	@Test
	@Tag("Null")
	void testCreateOperation_Null() {
		assertThrows(NullPointerException.class, () -> engine.createOperation(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * to create an operation from nothing.
	 */
	@Test
	void testCreateOperation_Empty() {
		Map<String, Object> data = new HashMap<>();
		assertThrows(ConfigurationImportException.class, () -> engine.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if main key {@link YAMLTags#OPDEF_COLUMN_KEY} is not present, but the file is not empty.
	 */
	@Test
	void testCreateOperation_NoMainKey() {
		Map<String, Object> data = new HashMap<>();
		data.put("MyMainKey", new Object());
		data.put("SomeValue", 1);
		assertThrows(ConfigurationImportException.class, () -> engine.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if an operation is not correctly configured.
	 *
	 * @throws IOException
	 */
	@Test
	void testCreateOperation_Wrong() throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put(YAMLTags.Chess.OPDEF_COLUMN_KEY, "ElColumn");
		data.put("Uhoh", "something's wrong");
		assertThrows(ConfigurationImportException.class, () -> engine.createOperation(data));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.ChessEngine#checkIfOneIsShortcut(java.util.Set)}.
	 */
	@Test
	void testCheckIfOneIsShortcut() {
		final String myShortcut1 = "Banana";
		Map<String, ChessOperationInstantiator> shortcuts = Map.of(myShortcut1, mock(ChessOperationInstantiator.class));
		engine.setOperationsShortcutsDictionary(shortcuts);
		assertTrue(engine.checkIfOneIsShortcut(Set.of(myShortcut1, "abcd")));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.chess.ChessEngine#checkIfOneIsShortcut(java.util.Set)}.
	 */
	@Test
	void testCheckIfOneIsShortcut_False() {
		final String myShortcut1 = "Plum";
		Map<String, ChessOperationInstantiator> shortcuts = Map.of(myShortcut1, mock(ChessOperationInstantiator.class));
		engine.setOperationsShortcutsDictionary(shortcuts);
		assertFalse(engine.checkIfOneIsShortcut(Set.of("kiwi", "abcd")));
	}

	/**
	 * Test method for {@link ReorgConfiguration#getOperationDictionary()}.
	 */
	@Test
	void testGetOperationsDictionary() {
		assertNotNull(engine.getOperationsDictionary());
		assertFalse(engine.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#getOperationShortcutsDictionary()}.
	 */
	@Test
	void testGetOperationsShortcutsDictionary() {
		assertNotNull(engine.getOperationsShortcutsDictionary());
		assertFalse(engine.getOperationsShortcutsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)}.
	 */
	@Test
	void testSetOperationsDictionary() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put("SomeFake", FakeOperation.class);
		dictionary.put("SomeAbstract", ChessAbstractReorgOperation.class);
		engine.setOperationsDictionary(dictionary);
		assertNotSame(dictionary, engine.getOperationsDictionary());
		assertEquals(dictionary, engine.getOperationsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing a null map.
	 */
	@Test
	@Tag("Null")
	void testSetOperationsDictionary_Null() {
		assertThrows(ConfigurationException.class, () -> engine.setOperationsDictionary(null));
		assertTrue(engine.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing an empty
	 * map.
	 */
	@Test
	void testSetOperationsDictionary_Empty() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> dictionary = new HashMap<>();
		engine.setOperationsDictionary(dictionary);
		assertNotSame(dictionary, engine.getOperationsDictionary());
		assertTrue(engine.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing an empty
	 * map when the dictionary was already set previously.
	 */
	@Test
	@Tag("Integration")
	void testSetOperationsDictionary_Replace() {
		Map<String, Class<? extends ChessAbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put("SomeFake", FakeOperation.class);
		dictionary.put("SomeAbstract", ChessAbstractReorgOperation.class);
		engine.setOperationsDictionary(dictionary);
		Map<String, Class<? extends ChessAbstractReorgOperation>> dictionary2 = new HashMap<>();
		dictionary.put("OtherFake", FakeOperation.class);
		dictionary.put("OtherFake2", FakeOperation.class);
		dictionary.put("OtherAbstract", ChessAbstractReorgOperation.class);
		engine.setOperationsDictionary(dictionary2);
		assertEquals(dictionary2, engine.getOperationsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)}.
	 */
	@Test
	void testSetOperationsShortcutsDictionary() {
		Map<String, ChessOperationInstantiator> map = Map.of("support", (n, d) -> null, "tank",
				(n, d) -> new ChessAbstractReorgOperation("are the best") {

					@Override
					public String apply(CSVRecord record) {
						return null;
					}
				});
		engine.setOperationsShortcutsDictionary(map);
		assertEquals(map, engine.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)} when providing
	 * an empty map.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Empty() {
		Map<String, ChessOperationInstantiator> map = Map.of();
		engine.setOperationsShortcutsDictionary(map);
		assertEquals(map, engine.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)}.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Replace() {
		Map<String, ChessOperationInstantiator> map = Map.of("jam", (n, d) -> null, "on",
				(n, d) -> new ChessAbstractReorgOperation("toast") {

					@Override
					public String apply(CSVRecord record) {
						return null;
					}
				});
		engine.setOperationsShortcutsDictionary(map);
		Map<String, ChessOperationInstantiator> map2 = Map.of("bread", (n, d) -> null, "and", (n, d) -> null, "butter",
				(n, d) -> null);
		engine.setOperationsShortcutsDictionary(map2);
		assertEquals(map2, engine.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)} when providing
	 * null map.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Null() {
		assertThrows(ConfigurationException.class, () -> engine.setOperationsShortcutsDictionary(null));
	}

}
