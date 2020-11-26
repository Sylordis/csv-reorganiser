package com.sylordis.tools.csv.reorganiser.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link Reorganiser} class.
 *
 * @author sylordis
 *
 */
@ExtendWith(MockitoExtension.class)
class ReorganiserTest {

	/**
	 * Test source file
	 */
	private final String SOURCE_CONTENT = "people.csv";

	/**
	 * Instance under test (reset during setup).
	 */
	private Reorganiser reorg;
	/**
	 * Mock instance of the configuration.
	 */
	private ReorgConfiguration cfg;
	@TempDir
	File workingDir;
	private File srcFile;
	private File targetFile;

	@BeforeEach
	void setUp() throws Exception {
		cfg = new ReorgConfiguration();
		srcFile = File.createTempFile("srcFile", null, workingDir);
		targetFile = File.createTempFile("targetFile", null, workingDir);
		reorg = new Reorganiser(srcFile, targetFile, cfg);
		try (OutputStream stream = new FileOutputStream(srcFile);
				InputStream istream = ReorganiserTest.class.getClassLoader().getResourceAsStream(SOURCE_CONTENT)) {
			stream.write(istream.readAllBytes());
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		srcFile.delete();
		targetFile.delete();
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#Reorganiser(java.io.File, java.io.File, com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiser() {
		assertNotNull(reorg, "Constructor should provide an object");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#getSrcFile()}.
	 */
	@Test
	void testGetSrcFile() {
		assertEquals(srcFile, reorg.getSrcFile(), "Source file should be equal to the one provided in the constructor");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setSrcFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetSrcFile() throws IOException {
		File file = File.createTempFile("aNewSrcFile", null, workingDir);
		reorg.setSrcFile(file);
		assertEquals(file, reorg.getSrcFile(), "Source file should be equal to the one set");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setSrcFile(File)}
	 * when provided file is null.
	 */
	@Test
	void testSetSrcFileNull() {
		reorg.setSrcFile(null);
		assertNull(reorg.getSrcFile(), "Source file should be null if set to null");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#getTargetFile()}.
	 */
	@Test
	void testGetTargetFile() {
		assertEquals(targetFile, reorg.getTargetFile(),
				"Target file should be equal to the one provided in the constructor");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setTargetFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	void testSetTargetFile() throws IOException {
		File file = File.createTempFile("aNewTargetFile", null, workingDir);
		reorg.setTargetFile(file);
		assertEquals(file, reorg.getTargetFile(), "Target file should be equal to the one set");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setTargetFile(File)}
	 * when provided file is null.
	 */
	@Test
	void testSetTargetFileNull() {
		reorg.setTargetFile(null);
		assertNull(reorg.getTargetFile(), "Target file should be null if set to null");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#getCfg()}.
	 */
	@Test
	void testGetCfg() {
		assertEquals(cfg, reorg.getCfg(), "Configuration should be the one provided in the constructor");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setCfg(ReorgConfiguration)}.
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
	 * {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#setCfg(ReorgConfiguration)} when
	 * provided configuration is null.
	 */
	@Test
	void testSetCfgNull() {
		reorg.setCfg(null);
		assertNull(reorg.getCfg(), "Configuration should be null if set to null");
	}

	/*
	 * Functional tests
	 */

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()} when an
	 * operation fails to process.
	 */
	@Test
	void testReorganiseWithFailedOperation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()} when
	 * the source is not reachable.
	 */
	@Test
	void testReorganiseWithUnreachableSource() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()} when
	 * the target file is not reachable.
	 */
	@Test
	void testReorganiseWithUnreachableTarget() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()} when
	 * the configuration is not set at all.
	 */
	@Test
	void testReorganiseWithEmptyConfiguration() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()} when a
	 * null configuration object is set.
	 */
	@Test
	void testReorganiseWithNullConfiguration() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.Reorganiser#reorganise()}.
	 */
	@Test
	void testReorganise() {
		// Setup configuration
		// Reorganise
		// Check source file hasn't changed
		// Check target file content
		fail("Not yet implemented");
	}

}
