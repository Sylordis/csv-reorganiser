package com.github.sylordis.csvreorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Denotes an operation to have a configuration shortcut
 *
 * @author sylordis
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ReorgOperationShortcut {

	/**
	 * YAML keyword for this shortcut
	 *
	 * @return
	 */
	String keyword();

	/**
	 * Property that this keyword's value links to
	 *
	 * @return
	 */
	String property();

}
