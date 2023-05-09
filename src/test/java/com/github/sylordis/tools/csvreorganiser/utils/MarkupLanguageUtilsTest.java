package com.github.sylordis.tools.csvreorganiser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author sylordis
 *
 */
class MarkupLanguageUtilsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

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
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#splitCamelCase(java.lang.String)}.
	 */
	@Test
	void testSplitCamelCase() {
		assertEquals("this Is Not A Test", MarkupLanguageUtils.splitCamelCase("thisIsNotATest"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#splitCamelCase(java.lang.String)}.
	 */
	@Test
	void testSplitCamelCase_blank() {
		assertEquals("  ", MarkupLanguageUtils.splitCamelCase("  "));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Emphasis() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_List() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Italic() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Bold() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Table() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Table2() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_Headers() {
		fail("Not yet implemented");
	}
	/**
	 * Test method for
	 * {@link com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils#htmlToMarkdown(java.lang.String)}.
	 */
	@Test
	void testHtmlToMarkdown_mix() {
		fail("Not yet implemented");
	}
}
