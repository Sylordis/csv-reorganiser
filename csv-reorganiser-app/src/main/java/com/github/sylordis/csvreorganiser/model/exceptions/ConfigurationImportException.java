package com.github.sylordis.csvreorganiser.model.exceptions;

/**
 * Exceptions occurring during configuration import.
 *
 * @author sylordis
 *
 */
public class ConfigurationImportException extends ReorganiserRuntimeException {

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public ConfigurationImportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 * @param message the detail message
	 */
	public ConfigurationImportException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param cause the cause
	 */
	public ConfigurationImportException(Throwable cause) {
		super(cause);
	}

}
