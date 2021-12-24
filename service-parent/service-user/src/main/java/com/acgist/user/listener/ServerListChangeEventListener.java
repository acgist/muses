package com.acgist.user.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;

@Component
public class ServerListChangeEventListener extends Subscriber<InstancesChangeEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ServerListChangeEventListener.class);

	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private NacosServiceRegistry nacosServiceRegistry;

	@PostConstruct
	private void post(){
		NotifyCenter.registerSubscriber(this);
	}


	/**
	 * Event callback.
	 *
	 * @param event {@link Event}
	 */
	@Override
	public void onEvent(InstancesChangeEvent event) {
		logger.info("接收到 ServerListChangeEvent 订阅事件：{}", JSON.toJSONString(event));
	}

	/**
	 * Type of this subscriber's subscription.
	 *
	 * @return Class which extends {@link Event}
	 */
	@Override
	public Class<? extends com.alibaba.nacos.common.notify.Event> subscribeType() {
		return InstancesChangeEvent.class;
	}

}
