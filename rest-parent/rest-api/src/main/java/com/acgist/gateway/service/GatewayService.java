package com.acgist.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.gateway.GatewaySession;

/**
 * <p>Service - 网关</p>
 * 
 * @author acgist
 */
@Service
public class GatewayService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);

	/**
	 * <p>网关请求地址</p>
	 */
	public static final String GATEWAY = "/gateway";
	/**
	 * <p>状态码</p>
	 */
	public static final String GATEWAY_CODE = "code";
	/**
	 * <p>状态信息</p>
	 */
	public static final String GATEWAY_MESSAGE = "message";
	/**
	 * <p>网关地址</p>
	 */
	public static final String GATEWAY_GATEWAY = "gateway";
	/**
	 * <p>网关标识</p>
	 */
	public static final String GATEWAY_QUERY_ID = "queryId";
	/**
	 * <p>签名</p>
	 */
	public static final String GATEWAY_SIGNATURE = "signature";
	/**
	 * <p>通知地址</p>
	 */
	public static final String GATEWAY_NOTICE_URL = "noticeURL";
	/**
	 * <p>响应时间</p>
	 */
	public static final String GATEWAY_RESPONSE_TIME = "responseTime";
	
	/**
	 * <p>保存网关</p>
	 * 
	 * @param session 网关
	 */
	public void record(GatewaySession session) {
		LOGGER.info("保存网关：{}", session.getQueryId());
	}
	
}
