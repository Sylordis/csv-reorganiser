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
class CapFirstFilterTest {

	/**
	 * Instance under test.
	 */
	private CapFirstFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new CapFirstFilter();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.CapFirstFilter#apply(java.lang.String)}.
	 */
	@Test
	void testApply_Null() {
		assertThrows(NullPointerException.class, () -> filter.apply(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.CapFirstFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = {"'',''", "abc,Abc", "Abc,Abc", "ABC,ABC","somEThing ELSe,SomEThing ELSe"})
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
