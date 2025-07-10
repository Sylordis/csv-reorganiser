package com.github.sylordis.csvreorganiser.model.hyde.config;

import com.github.sylordis.csvreorganiser.model.constants.ConfigConstants;
import com.github.sylordis.csvreorganiser.model.hyde.HydeAbstractFilter;

public class HydeDefaultConfigurationSupplier implements HydeConfigurationSupplier {

	@Override
	public String getBasePackage() {
		return ConfigConstants.Hyde.FILTERS_PACKAGE;
	}

	@Override
	public Class<HydeAbstractFilter> getBaseType() {
		return HydeAbstractFilter.class;
	}

}
