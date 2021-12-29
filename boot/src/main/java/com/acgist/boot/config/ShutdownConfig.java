package com.xyh.boot.core.gateway;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;

@Configuration
public class ShutdownConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownConfig.class);

    /**
     * 服务名称
     */
    @Value("${spring.application.name:}")
    private String serviceName;
    /**
     * 是否启动自动关闭
     */
    @Value("${system.shutdown.enable:true}")
    private boolean shutdownEnable;
    /**
     * 优雅关闭：等待时间（秒）
     */
    @Value("${system.shutdown.gracefully:30}")
    private int shutdownGracefully;
    /**
     * 是否关闭
     */
    private volatile boolean shutdown = false;

    @Autowired(required = false)
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private NacosServiceManager nacosServiceManager;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    
    @PostConstruct
    public void init() {
        if(Objects.isNull(this.executor)) {
            LOGGER.info("服务自动关机没有线程：{}", this.serviceName);
        }
        if(!this.shutdownEnable) {
            LOGGER.info("服务没有配置自动关机：{}", this.serviceName);
        }
        LOGGER.info("启动服务关机配置：{}-{}-{}", this.serviceName, this.shutdownGracefully);
        NotifyCenter.registerSubscriber(new Subscriber<InstancesChangeEvent>() {

            /**
             * 锁
             */
            private final Lock lock = new ReentrantLock();
            /**
             * 条件
             */
            private final Condition condition = this.lock.newCondition();
            
            @Override
            public void onEvent(InstancesChangeEvent event) {
                // 事件获取实例列表
                final Optional<Instance> optional = event.getHosts().stream()
                    .filter(ShutdownConfig.this::myself)
                    .findFirst()
                    .or(() -> ShutdownConfig.this.findMyself());
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
                if(!ShutdownConfig.this.shutdown) {
                    LOGGER.debug("实例有效：已经生效：{}", ShutdownConfig.this.serviceName);
                    return;
                }
                LOGGER.debug("实例有效：重置关闭事件：{}", ShutdownConfig.this.serviceName);
                ShutdownConfig.this.shutdown = false;
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
                if(ShutdownConfig.this.shutdown) {
                    LOGGER.debug("实例无效：已经启动关闭事件：{}", ShutdownConfig.this.serviceName);
                    return;
                }
                LOGGER.debug("实例无效：启动关闭事件：{}-{}", ShutdownConfig.this.serviceName, ShutdownConfig.this.shutdownGracefully);
                ShutdownConfig.this.shutdown = true;
                ShutdownConfig.this.executor.submit(() -> {
                    try {
                        this.lock.lock();
                        final long remaing = this.condition.awaitNanos(TimeUnit.SECONDS.toNanos(ShutdownConfig.this.shutdownGracefully));
                        if (ShutdownConfig.this.shutdown) {
                            LOGGER.info("实例无效：关闭实例：{}", ShutdownConfig.this.serviceName);
                            // 关闭NacosServiceManager.nacosServiceShutDown()会出现空指针异常：2021-08以后版本已经修复
                            ShutdownConfig.this.context.close();
                        } else {
                            LOGGER.debug("实例有效：忽略关闭事件：{}-{}", ShutdownConfig.this.serviceName, TimeUnit.NANOSECONDS.toSeconds(remaing));
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("关闭实例异常", e);
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
    private boolean myself(Instance instance) {
        return
            this.nacosDiscoveryProperties.getIp().equals(instance.getIp()) &&
            this.nacosDiscoveryProperties.getPort() == instance.getPort();
    }
    
    /**
     * 获取服务实例本身
     * 
     * @return 服务实例本身
     */
    private Optional<Instance> findMyself() {
        try {
            return this.nacosServiceManager
                .getNamingService(this.nacosDiscoveryProperties.getNacosProperties())
                .getAllInstances(this.serviceName).stream()
                .filter(this::myself)
                .findFirst();
        } catch (NacosException e) {
            LOGGER.error("获取服务实例本身异常", e);
        }
        return Optional.empty();
    }
    
}
