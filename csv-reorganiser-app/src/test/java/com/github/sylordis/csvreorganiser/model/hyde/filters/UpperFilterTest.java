/**
 * 
 */
package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 
 */
class UpperFilterTest {

	/**
	 * Instance under test.
	 */
	private UpperFilter op;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new UpperFilter();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.UpperFilter#apply(java.lang.String)}.
	 */
	@Test
	void testApply_Null() {
		assertThrows(NullPointerException.class, () -> op.apply(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.UpperFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = {"'',''", "abc,ABC", "Abc,ABC", "ABC,ABC","SomEThing ELSe,SOMETHING ELSE"})
	void testApply(String input, String expected) {
		assertEquals(expected, op.apply(input));
	}

}
