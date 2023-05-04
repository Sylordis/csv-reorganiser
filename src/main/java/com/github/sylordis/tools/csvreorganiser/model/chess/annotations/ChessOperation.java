package com.github.sylordis.tools.csvreorganiser.model.chess.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for operations classes. Particular properties of the operations, like parameters and
 * optional parameters are specified via {@link ChessOperationProperty} and shortcuts via
 * {@link ChessOperationShortcut}.
 *
 * @author sylordis
 * @since 1.0
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ChessOperation {

	/**
	 * Name of the operation to be used in the dictionary. This name is supposed to be used in a case
	 * insensitive manner.
	 *
	 * @return
	 */
	String name();

}
