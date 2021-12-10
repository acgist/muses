package com.acgist.sentinel.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.StringUtils;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;

@Configuration
@ConfigurationProperties(prefix = "sentinel")
public class UrlCleanerAutoConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlCleanerAutoConfiguration.class);
	
	private Map<String, String> urlCleaner = new HashMap<>();
	
	public Map<String, String> getUrlCleaner() {
		return urlCleaner;
	}
	
	public void setUrlCleaner(Map<String, String> urlCleaner) {
		this.urlCleaner = urlCleaner;
		this.urlCleaner.forEach((key, value) -> LOGGER.info("URL清洗：{}-{}", key, value));
	}

	@Bean
	public UrlCleaner urlCleaner() {
		return new UrlCleaner() {
			@Override
			public String clean(String url) {
				if (StringUtils.isEmpty(url)) {
					return url;
				}
				final Optional<String> optional = UrlCleanerAutoConfiguration.this.urlCleaner.entrySet().stream()
					.filter(entity -> url.startsWith(entity.getKey()))
					.map(Map.Entry::getValue)
					.findAny();
				return optional.orElse(url);
			}
		};
	}

}
