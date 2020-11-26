package com.sylordis.tools.csv.reorganiser.model.exceptions;

import com.sylordis.tools.csv.reorganiser.model.SelfFiller;

/**
 * Exception occurring when the method {@link SelfFiller#fill(Object)} ends up in an unexpected way.
 *
 * @author sylordis
 *
 */
public class SelfFillingException extends Exception {

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public SelfFillingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 * @param message the detail message
	 */
	public SelfFillingException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param cause the cause
	 */
	public SelfFillingException(Throwable cause) {
		super(cause);
	}

}
