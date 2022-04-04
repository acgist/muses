package com.acgist.gateway.notify.socket;

import com.acgist.gateway.model.entity.GatewayEntity;
import com.acgist.gateway.notify.Notify;

import lombok.extern.slf4j.Slf4j;

/**
 * Socket通知
 * 
 * @author acgist
 */
@Slf4j
public class SocketNotify extends Notify {
	
//	@Autowired
//	private SocketNotifyConfig config;

	public SocketNotify(SocketNotifyConfig config) {
		super(config);
	}

	@Override
	public String execute(GatewayEntity gatewayEntity) {
		log.debug("发送通知：{}", gatewayEntity);
		return null;
	}

}
