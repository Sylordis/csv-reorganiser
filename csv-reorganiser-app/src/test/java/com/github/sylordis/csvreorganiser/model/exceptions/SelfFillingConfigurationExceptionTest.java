package com.github.sylordis.csvreorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link SelfFillingSelfFillingConfigurationException} class.
 *
 * @author sylordis
 *
 */
class SelfFillingSelfFillingConfigurationExceptionTest {

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingConfigurationExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingConfigurationExceptionStringThrowable_Null() {
		final String message = null;
		Throwable t = null;
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingConfigurationExceptionString() {
		final String message = "This is an error message... ABORT!";
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingConfigurationExceptionString_Null() {
		final String message = null;
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingConfigurationExceptionThrowable() {
		Throwable t = new Exception();
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingConfigurationException#SelfFillingConfigurationException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingConfigurationExceptionThrowable_Null() {
		Throwable t = null;
		SelfFillingConfigurationException ex = new SelfFillingConfigurationException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
