package com.github.sylordis.csvreorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation keeping track of operations required properties to be set to facilitate configuration
 * of future operations. Setup of operations will just parse all {@link ReorgOperationProperty}
 * annotations and fill everything by itself without having to override the setup() method.
 *
 * @author sylordis
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(ReorgOperationProperties.class)
public @interface ReorgOperationProperty {

	/**
	 * Name of the required property in configuration.
	 *
	 * @return name of the property
	 */
	public String name();

	/**
	 * Java field holding the property's value.
	 *
	 * @return java field of the property
	 */
	public String field();

	/**
	 * Whether this property is required (true) or not (false).
	 * 
	 * @return
	 */
	public boolean required() default false;

	/**
	 * Description of the required property.
	 *
	 * @return description of the property
	 */
	public String description() default "";

	/**
	 * Position of the property.
	 * 
	 * @return
	 */
	public int position() default -1;

}
