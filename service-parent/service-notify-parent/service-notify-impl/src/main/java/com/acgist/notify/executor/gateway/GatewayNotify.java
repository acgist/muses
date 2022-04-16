package com.acgist.notify.executor.gateway;

import com.acgist.notify.executor.Notify;
import com.acgist.notify.gateway.model.entity.GatewayEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Gateway通知
 * 
 * @author acgist
 */
@Slf4j
public class GatewayNotify extends Notify<GatewayEntity, GatewayNotifyConfig> {

	public GatewayNotify(GatewayNotifyConfig config) {
		super(GatewayEntity.class, config);
	}
	
	@Override
	public boolean execute(GatewayEntity gatewayEntity) {
		log.debug("发送Gateway通知：{}", gatewayEntity);
		// TODO：注意IP地址防止通知内网服务
		return true;
	}

}
