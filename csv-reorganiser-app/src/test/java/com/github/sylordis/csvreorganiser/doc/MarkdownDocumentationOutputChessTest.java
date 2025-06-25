package com.github.sylordis.csvreorganiser.doc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperationProperty;
import com.github.sylordis.csvreorganiser.model.chess.config.dictionary.ChessConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.config.dictionary.ChessDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ConcatenationOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.GetOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.RegularExpressionReplacementOperation;
import com.github.sylordis.csvreorganiser.model.chess.operations.defs.ValueOperation;
import com.github.sylordis.csvreorganiser.test.chess.defs.FakeOperation;

/**
 * @author sylordis
 *
 */
@Tag("Documentation")
class MarkdownDocumentationOutputChessTest {

	/**
	 * Class under test.
	 */
	private MarkdownDocumentationOutputChess mdoc;
	/**
	 * Holder for documentation output.
	 */
	private StringBuilder mdocOutput;
	/**
	 * String consumer to append output of the documentation into the string builder.
	 */
	private Consumer<String> mdocConsumer = s -> mdocOutput.append(s).append("\n");

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		mdoc = spy(new MarkdownDocumentationOutputChess());
		mdocOutput = new StringBuilder();
		mdoc.setOutputConsumer(mdocConsumer);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#generate()}.
	 * Sorry for this lazy test but it's too big to match against. Since this method just calls
	 * {@link MarkdownDocumentationOutputChess#generateOperationDocumentation(Class)} for each chess
	 * operation, we just check that this method was called once per operation.
	 */
	@Test
	@Tag("Integration")
	void testGenerate() {
		// Mock configuration supplier, otherwise test operations classes will show up for some reason
		ChessConfigurationSupplier cfgSupplier = mock(ChessDefaultConfigurationSupplier.class);
		when(((ChessDefaultConfigurationSupplier) cfgSupplier).getOperationsByReflection()).thenReturn(
		        Set.of(ConcatenationOperation.class, GetOperation.class, RegularExpressionReplacementOperation.class));
		when(cfgSupplier.getOperationsDictionary()).thenCallRealMethod();
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
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#generateOperationDocumentation(Class)}.
	 */
	@Test
	@Tag("Integration")
	void testGenerateOperationDocumentation() {
		mdoc.generateOperationDocumentation(GetOperation.class);
		// \s is to conserve the whitespaces.
		assertEquals(
		        """
		                # Get

		                **Configuration name:** `get`

		                *Get* operation simply takes the full content of a column. If the source column does not exist, an error will be raised.
		                \s
		                ```yaml
		                column: <column-name>
		                operation:
		                  type: get
		                  source: <source>
		                ```

		                Shortcut:
		                ```yaml
		                column: <column-name>
		                source: <source>
		                ```

		                | Property | type | required? | description |
		                | --- | --- | --- | --- |
		                | `source` | String | y | Name of the column to get the value from |

		                """,
		        mdocOutput.toString());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#propToYaml(java.lang.Class, com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperationProperty)}.
	 */
	@Test
	@Tag("Integration")
	void testPropToYaml() {
		String yaml = mdoc.propToYaml(GetOperation.class,
		        GetOperation.class.getDeclaredAnnotation(ChessOperationProperty.class));
		assertEquals("  source: <source>", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#propValueToYaml(java.lang.Class, com.github.sylordis.csvreorganiser.model.chess.annotations.ChessOperationProperty)}.
	 */
	@Test
	@Tag("Integration")
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationChessOperationProperty() {
		String yaml = mdoc.propValueToYaml(ValueOperation.class,
		        ValueOperation.class.getDeclaredAnnotation(ChessOperationProperty.class));
		assertEquals(" <value>", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#propValueToYaml(java.lang.Class, java.lang.String, java.util.function.Supplier)}.
	 */
	@Test
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationStringSupplierOfString() {
		final String name = "wellow";
		String yaml = mdoc.propValueToYaml(FakeOperation.class, "theValue", () -> name);
		assertEquals(" <" + name + ">", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#propValueToYaml(java.lang.Class, java.lang.String, java.util.function.Supplier)}.
	 */
	@Test
	void testPropValueToYamlClassOfQextendsChessAbstractReorgOperationStringSupplierOfString_list() {
		final String name = "myList";
		String yaml = mdoc.propValueToYaml(ConcatenationOperation.class, "values", () -> name);
		assertEquals("\n" + "    - <value>\n" + "    - <value>\n" + "    - ...", yaml);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.doc.MarkdownDocumentationOutputChess#getFieldType(java.lang.Class, java.lang.String)}.
	 */
	@Test
	void testGetFieldType() {
		assertEquals(String.class, mdoc.getFieldType(FakeOperation.class, "theValue"));
	}

}
