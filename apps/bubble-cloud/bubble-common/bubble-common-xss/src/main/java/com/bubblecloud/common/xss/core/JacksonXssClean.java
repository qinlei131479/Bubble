package com.bubblecloud.common.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.bubblecloud.common.xss.config.XssProperties;
import com.bubblecloud.common.xss.utils.XssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * jackson xss 处理
 *
 * @author L.cm
 */
@Slf4j
public class JacksonXssClean extends StdDeserializer<String> {

	private final XssProperties properties;

	private final XssCleaner xssCleaner;

	public JacksonXssClean(XssProperties properties, XssCleaner xssCleaner) {
		super(String.class);
		this.properties = properties;
		this.xssCleaner = xssCleaner;
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		// XSS filter
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		}
		if (XssHolder.isEnabled()) {
			String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
			log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			return value;
		}
		else {
			return XssUtil.trim(text, properties.isTrimText());
		}
	}

}
