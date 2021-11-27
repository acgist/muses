package com.acgist.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.rest.GatewaySession;

/**
 * @author acgist
 * 
 * TODO：MQ：保存推送
 */
@Service
public class GatewayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);

	/**
	 * 保存网关
	 * 
	 * @param session 网关
	 */
	public void record(GatewaySession session) {
		LOGGER.info("保存网关：{}", session.getQueryId());
	}

}
