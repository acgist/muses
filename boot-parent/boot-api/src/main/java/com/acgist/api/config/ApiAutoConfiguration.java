package com.acgist.api.config;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.utils.ErrorUtils;

/**
 * Dubbo服务自动配置
 * 
 * @author acgist
 */
@DubboComponentScan("com.acgist.**.api")
@ConditionalOnClass(DubboAutoConfiguration.class)
public class ApiAutoConfiguration {

	@PostConstruct
	public void init() {
		this.registerException();
	}
	
	/**
	 * 注册异常
	 */
	public void registerException() {
		ErrorUtils.register(MessageCode.CODE_2000, RpcException.class);
		ErrorUtils.register(MessageCode.CODE_2001, TimeoutException.class);
		ErrorUtils.register(MessageCode.CODE_2000, RemotingException.class);
	}
	
}
