package com.github.sylordis.csvreorganiser.model;

import static com.github.sylordis.csvreorganiser.test.SamplesFilesConstants.CONFIG_CONTENT_CHESS;
import static com.github.sylordis.csvreorganiser.test.SamplesFilesConstants.SOURCE_CONTENT;
import static com.github.sylordis.csvreorganiser.test.SamplesFilesConstants.SOURCE_CONTENT_2;
import static com.github.sylordis.csvreorganiser.test.SamplesFilesConstants.TARGET_CONTENT;
import static com.github.sylordis.csvreorganiser.test.SamplesFilesConstants.TARGET_CONTENT_2;
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

import com.github.sylordis.csvreorganiser.model.chess.ChessEngine;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.constants.MessagesConstants;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.EngineException;
import com.github.sylordis.csvreorganiser.model.exceptions.ReorganiserRuntimeException;

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
	private ChessAbstractReorgOperation op;

	@TempDir
	File workingDir;
	private File srcFile;
	private File targetFile;

	@BeforeEach
	void setUp() throws Exception {
		srcFile = File.createTempFile("srcFile", null, workingDir);
		targetFile = File.createTempFile("targetFile", null, workingDir);
		reorg = new Reorganiser(cfg, targetFile, List.of(srcFile));
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
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#Reorganiser(java.io.File, java.io.File, com.github.sylordis.csvreorganiser.model.ReorgConfiguration)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiser() {
		assertNotNull(reorg, "Constructor should provide an object");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#getSrcFile()}.
	 */
	@Test
	void testGetSrcFiles() {
		assertEquals(List.of(srcFile), reorg.getSrcFiles(),
		        "Source files should be equal to the ones provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setSrcFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetSrcFiles(TestInfo testInfo) throws IOException {
		File file = File.createTempFile(testInfo.getDisplayName(), null, workingDir);
		reorg.setSrcFiles(List.of(file));
		assertEquals(List.of(file), reorg.getSrcFiles(), "Source file should be equal to the ones set");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setSrcFile(File)} when provided
	 * multiple files.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetSrcFiles_SeveralFiles(TestInfo testInfo) throws IOException {
		File fileA = new File("my/a");
		File fileB = new File("my/b");
		File fileC = new File("my/c");
		List<File> srcFiles = List.of(fileA, fileB, fileC);
		reorg.setSrcFiles(srcFiles);
		assertEquals(srcFiles, reorg.getSrcFiles(),
		        "Source file should be equal to the ones set and in the same order");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setSrcFile(File)} when provided
	 * file is null.
	 */
	@Test
	@Tag("Null")
	void testSetSrcFiles_Null() {
		reorg.setSrcFiles(null);
		assertEquals(new ArrayList<>(), reorg.getSrcFiles(), "Source files should be empty if provided null");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#getTargetFile()}.
	 */
	@Test
	void testGetTargetFile() {
		assertEquals(targetFile, reorg.getTargetFile(),
		        "Target file should be equal to the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setTargetFile(File)}.
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
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setTargetFile(File)} when
	 * provided file is null.
	 */
	@Test
	@Tag("Null")
	void testSetTargetFile_Null() {
		reorg.setTargetFile(null);
		assertNull(reorg.getTargetFile(), "Target file should be null if set to null");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#getCfg()}.
	 */
	@Test
	void testGetCfg() {
		assertEquals(cfg, reorg.getCfg(), "Configuration should be the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setCfg(ReorgConfiguration)}.
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
	 * {@link com.github.sylordis.csvreorganiser.model.Reorganiser#setCfg(ReorgConfiguration)}
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
	@Tag("Integration")
	class IntegrationTests {
		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when an operation fails to process.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		void testReorganise_WithFailedOperation() throws FileNotFoundException, IOException {
			List<ReorganiserOperation> operations = new ArrayList<>();
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
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the source is not reachable.
		 */
		@Test
		void testReorganise_WithUnreachableSource() {
			List<ReorganiserOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			reorg.setSrcFiles(List.of(new File("I/do/not/exist")));
			assertThrows(FileNotFoundException.class, reorg::reorganise, "A FileNotFound exception should be thrown");
			assertEquals(0L, targetFile.length(), "File should not be written in if source is unreachable");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the source is not readable.
		 *
		 * @throws IOException
		 */
		@Test
		@Tag("FilePermissions")
		@DisabledOnOs({ OS.WINDOWS })
		void testReorganise_WithUnreadableSource(TestInfo info) throws IOException {
			List<ReorganiserOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			final File sourceFile = File.createTempFile(info.getDisplayName(), null, workingDir);
			sourceFile.setReadable(false, true);
			reorg.setSrcFiles(List.of(sourceFile));
			assertThrows(IOException.class, reorg::reorganise,
			        "An IO exception should be thrown because the file cannot be read");
			assertEquals(0L, targetFile.length(), "File should not be written in if the source is unreadable");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the target file is not reachable.
		 */
		@Test
		@Tag("FilePermissions")
		void testReorganise_WithUnreachableTarget() {
			List<ReorganiserOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			reorg.setTargetFile(new File("I/do/not/exist"));
			assertThrows(FileNotFoundException.class, reorg::reorganise, "A FileNotFound exception should be thrown");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the target file is not reachable.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		@Tag("FilePermissions")
		void testReorganise_WithUnwritableTarget() throws FileNotFoundException, IOException {
			List<ReorganiserOperation> operations = new ArrayList<>();
			operations.add(op);
			when(cfg.getOperations()).thenReturn(operations);
			targetFile.setWritable(false);
			assertThrows(IOException.class, reorg::reorganise,
			        "An exception should be thrown as the file is not writable");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when the configuration is not null but empty. An error should be triggered as copying the file is
		 * useless.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		@Test
		void testReorganise_WithEmptyConfiguration() throws FileNotFoundException, IOException {
			when(cfg.getOperations()).thenReturn(new ArrayList<ReorganiserOperation>());
			assertThrows(ConfigurationException.class, reorg::reorganise, "A Configuration exception should be thrown");
			assertTrue(reorg.getCfg().getOperations().isEmpty());
			assertEquals(0L, targetFile.length(), "File should not be written in if no configuration has been done");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}
		 * when a null configuration object is set.
		 */
		@Test
		@Tag("Null")
		void testReorganise_WithNullConfiguration() {
			reorg.setCfg(null);
			assertThrows(ConfigurationException.class, reorg::reorganise, "A Configuration exception should be thrown");
			assertEquals(0L, targetFile.length(), "File should not be written in if a null configuration is provided");
		}

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 * @throws EngineException
		 * @throws ConfigurationImportException
		 */
		@Test
		void testReorganise_Chess(TestInfo testinfo)
		        throws FileNotFoundException, IOException, ConfigurationImportException, EngineException {
			// Setup files and samples
			File sourceBackup = File.createTempFile(testinfo.getDisplayName() + "-srcbkp", null, workingDir);
			fillFileWithSamples(srcFile, SOURCE_CONTENT);
			fillFileWithSamples(sourceBackup, SOURCE_CONTENT);
			File expectedFile = File.createTempFile(testinfo.getDisplayName() + "-tgt", null, workingDir);
			fillFileWithSamples(expectedFile, TARGET_CONTENT);
			File configFile = File.createTempFile(testinfo.getDisplayName() + "-cfg", "yaml", workingDir);
			fillFileWithSamples(configFile, CONFIG_CONTENT_CHESS);
			// Reorganise
			cfg = ReorgConfiguration.fromFile(configFile, ChessEngine.createDefaultEngine());
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

		/**
		 * Test method for {@link com.github.sylordis.csvreorganiser.model.Reorganiser#reorganise()}.
		 *
		 * @throws IOException
		 * @throws FileNotFoundException
		 * @throws EngineException
		 * @throws ConfigurationImportException
		 */
		@Test
		void testReorganise_Chess_withMultipleSources(TestInfo testinfo)
		        throws FileNotFoundException, IOException, ConfigurationImportException, EngineException {
			// Setup files and samples
			// First source
			File sourceBackup = File.createTempFile(testinfo.getDisplayName() + "-srcbkp", null, workingDir);
			fillFileWithSamples(srcFile, SOURCE_CONTENT);
			fillFileWithSamples(sourceBackup, SOURCE_CONTENT);
			// Second source
			File srcFile2 = File.createTempFile("srcFile_add", null, workingDir);
			File sourceBackup2 = File.createTempFile(testinfo.getDisplayName() + "_2-srcbkp", null, workingDir);
			fillFileWithSamples(srcFile2, SOURCE_CONTENT_2);
			fillFileWithSamples(sourceBackup2, SOURCE_CONTENT_2);
			// Expectation
			File expectedFile = File.createTempFile(testinfo.getDisplayName() + "-tgt", null, workingDir);
			fillFileWithSamples(expectedFile, TARGET_CONTENT_2);
			// Config
			File configFile = File.createTempFile(testinfo.getDisplayName() + "-cfg", "yaml", workingDir);
			fillFileWithSamples(configFile, CONFIG_CONTENT_CHESS);
			// Reorganise
			cfg = ReorgConfiguration.fromFile(configFile, ChessEngine.createDefaultEngine());
			reorg = new Reorganiser(cfg, targetFile, List.of(srcFile, srcFile2));
			reorg.reorganise();
			// Checks
			assertTrue(FileUtils.contentEquals(sourceBackup, srcFile), "Source file should not be modified");
			assertTrue(FileUtils.contentEquals(sourceBackup2, srcFile2), "Second source file should not be modified");
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
