package com.acgist.rest.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import com.acgist.cloud.config.PortConfig;

/**
 * 启动
 * 
 * 注意：SpringApplicationRunListeners里面同步执行
 * 
 * @author acgist
 */
public class RestStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		PortConfig.buildPort(PortConfig.Type.REST, event.getArgs());
	}

}