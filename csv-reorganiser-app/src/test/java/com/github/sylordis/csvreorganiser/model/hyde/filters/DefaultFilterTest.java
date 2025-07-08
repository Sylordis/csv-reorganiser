package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * Test class for {@link DefaultFilter}.
 */
class DefaultFilterTest {

	/**
	 * Object under test.
	 */
	private DefaultFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new DefaultFilter();
	}

	private static Stream<Arguments> provideForTestApply() {
		return Stream.of(Arguments.of("", "", ""),
				Arguments.of("abc", "def", "abc"),
				Arguments.of("", "ghi", "ghi"),
				Arguments.of("   \t", "dog", "dog"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.DefaultFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@MethodSource("provideForTestApply")
	void testApply(String data, String defaultValue, String expected) {
		filter.setDefaultValue(defaultValue);
		assertEquals(expected, filter.apply(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.DefaultFilter#getDefaultValue()}.
	 */
	@Test
	void testGetDefaultValue() {
		assertEquals(null, filter.getDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.DefaultFilter#setDefaultValue(java.lang.String)}.
	 */
	@Test
	void testSetDefaultValue() {
		final String defaultValue = "this is a defaultValue";
		filter.setDefaultValue(defaultValue);
		assertEquals(defaultValue, filter.getDefaultValue());
	}

	@DisplayName("Integration tests")
	@Nested
	@Tag("Integration")
	class IntegrationTests {

		@Test
		void testFill() throws SelfFillingException {
			final String defaultValue = "Cost of magic";
			filter.fill(List.of(defaultValue));
			assertEquals(defaultValue, filter.getDefaultValue());
		}

		@Test
		void testFill_NullArgument() throws SelfFillingException {
			List<Object> data = new ArrayList<>();
			data.add(null);
			assertThrows(NullPointerException.class, () -> filter.fill(data));
		}

		@Test
		void testFill_MissingMandatoryArgument() throws SelfFillingException {
			assertThrows(SelfFillingException.class, () -> filter.fill(List.of()));
		}
	}
}
