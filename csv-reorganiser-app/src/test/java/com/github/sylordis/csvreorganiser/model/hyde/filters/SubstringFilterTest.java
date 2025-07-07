/**
 * 
 */
package com.github.sylordis.csvreorganiser.model.hyde.filters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 
 */
class SubstringFilterTest {

	/**
	 * Test instance.
	 */
	private SubstringFilter op;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new SubstringFilter();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#apply(java.lang.String)}.
	 */
	@Test
	void testApply() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(op.toString());
		assertFalse(op.toString().isBlank(), "toString() should always return something readable.");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#getStartIndex()}.
	 */
	@Test
	void testGetStartIndex() {
		assertEquals(0, op.getStartIndex());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#setStartIndex(int)}.
	 */
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 4, 1589, Integer.MAX_VALUE})
	void testSetStartIndex(int index) {
		op.setStartIndex(index);
		assertEquals(index, op.getStartIndex());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#getEndIndex()}.
	 */
	@Test
	void testGetEndIndex() {
		assertNull(op.getEndIndex());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.filters.HydeFilterSubstring#setEndIndex(int)}.
	 */
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 3, 10, 1589, Integer.MAX_VALUE})
	void testSetEndIndex(int index) {
		op.setStartIndex(index);
		assertEquals(index, op.getStartIndex());
	}

}
