package com.acgist.web.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import com.acgist.boot.config.PortConfig;

/**
 * 启动
 * 
 * 注意：SpringApplicationRunListeners里面同步执行
 * 
 * @author acgist
 */
public class StartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		PortConfig.buildPort(PortConfig.Type.WEB, event.getArgs());
	}

}