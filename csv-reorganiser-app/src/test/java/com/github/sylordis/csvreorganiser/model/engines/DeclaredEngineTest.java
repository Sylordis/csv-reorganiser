package com.github.sylordis.csvreorganiser.model.engines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author sylordi
 *
 */
class DeclaredEngineTest {

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getEngineType()}.
	 */
	@ParameterizedTest
	@EnumSource(DeclaredEngine.class)
	void testGetEngineType(DeclaredEngine type) {
		assertNotNull(type.getEngineType());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getIds()}.
	 */
	@ParameterizedTest
	@EnumSource(DeclaredEngine.class)
	void testGetName(DeclaredEngine type) {
		assertNotNull(type.getName(), "A declared engine should have a name");
		assertFalse(type.getName().isEmpty(), "A declared engine's name should not be empty");
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getEngineTypeFromId(java.lang.Object)}.
	 */
	@Test
	void testGetEngineTypeFromId_IntID() {
		assertEquals(DeclaredEngine.CHESS, DeclaredEngine.getEngineTypeFromId(1));
		assertEquals(DeclaredEngine.CHESS, DeclaredEngine.getEngineTypeFromId("1"));
		assertEquals(DeclaredEngine.HYDE, DeclaredEngine.getEngineTypeFromId(2));
		assertEquals(DeclaredEngine.HYDE, DeclaredEngine.getEngineTypeFromId("2"));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getEngineTypeFromId(java.lang.Object)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = {"chess", "Chess", "CHESS", "cHeSs", "   	chess"})
	void testGetEngineTypeFromId_Chess(String input) {
		assertEquals(DeclaredEngine.CHESS, DeclaredEngine.getEngineTypeFromId(input));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getEngineTypeFromId(java.lang.Object)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = {"hyde", "Hyde", "HYDE", "hYdE", "   	hyde"})
	void testGetEngineTypeFromId_Hyde(String input) {
		assertEquals(DeclaredEngine.HYDE, DeclaredEngine.getEngineTypeFromId(input));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.DeclaredEngine#getEngineTypeFromId(java.lang.Object)}.
	 */
	@Test
	void testGetEngineTypeFromId_Unknown() {
		assertNull(DeclaredEngine.getEngineTypeFromId(Integer.MAX_VALUE));
		assertNull(DeclaredEngine.getEngineTypeFromId(-3));
		assertNull(DeclaredEngine.getEngineTypeFromId("yodelehihi"));
	}

}
