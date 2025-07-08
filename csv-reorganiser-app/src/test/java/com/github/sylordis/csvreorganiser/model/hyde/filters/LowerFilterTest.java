/**
 * 
 */
package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * 
 */
class LowerFilterTest {

	/**
	 * Instance under test.
	 */
	private LowerFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new LowerFilter();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.LowerFilter#apply(java.lang.String)}.
	 */
	@Test
	void testApply_Null() {
		assertThrows(NullPointerException.class, () -> filter.apply(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.LowerFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = {"'',''", "abc,abc", "Abc,abc", "ABC,abc","SomEThing ELSe,something else"})
	void testApply(String input, String expected) {
		assertEquals(expected, filter.apply(input));
	}

	@DisplayName("Integration tests")
	@Nested
	@Tag("Integration")
	class IntegrationTests {

		@Test
		void testFill() throws SelfFillingException {
			filter.fill(List.of());
			assertNotNull(filter);
		}

	}
}
