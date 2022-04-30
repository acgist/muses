package com.acgist.cloud.config;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Cloud配置
 * 
 * @author acgist
 */
@Getter
@Setter
public class CloudConfig {

	/**
	 * 最大系统编号
	 */
	public static final int MAX_SN = 100;
	/**
	 * 系统自动配置
	 */
	public static final String MUSES_CONFIG = "muses.json";
	
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
