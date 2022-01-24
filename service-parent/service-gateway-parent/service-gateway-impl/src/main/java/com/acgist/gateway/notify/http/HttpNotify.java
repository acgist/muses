package com.acgist.gateway.notify.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.gateway.notify.Notify;
import com.acgist.gateway.pojo.entity.GatewayEntity;

/**
 * HTTP通知
 * 
 * @author acgist
 */
public class HttpNotify extends Notify {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpNotify.class);
	
//	@Autowired
//	private HttpNotifyConfig config;

	public HttpNotify(HttpNotifyConfig config) {
		super(config);
	}
	
	@Override
	public String execute(GatewayEntity gatewayEntity) {
		LOGGER.debug("发送通知：{}", gatewayEntity);
		return null;
	}

}
