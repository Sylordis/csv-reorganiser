package com.github.sylordis.csvreorganiser.model.engines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.chess.ChessEngine;
import com.github.sylordis.csvreorganiser.model.hyde.HydeEngine;

/**
 * @author sylordis
 *
 */
class EngineFactoryTest {

	private EngineFactory factory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		factory = new EngineFactory();
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.EngineFactory#getEngineFromId(java.lang.Object)}.
	 */
	@Test
	@Tag("Functional")
	void testGetEngineFromId_Chess() {
		ReorganiserEngine engine = factory.getEngineFromId(1);
		assertNotNull(engine);
		assertEquals(ChessEngine.class, engine.getClass());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.EngineFactory#getEngineFromId(java.lang.Object)}.
	 */
	@Test
	@Tag("Functional")
	void testGetEngineFromId_Hyde() {
		ReorganiserEngine engine = factory.getEngineFromId(2);
		assertNotNull(engine);
		assertEquals(HydeEngine.class, engine.getClass());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.engines.EngineFactory#getDefaultEngine()}.
	 */
	@Test
	void testGetDefaultEngine() {
		assertNotNull(EngineFactory.getDefaultEngine(), "Default engine should never be null");
	}

}
