package com.github.sylordis.csvreorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for operations classes. Particular properties of the operations, like parameters and
 * optional parameters are specified via {@link ReorgOperationProperty} and shortcuts via
 * {@link ReorgOperationShortcut}.
 *
 * @author sylordis
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ReorgOperation {

	/**
	 * Name of the operation to be used in the dictionary. This name is supposed to be used in a case
	 * insensitive manner.
	 *
	 * @return
	 */
	String name();

}
