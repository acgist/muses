package com.acgist.oauth2.model;

/**
 * IP请求次数统计
 * 
 * @author acgist
 */
public class IPCountSession {

	/**
	 * 最后一次统计时间
	 */
	private long lastTime;
	/**
	 * 请求次数
	 */
	private int count;

	public IPCountSession() {
		this.lastTime = System.currentTimeMillis();
		this.count = 1;
	}

	/**
	 * @param count 最大请求次数
	 * @param duration 锁定时长
	 * 
	 * @return 是否拦截
	 */
	public boolean verify(int count, int duration) {
		if(
			this.count >= count &&
			(System.currentTimeMillis() - this.lastTime < duration * 1000)
		) {
			return false;
		}
		return true;
	}

	/**
	 * 增加次数
	 * 
	 * @return 当前次数
	 */
	public int increment() {
		this.lastTime = System.currentTimeMillis();
		return ++this.count;
	}
	
}
