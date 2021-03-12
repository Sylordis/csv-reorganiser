package com.sylordis.tools.csv.reorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ReorganiserRuntimeException} class.
 *
 * @author sylordis
 *
 */
class ReorganiserRuntimeExceptionTest {

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiserRuntimeExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testReorganiserRuntimeExceptionStringThrowableNull() {
		final String message = null;
		Throwable t = null;
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiserRuntimeExceptionString() {
		final String message = "This is an error message... ABORT!";
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testReorganiserRuntimeExceptionStringNull() {
		final String message = null;
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testReorganiserRuntimeExceptionThrowable() {
		Throwable t = new Exception();
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.ReorganiserRuntimeException#ReorganiserRuntimeException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testReorganiserRuntimeExceptionThrowableNull() {
		Throwable t = null;
		ReorganiserRuntimeException ex = new ReorganiserRuntimeException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
