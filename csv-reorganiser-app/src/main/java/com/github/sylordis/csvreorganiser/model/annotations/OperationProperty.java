package com.github.sylordis.csvreorganiser.model.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation keeping track of operations required properties to be set to facilitate configuration
 * of future operations. Setup of operations will just parse all {@link OperationProperty}
 * annotations and fill everything by itself without having to override the setup() method.
 *
 * @author sylordis
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(OperationProperties.class)
public @interface OperationProperty {

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
	 * Whether this property is required (true) or not (false). Default is <code>false</code>.
	 * 
	 * @return
	 */
	public boolean required() default false;

	/**
	 * Position of the property. Default is <code>-1</code> for no position.
	 * 
	 * @return
	 */
	public int position() default -1;

	/**
	 * Description of the property. If left blank, the {@link #field()}'s javadoc will be taken instead.
	 * Default is empty string.
	 * 
	 * @return
	 */
	public String description() default "";

}
