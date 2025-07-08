package com.github.sylordis.csvreorganiser.doc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.chess.config.ChessDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.RegularExpressionReplacementOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation;
import com.github.sylordis.csvreorganiser.test.chess.defs.DummyOperation;
import com.github.sylordis.csvreorganiser.test.chess.defs.DummyOperationWithShortcut;
import com.github.sylordis.csvreorganiser.test.chess.defs.FakeOperation;
import com.github.sylordis.csvreorganiser.utils.ParsingUtils;

/**
 * @author sylordis
 *
 */
@Tag("Documentation")
class MarkdownDocumentationGeneratorChessTest {

	/**
	 * Class under test.
	 */
	private MarkdownDocumentationGeneratorChess mdoc;
	/**
	 * Holder for documentation output.
	 */
	private StringBuilder mdocOutput;
	/**
	 * String consumer to append output of the documentation into the string builder.
	 */
	private Consumer<String> mdocConsumer = s -> mdocOutput.append(s).append("\n");

	private Map<String, Class<? extends ChessAbstractReorgOperation>> CHESS_OPERATIONS = Map.of("concatenate",
	        ConcatenationOperation.class, "get", GetOperation.class, "regreplace",
	        RegularExpressionReplacementOperation.class);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		mdoc = spy(new MarkdownDocumentationGeneratorChess());
		mdocOutput = new StringBuilder();
		mdoc.setOutputConsumer(mdocConsumer);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#generate()}. Sorry
	 * for this lazy test but it's too big to match against. Since this method just calls
	 * {@link MarkdownDocumentationGeneratorChess#generateOperationDocumentation(Class)} for each chess
	 * operation, we just check that this method was called once per operation.
	 */
	@Test
	@Tag("Integration")
	void testGenerate() {
		// Mock configuration supplier, otherwise test operations classes will show up for some reason
		ChessDefaultConfigurationSupplier cfgSupplier = mock(ChessDefaultConfigurationSupplier.class);
		when(cfgSupplier.getConfigurationByReflection(anyString(), any())).thenReturn(
		        new HashSet<>(CHESS_OPERATIONS.values()));
		when(cfgSupplier.getConfigurationDictionary()).thenReturn(CHESS_OPERATIONS);
		// Test
		mdoc.generate(cfgSupplier);
		final String doc = mdocOutput.toString();
		assertNotNull(doc);
		assertFalse(doc.isBlank());
		verify(mdoc).generateOperationDocumentation(ConcatenationOperation.class);
		verify(mdoc).generateOperationDocumentation(GetOperation.class);
		verify(mdoc).generateOperationDocumentation(RegularExpressionReplacementOperation.class);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#generateOperationDocumentation(Class)}.
	 */
	@Test
	@Tag("Integration")
	void testGenerateOperationDocumentation() {
		mdoc = createForTest();
		mdoc.generateOperationDocumentation(DummyOperation.class);
		assertEquals(
		        """
		                # Dummy

		                **Configuration name:** `dummy`

		                *Dummy* operation for the generation.

		                ```yaml
		                column: <column-name>
		                operation:
		                  type: dummy
		                  field1: <field1>
		                  field2: <field2>
		                ```

		                | Property | type | required? | description |
		                | --- | --- | --- | --- |
		                | `field1` | String | y | This is field 1. |
		                | `field2` | int | n | This is field 2. |

		                """,
		        mdocOutput.toString());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#generateOperationDocumentation(Class)}.
	 */
	@Test
	@Tag("Integration")
	void testGenerateOperationDocumentation_WithShortcut() {
		mdoc = createForTest();
		mdoc.generateOperationDocumentation(DummyOperationWithShortcut.class);
		assertEquals(
		        """
		                # Dummy With Shortcut

		                **Configuration name:** `dummyshort`

		                Dummy *operation* with a shortcut for the generation.

		                ```yaml
		                column: <column-name>
		                operation:
		                  type: dummyshort
		                  important: <important>
		                ```

		                Shortcut:
		                ```yaml
		                column: <column-name>
		                dumdum: <important>
		                ```

		                | Property | type | required? | description |
		                | --- | --- | --- | --- |
		                | `important` | int | y | Priority number 1. |

		                """,
		        mdocOutput.toString());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#propToYaml(java.lang.Class, com.github.sylordis.csvreorganiser.model.annotations.OperationProperty)}.
	 */
	@Test
	@Tag("Integration")
	void testPropToYaml() {
		String yaml = mdoc.propToYaml(GetOperation.class,
		        GetOperation.class.getDeclaredAnnotation(OperationProperty.class));
		assertEquals("  source: <source>", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#propValueToYaml(java.lang.Class, com.github.sylordis.csvreorganiser.model.annotations.OperationProperty)}.
	 */
	@Test
	@Tag("Integration")
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationChessOperationProperty() {
		String yaml = mdoc.propValueToYaml(ValueOperation.class,
		        ValueOperation.class.getDeclaredAnnotation(OperationProperty.class));
		assertEquals(" <value>", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#propValueToYaml(java.lang.Class, java.lang.String, java.util.function.Supplier)}.
	 */
	@Test
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationStringSupplierOfString() {
		final String name = "wellow";
		String yaml = mdoc.propValueToYaml(FakeOperation.class, "theValue", () -> name);
		assertEquals(" <" + name + ">", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#propValueToYaml(java.lang.Class, java.lang.String, java.util.function.Supplier)}.
	 */
	@Test
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationStringSupplierOfString_list() {
		final String name = "myList";
		String yaml = mdoc.propValueToYaml(ConcatenationOperation.class, "values", () -> name);
		assertEquals("\n" + "    - <value>\n" + "    - <value>\n" + "    - ...", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationGeneratorChess#getFieldType(java.lang.Class, java.lang.String)}.
	 */
	@Test
	void testGetFieldType() {
		assertEquals(String.class, mdoc.getFieldType(FakeOperation.class, "theValue"));
	}

	/**
	 * Creates a documentation generator for this test (for internal classes)
	 * @return
	 */
	private MarkdownDocumentationGeneratorChess createForTest() {
		return new MarkdownDocumentationGeneratorChess(ParsingUtils.sourceFromRoot("src/test/java"), (r,t) -> r.parse("com.github.sylordis.csvreorganiser.test.chess.defs", t.getSimpleName() + ".java"), mdocConsumer);
	}
	
}
