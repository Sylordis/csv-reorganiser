package com.github.sylordis.csvreorganiser.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.utils.CodeGenerationUtils;

/**
 * Utils for pathing.
 */
public class PathsUtils {

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

	private PathsUtils() {
		// Nothing to do here
	}

}
