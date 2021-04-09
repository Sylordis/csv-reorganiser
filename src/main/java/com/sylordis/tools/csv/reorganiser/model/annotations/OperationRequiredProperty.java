package com.sylordis.tools.csv.reorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation keeping track of operations required properties to be set to facilitate configuration
 * of future operations. Setup of operations will just parse all {@link OperationRequiredProperty}
 * annotations and fill everything by itself without having to override the setup() method.
 *
 * @author sylordis
 * @since 0.2
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(OperationRequiredProperties.class)
public @interface OperationRequiredProperty {

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
	 * Description of the required property.
	 *
	 * @return description of the property
	 */
	public String description() default "";

}
