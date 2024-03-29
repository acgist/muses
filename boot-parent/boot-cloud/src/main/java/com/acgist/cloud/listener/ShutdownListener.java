package com.acgist.cloud.listener;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;

import lombok.extern.slf4j.Slf4j;

/**
 * 关机监听
 * 
 * @author acgist
 */
@Slf4j
public class ShutdownListener {

	@Value("${spring.application.name:muses}")
	private String serviceName;
	@Value("${system.shutdown.enable:true}")
	private boolean shutdownEnable;
	@Value("${system.shutdown.gracefully:30}")
	private int shutdownGracefully;
	
	/**
	 * 是否关闭
	 */
	private volatile boolean shutdown = false;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	private ConfigurableApplicationContext context;
	@Autowired
	private NacosServiceManager nacosServiceManager;
	@Autowired
	private NacosDiscoveryProperties nacosDiscoveryProperties;
	
	@PostConstruct
	public void init() {
		if(!this.shutdownEnable) {
			log.info("服务没有配置自动关机：{}", this.serviceName);
			return;
		}
		log.info("启动服务关机配置：{}-{}", this.serviceName, this.shutdownGracefully);
		NotifyCenter.registerSubscriber(new Subscriber<InstancesChangeEvent>() {

			/**
			 * 关机锁
			 */
			private final Lock lock = new ReentrantLock();
			/**
			 * 锁条件
			 */
			private final Condition condition = this.lock.newCondition();
			
			@Override
			public void onEvent(InstancesChangeEvent event) {
				final Optional<Instance> optional = event.getHosts().stream()
					.filter(ShutdownListener.this::self)
					.findFirst()
					.or(ShutdownListener.this::findSelf);
				if (optional.isPresent()) {
					this.up();
				} else {
					this.down();
				}
			}
			
			/**
			 * 启动
			 */
			private void up() {
				if(!ShutdownListener.this.shutdown) {
					log.debug("实例有效（已经生效）：{}", ShutdownListener.this.serviceName);
					return;
				}
				log.debug("实例有效（重置关闭事件）：{}", ShutdownListener.this.serviceName);
				ShutdownListener.this.shutdown = false;
				try {
					this.lock.lock();
					this.condition.signalAll();
				} finally {
					this.lock.unlock();
				}
			}
			
			/**
			 * 关闭
			 */
			private void down() {
				if(ShutdownListener.this.shutdown) {
					log.debug("实例无效（已经启动关闭事件）：{}", ShutdownListener.this.serviceName);
					return;
				}
				log.debug("实例无效（启动关闭事件）：{}-{}", ShutdownListener.this.serviceName, ShutdownListener.this.shutdownGracefully);
				ShutdownListener.this.shutdown = true;
				ShutdownListener.this.taskExecutor.execute(() -> {
					try {
						this.lock.lock();
						final long remaing = this.condition.awaitNanos(TimeUnit.SECONDS.toNanos(ShutdownListener.this.shutdownGracefully));
						if (ShutdownListener.this.shutdown) {
							log.info("实例无效（关闭实例）：{}", ShutdownListener.this.serviceName);
							// 关闭NacosServiceManager.nacosServiceShutDown()会出现空指针异常：2021-08以后版本已经修复
							// 阻塞关闭：不用再次等待
							ShutdownListener.this.context.close();
							// 强制关机：无效
//							System.exit(0);
							// 强制关机
							Runtime.getRuntime().halt(0);
						} else {
							log.debug("实例有效（忽略关闭事件）：{}-{}", ShutdownListener.this.serviceName, TimeUnit.NANOSECONDS.toSeconds(remaing));
						}
					} catch (InterruptedException e) {
						log.error("关闭实例异常", e);
					} finally {
						this.lock.unlock();
					}
				});
			}
			
			@Override
			public Class<? extends Event> subscribeType() {
				return InstancesChangeEvent.class;
			}
			
		});
	}
	
	/**
	 * 判断是否实例本身
	 * 
	 * @param instance 实例
	 * 
	 * @return 是否实例本身
	 */
	private boolean self(Instance instance) {
		return
			// 匹配IP地址
			instance.getIp().equals(this.nacosDiscoveryProperties.getIp()) &&
			(
				// Www服务端口
				instance.getPort() == this.nacosDiscoveryProperties.getPort() ||
				// Dubbo服务端口
				String.valueOf(instance.getPort()).equals(this.nacosDiscoveryProperties.getMetadata().get("dubbo.protocols.dubbo.port"))
			);
	}
	
	/**
	 * 获取服务实例本身
	 * 
	 * @return 服务实例本身
	 */
	private Optional<Instance> findSelf() {
		try {
			return this.nacosServiceManager
				.getNamingService(this.nacosDiscoveryProperties.getNacosProperties())
				.getAllInstances(this.serviceName).stream()
				.filter(this::self)
				.findFirst();
		} catch (NacosException e) {
			log.error("获取服务实例本身异常", e);
		}
		return Optional.empty();
	}
	
}
