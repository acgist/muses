package com.acgist.notify.executor;

import com.acgist.boot.model.Model;

/**
 * 通知
 * 
 * @param <T> 通知类型
 * @param <C> 配置类型
 * 
 * @author acgist
 */
public abstract class Notify<T extends Model, C extends NotifyConfig> {

	/**
	 * 通知类型
	 */
	protected Class<T> notifyClazz;
	/**
	 * 通知配置
	 */
	protected C notifyConfig;
	
	public Notify(Class<T> notifyClazz, C notifyConfig) {
		this.notifyClazz = notifyClazz;
		this.notifyConfig = notifyConfig;
	}
	
	/**
	 * @return 通知名称
	 */
	public String name() {
		return this.notifyConfig.getName();
	}
	
	/**
	 * @return 排序
	 */
	public Integer sorted() {
		return this.notifyConfig.getSorted();
	}
	
	/**
	 * @return 是否启用
	 */
	public boolean enabled() {
		return this.notifyConfig.isEnabled();
	}
	
	/**
	 * 匹配通知
	 * 
	 * @param notify 通知对象
	 * 
	 * @return 是否匹配
	 */
	public boolean match(Object notify) {
		return notify != null && this.notifyClazz.isAssignableFrom(notify.getClass());
	}

	/**
	 * 发送通知
	 * 
	 * @param notify 通知信息
	 * 
	 * @return 通知结果
	 */
	@SuppressWarnings("unchecked")
	public boolean notify(Object notify) {
		return this.execute((T) notify);
	}
	
	/**
	 * 发送通知
	 * 
	 * @param notify 通知消息
	 * 
	 * @return 通知结果
	 */
	protected abstract boolean execute(T notify);
	
}
