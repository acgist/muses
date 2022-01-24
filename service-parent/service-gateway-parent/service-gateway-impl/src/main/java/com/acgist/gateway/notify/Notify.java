package com.acgist.gateway.notify;

import com.acgist.gateway.pojo.entity.GatewayEntity;

/**
 * 通知
 * 
 * @author acgist
 */
public abstract class Notify implements Comparable<Notify> {

	private NotifyConfig notifyConfig;
	
	public Notify(NotifyConfig notifyConfig) {
		this.notifyConfig = notifyConfig;
	}
	
	/**
	 * @return 通知名称
	 */
	public String name() {
		return this.notifyConfig.getName();
	}
	/**
	 * @return 是否启用
	 */
	public boolean enable() {
		return this.notifyConfig.isEnable();
	}
	
	/**
	 * 匹配通知地址
	 * 
	 * @param url 通知地址
	 * 
	 * @return 是否匹配
	 */
	public boolean match(String url) {
		final String match = this.notifyConfig.getMatch();
		return match != null && url != null && url.matches(match);
	}

	@Override
	public int compareTo(Notify target) {
		return this.notifyConfig.getOrder().compareTo(target.notifyConfig.getOrder());
	}
	
	/**
	 * 发送通知
	 * 
	 * @param gatewayEntity 网关信息
	 * 
	 * @return 响应报文
	 */
	public abstract String execute(GatewayEntity gatewayEntity);
	
}
