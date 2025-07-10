package com.github.sylordis.csvreorganiser.doc.handlers;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.sylordis.csvreorganiser.doc.EngineDocumentationHandler;
import com.github.sylordis.csvreorganiser.doc.elements.BlockType;
import com.github.sylordis.csvreorganiser.doc.elements.Element;
import com.github.sylordis.csvreorganiser.doc.elements.Section;
import com.github.sylordis.csvreorganiser.doc.elements.Table;
import com.github.sylordis.csvreorganiser.doc.elements.Text;
import com.github.sylordis.csvreorganiser.doc.elements.TextSequence;
import com.github.sylordis.csvreorganiser.doc.elements.Heading;
import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.annotations.OperationShortcut;
import com.github.sylordis.csvreorganiser.model.chess.operations.ChessAbstractReorgOperation;
import com.github.sylordis.csvreorganiser.model.constants.MessagesConstants;
import com.github.sylordis.csvreorganiser.utils.ClassUtils;
import com.github.sylordis.csvreorganiser.utils.MarkupLanguageUtils;
import com.github.sylordis.csvreorganiser.utils.ParsingUtils;

public class ChessDocumentationHandler implements EngineDocumentationHandler<ChessAbstractReorgOperation> {

	@Override
	public Element generateOperationDocumentation(Class<? extends ChessAbstractReorgOperation> type,
			CompilationUnit compilationUnit) {
		ClassOrInterfaceDeclaration decl = compilationUnit.getClassByName(type.getSimpleName()).get();
		Section mainSection = new Section();
		Heading title = new Heading(MarkupLanguageUtils.splitCamelCase(type.getSimpleName().replace("Filter", "")));

		String operationName = type.getAnnotation(Operation.class).name();
		Text configLabel = new Text("Configuration name:").bold();
		Text configText = new Text(operationName).code();
		TextSequence paragraphConfig = new TextSequence();
		paragraphConfig.addChildren(configText, configLabel);

		JavadocComment opComment = decl.getJavadocComment()
				.orElse(new JavadocComment(MessagesConstants.DOCUMENTATION_NOT_PROVIDED));
		String opCommentText = ParsingUtils.sanitiseJavadoc(opComment.getContent());
		Text description = new Text(opCommentText);
		TextSequence paragraphDescription = new TextSequence();
		paragraphDescription.addChild(description);

		// TODO YAML schema
		TextSequence schema = (TextSequence) new TextSequence(BlockType.CODE, "yaml").with(
				TextSequence.line("column:", "<column-name>"),
				TextSequence.line("operation:"),
				TextSequence.line("type:", operationName)
		);

		mainSection.addChildren(title, paragraphConfig, paragraphDescription, schema);

		// Shortcut
		OperationShortcut shortcut = type.getAnnotation(OperationShortcut.class);
//		(TextSequence) new TextSequence(BlockType.CODE, "yaml");
		if (shortcut != null) {
			TextSequence shortcutSchema = (TextSequence) new TextSequence(BlockType.CODE, "yaml").with(
					TextSequence.line("column:","<column-name>"),
					TextSequence.line(shortcut.keyword())
					// TODO values
			);
			mainSection.addChildren(TextSequence.line("Shortcut"), shortcutSchema);
		}

		Text argumentsLabel = new Text("Arguments:").bold();
		TextSequence paragraphArguments = new TextSequence();
		paragraphArguments.addChild(argumentsLabel);

		mainSection.addChildren(paragraphArguments);

		OperationProperty[] properties = type.getAnnotationsByType(OperationProperty.class);
		if (properties.length == 0) {
			Text argumentsNoContent = new Text("this filter does not accept any arguments.");
			paragraphArguments.addChild(argumentsNoContent);
		} else {
			Table tableArguments = new Table().withHeaders(List.of("Argument", "type", "required?", "description"));
			for (OperationProperty property : properties) {
				Text argName = new Text(property.name()).code();
				Text argType = new Text(ClassUtils.getFieldType(type, property.field()).getSimpleName());
				Text argRequired = new Text(property.required() ? "y" : "n");
				String documentation = property.description();
				if (documentation.isBlank())
					documentation = ParsingUtils.sanitiseJavadoc(decl.getFieldByName(property.field()).get()
							.getJavadocComment()
							.orElse(new JavadocComment(MessagesConstants.DOCUMENTATION_NOT_PROVIDED)).getContent());
				Text argDescription = new Text(documentation);
				TextSequence line = new TextSequence();
				line.addChildren(argName, argType, argRequired, argDescription);
				tableArguments.addChild(line);
			}
			mainSection.addChild(tableArguments);
		}

		return mainSection;
	}

}
