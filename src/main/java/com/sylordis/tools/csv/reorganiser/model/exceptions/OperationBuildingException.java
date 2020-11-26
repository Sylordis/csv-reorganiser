package com.sylordis.tools.csv.reorganiser.model.exceptions;

/**
 * Exception occurring when building an operation.
 *
 * @author sylordis
 *
 */
public class OperationBuildingException extends ReorganiserRuntimeException {

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public OperationBuildingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 * @param message the detail message
	 */
	public OperationBuildingException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param cause the cause
	 */
	public OperationBuildingException(Throwable cause) {
		super(cause);
	}

}
