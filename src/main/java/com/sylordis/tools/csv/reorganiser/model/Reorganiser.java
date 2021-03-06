package com.sylordis.tools.csv.reorganiser.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationException;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException;
import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * Model of the reorganiser.
 *
 * @author sylordis
 *
 */
public class Reorganiser {

	/**
	 * Comment written as first line of generated reorganised files.
	 */
	public static final String TARGET_COMMENT = "# Generated by CSV reorganiser (%DATE)";
	/**
	 * File with base content.
	 */
	private File srcFile;
	/**
	 * File to be written to.
	 */
	private File targetFile;
	/**
	 * Configuration to be extracted.
	 */
	private ReorgConfiguration cfg;
	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 *
	 * @param srcFile    File with content to be reorganised
	 * @param targetFile File created or rewritten by the process
	 * @param cfgFile    Configuration file for the reorganisation
	 */
	public Reorganiser(File srcFile, File targetFile, ReorgConfiguration cfg) {
		logger = LogManager.getLogger();
		this.srcFile = srcFile;
		this.targetFile = targetFile;
		this.cfg = cfg;
	}

	/**
	 * Triggers the generation of the target CSV file according to configuration to the target file.
	 * Target file is only written/created if at least one record has been generated.
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ConfigurationException if the configuration is null or the list of operations empty
	 */
	public void reorganise() throws FileNotFoundException, IOException {
		logger.info("Starting reorganisation");
		if (cfg == null) {
			throw new ConfigurationException("No configuration was found (null)");
		} else if (cfg.getOperations().isEmpty()) {
			throw new ConfigurationException("Configuration operations list is empty");
		} else {
			List<List<String>> recordsOut = new ArrayList<>();
			// Fill header
			String[] headerOut = cfg.getOperations().stream().map(AbstractReorgOperation::getName)
					.toArray(String[]::new);
			logger.info("Header: {}", Arrays.toString(headerOut));
			try (Reader srcInput = new FileReader(srcFile);
					FileWriter out = new FileWriter(targetFile, true);
					CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headerOut))) {
				// Generate records file
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
					writer.write(TARGET_COMMENT.replace("%DATE", new SimpleDateFormat().format(new Date())));
					writer.newLine();
				}
				Iterable<CSVRecord> recordsIn = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(srcInput);
				// Apply operations for each record
				// This solution makes it easier for later processing, but is it scalable?
				for (CSVRecord record : recordsIn) {
					List<String> recordOut = new ArrayList<>();
					for (AbstractReorgOperation op : cfg.getOperations())
						recordOut.add(op.apply(record));
					recordsOut.add(recordOut);
				}
				logger.info("{} record(s) generated", recordsOut.size());
				// Print records to target file
				for (List<String> tupleRecordOut : recordsOut)
					printer.printRecord(tupleRecordOut);
				logger.info("Reorganisation finished: {}", targetFile.getAbsolutePath());
			} catch (IllegalArgumentException e) {
				logger.error("Error when processing an operation", e);
				throw new ReorganiserRuntimeException(e);
			}
		}
	}

	/**
	 * @return the source file
	 */
	public File getSrcFile() {
		return srcFile;
	}

	/**
	 * @param srcFile the source file to set
	 */
	public void setSrcFile(File srcFile) {
		this.srcFile = srcFile;
	}

	/**
	 * @return the target file
	 */
	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * @param targetFile the target file to set
	 */
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

	/**
	 * @return the configuration
	 */
	public ReorgConfiguration getCfg() {
		return cfg;
	}

	/**
	 * @param cfg the configuration to set
	 */
	public void setCfg(ReorgConfiguration cfg) {
		this.cfg = cfg;
	}

}
