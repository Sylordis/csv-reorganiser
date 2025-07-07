/**
 * 
 */
package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 
 */
class LowerFilterTest {

	/**
	 * Instance under test.
	 */
	private LowerFilter op;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new LowerFilter();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.LowerFilter#apply(java.lang.String)}.
	 */
	@Test
	void testApply_Null() {
		assertThrows(NullPointerException.class, () -> op.apply(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.LowerFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = {"'',''", "abc,abc", "Abc,abc", "ABC,abc","SomEThing ELSe,something else"})
	void testApply(String input, String expected) {
		assertEquals(expected, op.apply(input));
	}

}
