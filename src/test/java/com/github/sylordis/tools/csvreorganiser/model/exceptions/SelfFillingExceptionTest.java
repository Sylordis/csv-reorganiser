package com.github.sylordis.tools.csvreorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author sylordis
 *
 */
class SelfFillingExceptionTest {

	/**
	 * Test method for {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		SelfFillingException ex = new SelfFillingException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingExceptionStringThrowable_Null() {
		final String message = null;
		Throwable t = null;
		SelfFillingException ex = new SelfFillingException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingExceptionString() {
		final String message = "This is an error message... ABORT!";
		SelfFillingException ex = new SelfFillingException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingExceptionString_Null() {
		final String message = null;
		SelfFillingException ex = new SelfFillingException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testSelfFillingExceptionThrowable() {
		Throwable t = new Exception();
		SelfFillingException ex = new SelfFillingException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.model.exceptions.SelfFillingException#SelfFillingException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testSelfFillingExceptionThrowable_Null() {
		Throwable t = null;
		SelfFillingException ex = new SelfFillingException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
