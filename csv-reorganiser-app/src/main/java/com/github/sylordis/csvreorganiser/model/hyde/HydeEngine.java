package com.github.sylordis.csvreorganiser.model.hyde;

import static com.github.sylordis.csvreorganiser.model.constants.YAMLTags.OPDEF_ROOT_KEY;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserEngine;
import com.github.sylordis.csvreorganiser.model.engines.ReorganiserOperation;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationException;
import com.github.sylordis.csvreorganiser.model.exceptions.ConfigurationImportException;
import com.github.sylordis.csvreorganiser.model.exceptions.OperationBuildingException;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;
import com.github.sylordis.csvreorganiser.model.hyde.config.HydeConfigurationSupplier;
import com.github.sylordis.csvreorganiser.model.hyde.config.HydeDefaultConfigurationSupplier;
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
	 * Dictionary of all existing operations.
	 */
	private final Map<String, Class<? extends HydeAbstractFilter>> filtersDictionary;

	/**
	 * Constructs a hyde engine with a default configuration supplier.
	 */
	public HydeEngine() {
		this(new HydeDefaultConfigurationSupplier());
	}

	/**
	 * Constructs a Hyde engine with a custom dictionary supplier.
	 * 
	 * @param dictionarySupplier supplier for all filters
	 */
	public HydeEngine(HydeConfigurationSupplier dictionarySupplier) {
		this.filtersDictionary = new HashMap<>();
		setFiltersDictionary(dictionarySupplier.getConfigurationDictionary());
		logger.debug("Dictionary: {}", this.filtersDictionary.keySet());
	}

	/**
	 * Creates an operation from the content string.
	 * 
	 * @param name    Name of the operation
	 * @param content Content of the operation
	 * @return
	 */
	public ReorganiserOperation createOperation(String name, String content) {
		logger.debug("yamlToOp[in]: name='{}' content='{}'", name, content);
		HydeReorgOperation op = new HydeReorgOperation(name);
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

	/**
	 * Creates a {@link HydeReorgOperationPart} from a given template string.
	 * 
	 * @param template
	 * @return
	 */
	public HydeReorgOperationPart createPartFromTemplate(String template) {
		String templateCfg = template.substring(ConfigConstants.Hyde.TEMPLATE_START.length(),
		        template.length() - ConfigConstants.Hyde.TEMPLATE_END.length());
		List<HydeFilter> filters = new ArrayList<>();
		String filterDelim = ConfigConstants.Hyde.TEMPLATE_FILTER_DELIMITER;
		String[] parts = templateCfg.split("\\Q" + filterDelim + "\\E");
		String field = parts[0];
		for (int i = 1; i < parts.length; i++) {
			logger.debug("Filter: {}", parts[i]);
			String[] filterParts = parts[i].split("\\Q" + ConfigConstants.Hyde.TEMPLATE_FILTER_PARAM_DELIMITER + "\\E");
			String filterName = filterParts[0];
			List<Object> args = new ArrayList<>();
			for (int j = 1; j < filterParts.length; j++)
				args.add(filterParts[j]);
			filters.add(createFilter(filterName, args));
		}
		logger.debug("Template: field='{}', filters={}", field,
		        filters.stream().map(f -> f.getClass().getSimpleName()).collect(Collectors.toList()));
		HydeReorgOperationTemplatePart part = new HydeReorgOperationTemplatePart();
		part.setField(field);
		part.setFilters(filters);
		return part;
	}

	/**
	 * Creates a filter from its name and a list of arguments.
	 * 
	 * @param name name of the filter, e.g. the type of the filter
	 * @param args arguments to provide to the filter
	 * @return
	 * @throws ConfigurationException     if something goes wrong
	 * @throws OperationBuildingException if the filter type is unknown
	 */
	public HydeFilter createFilter(String name, List<Object> args) {
		logger.debug("Creating filter {}{}", name, args);
		String lname = name.toLowerCase();
		HydeAbstractFilter filter = null;
		if (filtersDictionary.containsKey(lname)) {
			try {
				final Class<? extends HydeAbstractFilter> type = filtersDictionary.get(lname);
				filter = type.getDeclaredConstructor().newInstance();
				filter.fill(args);
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | SecurityException | SelfFillingException e) {
				throw new ConfigurationException(e);
			}
		} else {
			throw new OperationBuildingException("Unknown filter type '" + lname + "'");
		}
		return filter;
	}

	/**
	 * Creates a {@link HydeReorgOperationPart} that outputs a constant.
	 * 
	 * @param content
	 * @return
	 */
	public HydeReorgOperationPart createConstantPart(String content) {
		return t -> content;
	}

	@Override
	public List<ReorganiserOperation> createOperations(Map<String, Object> root) {
		return YAMLUtils.get(OPDEF_ROOT_KEY, root).entrySet().stream()
		        .map(e -> createOperation(e.getKey(), (String) e.getValue())).collect(Collectors.toList());
	}

	/**
	 * @return the filterDictionary
	 */
	public Map<String, Class<? extends HydeAbstractFilter>> getFiltersDictionary() {
		return filtersDictionary;
	}

	/**
	 * Sets dictionary of all existing filters.
	 *
	 * @param dictionary a map of all filters indexed by their names
	 * @throws ConfigurationException when supplying a null map
	 */
	public void setFiltersDictionary(Map<String, Class<? extends HydeAbstractFilter>> dictionary) {
		this.filtersDictionary.clear();
		if (dictionary != null)
			this.filtersDictionary.putAll(dictionary);
		else
			throw new ConfigurationException("Can't supply null filters dictionary");
	}

}
