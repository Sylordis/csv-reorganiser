package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
 * Test class for {@link ReplaceFilter}.
 */
class ReplaceFilterTest {

	/**
	 * Object under test.
	 */
	private ReplaceFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new ReplaceFilter();
	}

	private static Stream<Arguments> provideForTestApply() {
		return Stream.of(Arguments.of("", "", "", ""), Arguments.of("abc", "a", "c", "cbc"),
		        Arguments.of("abcdefg", "klm", "xyz", "abcdefg"), Arguments.of("abcdabcd", "d", "m", "abcmabcm"),
		        Arguments.of("The quick brown fox jumps over the lazy dog", "", "",
		                "The quick brown fox jumps over the lazy dog"),
		        Arguments.of("Sphinx of black quartz, judge my vow", ".a.", "bob",
		                "Sphinx of bbobk qbobtz, judge my vow"),
		        Arguments.of("192.168.0.1", "([0-9]{1,3}\\.[0-9]{1,3})\\..*", "$1", "192.168"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.ReplaceFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@MethodSource("provideForTestApply")
	void testApply(String data, String pattern, String replacement, String expected) {
		filter.setPattern(pattern);
		filter.setReplacement(replacement);
		assertEquals(expected, filter.apply(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.ReplaceFilter#getPattern()}.
	 */
	@Test
	void testGetPattern() {
		assertNull(filter.getPattern());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.ReplaceFilter#setPattern(java.lang.String)}.
	 */
	@Test
	void testSetPattern() {
		final String pattern = "this is a pattern";
		filter.setPattern(pattern);
		assertEquals(pattern, filter.getPattern());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.ReplaceFilter#getReplacement()}.
	 */
	@Test
	void testGetReplacement() {
		assertEquals("", filter.getReplacement());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.ReplaceFilter#setReplacement(java.lang.String)}.
	 */
	@Test
	void testSetReplacement() {
		final String replacement = "this is a replacement";
		filter.setReplacement(replacement);
		assertEquals(replacement, filter.getReplacement());
	}

	@DisplayName("Integration tests")
	@Nested
	@Tag("Integration")
	class IntegrationTests {

		@Test
		void testFill_OnlyOneArgument() throws SelfFillingException {
			final String pattern = "Little red riding hood";
			filter.fill(List.of(pattern));
			assertEquals(pattern, filter.getPattern());
			assertEquals("", filter.getReplacement());
		}

		@Test
		void testFill() throws SelfFillingException {
			final String pattern = "Cost of magic";
			final String replacement = "mana";
			filter.fill(List.of(pattern, replacement));
			assertEquals(pattern, filter.getPattern());
			assertEquals(replacement, filter.getReplacement());
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
