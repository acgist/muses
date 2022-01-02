package com.acgist.sentinel.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.StringUtils;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;

/**
 * URL清洗
 * 
 * @author acgist
 */
@Configuration
@ConfigurationProperties(prefix = "sentinel")
public class UrlCleanerAutoConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlCleanerAutoConfiguration.class);
	
	/**
	 * URL清洗
	 */
	private Map<String, String> urlCleaner = new HashMap<>();
	
	public Map<String, String> getUrlCleaner() {
		return urlCleaner;
	}
	
	public void setUrlCleaner(Map<String, String> urlCleaner) {
		this.urlCleaner = urlCleaner;
		this.urlCleaner.forEach((key, value) -> LOGGER.info("URL清洗：{}-{}", key, value));
	}

	@Bean
	@ConditionalOnMissingBean
	public UrlCleaner urlCleaner() {
		return new UrlCleaner() {
			@Override
			public String clean(String url) {
				if (StringUtils.isEmpty(url)) {
					return url;
				}
				return UrlCleanerAutoConfiguration.this.urlCleaner.entrySet().stream()
					.filter(entry -> url.startsWith(entry.getKey()))
					.map(Map.Entry::getValue)
					.findFirst()
					.orElse(url);
			}
		};
	}

}
