package com.acgist.rule.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 字段属性映射配置
 * 
 * @author acgist
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "condition.field")
public class ConditionMapping {
	
	/**
	 * 属性
	 */
	private Map<String, String> mapping;

	/**
	 * 翻译
	 * 
	 * @param field 字段
	 * 
	 * @return 名称
	 */
	public String transfer(String field) {
		return this.mapping == null ? field : this.mapping.getOrDefault(field, field);
	}
	
}
