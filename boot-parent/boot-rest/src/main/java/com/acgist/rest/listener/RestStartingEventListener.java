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

	/**
	 * 是否初始化
	 */
	private static boolean init = false;
	
	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		synchronized (RestStartingEventListener.class) {
			if(!RestStartingEventListener.init) {
				RestStartingEventListener.init = true;
				PortConfig.buildPort(PortConfig.Type.REST, event.getArgs());
			}
		}
	}

}