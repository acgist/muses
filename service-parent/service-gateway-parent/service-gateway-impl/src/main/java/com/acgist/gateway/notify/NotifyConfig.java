package com.acgist.gateway.notify;

import lombok.Getter;
import lombok.Setter;

/**
 * 通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
public abstract class NotifyConfig {

	/**
	 * 是否启用
	 */
	private boolean enable;
	/**
	 * 通知名称
	 */
	private String name;
	/**
	 * 通知匹配正则表达式
	 */
	private String match;
	/**
	 * 排序
	 */
	private Integer order;

}
