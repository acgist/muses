package com.acgist.scheduled.aop;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式定时任务切点
 * 
 * @author acgist
 */
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributedScheduled {

	/**
	 * 分布式锁名称
	 */
	String name();

	/**
	 * 分布式锁时长（单位：秒）
	 * 
	 * 注意：Redis时长必须大于定时任务间隔
	 */
	int ttl() default 0;
	
	/**
	 * 判断是否自动释放
	 * 
	 * 注意：如果时间不同步并且任务时间果断不建议自动释放
	 * 
	 * @return 是否自动释放
	 */
	boolean unlock() default false;

}
