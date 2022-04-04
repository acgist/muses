package com.acgist.web.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Freemarker自动配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
public class FreemarkerAutoConfiguration {

	@Value("${system.static.host:}")
	private String staticHost;

	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		log.info("Freemarker静态文件域名：{}", this.staticHost);
		this.configuration.setSharedVariable("staticHost", this.staticHost);
	}

}
