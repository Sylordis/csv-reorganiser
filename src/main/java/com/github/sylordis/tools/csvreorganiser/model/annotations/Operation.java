package com.github.sylordis.tools.csvreorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for operations classes.
 *
 * @author sylordis
 * @since 1.0
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Operation {

	/**
	 * Name of the operation to be used in the dictionary. This name is supposed to be used in a case
	 * insensitive manner.
	 *
	 * @return
	 */
	String name();

}
