package com.github.sylordis.tools.csvreorganiser.model;

import static com.github.sylordis.tools.csvreorganiser.test.SamplesFilesConstants.CONFIG_CONTENT;
import static com.github.sylordis.tools.csvreorganiser.test.SamplesFilesConstants.SOURCE_CONTENT;
import static com.github.sylordis.tools.csvreorganiser.test.SamplesFilesConstants.TARGET_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sylordis.tools.csvreorganiser.model.config.dictionary.DefaultConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.constants.MessagesConstants;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ReorganiserRuntimeException;
import com.github.sylordis.tools.csvreorganiser.model.operations.AbstractReorgOperation;

/**
 * Test suite for {@link Reorganiser} class.
 *
 * @author sylordis
 *
 */
@ExtendWith(MockitoExtension.class)
class ReorganiserTest {

	/**
	 * Instance under test (reset during setup).
	 */
	private Reorganiser reorg;
	/**
	 * Mock instance of the configuration.
	 */
	@Mock
	private ReorgConfiguration cfg;
	/**
	 * Mock instance of the operation.
	 */
	@Mock
	private AbstractReorgOperation op;

	@TempDir
	File workingDir;
	private File srcFile;
	private File targetFile;

	@BeforeEach
	void setUp() throws Exception {
		srcFile = File.createTempFile("srcFile", null, workingDir);
		targetFile = File.createTempFile("targetFile", null, workingDir);
		reorg = new Reorganiser(srcFile, targetFile, cfg);
	}

	@AfterEach
	void tearDown() throws Exception {
		srcFile.delete();
		targetFile.delete();
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
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#Reorganiser(java.io.File, java.io.File, com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiser() {
		assertNotNull(reorg, "Constructor should provide an object");
	}

