package com.github.sylordis.tools.csvreorganiser.doc;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.github.sylordis.tools.csvreorganiser.model.chess.annotations.ChessOperation;
import com.github.sylordis.tools.csvreorganiser.model.chess.annotations.ChessOperationProperty;
import com.github.sylordis.tools.csvreorganiser.model.chess.annotations.ChessOperationShortcut;
import com.github.sylordis.tools.csvreorganiser.model.chess.config.dictionary.ChessConfigurationSupplier;
import com.github.sylordis.tools.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.tools.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.tools.csvreorganiser.utils.MarkupLanguageUtils;

/**
 * This class scans the project and outputs a documentation for each operation from the default
 * configuration.
 * 
 * @author sylordis
 *
 * 
 */
public class MarkdownDocumentationOutputChess {

	/**
	 * Output consumer.
	 */
	private Consumer<String> out = System.out::println;
	/**
	 * Source root for java code parser.
	 */
	private final SourceRoot sourceRoot = new SourceRoot(
	        CodeGenerationUtils.mavenModuleRoot(this.getClass()).resolve("src/main/java"));

	/**
	 * Generates the documentation for the Chess engine.
	 */
	public void generate(ChessConfigurationSupplier cfgSupplier) {
		// Have a set ordering classes by simple alphabetical class name ordering
		Set<Class<? extends ChessAbstractReorgOperation>> dictionary = new TreeSet<>(
		        (c1, c2) -> c1.getSimpleName().compareTo(c2.getSimpleName()));
		// Get all operations classes
		dictionary.addAll(cfgSupplier.getOperationsDictionary().values());
		// For each operation type
		for (var type : dictionary) {
			generateOperationDocumentation(type);
		}
	}

	/**
	 * Generates documentation for one operation type.
	 * 
	 * @param sourceRoot
	 * @param type
	 */
	public void generateOperationDocumentation(Class<? extends ChessAbstractReorgOperation> type) {
		// Get the actual class
		CompilationUnit compilationUnit = sourceRoot.parse(ConfigConstants.Chess.OPERATIONS_PACKAGE,
		        type.getSimpleName() + ".java");
		ClassOrInterfaceDeclaration decl = compilationUnit.getClassByName(type.getSimpleName()).get();
		// Title
		out.accept("# " + MarkupLanguageUtils.splitCamelCase(type.getSimpleName().replace("Operation", "")) + "\n");
		// Config name
		String opTag = type.getAnnotation(ChessOperation.class).name();
		out.accept("**Configuration name:** `" + opTag + "`" + "\n");
		// Operation description extracted from Javadoc
		JavadocComment opComment = decl.getJavadocComment().orElse(new JavadocComment());
		String opCommentText = opComment.getContent().replaceAll("\n[ \t]+\\* ?", "\n")
		        .replaceAll("(?m)^([ \t]*|@.*)\r?\n", "");
		out.accept(MarkupLanguageUtils.htmlToMarkdown(opCommentText));
		// Yaml definition example
		ChessOperationProperty[] properties = type.getAnnotationsByType(ChessOperationProperty.class);
		out.accept("```yaml");
		out.accept(String.format("""
		        column: <column-name>
		        operation:
		          type: %s""", opTag));
		for (ChessOperationProperty prop : properties) {
			out.accept(propToYaml(type, prop));
		}
		out.accept("```\n");
		// Shortcut Yaml definition example
		if (type.isAnnotationPresent(ChessOperationShortcut.class)) {
			out.accept("Shortcut:");
			out.accept("```yaml");
			ChessOperationShortcut shortcutAnnotation = type.getAnnotation(ChessOperationShortcut.class);
			String shortcut = shortcutAnnotation.keyword();
			out.accept(String.format("""
			        column: <column-name>
			        %s:%s""", shortcut, propValueToYaml(type, shortcutAnnotation.property(), () -> shortcut)));
			out.accept("```\n");
		}
		// Properties description
		out.accept("| Property | type | required? | description |");
		out.accept("| --- | --- | --- | --- |");
		// Create properties lines for each property
		for (ChessOperationProperty prop : properties) {
			StringBuilder rame = new StringBuilder();
			rame.append("| `").append(prop.name()).append("`");
			rame.append(" | ").append(getFieldType(type, prop.field()).getSimpleName());
			rame.append(" | ").append(prop.required() ? "y" : "n");
			rame.append(" | ").append(prop.description());
			rame.append(" |");
			out.accept(rame.toString());
		}
		out.accept("");
	}

	/**
	 * Converts a parameter into Yaml format.
	 * 
	 * @param type
	 * @param prop
	 * @return
	 */
	public String propToYaml(Class<? extends ChessAbstractReorgOperation> type, ChessOperationProperty prop) {
		StringBuilder rame = new StringBuilder();
		rame.append("  ").append(prop.name()).append(":");
		rame.append(propValueToYaml(type, prop));
		return rame.toString();
	}

	/**
	 * Converts a parameter into Yaml format, giving the property name as name.
	 * 
	 * @param type Type holding the property
	 * @param prop Property to transcribe to YAML
	 * @return
	 */
	public String propValueToYaml(Class<? extends ChessAbstractReorgOperation> type, ChessOperationProperty prop) {
		return propValueToYaml(type, prop.field(), () -> prop.name());
	}

	/**
	 * Converts a parameter into Yaml format.
	 * 
	 * @param type Type holding the property
	 * @param field Name of the class' field
	 * @param name Supplier for the name of the parameter
	 * @return
	 */
	public String propValueToYaml(Class<? extends ChessAbstractReorgOperation> type, String field,
	        Supplier<String> name) {
		StringBuilder rame = new StringBuilder();
		Class<?> propType = getFieldType(type, field);
		if (java.util.List.class.equals(propType)) {
			rame.append("\n").append("    - <value>");
			rame.append("\n").append("    - <value>");
			rame.append("\n").append("    - ...");
		} else {
			rame.append(" <").append(name.get()).append(">");
		}
		return rame.toString();
	}

	/**
	 * Gets the type of a given field declared in a property of an operation.
	 * 
	 * @param type The operation under analysis
	 * @param prop The property to check for
	 * @return
	 */
	public Class<?> getFieldType(Class<? extends ChessAbstractReorgOperation> type, String fieldName) {
		Class<?> fieldType = null;
		try {
			Field field = type.getDeclaredField(fieldName);
			fieldType = field.getType();
		} catch (NoSuchFieldException | SecurityException e) {
			// Do nothing, null will be returned
		}
		return fieldType;
	}

	/**
	 * Replaces the output consumer.
	 * 
	 * @param out
	 */
	public void setOutputConsumer(Consumer<String> out) {
		this.out = out;
	}

}