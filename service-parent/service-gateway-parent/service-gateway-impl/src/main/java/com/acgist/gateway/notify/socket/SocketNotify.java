package com.acgist.gateway.notify.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.gateway.notify.Notify;

/**
 * Socket通知
 * 
 * @author acgist
 */
public class SocketNotify extends Notify {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketNotify.class);

	@Autowired
	private SocketNotifyConfig config;

	public SocketNotify(SocketNotifyConfig config) {
		super(config);
	}

	@Override
	public String execute() {
		LOGGER.debug("超时时间：{}", this.config.getTimeout());
		return null;
	}

}
