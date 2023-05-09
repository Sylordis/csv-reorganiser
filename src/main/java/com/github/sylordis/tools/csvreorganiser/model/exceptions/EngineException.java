package com.github.sylordis.tools.csvreorganiser.model.exceptions;

/**
 * Engine exceptions are raised when the something is wrong with the engine.
 * 
 * @author sylordis
 *
 */
public class EngineException extends Exception {

	public EngineException() {
		super();
	}

	public EngineException(String message) {
		super(message);
	}

	public EngineException(Throwable cause) {
		super(cause);
	}

	public EngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public EngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
