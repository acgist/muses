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
	 * 当前机器编号：01~99
	 * 
	 * 可以配置负数：自动生成
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
	 * 服务实例编号
	 */
	private Map<String, Integer> sns;
	
}
