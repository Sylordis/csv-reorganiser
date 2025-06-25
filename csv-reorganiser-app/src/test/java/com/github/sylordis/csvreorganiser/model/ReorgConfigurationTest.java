package com.github.sylordis.csvreorganiser.model;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sylordis.csvreorganiser.model.chess.ChessEngine;
import com.github.sylordis.csvreorganiser.model.chess.config.dictionary.ChessDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessOperationInstantiator;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.RegularExpressionReplacementOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.SubstringOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation;
import com.github.sylordis.csvreorganiser.model.constants.YAMLTags;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.EngineException;
import com.github.sylordis.csvreorganiser.test.SamplesFilesConstants;

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
	
	@TempDir
	File workingDir;
	
	@Mock
	private ChessAbstractReorgOperation op;
	@Mock
	private ChessAbstractReorgOperation op2;
	@Mock
	private ChessAbstractReorgOperation op3;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		rcfg = new ReorgConfiguration();
		cfgFile = File.createTempFile("ReorgConfigurationTest-cfg", "yaml", workingDir);
		fillFileWithSamples(cfgFile, SamplesFilesConstants.CONFIG_CONTENT_CHESS);
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#ReorgConfiguration()}
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 * @throws EngineException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile() throws ConfigurationImportException, FileNotFoundException, IOException, EngineException {
		rcfg = new ReorgConfiguration(ChessEngine.createDefaultEngine());
		rcfg.loadFromFile(cfgFile);
		assertEquals(6, rcfg.getOperations().size());
		ChessAbstractReorgOperation op = (ChessAbstractReorgOperation) rcfg.getOperations().get(0);
		assertEquals(GetOperation.class, op.getClass());
		assertEquals("Name", op.getName());
		assertEquals("first_name", ((GetOperation) op).getSrcColumn());
		op = (ChessAbstractReorgOperation) rcfg.getOperations().get(1);
		assertEquals(GetOperation.class, op.getClass());
		assertEquals("Identifier", op.getName());
		assertEquals("id", ((GetOperation) op).getSrcColumn());
		op = (ChessAbstractReorgOperation) rcfg.getOperations().get(2);
		assertEquals(ValueOperation.class, op.getClass());
		assertEquals("something", op.getName());
		assertEquals("s", ((ValueOperation) op).getValue());
		op = (ChessAbstractReorgOperation) rcfg.getOperations().get(3);
		assertEquals(ConcatenationOperation.class, op.getClass());
		assertEquals("login", op.getName());
		Collection<String> values = new ArrayList<>(List.of("first_name", ".", "last_name"));
		assertEquals(values, ((ConcatenationOperation) op).getValues());
		op = (ChessAbstractReorgOperation) rcfg.getOperations().get(4);
		assertEquals(RegularExpressionReplacementOperation.class, op.getClass());
		assertEquals("Network group", op.getName());
		assertEquals("ip_address", ((RegularExpressionReplacementOperation) op).getSrcColumn());
		assertEquals("([0-9]{1,3}\\.[0-9]{1,3})\\..*", ((RegularExpressionReplacementOperation) op).getPattern());
		assertEquals("$1", ((RegularExpressionReplacementOperation) op).getReplacement());
		op = (ChessAbstractReorgOperation) rcfg.getOperations().get(5);
		assertEquals(SubstringOperation.class, op.getClass());
		assertEquals("Gender", op.getName());
		assertEquals("gender", ((SubstringOperation) op).getSrcColumn());
		assertEquals(0, ((SubstringOperation) op).getStartIndex());
		assertEquals(1, ((SubstringOperation) op).getEndIndex());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when no proper shortcut is found.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile_Chess_NoShortcut() throws ConfigurationImportException, FileNotFoundException, IOException {
		ChessEngine engine = new ChessEngine() {
			@Override
			public Map<String, ChessOperationInstantiator> getOperationsShortcutsDictionary() {
				return new HashMap<>();
			}
		};
		rcfg.setEngine(engine);
		engine.setOperationsDictionary(new ChessDefaultConfigurationSupplier().getOperationsDictionary());
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfgFile));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when no operation dictionary has been setup.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 * @throws EngineException
	 */
	@Test
	@Tag("Integration")
	void testLoadFromFile_NoEngine()
	        throws ConfigurationImportException, FileNotFoundException, IOException, EngineException {
		rcfg.loadFromFile(cfgFile);
		assertNotNull(rcfg);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when provided file does not exist.
	 */
	@Test
	void testLoadFromFile_NoFile() {
		assertThrows(FileNotFoundException.class, () -> rcfg.loadFromFile(new File("a/b/c/d")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * if configuration file is empty.
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * if {@link YAMLTags#OPDEF_ROOT_KEY} is not present.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoRootKeyword() throws IOException {
		File cfg = createFileWith("Hello:");
		rcfg.setEngine(new DummyEngine());
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * if {@link YAMLTags#OPDEF_ROOT_KEY} is not present.
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * if no operations are present under root.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoOperations() throws IOException, EngineException {
		File cfg = createFileWith(YAMLTags.OPDEF_ROOT_KEY + ": []");
		rcfg.setEngine(new DummyEngine());
		rcfg.loadFromFile(cfg);
		assertTrue(rcfg.getOperations().isEmpty());
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * if root does not contain a list for values.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFile_NoListUnderRoot() throws IOException {
		File cfg = createFileWith(YAMLTags.OPDEF_ROOT_KEY + ":\n" + "  hello: there");
		rcfg.setEngine(new DummyEngine());
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationAbstractReorgOperation() {
		rcfg.addOperation(op);
		assertIterableEquals(List.of(op), rcfg.getOperations(),
		        "Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}
	 * to check that adding multiple operations keep them in insertion order.
	 */
	@Test
	void testAddOperationAbstractReorgOperation_Multiple() {
		rcfg.addOperation(op);
		rcfg.addOperation(op2);
		assertIterableEquals(List.of(op, op2), rcfg.getOperations(),
		        "Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation() {
		rcfg.addOperation(0, op);
		assertIterableEquals(List.of(op), rcfg.getOperations(),
		        "Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}
	 * checks that adding an operation at an index out of bounds just adds the operation at the end of
	 * the list without triggering an error.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation_NoOutOfBounds() {
		rcfg.addOperation(op2);
		rcfg.addOperation(5, op);
		assertIterableEquals(List.of(op2, op), rcfg.getOperations(),
		        "Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}
	 * checks that multiple operations can be added via index and that they will respect the assigned
	 * order.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation_Multiple() {
		rcfg.addOperation(0, op);
		rcfg.addOperation(0, op2);
		rcfg.addOperation(1, op3);
		assertIterableEquals(List.of(op2, op3, op), rcfg.getOperations(),
		        "Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#addOperation(int, com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation)}
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
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#fromFile(java.io.File)}
	 * checking that calling this static constructor creates a new instance loading the provided file.
	 * Content of the file and proper loading tests will be done with appropriate unit tests on
	 * {@link ReorgConfiguration#loadFromFile(File)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 * @throws EngineException
	 */
	@Test
	@Tag("Constructor")
	void testFromFile() throws ConfigurationImportException, FileNotFoundException, IOException, EngineException {
		ReorgConfiguration nrcfg;
		nrcfg = ReorgConfiguration.fromFile(cfgFile, ChessEngine.createDefaultEngine());
		assertNotNull(nrcfg);
		assertNotSame(nrcfg, rcfg);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.ReorgConfiguration#getOperations()} after
	 * construction.
	 */
	@Test
	void testGetOperations() {
		assertNotNull(rcfg.getOperations(), "getOperations() should never return null");
		assertTrue(rcfg.getOperations().isEmpty(), "List of operations should be empty by default");
	}

	/**
	 * Test engine.
	 */
	public final class DummyEngine implements ReorganiserEngine {

		@Override
		public ReorganiserOperation createOperation(Map<String, Object> node) {
			return new DummyOperation();
		}

	}

	/**
	 * Test operation.
	 */
	public final class DummyOperation implements ReorganiserOperation {

		@Override
		public String apply(CSVRecord t) {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

	}
}
