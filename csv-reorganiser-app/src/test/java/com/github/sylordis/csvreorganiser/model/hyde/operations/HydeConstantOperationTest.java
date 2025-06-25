package com.github.sylordis.csvreorganiser.model.hyde.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.exceptions.OperationBuildingException;
import com.github.sylordis.csvreorganiser.model.hyde.HydeFilter;

/**
 * @author sylordis
 *
 */
class HydeConstantOperationTest {

	/**
	 * Default value of the test instance.
	 */
	private final String VALUE_DEFAULT = "Nasty cocktail";
	
	/**
	 * Basic test instance.
	 */
	private HydeConstantOperation op;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		op = new HydeConstantOperation(VALUE_DEFAULT);
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#HydeConstantOperation(java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeConstantOperationString() {
		assertNotNull(op);
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#HydeConstantOperation(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("Constructor")
	void testHydeConstantOperationStringString() {
		final String name = "MyConstant";
		final String value = "Sun";
		op = new HydeConstantOperation(name, value);
		assertNotNull(op);
		assertEquals(name, op.getName());
		assertEquals(value, op.getSource());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#apply(org.apache.commons.csv.CSVRecord)}.
	 */
	@Test
	void testApply() {
		assertEquals(VALUE_DEFAULT, op.apply(mock(CSVRecord.class)));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#addChild(com.github.sylordis.csvreorganiser.model.hyde.operations.HydeOperation)}.
	 */
	@Test
	void testAddChild() {
		assertThrows(OperationBuildingException.class, () -> op.addChild(new HydeConstantOperation(VALUE_DEFAULT)));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#setChildren(java.util.List)}.
	 */
	@Test
	void testSetChildren() {
		assertThrows(OperationBuildingException.class, () -> op.setChildren(List.of()));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#getChildren()}.
	 */
	@Test
	void testGetChildren() {
		List<HydeOperation> children = op.getChildren();
		assertNotNull(children);
		assertTrue(children.isEmpty());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#getFilters()}.
	 */
	@Test
	void testGetFilters() {
		List<HydeFilter> filters = op.getFilters();
		assertNotNull(filters);
		assertTrue(filters.isEmpty());
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#addFilter(com.github.sylordis.csvreorganiser.model.hyde.HydeFilter)}.
	 */
	@Test
	void testAddFilter() {
		assertThrows(OperationBuildingException.class, () -> op.addFilter(t -> t));
	}

	/**
	 * Test method for {@link com.github.sylordis.csvreorganiser.model.hyde.operations.HydeConstantOperation#setFilters(java.util.List)}.
	 */
	@Test
	void testSetFilters() {
		assertThrows(OperationBuildingException.class, () -> op.setFilters(List.of()));
	}

}
