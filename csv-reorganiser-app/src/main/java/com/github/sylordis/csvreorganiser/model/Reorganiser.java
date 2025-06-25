package com.github.sylordis.csvreorganiser.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.constants.MessagesConstants;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.ReorganiserRuntimeException;

/**
 * Model of the reorganiser.
 *
 * @author sylordis
 *
 */
public class Reorganiser {

	/**
	 * Files with base content.
	 */
	private List<File> srcFiles;
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
	 * @param srcFiles   Files with content to be reorganised
	 * @param targetFile File created or rewritten by the process
	 * @param cfgFile    Configuration file for the reorganisation
	 */
	public Reorganiser(ReorgConfiguration cfg, File targetFile, List<File> srcFiles) {
		logger = LogManager.getLogger();
		this.srcFiles = new ArrayList<>();
		this.srcFiles.addAll(srcFiles);
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
		} else if (srcFiles.isEmpty()) {
			throw new ReorganiserRuntimeException("No source files provided");
		} else {
			validateFiles();
			// Fill header
			String[] headerOut = cfg.getOperations().stream().map(ReorganiserOperation::getName)
			        .toArray(String[]::new);
			logger.info("Header: {}", Arrays.toString(headerOut));
			logger.debug("Source files to process: {}", srcFiles);
			try (FileWriter out = new FileWriter(targetFile, true);
			        CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create().setHeader(headerOut).build())) {
				// Generate records file
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
					writer.write(MessagesConstants.TARGET_COMMENT.replace("%DATE",
					        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
					writer.newLine();
				}
				// For each file
				for (File srcFile : srcFiles) {
					List<List<String>> recordsOut = new ArrayList<>();
					logger.debug("Processing source file {}", srcFile);
					try (Reader srcInput = new FileReader(srcFile)) {
						Iterable<CSVRecord> recordsIn = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true)
						        .build().parse(srcInput);
						// Apply operations for each record
						for (CSVRecord record : recordsIn) {
							List<String> recordOut = new ArrayList<>();
							for (ReorganiserOperation op : cfg.getOperations())
								recordOut.add(op.apply(record));
							recordsOut.add(recordOut);
						}
						logger.info("{} record(s) generated", recordsOut.size());
						// Print records to target file
						for (List<String> tupleRecordOut : recordsOut)
							printer.printRecord(tupleRecordOut);
					}
				}
				logger.info("Reorganisation finished: {}", targetFile.getAbsolutePath());
			} catch (IllegalArgumentException e) {
				logger.error("Error when processing an operation", e);
				throw new ReorganiserRuntimeException(e);
			}
		}
	}

	/**
	 * Checks if all files are valid. An exception is thrown if at least one file is not found or does
	 * not satisfy its criteria.
	 * 
	 * @return
	 * @throws FileNotFoundException if at least one file is not valid
	 */
	public void validateFiles() throws FileNotFoundException {
		List<File> notValid = new ArrayList<>();
		// Check if all source files are reachable
		for (File srcFile : srcFiles) {
			if (!srcFile.canRead()) {
				notValid.add(srcFile);
			}
		}
		if (targetFile.exists() && !targetFile.canWrite())
			notValid.add(targetFile);
		if (!notValid.isEmpty())
			throw new FileNotFoundException(notValid.toString());
	}

	/**
	 * @return the source files
	 */
	public List<File> getSrcFiles() {
		return srcFiles;
	}

	/**
	 * @param srcFiles the source file to set
	 */
	public void setSrcFiles(List<File> srcFiles) {
		this.srcFiles.clear();
		if (srcFiles != null)
			this.srcFiles.addAll(srcFiles);
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