	/**
	 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#getSrcFile()}.
	 */
	@Test
	void testGetSrcFile() {
		assertEquals(srcFile, reorg.getSrcFile(), "Source file should be equal to the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setSrcFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetSrcFile(TestInfo testInfo) throws IOException {
		File file = File.createTempFile(testInfo.getDisplayName(), null, workingDir);
		reorg.setSrcFile(file);
		assertEquals(file, reorg.getSrcFile(), "Source file should be equal to the one set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setSrcFile(File)} when provided
	 * file is null.
	 */
	@Test
	@Tag("Null")
	void testSetSrcFile_Null() {
		reorg.setSrcFile(null);
		assertNull(reorg.getSrcFile(), "Source file should be null if set to null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#getTargetFile()}.
	 */
	@Test
	void testGetTargetFile() {
		assertEquals(targetFile, reorg.getTargetFile(),
		        "Target file should be equal to the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setTargetFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetTargetFile(TestInfo testInfo) throws IOException {
		File file = File.createTempFile(testInfo.getDisplayName(), null, workingDir);
		reorg.setTargetFile(file);
		assertEquals(file, reorg.getTargetFile(), "Target file should be equal to the one set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setTargetFile(File)} when
	 * provided file is null.
	 */
	@Test
	@Tag("Null")
	void testSetTargetFile_Null() {
		reorg.setTargetFile(null);
		assertNull(reorg.getTargetFile(), "Target file should be null if set to null");
	}

	/**
	 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#getCfg()}.
	 */
	@Test
	void testGetCfg() {
		assertEquals(cfg, reorg.getCfg(), "Configuration should be the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setCfg(ReorgConfiguration)}.
	 */
	@Test
	void testSetCfg() {
		final ReorgConfiguration config = new ReorgConfiguration();
		assumeFalse(config.equals(cfg));
		reorg.setCfg(config);
		assertEquals(config, reorg.getCfg(), "Configuration should be set to the one provided");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#setCfg(ReorgConfiguration)}
	 * when provided configuration is null.
	 */
	@Test
	@Tag("Null")
	void testSetCfg_Null() {
		reorg.setCfg(null);
		assertNull(reorg.getCfg(), "Configuration should be null if set to null");
	}

	/*
	 * ========================================== Integration tests
	 * ==========================================
	 */

	@DisplayName("Integration tests")
	@Nested
	class IntegrationTests {
		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when an operation fails to process.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		@Tag("Integration")
		void testReorganise_WithFailedOperation() throws FileNotFoundException, IOException {
			List<AbstractReorgOperation> operations = new ArrayList<>();
			when(op.getName()).thenReturn("Failure");
			when(op.apply(any(CSVRecord.class))).thenThrow(new IllegalArgumentException("trooper"));
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			fillFileWithSamples(srcFile, SOURCE_CONTENT);
			assertThrows(ReorganiserRuntimeException.class, reorg::reorganise,
			        "An exception should be thrown when an operation is failing.");
			assertTrue(targetFile.length() > 0L,
			        "Target file should have generated content (comment + header, plus some records before failing)");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the source is not reachable.
		 */
		@Test
		@Tag("Integration")
		void testReorganise_WithUnreachableSource() {
			List<AbstractReorgOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			reorg.setSrcFile(new File("I/do/not/exist"));
			assertThrows(FileNotFoundException.class, reorg::reorganise, "A FileNotFound exception should be thrown");
			assertEquals(0L, targetFile.length(), "File should not be written in if source is unreachable");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the source is not readable.
		 *
		 * @throws IOException
		 */
		@Test
		@Tag("Integration")
		@Tag("FilePermissions")
		@DisabledOnOs({ OS.WINDOWS })
		void testReorganise_WithUnreadableSource(TestInfo info) throws IOException {
			List<AbstractReorgOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			final File sourceFile = File.createTempFile(info.getDisplayName(), null, workingDir);
			sourceFile.setReadable(false, true);
			reorg.setSrcFile(sourceFile);
			assertThrows(IOException.class, reorg::reorganise,
			        "An IO exception should be thrown because the file cannot be read");
			assertEquals(0L, targetFile.length(), "File should not be written in if the source is unreadable");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the target file is not reachable.
		 */
		@Test
		@Tag("Integration")
		@Tag("FilePermissions")
		void testReorganise_WithUnreachableTarget() {
			List<AbstractReorgOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			reorg.setTargetFile(new File("I/do/not/exist"));
			assertThrows(FileNotFoundException.class, reorg::reorganise, "A FileNotFound exception should be thrown");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the target file is not reachable.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		@Tag("Integration")
		@Tag("FilePermissions")
		void testReorganise_WithUnwritableTarget() throws FileNotFoundException, IOException {
			List<AbstractReorgOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			targetFile.setWritable(false);
			assertThrows(IOException.class, reorg::reorganise,
			        "An exception should be thrown as the file is not writable");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the configuration is not null but empty. An error should be triggered as copying the file is
		 * useless.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		@Tag("Integration")
		void testReorganise_WithEmptyConfiguration() throws FileNotFoundException, IOException {
			when(cfg.getOperations()).thenReturn(new ArrayList<AbstractReorgOperation>());
			assertThrows(ConfigurationException.class, reorg::reorganise, "A Configuration exception should be thrown");
			assertTrue(reorg.getCfg().getOperations().isEmpty());
			assertEquals(0L, targetFile.length(), "File should not be written in if no configuration has been done");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}
		 * when a null configuration object is set.
		 */
		@Test
		@Tag("Integration")
		@Tag("Null")
		void testReorganise_WithNullConfiguration() {
			reorg.setCfg(null);
			assertThrows(ConfigurationException.class, reorg::reorganise, "A Configuration exception should be thrown");
			assertEquals(0L, targetFile.length(), "File should not be written in if a null configuration is provided");
		}

		/**
		 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.Reorganiser#reorganise()}.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		@Tag("Integration")
		void testReorganise(TestInfo testinfo) throws FileNotFoundException, IOException {
			// Setup files and samples
			File sourceBackup = File.createTempFile(testinfo.getDisplayName() + "-srcbkp", null, workingDir);
			fillFileWithSamples(srcFile, SOURCE_CONTENT);
			fillFileWithSamples(sourceBackup, SOURCE_CONTENT);
			File expectedFile = File.createTempFile(testinfo.getDisplayName() + "-tgt", null, workingDir);
			fillFileWithSamples(expectedFile, TARGET_CONTENT);
			File configFile = File.createTempFile(testinfo.getDisplayName() + "-cfg", "yaml", workingDir);
			fillFileWithSamples(configFile, CONFIG_CONTENT);
			// Reorganise
			cfg = ReorgConfiguration.fromFile(configFile, new DefaultConfigurationSupplier());
			reorg.setCfg(cfg);
			reorg.reorganise();
			// Checks
			assertTrue(FileUtils.contentEquals(sourceBackup, srcFile), "Source file should not be modified");
			try (BufferedReader readerExpected = new BufferedReader(new FileReader(expectedFile));
			        BufferedReader readerTarget = new BufferedReader(new FileReader(targetFile))) {
				String lineExpected = readerExpected.readLine(), lineTarget = readerTarget.readLine();
				assertTrue(lineTarget.matches(MessagesConstants.TARGET_COMMENT.replace("%DATE", ".*")),
				        "First line of target file should be a comment");
				while ((lineExpected = readerExpected.readLine()) != null
				        && (lineTarget = readerTarget.readLine()) != null) {
					assertEquals(lineExpected, lineTarget, "Target and expected files should be equal");
				}
			}
			// Clean up
			sourceBackup.delete();
			expectedFile.delete();
			configFile.delete();
		}
	}

}
