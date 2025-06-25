package com.github.sylordis.csvreorganiser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author sylordis
 *
 */
class MarkupLanguageUtilsTest {

	/**
	 * Test method for {@link MarkupLanguageUtils} to check if the class cannot be instantiated.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Test
	@Tag("Constructor")
	void testConstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	        InvocationTargetException, NoSuchMethodException, SecurityException {
		assertThrows(IllegalAccessException.class,
		        () -> MarkupLanguageUtils.class.getDeclaredConstructor().newInstance());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.MarkupLanguageUtils#splitCamelCase(java.lang.String)}.
	 */
	@Test
	void testSplitCamelCase() {
		assertEquals("this Is Not A Test", MarkupLanguageUtils.splitCamelCase("thisIsNotATest"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.MarkupLanguageUtils#splitCamelCase(java.lang.String)}.
	 */
	@Test
	void testSplitCamelCase_blank() {
		assertEquals("  ", MarkupLanguageUtils.splitCamelCase("  "));
	}

}
