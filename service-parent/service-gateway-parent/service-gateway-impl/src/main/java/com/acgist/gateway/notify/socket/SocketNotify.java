package com.acgist.gateway.notify.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.gateway.model.entity.GatewayEntity;
import com.acgist.gateway.notify.Notify;

/**
 * Socket通知
 * 
 * @author acgist
 */
public class SocketNotify extends Notify {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketNotify.class);

//	@Autowired
//	private SocketNotifyConfig config;

	public SocketNotify(SocketNotifyConfig config) {
		super(config);
	}

	@Override
	public String execute(GatewayEntity gatewayEntity) {
		LOGGER.debug("发送通知：{}", gatewayEntity);
		return null;
	}

}
