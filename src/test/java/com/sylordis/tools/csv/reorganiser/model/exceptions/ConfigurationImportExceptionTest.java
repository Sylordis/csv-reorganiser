package com.sylordis.tools.csv.reorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ConfigurationImportException} class.
 *
 * @author sylordis
 *
 */
class ConfigurationImportExceptionTest {

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationImportExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		ConfigurationImportException ex = new ConfigurationImportException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testConfigurationImportExceptionStringThrowableNull() {
		final String message = null;
		Throwable t = null;
		ConfigurationImportException ex = new ConfigurationImportException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationImportExceptionString() {
		final String message = "This is an error message... ABORT!";
		ConfigurationImportException ex = new ConfigurationImportException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testConfigurationImportExceptionStringNull() {
		final String message = null;
		ConfigurationImportException ex = new ConfigurationImportException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testConfigurationImportExceptionThrowable() {
		Throwable t = new Exception();
		ConfigurationImportException ex = new ConfigurationImportException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ConfigurationImportException#ConfigurationImportException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testConfigurationImportExceptionThrowableNull() {
		Throwable t = null;
		ConfigurationImportException ex = new ConfigurationImportException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
