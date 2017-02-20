package com.koolearn.cloud;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

public class SystemGlobalsPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	@Override
	protected String resolvePlaceholder(String placeholder, Properties props,
			int systemPropertiesMode) {
		String value = PropertiesConfigUtils.getProperty(placeholder);
		return StringUtils.trimToEmpty(value);
	}
}
