package com.github.sylordis.csvreorganiser.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

/**
 * Utils for parsing.
 */
public class ParsingUtils {

	/**
	 * Gets the root of the current Gradle module from a given class.
	 * 
	 * @param c
	 * @return
	 */
	public static Path gradleModuleRoot(Class<?> c) {
		String buildFileName = "build.gradle";

		Path other = Paths.get(".");
		Path normalize = CodeGenerationUtils.classLoaderRoot(c).resolve(other).normalize();

		// If it's not a directory, or if it is a directory and the build file isn't present, go up a level.
		while (!normalize.toFile().isDirectory() || !normalize.resolve(buildFileName).toFile().exists()) {
			other = other.resolve("..");
			normalize = CodeGenerationUtils.classLoaderRoot(c).resolve(other).normalize();
		}

		return normalize;
	}

	/**
	 * Resolves the given path from the Gradle module root.
	 * 
	 * @param path additional path to resolve from the module root
	 * @return a Source Root
	 */
	public static SourceRoot sourceFromRoot(String path) {
		return new SourceRoot(ParsingUtils.gradleModuleRoot(ParsingUtils.class).resolve(path));
	}

	/**
	 * Sanitises a Javadoc to be processed properly as String.
	 * @param doc
	 * @return
	 */
	public static String sanitiseJavadoc(String doc) {
		return doc.replaceAll("\n[ \t]+\\* ?", "\n").replaceAll("(?m)^([ \t]*|@.*)\r?\n", "").trim();
	}

	private ParsingUtils() {
		// Nothing to do here
	}

}
