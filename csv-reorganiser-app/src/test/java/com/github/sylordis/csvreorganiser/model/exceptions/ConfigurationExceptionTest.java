package com.github.sylordis.csvreorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ConfigurationException} class.
 *
 * @author sylordis
 *
 */
class ConfigurationExceptionTest {

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		ConfigurationException ex = new ConfigurationException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationExceptionStringThrowable_Null() {
		final String message = null;
		Throwable t = null;
		ConfigurationException ex = new ConfigurationException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationExceptionString() {
		final String message = "This is an error message... ABORT!";
		ConfigurationException ex = new ConfigurationException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testConfigurationExceptionString_Null() {
		final String message = null;
		ConfigurationException ex = new ConfigurationException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationExceptionThrowable() {
		Throwable t = new Exception();
		ConfigurationException ex = new ConfigurationException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException#ConfigurationException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testConfigurationExceptionThrowable_Null() {
		Throwable t = null;
		ConfigurationException ex = new ConfigurationException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
