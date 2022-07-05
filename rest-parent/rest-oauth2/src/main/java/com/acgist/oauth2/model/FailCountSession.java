package com.acgist.oauth2.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 失败次数
 * 
 * @author acgist
 */
public class FailCountSession {

	/**
	 * 最后一次登陆失败时间
	 */
	private long lastTime;
	/**
	 * 失败次数
	 */
	private AtomicInteger count;

	public FailCountSession() {
		this.lastTime = System.currentTimeMillis();
		this.count = new AtomicInteger(0);
	}

	/**
	 * @param count 失败次数
	 * @param duration 锁定时长
	 * 
	 * @return 是否可以登陆
	 */
	public boolean verify(int count, int duration) {
		if(this.count.get() >= count && (System.currentTimeMillis() - this.lastTime < duration * 1000)) {
			return false;
		}
		return true;
	}

	/**
	 * 失败
	 * 
	 * @return 失败次数
	 */
	public int fail() {
		this.lastTime = System.currentTimeMillis();
		return this.count.incrementAndGet();
	}
	
}
