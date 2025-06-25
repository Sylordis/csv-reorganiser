package com.github.sylordis.csvreorganiser.model.exceptions;

/**
 * Exceptions occurring because of configuration screwups.
 *
 * @author sylordis
 *
 */
public class ConfigurationException extends ReorganiserRuntimeException {

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 * @param message the detail message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param cause the cause
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
