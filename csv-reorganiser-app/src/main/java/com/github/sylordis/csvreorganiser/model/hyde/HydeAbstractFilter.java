package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.csvreorganiser.model.SelfFiller;
import com.github.sylordis.csvreorganiser.model.annotations.ReorgOperationProperty;
import com.github.sylordis.csvreorganiser.model.exceptions.SelfFillingException;

/**
 * Abstract class for Hyde filters.
 * 
 * When filled via {@link #fill(List)}, the list of arguments will be used according to the
 * annotation {@link ReorgOperationProperty#position()}, i.e. argument N will be used to fill
 * property position N.
 */
public abstract class HydeAbstractFilter implements HydeFilter, SelfFiller<List<Object>> {

	/**
	 * Class logger.
	 */
	private final Logger logger;

	/**
	 * Creates a new Hyde abstract filter.
	 */
	public HydeAbstractFilter() {
		this.logger = LogManager.getLogger();
	}

	@Override
	public void fill(List<Object> data) throws SelfFillingException {
		logger.debug("Filling");
		List<ReorgOperationProperty> properties = new ArrayList<>(
		        Arrays.asList(this.getClass().getAnnotationsByType(ReorgOperationProperty.class)));
		logger.debug("class={} annotations={}", this.getClass(),
		        properties.stream().map(a -> a.name()).collect(Collectors.toList()));
		Collections.sort(properties, (p1, p2) -> Integer.compare(p1.position(), p2.position()));
		Deque<Object> args = new ArrayDeque<>(data);
		for (ReorgOperationProperty prop : properties) {
			if (!args.isEmpty()) {
				Object arg = args.pop();
				this.setField(prop.field(), arg);
			} else if (prop.required()) {
				throw new SelfFillingException(
				        "Mandatory property '" + prop.name() + "' (#" + prop.position() + ") not provided.");
			} else
				break;
		}
	}

}
