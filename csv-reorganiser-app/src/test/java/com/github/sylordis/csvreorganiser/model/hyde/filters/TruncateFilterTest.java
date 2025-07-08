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

import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * Test class for {@link TruncateFilter}.
 */
class TruncateFilterTest {

	/**
	 * Object under test.
	 */
	private TruncateFilter filter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		filter = new TruncateFilter();
	}

	private static Stream<Arguments> provideForTestApply() {
		return Stream.of(Arguments.of("", 5, ""), Arguments.of("abc", 8, "abc"),
		        Arguments.of("abc", 1, "a" + ConfigConstants.DEFAULT_ELLIPSIS),
		        Arguments.of("The quick brown fox", 8, "The quic..."));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.TruncateFilter#apply(java.lang.String)}.
	 */
	@ParameterizedTest
	@MethodSource("provideForTestApply")
	void testApply(String data, int length, String expected) {
		filter.setLength(length);
		assertEquals(expected, filter.apply(data));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.TruncateFilter#getLength()}.
	 */
	@Test
	void testGetLength() {
		assertEquals(0, filter.getLength());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.model.hyde.filters.TruncateFilter#setLength(java.lang.String)}.
	 */
	@Test
	void testSetLength() {
		final int length = 2393;
		filter.setLength(length);
		assertEquals(length, filter.getLength());
	}

	@DisplayName("Integration tests")
	@Nested
	@Tag("Integration")
	class IntegrationTests {

		@Test
		void testFill() throws SelfFillingException {
			final int length = 923;
			filter.fill(List.of(length));
			assertEquals(length, filter.getLength());
		}

		@Test
		void testFill_NullArgument() throws SelfFillingException {
			List<Object> data = new ArrayList<>();
			data.add(null);
			assertThrows(NullPointerException.class, () -> filter.fill(data));
		}

		@Test
		void testFill_ConversionError() throws SelfFillingException {
			final String param = "uhoh";
			assertThrows(SelfFillingException.class, () -> filter.fill(List.of(param)));
		}

		@Test
		void testFill_MissingMandatoryArgument() throws SelfFillingException {
			assertThrows(SelfFillingException.class, () -> filter.fill(List.of()));
		}
	}
}
