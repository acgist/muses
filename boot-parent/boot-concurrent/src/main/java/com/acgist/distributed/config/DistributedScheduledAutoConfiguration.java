package com.acgist.distributed.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.MessageCodeException;
import com.acgist.distributed.lock.DistributedLock;
import com.acgist.distributed.scheduled.DistributedScheduled;

/**
 * 分布式定时任务
 * 
 * @author acgist
 */
@Aspect
@Configuration
@ConditionalOnBean(value = DistributedLock.class)
@AutoConfigureAfter(value = DistributedLockAutoConfiguration.class)
public class DistributedScheduledAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistributedScheduledAutoConfiguration.class);

	@Autowired
	private DistributedLock distributedLock;

	/**
	 * 注解切点
	 */
	@Pointcut("@annotation(com.acgist.distributed.scheduled.DistributedScheduled)")
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
		final DistributedScheduled distributedScheduled = this.getAnnotation(proceedingJoinPoint);
		if (distributedScheduled == null) {
			throw MessageCodeException.of("获取注解失败");
		}
		final String name = distributedScheduled.name();
		try {
			if (this.distributedLock.tryLock(name, distributedScheduled.ttl())) {
				LOGGER.debug("定时任务加锁成功执行：{}", name);
				return proceedingJoinPoint.proceed();
			} else {
				LOGGER.debug("定时任务加锁失败执行：{}", name);
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

	/**
	 * 获取切点注解
	 * 
	 * @param proceedingJoinPoint 切点
	 * 
	 * @return 注解
	 */
	private DistributedScheduled getAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
		if (proceedingJoinPoint.getSignature() instanceof MethodSignature methodSignature) {
			return methodSignature.getMethod().getAnnotation(DistributedScheduled.class);
		}
		throw MessageCodeException.of("注解错误");
	}

}
