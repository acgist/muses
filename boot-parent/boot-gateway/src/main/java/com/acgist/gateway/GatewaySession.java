package com.acgist.gateway;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.boot.DateUtils;
import com.acgist.boot.JSONUtils;
import com.acgist.boot.MapUtils;
import com.acgist.boot.pojo.bean.Message;
import com.acgist.boot.pojo.bean.MessageCode;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.RsaService;
import com.acgist.www.ResponseUtils;

/**
 * 请求数据
 * 
 * session：发生异常、请求转发均不会丢失数据（重定向时不丢失数据）
 * request：发生异常、请求转发均不会丢失数据（重定向时会丢失数据）
 * 
 * @author acgist
 */
@Component
@Scope("request")
public class GatewaySession implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final ThreadLocal<GatewaySession> LOCAL = new ThreadLocal<>();
	
	@Autowired
	private RsaService rsaService;
	
	/**
	 * 是否正在处理
	 */
	private boolean process = false;
	/**
	 * 请求标识
	 */
	private Long queryId;
	/**
	 * 网关映射
	 */
	private GatewayMapping gatewayMapping;
	/**
	 * 请求数据
	 */
	private String requestJSON;
	/**
	 * 请求数据
	 */
	private GatewayRequest gatewayRequest;
	/**
	 * 响应数据
	 */
	private Message<Map<String, Object>> gatewayResponse;
	/**
	 * 响应数据
	 */
	private final Map<String, Object> responseData = new HashMap<>();

	public static final GatewaySession getInstance() {
		return LOCAL.get();
	}
	
	public static final GatewaySession getInstance(ApplicationContext context) {
		final GatewaySession session = context.getBean(GatewaySession.class);
		LOCAL.set(session);
		return session;
	}

	/**
	 * 创建请求
	 * 
	 * @param queryId 请求标识
	 * 
	 * @return 是否创建成功
	 */
	public boolean buildProcess(Long queryId) {
		synchronized (this) {
			if (this.process) {
				return false;
			} else {
				this.process = true;
				this.queryId = queryId;
				return true;
			}
		}
	}

	/**
	 * 完成请求
	 */
	public void completeProcess() {
		synchronized (this) {
			this.process = false;
			this.queryId = null;
			this.requestJSON = null;
			this.gatewayRequest = null;
			this.gatewayMapping = null;
			this.responseData.clear();
		}
	}

	/**
	 * 判断是否记录网关
	 * 
	 * @return 是否记录
	 */
	public boolean record() {
		return this.gatewayMapping != null && this.gatewayMapping.isRecord();
	}
	
	/**
	 * 创建失败响应
	 * 
	 * @param code 状态码
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> buildFail(MessageCode code) {
		return this.buildFail(code, code.getMessage());
	}
	
	/**
	 * 创建失败响应
	 * 
	 * @param code 状态编码
	 * @param message 状态描述
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> buildFail(MessageCode code, String message) {
		this.buildResponse();
		this.gatewayResponse = Message.fail(code, message, this.responseData);
		final String signature = this.rsaService.signature(this.responseData);
		this.gatewayResponse.setSignature(signature);
		return this.gatewayResponse;
	}

	/**
	 * 创建成功响应
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> buildSuccess() {
		return this.buildSuccess(null);
	}
	
	/**
	 * 创建成功响应
	 * 
	 * @param response 响应数据
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> buildSuccess(Map<String, Object> response) {
		if(MapUtils.isNotEmpty(response)) {
			this.responseData.putAll(response);
		}
		this.buildResponse();
		this.gatewayResponse = Message.success(this.responseData);
		final String signature = this.rsaService.signature(this.responseData);
		this.gatewayResponse.setSignature(signature);
		return this.gatewayResponse;
	}
	
	/**
	 * 获取请求ID
	 * 
	 * @return 请求ID
	 */
	public Long getQueryId() {
		return queryId;
	}

	/**
	 * 获取请求JSON
	 * 
	 * @return 请求JSON
	 */
	public String getRequestJSON() {
		return this.requestJSON;
	}
	
	public void setRequestJSON(String requestJSON) {
		this.requestJSON = requestJSON;
	}
	
	public GatewayRequest getGatewayRequest() {
		return gatewayRequest;
	}

	public void setGatewayRequest(GatewayRequest gatewayRequest) {
		this.gatewayRequest = gatewayRequest;
	}

	public void setGatewayMapping(GatewayMapping gatewayMapping) {
		this.gatewayMapping = gatewayMapping;
	}

	/**
	 * 获取响应JSON
	 * 
	 * @return 响应JSON
	 */
	public String getResponseJSON() {
		return JSONUtils.toJSON(this.gatewayResponse);
	}
	
	/**
	 * 设置响应内容
	 * 
	 * @param key key
	 * @param value value
	 * 
	 * @return this
	 */
	public GatewaySession putResponse(String key, Object value) {
		this.responseData.put(key, value);
		return this;
	}
	
	/**
	 * 创建响应数据
	 */
	public void buildResponse() {
		this.responseData.put(Gateway.PROPERTY_QUERY_ID, this.queryId);
		if(this.gatewayRequest != null) {
			this.responseData.put(Gateway.PROPERTY_RESERVED, this.gatewayRequest.getReserved());
		}
		this.responseData.put(Gateway.PROPERTY_RESP_TIME, DateUtils.buildTime());
	}
	
	/**
	 * 写出响应数据
	 * 
	 * @param response 响应
	 */
	public void response(HttpServletResponse response) {
	    ResponseUtils.response(this.gatewayResponse, response);
	}
	
}
