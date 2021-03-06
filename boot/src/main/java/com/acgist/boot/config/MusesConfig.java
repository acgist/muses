package com.acgist.boot.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	/**
	 * 默认编码
	 */
	public static final String CHARSET_VALUE = CHARSET.name();
	/**
	 * JSON HTTP数据类型
	 */
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

}
