package com.acgist.gateway.notify.http;

import com.acgist.gateway.model.entity.GatewayEntity;
import com.acgist.gateway.notify.Notify;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP通知
 * 
 * @author acgist
 */
@Slf4j
public class HttpNotify extends Notify {

//	@Autowired
//	private HttpNotifyConfig config;

	public HttpNotify(HttpNotifyConfig config) {
		super(config);
	}
	
	@Override
	public String execute(GatewayEntity gatewayEntity) {
		log.debug("发送通知：{}", gatewayEntity);
		return null;
	}

}
