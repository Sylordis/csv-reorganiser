package com.github.sylordis.tools.csvreorganiser.model.chess.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mandatory annotation class for repeatable {@link ChessOperationProperty} encapsulation.
 *
 * @author sylordis
 * @since 0.2
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ChessOperationProperties {

	ChessOperationProperty[] value();

}
