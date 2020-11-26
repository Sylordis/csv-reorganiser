package com.sylordis.tools.csv.reorganiser.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link OperationBuildingException} class.
 *
 * @author sylordis
 *
 */
class OperationBuildingExceptionTest {

	/**
	 * Test method for {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testOperationBuildingExceptionStringThrowable() {
		final String message = "This is an error message... ABORT!";
		Throwable t = new Exception();
		OperationBuildingException ex = new OperationBuildingException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.String, java.lang.Throwable)}
	 * when providing a null cause or message.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testOperationBuildingExceptionStringThrowableNull() {
		final String message = null;
		Throwable t = null;
		OperationBuildingException ex = new OperationBuildingException(message, t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testOperationBuildingExceptionString() {
		final String message = "This is an error message... ABORT!";
		OperationBuildingException ex = new OperationBuildingException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.String)}
	 * when a null message is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testOperationBuildingExceptionStringNull() {
		final String message = null;
		OperationBuildingException ex = new OperationBuildingException(message);
		assertNotNull(ex, "Newly created exception should not be null");
		assertEquals(message, ex.getMessage(), "Same message should be returned");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.Throwable)}.
	 */
	@Test
	@Tag("Constructor")
	void testOperationBuildingExceptionThrowable() {
		Throwable t = new Exception();
		OperationBuildingException ex = new OperationBuildingException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.exceptions.OperationBuildingException#OperationBuildingException(java.lang.Throwable)}
	 * when a null cause is provided.
	 */
	@Test
	@Tag("Constructor")
	@Tag("Null")
	void testOperationBuildingExceptionThrowableNull() {
		Throwable t = null;
		OperationBuildingException ex = new OperationBuildingException(t);
		assertNotNull(ex, "Newly created exception should not be null");
		assertSame(t, ex.getCause(), "Same cause should be returned when asked");
	}

}
