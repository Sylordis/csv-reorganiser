/**
 * 
 */
package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * 
 */
class SubstringFilterTest {

	/**
	 * Test instance.
	 */
	private SubstringFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new SubstringFilter();
	}

	/**
	 * Provide for {@link #testApply(String, int, Integer, String)}.
	 * 
	 * @return
	 */
	private static Stream<Arguments> provider_testApply() {
		return Stream.of(Arguments.of("", 0, 0, ""), Arguments.of("a", 0, 1, "a"), Arguments.of("abcd", 0, 2, "ab"),
		        Arguments.of("Smooth sailing", 3, 6, "oth"), Arguments.of("Barking cat", 3, null, "king cat"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@MethodSource(value = { "provider_testApply" })
	void testApply(String data, int start, Integer end, String expected) {
		filter.setStartIndex(start);
		if (end != null)
			filter.setEndIndex(end);
		assertEquals(expected, filter.apply(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(filter.toString());
		assertFalse(filter.toString().isBlank(), "toString() should always return something readable.");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#getStartIndex()}.
	 */
	@Test
	void testGetStartIndex() {
		assertEquals(0, filter.getStartIndex());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#setStartIndex(int)}.
	 */
	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 4, 1589, Integer.MAX_VALUE })
	void testSetStartIndex(int index) {
		filter.setStartIndex(index);
		assertEquals(index, filter.getStartIndex());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#getEndIndex()}.
	 */
	@Test
	void testGetEndIndex() {
		assertEquals(SubstringFilter.DEFAULT_END_INDEX, filter.getEndIndex());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#setEndIndex(int)}.
	 */
	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 3, 10, 1589, Integer.MAX_VALUE })
	void testSetEndIndex(int index) {
		filter.setStartIndex(index);
		assertEquals(index, filter.getStartIndex());
	}

	@DisplayName("Integration tests")
	@Nested
	@Tag("Integration")
	class IntegrationTests {

		@Test
		void testFill_OnlyOneArgument() throws SelfFillingException {
			final int start = 5;
			filter.fill(List.of(start));
			assertEquals(start, filter.getStartIndex());
			assertEquals(SubstringFilter.DEFAULT_END_INDEX, filter.getEndIndex());
		}

		@Test
		void testFill() throws SelfFillingException {
			final int start = 3;
			final int end = 5;
			filter.fill(List.of(start, end));
			assertEquals(start, filter.getStartIndex());
			assertEquals(end, filter.getEndIndex());
		}

		/**
		 * Provide for {@link #testApply(String, int, Integer, String)}.
		 * 
		 * @return
		 */
		private static Stream<Arguments> provider_testFill_ConversionError() {
			return Stream.of(Arguments.of(List.of("uhoh")), Arguments.of(List.of(1, "missed chance")));
		}

		/**
		 * Test method for
		 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.SubstringFilter#apply(java.lang.String)}.
		 */
		@ParameterizedTest
		@MethodSource(value = { "provider_testFill_ConversionError" })
		void testFill_ConversionError(List<Object> args) throws SelfFillingException {
			assertThrows(SelfFillingException.class, () -> filter.fill(args));
		}

		@Test
		void testFill_NullArgument() throws SelfFillingException {
			List<Object> data = new ArrayList<>();
			data.add(null);
			data.add(null);
			assertThrows(NullPointerException.class, () -> filter.fill(data));
		}

		@Test
		void testFill_MissingMandatoryArgument() throws SelfFillingException {
			assertThrows(SelfFillingException.class, () -> filter.fill(List.of()));
		}
	}
}
