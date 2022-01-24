package com.acgist.gateway.notify;

/**
 * 通知配置
 * 
 * @author acgist
 */
public class NotifyConfig {

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

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
