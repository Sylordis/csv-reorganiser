package com.sylordis.tools.csv.reorganiser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sylordis.tools.csv.reorganiser.model.ReorgConfiguration;
import com.sylordis.tools.csv.reorganiser.model.Reorganiser;
import com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException;

/**
 *
 * CLI main entry point for the CSV reorganiser.
 *
 * @author sylordis
 *
 */
public final class CSVReorganiserMain {

	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Class constructor.
	 *
	 * @param args
	 */
	public CSVReorganiserMain() {
		logger = LogManager.getLogger();
	}

	/**
	 * Runs the activity of taking the input, error checking it and running the Reorganiser model to
	 * perform the required operations.
	 *
	 * @param args command line arguments
	 * @see #usage()
	 */
	public void reorganise(String[] args) {
		logger.debug("Starting {} {}", Arrays.toString(args), args.length);
		// Args check
		if (args == null || args.length < 3)
			fatal("Wrong number of arguments.", this::usage);
		// Files check
		// We perform all checks and then stop if any error occurred
		boolean error = false;
		// Configuration file
		File cfgFile = new File(args[0]);
		if (!cfgFile.exists() || cfgFile.isDirectory() || !cfgFile.canRead()) {
			logger.error("File {} is not an existing readable file.", cfgFile.getName());
			error = true;
		}
		// Source file
		File srcFile = new File(args[1]);
		if (!srcFile.exists() || srcFile.isDirectory() || !srcFile.canRead()) {
			logger.error("File {} is not an existing readable file.", srcFile.getName());
			error = true;
		}
		// Target file
		File targetFile = new File(args[2]);
		if (targetFile.exists() && (targetFile.isDirectory() || !targetFile.canWrite())) {
			logger.error("File {} exists and cannot be written to.", targetFile.getName());
			error = true;
		}
		// Error while checking any of the previous file
		if (error)
			fatal("Files check was unsuccessful.");
		// Operations
		try {
			ReorgConfiguration cfg = ReorgConfiguration.fromFile(cfgFile);
			Reorganiser model = new Reorganiser(srcFile, targetFile, cfg);
			model.reorganise();
		} catch (IOException e) {
			logger.fatal("Error during file operation", e);
			System.exit(1);
		} catch (ReorganiserRuntimeException e) {
			logger.fatal(e);
			System.exit(1);
		}
	}

	/**
	 * Logs an error message with level fatal and exits the program in error.
	 *
	 * @param message   Fatal error message.
	 * @param runnables All runnables to run before exiting.
	 */
	private void fatal(String message, Runnable... runnables) {
		logger.fatal(message);
		for (Runnable run : runnables)
			run.run();
		System.exit(1);
	}

	/**
	 * Prints basic usage.
	 */
	public void usage() {
		logger.info("usage: <cfgfile> <src> <target>");
	}

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new CSVReorganiserMain().reorganise(args);
	}

}
