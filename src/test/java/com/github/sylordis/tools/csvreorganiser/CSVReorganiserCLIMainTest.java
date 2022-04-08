package com.github.sylordis.tools.csvreorganiser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.github.sylordis.tools.csvreorganiser.test.SamplesFilesConstants;

/**
 * Test suite for {@link CSVReorganiserCLIMain} class.
 *
 * @author sylordis
 *
 */
@Disabled("SecurityManager is getting removed in Java 17, state of this test suite is unknown at the moment")
class CSVReorganiserCLIMainTest {

	/**
	 * Object under test.
	 */
	private CSVReorganiserCLIMain main;

	@TempDir
	File workingDir;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		main = new CSVReorganiserCLIMain();
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
	 * Creates a temporary file in the working directory.
	 *
	 * @return
	 * @throws IOException
	 */
	private File createFile() throws IOException {
		return File.createTempFile(getClass().getName(), null, workingDir);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#CSVReorganiserMain()}.
	 */
	@Test
	@Tag("Constructor")
	void testCSVReorganiserMain() {
		assertNotNull(main);
	}

	/**
	 * Test method for {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_NotEnoughArguments() {
		main.reorganise(new String[] {});
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 *
	 * @throws IOException
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_ConfigFileNotExists() throws IOException {
		File src = createFile();
		File tgt = createFile();
		final String[] args = new String[] { "/not exists", src.getAbsolutePath(), tgt.getAbsolutePath() };
		main.reorganise(args);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_ConfigFileDirectory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	@Tag("FilePermissions")
	//	@ExpectSystemExitWithStatus(1)
	// @DisabledOnOs({ OS.WINDOWS })
	void testReorganise_ConfigFileUnreadable() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 *
	 * @throws IOException
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_SourceFileNotExists() throws IOException {
		File tgt = createFile();
		File cfg = createFile();
		final String[] args = new String[] { cfg.getAbsolutePath(), "/not exists", tgt.getAbsolutePath() };
		main.reorganise(args);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_SourceFileDirectory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	@Tag("FilePermissions")
	//	@ExpectSystemExitWithStatus(1)
	// @DisabledOnOs({ OS.WINDOWS })
	void testReorganise_SourceFileUnreadable() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 *
	 * @throws IOException
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_TargetFileNotExists() throws IOException {
		File src = createFile();
		File cfg = createFile();
		final String[] args = new String[] { cfg.getAbsolutePath(), src.getAbsolutePath(), "/not exists" };
		main.reorganise(args);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_TargetFileDirectory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 *
	 * @throws IOException
	 */
	@Test
	@Tag("FilePermissions")
	//	@ExpectSystemExitWithStatus(1)
	// @DisabledOnOs({ OS.WINDOWS })
	void testReorganise_TargetFileUnwrittable() throws IOException {
		File cfg = createFile();
		File src = createFile();
		File tgt = createFile();
		final String[] args = new String[] { cfg.getAbsolutePath(), src.getAbsolutePath(), tgt.getAbsolutePath() };
		try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(tgt))) {
			main.reorganise(args);
		}
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testReorganise_ReorganiserRuntimeException() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#reorganise(java.lang.String[])}.
	 *
	 * @throws IOException
	 */
	@Test
	@Tag("Integration")
	void testReorganise() throws IOException {
		File cfg = createFile();
		fillFileWithSamples(cfg, SamplesFilesConstants.CONFIG_CONTENT);
		File cfgExpected = createFile();
		fillFileWithSamples(cfgExpected, SamplesFilesConstants.CONFIG_CONTENT);
		File src = createFile();
		fillFileWithSamples(src, SamplesFilesConstants.SOURCE_CONTENT);
		File srcExpected = createFile();
		fillFileWithSamples(srcExpected, SamplesFilesConstants.SOURCE_CONTENT);
		File tgt = createFile();
		File tgtExpected = createFile();
		fillFileWithSamples(tgtExpected, SamplesFilesConstants.TARGET_CONTENT);
		final String[] args = new String[] { cfg.getAbsolutePath(), src.getAbsolutePath(), tgt.getAbsolutePath() };
		main.reorganise(args);
		readFile(cfg);
		System.out.println("---");
		readFile(cfgExpected);
		assertTrue(FileUtils.contentEquals(cfgExpected, cfg), "Config file should not be modified");
		assertTrue(FileUtils.contentEquals(srcExpected, src), "Source file should not be modified");
		assertTrue(FileUtils.contentEquals(tgtExpected, tgt), "Source file should not be modified");
	}

	private void readFile(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		}
	}

	/**
	 * Test method for {@link com.github.sylordis.tools.csvreorganiser.CSVReorganiserCLIMain#main(java.lang.String[])}.
	 */
	@Test
	//	@ExpectSystemExitWithStatus(1)
	void testMain_NotEnoughArguments() {
		CSVReorganiserCLIMain.main(new String[] {});
	}

}
