package com.acgist.notify.executor;

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
	 * 通知名称
	 */
	private String name;
	/**
	 * 排序
	 */
	private Integer sorted;
	/**
	 * 是否启用
	 */
	private boolean enabled;

}
