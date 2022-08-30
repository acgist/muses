package com.acgist.web.listener;

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
public class WebStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

	/**
	 * 是否初始化
	 */
	private static boolean init = false;
	
	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		if(!WebStartingEventListener.init) {
			WebStartingEventListener.init = true;
			PortConfig.buildPort(PortConfig.Type.WEB, event.getArgs());
		}
	}

}