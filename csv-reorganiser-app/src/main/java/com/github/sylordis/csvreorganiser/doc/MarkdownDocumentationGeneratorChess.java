package com.github.sylordis.csvreorganiser.doc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.utils.SourceRoot;
import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.config.ChessConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.utils.MarkupLanguageUtils;
import com.github.sylordis.csvreorganiser.utils.ParsingUtils;

/**
 * This class scans the project and outputs a documentation for each operation from the default
 * configuration.
 * 
 * @author sylordis
 */
public class MarkdownDocumentationGeneratorChess {

	/**
	 * Default compilation unit provider (
	 */
	public static final BiFunction<SourceRoot, Class<? extends ChessAbstractReorgOperation>, CompilationUnit> DEFAULT_COMPILATION_UNIT_PROVIDER = (
	        r, t) -> r.parse(ConfigConstants.Chess.OPERATIONS_PACKAGE, t.getSimpleName() + ".java");
	/**
	 * Default output consumer.
	 */
	public static final Consumer<String> DEFAULT_OUTPUT_CONSUMER = System.out::println;

	/**
	 * Default statement for empty documentation.
	 */
	private static final String DOCUMENTATION_NOT_PROVIDED = "Documentation not provided";

	/**
	 * Output consumer.
	 */
	private Consumer<String> out;
	/**
	 * Source root for java code parser.
	 */
	private SourceRoot sourceRoot;
	/**
	 * Compilation unit provider.
	 */
	private BiFunction<SourceRoot, Class<? extends ChessAbstractReorgOperation>, CompilationUnit> compilationUnitProvider;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Default Markdown documentation output producer.
	 */
	public MarkdownDocumentationGeneratorChess() {
		this(ParsingUtils.sourceFromRoot("src/main/java/"), DEFAULT_COMPILATION_UNIT_PROVIDER, DEFAULT_OUTPUT_CONSUMER);
	}

	/**
	 * Creates a custom markdown documentation output producer.
	 * 
	 * @param sourceRoot              Source root for java code parser
	 * @param compilationUnitProvider Compilation unit provider from
	 */
	public MarkdownDocumentationGeneratorChess(SourceRoot sourceRoot,
	        BiFunction<SourceRoot, Class<? extends ChessAbstractReorgOperation>, CompilationUnit> compilationUnitProvider,
	        Consumer<String> outputConsumer) {
		this.sourceRoot = sourceRoot;
		this.compilationUnitProvider = compilationUnitProvider;
		this.out = outputConsumer;
	}

	/**
	 * Generates the documentation for the Chess engine.
	 */
	public void generate(ChessConfigurationSupplier cfgSupplier) {
		logger.debug("Generating chess documentation (source root: {})", sourceRoot);
		// Have a set ordering classes by simple alphabetical class name ordering
		Set<Class<? extends ChessAbstractReorgOperation>> dictionary = new TreeSet<>(
		        (c1, c2) -> c1.getSimpleName().compareTo(c2.getSimpleName()));
		// Get all operations classes
		dictionary.addAll(cfgSupplier.getConfigurationDictionary().values());
		// For each operation type
		for (var type : dictionary) {
			generateOperationDocumentation(type);
		}
	}

	/**
	 * Generates documentation for one operation type.
	 * 
	 * @param type
	 */
	public void generateOperationDocumentation(Class<? extends ChessAbstractReorgOperation> type) {
		logger.debug("Generating operation documentation for {}", type);
		// Get the actual class
		CompilationUnit compilationUnit = compilationUnitProvider.apply(sourceRoot, type);
		ClassOrInterfaceDeclaration decl = compilationUnit.getClassByName(type.getSimpleName()).get();
		// Title
		out.accept("# " + MarkupLanguageUtils.splitCamelCase(type.getSimpleName().replace("Operation", "")) + "\n");
		// Config name
		String opTag = type.getAnnotation(Operation.class).name();
		out.accept("**Configuration name:** `" + opTag + "`" + "\n");
		// Operation description extracted from Javadoc
		JavadocComment opComment = decl.getJavadocComment().orElse(new JavadocComment(DOCUMENTATION_NOT_PROVIDED));
		String opCommentText = sanitiseJavadoc(opComment.getContent()) + "\n";
		out.accept(MarkupLanguageUtils.htmlToMarkdown(opCommentText));
		// Yaml definition example
		OperationProperty[] properties = type.getAnnotationsByType(OperationProperty.class);
		out.accept("```yaml");
		out.accept(String.format("""
		        column: <column-name>
		        operation:
		          type: %s""", opTag));
		for (OperationProperty prop : properties) {
			out.accept(propToYaml(type, prop));
		}
		out.accept("```\n");
		// Shortcut Yaml definition example
		if (type.isAnnotationPresent(OperationShortcut.class)) {
			out.accept("Shortcut:");
			out.accept("```yaml");
			OperationShortcut shortcutAnnotation = type.getAnnotation(OperationShortcut.class);
			String shortcut = shortcutAnnotation.keyword();
			String propertyFromShortcut = Arrays.stream(type.getAnnotationsByType(OperationProperty.class))
			        .filter(a -> a.name().equals(shortcutAnnotation.property())).findFirst().get().name();
			out.accept(String.format("""
			        column: <column-name>
			        %s:%s""", shortcut,
			        propValueToYaml(type, shortcutAnnotation.property(), () -> propertyFromShortcut)));
			out.accept("```\n");
		}
		// Properties description
		out.accept("| Property | type | required? | description |");
		out.accept("| --- | --- | --- | --- |");
		// Create properties lines for each property
		for (OperationProperty prop : properties) {
			StringBuilder rame = new StringBuilder();
			rame.append("| `").append(prop.name()).append("`");
			rame.append(" | ").append(getFieldType(type, prop.field()).getSimpleName());
			rame.append(" | ").append(prop.required() ? "y" : "n");
			String documentation = prop.description();
			if (documentation.isBlank())
				documentation = sanitiseJavadoc(decl.getFieldByName(prop.field()).get().getJavadocComment()
				        .orElse(new JavadocComment(DOCUMENTATION_NOT_PROVIDED)).getContent());
			rame.append(" | ").append(documentation);
			rame.append(" |");
			out.accept(rame.toString());
		}
		out.accept("");
	}

	/**
	 * Sanitises a Javadoc to be processed properly as String.
	 * @param doc
	 * @return
	 */
	protected String sanitiseJavadoc(String doc) {
		return doc.replaceAll("\n[ \t]+\\* ?", "\n").replaceAll("(?m)^([ \t]*|@.*)\r?\n", "").trim();
	}

	/**
	 * Converts a parameter into Yaml format.
	 * 
	 * @param type
	 * @param prop
	 * @return
	 */
	public String propToYaml(Class<? extends ChessAbstractReorgOperation> type, OperationProperty prop) {
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
	public String propValueToYaml(Class<? extends ChessAbstractReorgOperation> type, OperationProperty prop) {
		return propValueToYaml(type, prop.field(), () -> prop.name());
	}

	/**
	 * Converts a parameter into Yaml format.
	 * 
	 * @param type  Type holding the property
	 * @param field Name of the class' field
	 * @param name  Supplier for the name of the parameter
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