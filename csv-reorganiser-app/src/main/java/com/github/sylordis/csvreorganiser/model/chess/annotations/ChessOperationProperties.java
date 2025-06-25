package com.github.sylordis.csvreorganiser.model.chess.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mandatory annotation class for repeatable {@link ChessOperationProperty} encapsulation.
 *
 * @author sylordis
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ChessOperationProperties {

	ChessOperationProperty[] value();

}
