package com.sylordis.tools.csv.reorganiser.model.exceptions;

import com.sylordis.tools.csv.reorganiser.model.SelfFiller;

/**
 * Exception occurring when the method {@link SelfFiller#fill(Object)} ends up in an unexpected way
 * because of the configuration.
 *
 * @author sylordis
 *
 */
public class SelfFillingConfigurationException extends ReorganiserRuntimeException {

	/**
	 * @see RuntimeException#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public SelfFillingConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#Exception(String)
	 * @param message the detail message
	 */
	public SelfFillingConfigurationException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#Exception(Throwable)
	 * @param cause the cause
	 */
	public SelfFillingConfigurationException(Throwable cause) {
		super(cause);
	}

}
