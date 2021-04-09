package com.sylordis.tools.csv.reorganiser.model.config.dictionary;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sylordis.tools.csv.reorganiser.model.operations.AbstractReorgOperation;

/**
 * @author sylordis
 *
 */
class DefaultConfigurationSupplierTest {

	/**
	 * Default object under test.
	 */
	private DefaultConfigurationSupplier dcs;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		dcs = new DefaultConfigurationSupplier();
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.config.dictionary.DefaultConfigurationSupplier#DefaultConfigurationSupplier()}
	 * to check that the constructor does create the object properly.
	 */
	@Test
	void testConstructor() {
		assertNotNull(dcs);
	}

	/**
	 * Test method for
	 * {@link com.sylordis.tools.csv.reorganiser.model.config.dictionary.DefaultConfigurationSupplier#get()}.
	 */
	@Test
	void testGet() {
		Map<String, Class<? extends AbstractReorgOperation>> m = dcs.get();
		assertNotNull(m);
		assertTrue(m.size() > 0);
	}

}
