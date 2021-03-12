package com.sylordis.tools.csv.reorganiser.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.GetOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.RegReplaceOperation;
import com.sylordis.tools.csv.reorganiser.model.operations.defs.ValueOperation;
import com.sylordis.tools.csv.reorganiser.test.SamplesFilesConstants;

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
				InputStream istream = ReorganiserTest.class.getClassLoader().getResourceAsStream(samplesStream)) {
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#ReorgConfiguration()}
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ConfigurationImportException
	 */
	@Test
	@Tag("Functional")
	void testLoadFromFile() throws ConfigurationImportException, FileNotFoundException, IOException {
		rcfg.loadFromFile(cfgFile);
		assertEquals(4, rcfg.getOperations().size());
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
		assertEquals(RegReplaceOperation.class, op.getClass());
		assertEquals("Gender", op.getName());
		assertEquals("gender", ((RegReplaceOperation) op).getSrcColumn());
		assertEquals("(.).*", ((RegReplaceOperation) op).getPattern());
		assertEquals("$1", ((RegReplaceOperation) op).getReplacement());
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)}
	 * when provided file does not exist.
	 */
	@Test
	void testLoadFromFileNoFile() {
		assertThrows(FileNotFoundException.class, () -> rcfg.loadFromFile(new File("a/b/c/d")));
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * configuration file is empty.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFileNoRoot() throws IOException {
		File cfg = createFileWith("");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * {@link YAMLtags#OPDEF_ROOT_KEY} is not present.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFileNoRootKeyword() throws IOException {
		File cfg = createFileWith("Hello:");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * {@link YAMLtags#OPDEF_ROOT_KEY} is not present.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFileNoYaml() throws IOException {
		File cfg = createFileWith("Hello");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * no operations are present under root.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFileNoOperations() throws IOException {
		File cfg = createFileWith(YAMLtags.OPDEF_ROOT_KEY + ": []");
		rcfg.loadFromFile(cfg);
		assertTrue(rcfg.getOperations().isEmpty());
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#loadFromFile(java.io.File)} if
	 * root does not contain a list for values.
	 *
	 * @throws IOException
	 */
	@Test
	void testLoadFromFileNoListUnderRoot() throws IOException {
		File cfg = createFileWith(YAMLtags.OPDEF_ROOT_KEY + ":\n" + "  hello: there");
		assertThrows(ConfigurationImportException.class, () -> rcfg.loadFromFile(cfg));
		cfg.delete();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}.
	 */
	@Test
	void testCreateOperation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}.
	 */
	@Test
	void testCreateOperationNull() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}.
	 */
	@Test
	void testCreateOperationEmpty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if main key {@link YAMLtags#OPDEF_COLUMN_KEY} is not present.
	 */
	@Test
	void testCreateOperationNoMainKey() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * if an operation is not correctly configured.
	 */
	@Test
	void testCreateOperationWrong() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * for the shortcut of a {@link GetOperation}.
	 */
	@Test
	@Tag("Functional")
	void testCreateOperationOpGet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#createOperation(java.util.Map)}
	 * for the shortcut of a {@link ValueOperation}.
	 */
	@Test
	@Tag("Functional")
	void testCreateOperationOpValue() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationAbstractReorgOperation() {
		rcfg.addOperation(op);
		assertIterableEquals(Arrays.asList(new AbstractReorgOperation[] { op }), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}
	 * to check that adding multiple operations keep them in insertion order.
	 */
	@Test
	void testAddOperationAbstractReorgOperationMultiple(@Mock AbstractReorgOperation op2) {
		rcfg.addOperation(op);
		rcfg.addOperation(op2);
		assertIterableEquals(Arrays.asList(new AbstractReorgOperation[] { op, op2 }), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}
	 * that checks that adding a null operation throws an exception.
	 */
	@Test
	void testAddOperationAbstractReorgOperationNull() {
		assertThrows(IllegalArgumentException.class, () -> rcfg.addOperation(null),
				"Adding a null operation should throw an exception");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(int, com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperation() {
		rcfg.addOperation(0, op);
		assertIterableEquals(Arrays.asList(new AbstractReorgOperation[] { op }), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(int, com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}
	 * checks that adding an operation at an index out of bounds just adds the operation at the end of
	 * the list without triggering an error.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperationNoOutOfBounds(@Mock AbstractReorgOperation opOrig) {
		rcfg.addOperation(opOrig);
		rcfg.addOperation(5, op);
		assertIterableEquals(Arrays.asList(new AbstractReorgOperation[] { opOrig, op }), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(int, com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}
	 * checks that multiple operations can be added via index and that they will respect the assigned
	 * order.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperationMultiple(@Mock AbstractReorgOperation op2,
			@Mock AbstractReorgOperation op3) {
		rcfg.addOperation(0, op);
		rcfg.addOperation(0, op2);
		rcfg.addOperation(1, op3);
		assertIterableEquals(Arrays.asList(new AbstractReorgOperation[] { op2, op3, op }), rcfg.getOperations(),
				"Operations should contain only provided operations in given order");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#addOperation(int, com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation)}
	 * checks that adding a null operation triggers an errors.
	 */
	@Test
	void testAddOperationIntAbstractReorgOperationNull() {
		assertThrows(IllegalArgumentException.class, () -> rcfg.addOperation(5, null),
				"Adding a null operation should throw an exception");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#fromFile(java.io.File)}
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
		nrcfg = ReorgConfiguration.fromFile(cfgFile);
		assertNotNull(nrcfg);
		assertNotSame(nrcfg, rcfg);
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration#getOperations()}.
	 */
	@Test
	void testGetOperations() {
		fail("Not yet implemented");
	}

}
