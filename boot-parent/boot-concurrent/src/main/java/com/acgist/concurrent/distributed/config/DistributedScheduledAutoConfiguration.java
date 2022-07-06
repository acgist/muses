package com.acgist.concurrent.distributed.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.utils.BeanUtils;
import com.acgist.concurrent.distributed.lock.DistributedLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 分布式定时任务自动配置
 * 
 * @author acgist
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnBean(value = DistributedLock.class)
@AutoConfigureAfter(value = DistributedLockAutoConfiguration.class)
public class DistributedScheduledAutoConfiguration {

	@Autowired
	private DistributedLock distributedLock;

	/**
	 * 注解切点
	 */
	@Pointcut("@annotation(com.acgist.distributed.config.DistributedScheduled)")
	public void scheduled() {
	}

	/**
	 * 环绕执行
	 * 
	 * @param proceedingJoinPoint 切点
	 * 
	 * @return 返回
	 * 
	 * @throws Throwable 异常
	 */
	@Around("scheduled()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		final DistributedScheduled distributedScheduled = BeanUtils.getAnnotation(proceedingJoinPoint, DistributedScheduled.class);
		final String name = distributedScheduled.name();
		try {
			if (this.distributedLock.tryLock(name, distributedScheduled.ttl())) {
				log.debug("定时任务加锁成功执行：{}", name);
				return proceedingJoinPoint.proceed();
			} else {
				log.debug("定时任务加锁失败执行：{}", name);
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			if(distributedScheduled.unlock()) {
				this.distributedLock.unlock(name);
			}
		}
		return null;
	}

}
