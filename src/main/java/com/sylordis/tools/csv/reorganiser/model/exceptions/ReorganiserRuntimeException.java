package com.sylordis.tools.csv.reorganiser.model.exceptions;

/**
 * Global runtime exception for the reorganiser.
 *
 * @author sylordis
 *
 */
public class ReorganiserRuntimeException extends RuntimeException {

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public ReorganiserRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 * @param message the detail message
	 */
	public ReorganiserRuntimeException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param cause the cause
	 */
	public ReorganiserRuntimeException(Throwable cause) {
		super(cause);
	}

}
