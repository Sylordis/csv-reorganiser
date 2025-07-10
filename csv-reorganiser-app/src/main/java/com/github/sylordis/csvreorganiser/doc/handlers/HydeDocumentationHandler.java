package com.github.sylordis.csvreorganiser.doc.handlers;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.sylordis.csvreorganiser.doc.EngineDocumentationHandler;
import com.github.sylordis.csvreorganiser.doc.elements.Element;
import com.github.sylordis.csvreorganiser.doc.elements.TextSequence;
import com.github.sylordis.csvreorganiser.doc.elements.Section;
import com.github.sylordis.csvreorganiser.doc.elements.Table;
import com.github.sylordis.csvreorganiser.doc.elements.Text;
import com.github.sylordis.csvreorganiser.doc.elements.Heading;
import com.github.sylordis.csvreorganiser.model.annotations.Operation;
import com.github.sylordis.csvreorganiser.model.annotations.OperationProperty;
import com.github.sylordis.csvreorganiser.model.constants.MessagesConstants;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;
import com.github.sylordis.csvreorganiser.utils.ClassUtils;
import com.github.sylordis.csvreorganiser.utils.MarkupLanguageUtils;
import com.github.sylordis.csvreorganiser.utils.ParsingUtils;

public class HydeDocumentationHandler implements EngineDocumentationHandler<HydeAbstractFilter> {

	@Override
	public Element generateOperationDocumentation(Class<? extends HydeAbstractFilter> type,
			CompilationUnit compilationUnit) {
		ClassOrInterfaceDeclaration decl = compilationUnit.getClassByName(type.getSimpleName()).get();
		Section mainSection = new Section();
		Heading title = new Heading(MarkupLanguageUtils.splitCamelCase(type.getSimpleName().replace("Filter", "")));

		String operationTag = type.getAnnotation(Operation.class).name();
		Text configLabel = new Text("Configuration name:").bold();
		Text configText = new Text(operationTag).code();
		TextSequence paragraphConfig = new TextSequence();
		paragraphConfig.addChildren(configText, configLabel);

		JavadocComment opComment = decl.getJavadocComment()
				.orElse(new JavadocComment(MessagesConstants.DOCUMENTATION_NOT_PROVIDED));
		String opCommentText = ParsingUtils.sanitiseJavadoc(opComment.getContent());
		Text description = new Text(opCommentText);
		TextSequence paragraphDescription = new TextSequence();
		paragraphDescription.addChild(description);

		Text argumentsLabel = new Text("Arguments:").bold();
		TextSequence paragraphArguments = new TextSequence();
		paragraphArguments.addChild(argumentsLabel);

		mainSection.addChildren(title, paragraphConfig, paragraphDescription, paragraphArguments);

		OperationProperty[] properties = type.getAnnotationsByType(OperationProperty.class);
		if (properties.length == 0) {
			Text argumentsNoContent = new Text("this filter does not accept any arguments.");
			paragraphArguments.addChild(argumentsNoContent);
		} else {
			Table tableArguments = new Table()
					.withHeaders(List.of("Argument", "position", "type", "required?", "description"));
			for (OperationProperty property : properties) {
				Text argName = new Text(property.name()).code();
				Text argPosition = new Text(Integer.toString(property.position()));
				Text argType = new Text(ClassUtils.getFieldType(type, property.field()).getSimpleName());
				Text argRequired = new Text(property.required() ? "y" : "n");
				String documentation = property.description();
				if (documentation.isBlank())
					documentation = ParsingUtils.sanitiseJavadoc(decl.getFieldByName(property.field()).get()
							.getJavadocComment()
							.orElse(new JavadocComment(MessagesConstants.DOCUMENTATION_NOT_PROVIDED)).getContent());
				Text argDescription = new Text(documentation);
				TextSequence line = new TextSequence();
				line.addChildren(argName, argPosition, argType, argRequired, argDescription);
				tableArguments.addChild(line);
			}
			mainSection.addChild(tableArguments);
		}

		return mainSection;
	}

}
