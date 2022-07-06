package com.acgist.boot.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 系统配置
 * 
 * @author acgist
 */
public class MusesConfig {
	
	private MusesConfig() {
	}

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
	 * JSON MIME
	 */
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
	/**
	 * 最小系统编号
	 * 
	 * 乘以系数不用担心位数对齐问题
	 */
	public static final int CLOUD_MIN_SN = 000;
	/**
	 * 最大系统编号
	 */
	public static final int CLOUD_MAX_SN = 100;
	/**
	 * 系统自动配置
	 */
	public static final String CLOUD_CONFIG = "muses.json";
	/**
	 * OAuth2用户ID
	 */
	public static final String OAUTH2_ID = "id";
	/**
	 * OAuth2用户名称
	 */
	public static final String OAUTH2_NAME = "name";
	/**
	 * OAuth2用户角色
	 */
	public static final String OAUTH2_ROLE = "role";

}
