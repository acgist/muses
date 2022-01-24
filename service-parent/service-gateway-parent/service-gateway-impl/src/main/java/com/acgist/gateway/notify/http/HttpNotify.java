package com.acgist.gateway.notify.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.gateway.notify.Notify;

/**
 * HTTP通知
 * 
 * @author acgist
 */
public class HttpNotify extends Notify {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpNotify.class);
	
	@Autowired
	private HttpNotifyConfig config;

	public HttpNotify(HttpNotifyConfig config) {
		super(config);
	}
	
	@Override
	public String execute() {
		LOGGER.debug("超时时间：{}", this.config.getTimeout());
		return null;
	}

}
