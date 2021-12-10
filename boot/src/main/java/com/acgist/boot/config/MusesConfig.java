package com.acgist.boot.config;

import org.yaml.snakeyaml.Yaml;

/**
 * 系统配置
 * 
 * @author acgist
 */
public class MusesConfig {

	/**
	 * 超时时间
	 */
	public static final int TIMEOUT = 5 * 1000;
	/**
	 * 默认编码
	 */
	public static final String CHARSET = "UTF-8";

	final Yaml yaml = new Yaml();

	public void deserialize(String content) {
		this.yaml.load(content);
	}
	
	public String serialize() {
		return this.yaml.dump
	}
	
}
