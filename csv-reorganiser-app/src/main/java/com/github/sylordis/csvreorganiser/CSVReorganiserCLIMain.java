package com.github.sylordis.csvreorganiser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess;
import com.github.sylordis.csvreorganiser.model.ReorgConfiguration;
import com.github.sylordis.csvreorganiser.model.Reorganiser;
import com.github.sylordis.csvreorganiser.model.chess.config.ChessDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.exceptions.EngineException;
import com.github.sylordis.csvreorganiser.model.exceptions.ReorganiserRuntimeException;

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
	private final Logger logger = LogManager.getLogger();
	/**
	 * Options for command line.
	 */
	private Options options = new Options();

	/**
	 * Runs the activity of taking the input, error checking it and running the Reorganiser model to
	 * perform the required operations.
	 *
	 * @param args command line arguments
	 * @see #usage()
	 */
	public void reorganise(String[] args) {
		// Args check
		if (args.length < 3)
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
		File targetFile = new File(args[args.length - 1]);
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
	 * Default run.
	 * 
	 * @param args Command line arguments
	 */
	public void run(String[] args) {
		this.options = new Options();
		Option optionDoc = new Option("d", "doc", false, "Generates the documentation without running the software.");
		Option optionEngine = new Option("e", "engine", true, "Specifies the engine.");
		Option optionHelp = new Option("h", "help", false, "Displays this help message.");
		options.addOption(optionDoc);
		options.addOption(optionEngine);
		options.addOption(optionHelp);
		CommandLineParser cliParser = new DefaultParser();
		try {
			CommandLine cli = cliParser.parse(options, args);
			if (cli.hasOption(optionHelp)) {
				usage();
			} else if (cli.hasOption(optionDoc)) {
				generateDocumentation();
			} else {
				reorganise(args);
			}
		} catch (ParseException e) {
			logger.error(e);
		}
	}

	/**
	 * Generates code documentation.
	 */
	private void generateDocumentation() {
		new MarkdownDocumentationOutputChess().generate(new ChessDefaultConfigurationSupplier());
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
		HelpFormatter help = new HelpFormatter();
		help.printHelp("software <cfgfile> <src..> <target>", """
		        with:
		          cfgfile
		            Path to configuration file for reorganisation.
		          src
		            Source files, paths to files to be reorganised and aggregated.
		            Those files should have the same columns available.
		          target
		            Target file, path to file to be written with the results.
		        
		        """, options, null, true);
	}

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new CSVReorganiserCLIMain().run(args);
	}

}
