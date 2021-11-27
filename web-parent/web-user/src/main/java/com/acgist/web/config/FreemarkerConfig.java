package com.acgist.web.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * freemarker
 * 
 * @author acgist
 */
@Configuration
public class FreemarkerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerConfig.class);
	
	@Value("${system.static.host:}")
	private String staticHost;
	
	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("freemarker静态文件域名：{}", staticHost);
		this.configuration.setSharedVariable("staticHost", this.staticHost);
	}

}
