package com.github.sylordis.tools.csvreorganiser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.tools.csvreorganiser.doc.MarkdownDocumentationOutput;
import com.github.sylordis.tools.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.tools.csvreorganiser.model.Reorganiser;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.EngineException;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ReorganiserRuntimeException;

/**
 *
 * CLI main entry point for the CSV reorganiser.
 *
 * @author sylordis
 *
 */
public final class CSVReorganiserCLIMain {

	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Class constructor.
	 *
	 * @param args
	 */
	public CSVReorganiserCLIMain() {
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
		boolean docMode = false;
		// Args check
		if (args == null || args.length == 0)
			fatal("Wrong number of arguments.", this::usage);
		else if ("--doc".equals(args[0])) {
			docMode = true;
		}
		if (docMode) {
			new MarkdownDocumentationOutput().generate();
		} else {
			run(args);
		}
	}

	/**
	 * Default run.
	 * @param args Command line arguments
	 */
	public void run(String[] args) {
		if (args.length < 3)
			fatal("Wrong number of arguments.", this::usage);
		// TODO args check
		// Files check
		// We perform all checks and then stop if any error occurred
		boolean error = false;
		// Configuration file
		File cfgFile = new File(args[0]);
		if (!cfgFile.exists() || cfgFile.isDirectory() || !cfgFile.canRead()) {
			logger.error("File {} is not an existing readable file.", cfgFile.getName());
			error = true;
		}
		// Source files
		List<File> srcFiles = new ArrayList<>();
		for (int i = 1; i < args.length - 1; i++) {
			File srcFile = new File(args[i]);
			if (!srcFile.exists() || srcFile.isDirectory() || !srcFile.canRead()) {
				logger.error("File {} is not an existing readable file.", srcFile.getName());
				error = true;
			} else {
				srcFiles.add(srcFile);
			}
		}
		// Target file (last argument)
		File targetFile = new File(args[args.length-1]);
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
			Reorganiser model = new Reorganiser(cfg, targetFile, srcFiles);
			model.reorganise();
		} catch (IOException e) {
			logger.fatal("Error during file operation", e);
			System.exit(1);
		} catch (ReorganiserRuntimeException e) {
			logger.fatal(e);
			System.exit(1);
		} catch (EngineException e) {
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
	private void usage() {
		logger.info("usage: <cfgfile> <src..> <target>");
	}

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new CSVReorganiserCLIMain().reorganise(args);
	}

}
