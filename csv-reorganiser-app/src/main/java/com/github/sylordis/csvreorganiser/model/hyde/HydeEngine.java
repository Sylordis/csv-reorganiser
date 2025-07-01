package com.github.sylordis.csvreorganiser.model.hyde;

import static com.github.sylordis.csvreorganiser.model.constants.YAMLTags.OPDEF_ROOT_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.utils.yaml.YAMLUtils;

/**
 * The Hyde engine uses a more simple declaration than the Chess Engine, where each operation is
 * declared from one string and using a syntax similar to the
 * <a href="https://www.djangoproject.com/">Django template language</a>. Each column is defined by
 * a name and a string composed of column names, constants, modified by filters in order to modify
 * and produce a result.
 * 
 * @author sylordis
 *
 */
public class HydeEngine implements ReorganiserEngine {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates an operation from the content string.
	 * 
	 * @param name    Name of the operation
	 * @param content Content of the operation
	 * @return
	 */
	public ReorganiserOperation createOperation(String name, String content) {
		logger.debug("yamlToOp[in]: name='{}' content='{}'", name, content);
		HydeOperation op = new HydeOperation(name);
		int index = 0;
		int nextTemplateStart = -1;
		int nextTemplateEnd = -1;
		while (index < content.length()) {
			nextTemplateStart = content.indexOf(ConfigConstants.Hyde.TEMPLATE_START, index);
			if (nextTemplateStart > -1) {
				if (nextTemplateStart > index) {
					String constantPart = content.substring(index, nextTemplateStart);
					op.addChild(createConstantPart(constantPart));
					logger.debug("Constant [{}:{}]='{}'", index, nextTemplateStart, constantPart);
				}
				nextTemplateEnd = content.indexOf(ConfigConstants.Hyde.TEMPLATE_END, nextTemplateStart);
				if (nextTemplateEnd == -1) {
					throw new ConfigurationImportException("Unfinished template declaration for field " + name);
				} else
					nextTemplateEnd += ConfigConstants.Hyde.TEMPLATE_END.length();
				String template = content.substring(nextTemplateStart, nextTemplateEnd);
				logger.debug("Template: [{}:{}]='{}'", nextTemplateStart, nextTemplateEnd, template);
				op.addChild(createPartFromTemplate(template));
				index = nextTemplateEnd;
			} else {
				// Catch up with the rest of the string if not already at the end
				if (index < content.length()) {
					String constantPart = content.substring(index);
					op.addChild(createConstantPart(constantPart));
					logger.debug("Constant [{}:]='{}'", index, constantPart);
				}
				index = content.length();
			}
		}

		return op;
	}

	public HydeOperationPart createPartFromTemplate(String content) {
		String template = content.substring(ConfigConstants.Hyde.TEMPLATE_START.length(),
		        content.length() - ConfigConstants.Hyde.TEMPLATE_END.length());
		List<HydeModifier> modifiers = new ArrayList<>();
		String field = "TODO"; // TODO
		// TODO Parse for modifiers and field name
		String modifDelim = ConfigConstants.Hyde.TEMPLATE_MODIFIER_DELIMITER;
		if (template.contains(modifDelim)) {
			int nextModifierStart = template.indexOf(modifDelim);
			field = template.substring(0, nextModifierStart);
			int index = 0;
			int nextModifierEnd = -1;
			while (index < template.length()) {
				nextModifierStart = template.indexOf(modifDelim, index);
				if (nextModifierStart > 0) {
					nextModifierStart += modifDelim.length();
					nextModifierEnd = template.indexOf(modifDelim, nextModifierStart + modifDelim.length());
					if (nextModifierEnd == -1)
						nextModifierEnd = template.length();
					String modifier = template.substring(nextModifierStart, nextModifierEnd);
					logger.debug("Modifier: [{}:{}]='{}'", nextModifierStart, nextModifierEnd, modifier);
					// TODO
					index = nextModifierEnd + 1;
				} else {
					index = template.length();
				}
			}
		} else
			field = template;
		logger.debug("Template: field='{}', modifiers={}", field, modifiers.size());
		HydeOperationTemplatePart part = new HydeOperationTemplatePart();
		part.setField(field);
		part.setModifiers(modifiers);
		return part;
	}

	public HydeOperationPart createConstantPart(String content) {
		return t -> content;
	}

	@Override
	public List<ReorganiserOperation> createOperations(Map<String, Object> root) {
		return YAMLUtils.get(OPDEF_ROOT_KEY, root).entrySet().stream()
		        .map(e -> createOperation(e.getKey(), (String) e.getValue())).collect(Collectors.toList());
	}

}
