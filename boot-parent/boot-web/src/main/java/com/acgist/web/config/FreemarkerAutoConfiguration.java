package com.acgist.web.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Freemarker自动配置
 * 
 * @author acgist
 */
@Configuration
public class FreemarkerAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerAutoConfiguration.class);

	@Value("${system.static.host:}")
	private String staticHost;

	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("Freemarker静态文件域名：{}", this.staticHost);
		this.configuration.setSharedVariable("staticHost", this.staticHost);
	}

}
