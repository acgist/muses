package com.acgist.boot.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置
 * 
 * @author acgist
 */
@Getter
@Setter
public class MusesConfig {

	/**
	 * 最大系统编号
	 */
	public static final int MAX_SN = 100;
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
	 * 系统自动配置
	 */
	public static final String MUSES_CONFIG = "muses.json";
	/**
	 * 时间格式
	 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/**
	 * 日期格式
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 日期时间格式
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * JSON HTTP数据类型
	 */
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

	/**
	 * 当前系统编号
	 */
	@JsonIgnore
	private Integer sn;
	/**
	 * 当前系统PID
	 */
	@JsonIgnore
	private Integer pid;
	/**
	 * 当前系统端口
	 */
	@JsonIgnore
	private Integer port;
	/**
	 * 实例系统编号
	 * 
	 * 如果服务编号配置为负数时，系统自动为不同服务配置一个递增的系统编号。
	 * 
	 * @see #MAX_SN
	 */
	private Map<String, Integer> sns;

}
