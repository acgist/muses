package com.acgist.api.config;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * Dubbo服务自动配置
 * 
 * @author acgist
 */
@DubboComponentScan("com.acgist.**.api")
@ConditionalOnClass(DubboAutoConfiguration.class)
public class ApiAutoConfiguration {

}
