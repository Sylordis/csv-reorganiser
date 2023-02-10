package com.github.sylordis.tools.csvreorganiser.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sylordis.tools.csvreorganiser.model.config.dictionary.DefaultConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.OperationInstantiator;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.ConcatenationOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.GetOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.RegularExpressionReplacementOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.SubstringOperation;
import com.github.sylordis.tools.csvreorganiser.model.operations.defs.ValueOperation;
import com.github.sylordis.tools.csvreorganiser.test.SamplesFilesConstants;
import com.github.sylordis.tools.csvreorganiser.test.defs.FakeOperation;

/**
 * Test suite for {@link ReorgConfiguration} class.
 *
 * @author sylordis
 *
 */
@ExtendWith(MockitoExtension.class)
class ReorgConfigurationTest {

	/**
	 * Cfg file to be loaded.
	 */
	private File cfgFile;
	/**
	 * Object under test.
	 */
	private ReorgConfiguration rcfg;

	/**
	 * Reorganiser operation.
	 */
	@Mock
	private AbstractReorgOperation op;

	@TempDir
	File workingDir;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		rcfg = new ReorgConfiguration();
		cfgFile = File.createTempFile("ReorgConfigurationTest-cfg", "yaml", workingDir);
		fillFileWithSamples(cfgFile, SamplesFilesConstants.CONFIG_CONTENT);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		cfgFile.delete();
	}

	/**
	 * Fills {@link #srcFile} with sample data from {@link #SOURCE_CONTENT}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void fillFileWithSamples(File file, String samplesStream) throws IOException, FileNotFoundException {
		try (OutputStream stream = new FileOutputStream(file);
				InputStream istream = this.getClass().getClassLoader().getResourceAsStream(samplesStream)) {
			stream.write(istream.readAllBytes());
		}
	}

	/**
	 * Creates a temporary file with given text.
	 *
	 * @param txt
	 * @return
	 * @throws IOException
	 */
	private File createFileWith(String txt) throws IOException {
		File file = File.createTempFile(getClass().getName(), null, workingDir);
		FileUtils.writeStringToFile(file, txt, Charset.defaultCharset());
		return file;
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#ReorgConfiguration()}
	 * constructor.
	 */
	@Test
	@Tag("Constructor")
	void testReorgConfiguration() {
		assertNotNull(rcfg.getOperations());
		assertTrue(rcfg.getOperations().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile() throws ConfigurationImportException, FileNotFoundException, IOException {
		rcfg = new ReorgConfiguration(new DefaultConfigurationSupplier());
		rcfg.loadFromFile(cfgFile);
		assertEquals(6, rcfg.getOperations().size());
		AbstractReorgOperation op = rcfg.getOperations().get(0);
		assertEquals(GetOperation.class, op.getClass());
		assertEquals("Name", op.getName());
		assertEquals("first_name", ((GetOperation) op).getSrcColumn());
		op = rcfg.getOperations().get(1);
		assertEquals(GetOperation.class, op.getClass());
		assertEquals("Identifier", op.getName());
		assertEquals("id", ((GetOperation) op).getSrcColumn());
		op = rcfg.getOperations().get(2);
		assertEquals(ValueOperation.class, op.getClass());
		assertEquals("something", op.getName());
		assertEquals("s", ((ValueOperation) op).getValue());
		op = rcfg.getOperations().get(3);
		assertEquals(ConcatenationOperation.class, op.getClass());
		assertEquals("login", op.getName());
		Collection<String> values = new ArrayList<>(List.of("first_name", ".", "last_name"));
		assertEquals(values, ((ConcatenationOperation) op).getValues());
		op = rcfg.getOperations().get(4);
		assertEquals(RegularExpressionReplacementOperation.class, op.getClass());
		assertEquals("Network group", op.getName());
		assertEquals("ip_address", ((RegularExpressionReplacementOperation) op).getSrcColumn());
		assertEquals("([0-9]{1,3}\\.[0-9]{1,3})\\..*", ((RegularExpressionReplacementOperation) op).getPattern());
		assertEquals("$1", ((RegularExpressionReplacementOperation) op).getReplacement());
		op = rcfg.getOperations().get(5);
		assertEquals(SubstringOperation.class, op.getClass());
		assertEquals("Gender", op.getName());
		assertEquals("gender", ((SubstringOperation) op).getSrcColumn());
		assertEquals(0, ((SubstringOperation) op).getStartIndex());
		assertEquals(1, ((SubstringOperation) op).getEndIndex());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when no proper shortcut is found.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile_NoShortcut() throws ConfigurationImportException, FileNotFoundException, IOException {
		rcfg.setOperationsDictionary(new DefaultConfigurationSupplier().getOperationsDictionary());
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfgFile));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when no operation dictionary has been setup.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile_NoConfiguration() throws ConfigurationImportException, FileNotFoundException, IOException {
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfgFile));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when provided file does not exist.
	 */
	@Test
	void testLoadFromFile_NoFile() {
		assertThrows(FileNotFoundException.class, () -> rcfg.loadFromFile(new File("a/b/c/d")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * configuration file is empty.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoRoot() throws IOException {
		File cfg = createFileWith("");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * {@link YAMLTags#OPDEF_ROOT_KEY} is not present.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoRootKeyword() throws IOException {
		File cfg = createFileWith("Hello:");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * {@link YAMLTags#OPDEF_ROOT_KEY} is not present.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoYaml() throws IOException {
		File cfg = createFileWith("Hello");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * no operations are present under root.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoOperations() throws IOException {
		File cfg = createFileWith(YAMLTags.OPDEF_ROOT_KEY + ": []");
		rcfg.loadFromFile(cfg);
		assertTrue(rcfg.getOperations().isEmpty());
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * root does not contain a list for values.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoListUnderRoot() throws IOException {
		File cfg = createFileWith(YAMLTags.OPDEF_ROOT_KEY + ":\n" + "  hello: there");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}.
	 */
	@Test
	void testCreateOperation() {
		final String fakeOperationName = "fake";
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put(fakeOperationName, FakeOperation.class);
		rcfg.setOperationsDictionary(dictionary);
		Map<String, Object> data = new HashMap<>();
		data.put(YAMLTags.OPDEF_COLUMN_KEY, "FakeCol");
		Map<String, Object> opConfig = new HashMap<>();
		opConfig.put(YAMLTags.OPDATA_TYPE_KEY, fakeOperationName);
		data.put(YAMLTags.OPDEF_OPERATION_KEY, opConfig);
		AbstractReorgOperation op = rcfg.createOperation(data);
		assertNotNull(op);
		assertTrue(op instanceof FakeOperation, "created operation should be a FakeOperation according to dictionary");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * when null is provided.
	 */
	@Test
	@Tag("Null")
	void testCreateOperation_Null() {
		assertThrows(NullPointerException.class, () -> rcfg.createOperation(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * to create an operation from nothing.
	 */
	@Test
	void testCreateOperation_Empty() {
		Map<String, Object> data = new HashMap<>();
		assertThrows(ConfigurationImportException.class, () -> rcfg.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if main key {@link YAMLTags#OPDEF_COLUMN_KEY} is not present, but the file is not empty.
	 */
	@Test
	void testCreateOperation_NoMainKey() {
		Map<String, Object> data = new HashMap<>();
		data.put("MyMainKey", new Object());
		data.put("SomeValue", 1);
		assertThrows(ConfigurationImportException.class, () -> rcfg.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if an operation is not correctly configured.
	 *
	 * @throws IOException
	 */
	@Test
	void testCreateOperation_Wrong() throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put(YAMLTags.OPDEF_COLUMN_KEY, "ElColumn");
		data.put("Uhoh", "something's wrong");
		assertThrows(ConfigurationImportException.class, () -> rcfg.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * when creating nested operations (Not yet implemented).
	 */
	@Test
	@Tag("Integration")
	void testCreateOperationNested() {
		Map<String, Object> data = new HashMap<>();
		data.put(YAMLTags.OPDEF_COLUMN_KEY, "ColA");
		data.put(YAMLTags.OPDEF_OPERATIONS_KEY, new Object());
		assertThrows(NotImplementedException.class, () -> rcfg.createOperation(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationAbstractReorgOperation() {
		rcfg.addOperation(op);
		assertIterableEquals(List.of(op), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}
	 * to check that adding multiple operations keep them in insertion order.
	 */
	@Test
	void testAddOperationAbstractReorgOperation_Multiple(@Mock AbstractReorgOperation op2) {
		rcfg.addOperation(op);
		rcfg.addOperation(op2);
		assertIterableEquals(List.of(op, op2), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}
	 * that checks that adding a null operation throws an exception.
	 */
	@Test
	@Tag("Null")
	void testAddOperationAbstractReorgOperation_Null() {
		assertThrows(IllegalArgumentException.class, () -> rcfg.addOperation(null),
				"Adding a null operation should throw an exception");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation() {
		rcfg.addOperation(0, op);
		assertIterableEquals(List.of(op), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}
	 * checks that adding an operation at an index out of bounds just adds the operation at the end of
	 * the list without triggering an error.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation_NoOutOfBounds(@Mock AbstractReorgOperation opOrig) {
		rcfg.addOperation(opOrig);
		rcfg.addOperation(5, op);
		assertIterableEquals(List.of(opOrig, op), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}
	 * checks that multiple operations can be added via index and that they will respect the assigned
	 * order.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation_Multiple(@Mock AbstractReorgOperation op2,
			@Mock AbstractReorgOperation op3) {
		rcfg.addOperation(0, op);
		rcfg.addOperation(0, op2);
		rcfg.addOperation(1, op3);
		assertIterableEquals(List.of(op2, op3, op), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation)}
	 * checks that adding a null operation triggers an errors.
	 */
	@Test
	@Tag("Null")
	void testAddOperationIntAbstractReorgOperation_Null() {
		assertThrows(IllegalArgumentException.class, () -> rcfg.addOperation(5, null),
				"Adding a null operation should throw an exception");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#fromFile(java.io.File)}
	 * checking that calling this static constructor creates a new instance loading the provided file.
	 * Content of the file and proper loading tests will be done with appropriate unit tests on
	 * {@link ReorgConfiguration#loadFromFile(File)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Constructor")
	void testFromFile() throws ConfigurationImportException, FileNotFoundException, IOException {
		ReorgConfiguration nrcfg;
		nrcfg = ReorgConfiguration.fromFile(cfgFile, new DefaultConfigurationSupplier());
		assertNotNull(nrcfg);
		assertNotSame(nrcfg, rcfg);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration#getOperations()} after
	 * construction.
	 */
	@Test
	void testGetOperations() {
		assertNotNull(rcfg.getOperations(), "getOperations() should never return null");
		assertTrue(rcfg.getOperations().isEmpty(), "List of operations should be empty by default");
	}

	/**
	 * Test method for {@link ReorgConfiguration#getOperationDictionary()}.
	 */
	@Test
	void testGetOperationsDictionary() {
		assertNotNull(rcfg.getOperationsDictionary());
		assertTrue(rcfg.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#getOperationShortcutsDictionary()}.
	 */
	@Test
	void testGetOperationsShortcutsDictionary() {
		assertNotNull(rcfg.getOperationsShortcutsDictionary());
		assertTrue(rcfg.getOperationsShortcutsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)}.
	 */
	@Test
	void testSetOperationsDictionary() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put("SomeFake", FakeOperation.class);
		dictionary.put("SomeAbstract", AbstractReorgOperation.class);
		rcfg.setOperationsDictionary(dictionary);
		assertNotSame(dictionary, rcfg.getOperationsDictionary());
		assertEquals(dictionary, rcfg.getOperationsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing a null map.
	 */
	@Test
	@Tag("Null")
	void testSetOperationsDictionary_Null() {
		assertThrows(ConfigurationException.class, () -> rcfg.setOperationsDictionary(null));
		assertNotNull(rcfg.getOperations());
		assertTrue(rcfg.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing an empty
	 * map.
	 */
	@Test
	void testSetOperationsDictionary_Empty() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		rcfg.setOperationsDictionary(dictionary);
		assertNotSame(dictionary, rcfg.getOperationsDictionary());
		assertTrue(rcfg.getOperationsDictionary().isEmpty());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationDictionary(Map)} when providing an empty
	 * map when the dictionary was already set previously.
	 */
	@Test
	@Tag("Integration")
	void testSetOperationsDictionary_Replace() {
		Map<String, Class<? extends AbstractReorgOperation>> dictionary = new HashMap<>();
		dictionary.put("SomeFake", FakeOperation.class);
		dictionary.put("SomeAbstract", AbstractReorgOperation.class);
		rcfg.setOperationsDictionary(dictionary);
		Map<String, Class<? extends AbstractReorgOperation>> dictionary2 = new HashMap<>();
		dictionary.put("OtherFake", FakeOperation.class);
		dictionary.put("OtherFake2", FakeOperation.class);
		dictionary.put("OtherAbstract", AbstractReorgOperation.class);
		rcfg.setOperationsDictionary(dictionary2);
		assertEquals(dictionary2, rcfg.getOperationsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)}.
	 */
	@Test
	void testSetOperationsShortcutsDictionary() {
		Map<String, OperationInstantiator> map = Map.of("support", (n, d) -> null, "tank",
				(n, d) -> new AbstractReorgOperation("are the best") {

					@Override
					public String apply(CSVRecord record) {
						return null;
					}
				});
		rcfg.setOperationsShortcutsDictionary(map);
		assertEquals(map, rcfg.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)} when providing
	 * an empty map.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Empty() {
		Map<String, OperationInstantiator> map = Map.of();
		rcfg.setOperationsShortcutsDictionary(map);
		assertEquals(map, rcfg.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)}.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Replace() {
		Map<String, OperationInstantiator> map = Map.of("jam", (n, d) -> null, "on",
				(n, d) -> new AbstractReorgOperation("toast") {

					@Override
					public String apply(CSVRecord record) {
						return null;
					}
				});
		rcfg.setOperationsShortcutsDictionary(map);
		Map<String, OperationInstantiator> map2 = Map.of("bread", (n, d) -> null, "and", (n, d) -> null, "butter",
				(n, d) -> null);
		rcfg.setOperationsShortcutsDictionary(map2);
		assertEquals(map2, rcfg.getOperationsShortcutsDictionary());
	}

	/**
	 * Test method for {@link ReorgConfiguration#setOperationsShortcutsDictionary(Map)} when providing
	 * null map.
	 */
	@Test
	void testSetOperationsShortcutsDictionary_Null() {
		assertThrows(ConfigurationException.class, () -> rcfg.setOperationsShortcutsDictionary(null));
	}

}
