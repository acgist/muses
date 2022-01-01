package com.acgist.distributed.lock;

import java.util.Objects;
import java.util.UUID;

import com.acgist.boot.MessageCodeException;

/**
 * 分布式锁
 * 
 * @author acgist
 */
public interface DistributedLock {

	/**
	 * 阻塞最短时长（单位：毫秒）
	 */
	public static final int BLOCKING_MILLIS = 10;

	/**
	 * 锁值
	 */
	public static final ThreadLocal<String> VALUE_LOCAL = ThreadLocal.withInitial(UUID.randomUUID()::toString);

	/**
	 * 重入
	 */
	public static final ThreadLocal<Integer> REENTRY_LOCAL = ThreadLocal.withInitial(() -> 0);

	/**
	 * 加锁
	 * 
	 * @param key 锁名
	 * @param ttl 加锁时长（单位：秒）
	 * 
	 * @return 是否成功
	 */
	public default boolean tryLock(String key, int ttl) {
		return this.tryLock(key, 0, ttl);
	}

	/**
	 * 加锁
	 * 
	 * @param key 锁名
	 * @param duration 尝试时长（单位：毫秒）
	 * @param ttl 加锁时长（单位：秒）
	 * 
	 * @return 是否成功
	 */
	public default boolean tryLock(String key, int duration, int ttl) {
		do {
			if (this.lock(key, ttl)) {
				return true;
			}
		} while ((duration = this.blocking(key, duration)) > 0);
		return false;
	}

	/**
	 * 加锁
	 * 
	 * @param key 锁名
	 * @param ttl 加锁时长（单位：秒）
	 * 
	 * @return 是否成功
	 */
	public default boolean lock(String key, int ttl) {
		if (Objects.isNull(key)) {
			throw MessageCodeException.of("锁名错误：" + key);
		}
		final String value = VALUE_LOCAL.get();
		// 加锁
		if (this.set(key, value, ttl)) {
			REENTRY_LOCAL.set(1);
			return true;
		}
		// 重入
		final String oldValue = this.get(key);
		if (value.equals(oldValue)) {
			// 重入刷新时长
			this.reset(key, oldValue, ttl);
			REENTRY_LOCAL.set(REENTRY_LOCAL.get() + 1);
			return true;
		} else if (oldValue == null) {
			// 如果为空立即尝试加锁
			return this.lock(key, ttl);
		}
		return false;
	}

	/**
	 * 解锁
	 * 
	 * @param key 锁名
	 */
	public default void unlock(String key) {
		if (Objects.isNull(key)) {
			throw MessageCodeException.of("锁名错误：" + key);
		}
		final String oldValue = this.get(key);
		// 优先判断oldValue避免VALUE_LOCAL内存泄露
		if (oldValue != null && oldValue.equals(VALUE_LOCAL.get())) {
			final int newReentry = REENTRY_LOCAL.get() - 1;
			if (newReentry == 0) {
				VALUE_LOCAL.remove();
				REENTRY_LOCAL.remove();
				this.delete(key);
			} else {
				REENTRY_LOCAL.set(newReentry);
			}
		}
	}

	/**
	 * 添加锁
	 * 
	 * @param key 锁名
	 * @param value 锁值
	 * @param ttl 加锁时长（单位：秒）
	 * 
	 * @return 是否成功
	 */
	boolean set(String key, String value, int ttl);

	/**
	 * 重置锁
	 * 
	 * @param key 锁名
	 * @param value 锁值
	 * @param ttl 加锁时长（单位：秒）
	 */
	default void reset(String key, String value, int ttl) {
	}

	/**
	 * 获取锁值
	 * 
	 * @param key 锁名
	 * 
	 * @return 锁值
	 */
	String get(String key);

	/**
	 * 删除锁
	 * 
	 * @param key 锁名
	 */
	void delete(String key);

	/**
	 * 阻塞
	 * 
	 * @param key 锁名
	 * @param duration 阻塞时长（单位：毫秒）
	 * 
	 * @return 剩余阻塞时长
	 */
	default int blocking(String key, int duration) {
		try {
			Thread.sleep(BLOCKING_MILLIS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return duration - BLOCKING_MILLIS;
	}

}
