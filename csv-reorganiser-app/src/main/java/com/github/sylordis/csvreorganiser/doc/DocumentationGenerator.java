package com.github.sylordis.csvreorganiser.doc;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import com.github.sylordis.csvreorganiser.model.chess.config.ChessDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.engines.ConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;
import com.github.sylordis.csvreorganiser.model.hyde.config.HydeDefaultConfigurationSupplier;
import com.github.sylordis.csvreorganiser.utils.ParsingUtils;

public class DocumentationGenerator<T> {

	/**
	 * Default output consumer.
	 */
	public static final Consumer<String> DEFAULT_OUTPUT_CONSUMER = System.out::println;

	/**
	 * Configuration supplier to get the operations.
	 */
	private ConfigurationSupplier<T> configSupplier;
	/**
	 * Output consumer.
	 */
	private Consumer<String> out = DEFAULT_OUTPUT_CONSUMER;
	/**
	 * Package where to find the operation classes to create the documentation of.
	 */
	private String classPackages;
	/**
	 * Java source root where to find the files to parse in the project.
	 */
	private SourceRoot sourceRoot;

	/**
	 * Generates the documentation.
	 */
	public void generate(EngineDocumentationHandler<T> handler,
			LanguageRenderer renderer) {
		Set<Class<? extends T>> dictionary = new TreeSet<>((c1, c2) -> c1.getSimpleName().compareTo(c2.getSimpleName()));
		dictionary.addAll(configSupplier.getConfigurationDictionary().values());
		for (var type : dictionary) {
			out.accept(renderer.render(handler.generateOperationDocumentation(type, getCompilationUnit(type))));
		}
	}

	/**
	 * Creates the compilation unit for a given class.
	 * 
	 * @param type
	 * @return a compilation unit
	 */
	public CompilationUnit getCompilationUnit(Class<?> type) {
		return sourceRoot.parse(classPackages, type.getSimpleName() + ".java");
	}

	/**
	 * Creates a documentation generator for Chess operations.
	 * 
	 * @return
	 */
	public static DocumentationGenerator<ChessAbstractReorgOperation> forChess() {
		return new DocumentationGenerator<ChessAbstractReorgOperation>().configSupplier(new ChessDefaultConfigurationSupplier())
				.sourceRoot(ParsingUtils.sourceFromRoot(ConfigConstants.SOURCE_ROOT))
				.classPackages(ConfigConstants.Chess.OPERATIONS_PACKAGE);
	}

	/**
	 * Creates a documentation generator for Hyde filters.
	 * 
	 * @return
	 */
	public static DocumentationGenerator<HydeAbstractFilter> forHyde() {
		return new DocumentationGenerator<HydeAbstractFilter>().configSupplier(new HydeDefaultConfigurationSupplier())
				.sourceRoot(ParsingUtils.sourceFromRoot(ConfigConstants.SOURCE_ROOT))
				.classPackages(ConfigConstants.Hyde.FILTERS_PACKAGE);
	}

	/**
	 * @return the configSupplier
	 */
	public ConfigurationSupplier<?> getConfigSupplier() {
		return configSupplier;
	}

	/**
	 * @param configSupplier the configSupplier to set
	 */
	public void setConfigSupplier(ConfigurationSupplier<T> configSupplier) {
		this.configSupplier = configSupplier;
	}

	/**
	 * @param configSupplier the configSupplier to set
	 * @return itself for chaining
	 */
	public DocumentationGenerator<T> configSupplier(ConfigurationSupplier<T> configSupplier) {
		setConfigSupplier(configSupplier);
		return this;
	}

	/**
	 * @return the out
	 */
	public Consumer<String> getOut() {
		return out;
	}

	/**
	 * @param out the out to set
	 */
	public void setOut(Consumer<String> out) {
		this.out = out;
	}

	/**
	 * @param out the out to set
	 * @return itself for chaining
	 */
	public DocumentationGenerator<T> out(Consumer<String> out) {
		setOut(out);
		return this;
	}

	/**
	 * @return the classPackages
	 */
	public String getClassPackages() {
		return classPackages;
	}

	/**
	 * @param classPackages the classPackages to set
	 */
	public void setClassPackages(String classPackages) {
		this.classPackages = classPackages;
	}

	/**
	 * @param classPackages the classPackages to set
	 * @return itself for chaining
	 */
	public DocumentationGenerator<T> classPackages(String classPackages) {
		setClassPackages(classPackages);
		return this;
	}

	/**
	 * @return the sourceRoot
	 */
	public SourceRoot getSourceRoot() {
		return sourceRoot;
	}

	/**
	 * @param sourceRoot the sourceRoot to set
	 */
	public void setSourceRoot(SourceRoot sourceRoot) {
		this.sourceRoot = sourceRoot;
	}

	/**
	 * @param sourceRoot the sourceRoot to set
	 * @return itself for chaining
	 */
	public DocumentationGenerator<T> sourceRoot(SourceRoot sourceRoot) {
		setSourceRoot(sourceRoot);
		return this;
	}

}
