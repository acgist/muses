package com.acgist.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.common.DateUtils;
import com.acgist.common.JSONUtils;
import com.acgist.rest.config.GatewayCode;
import com.acgist.rest.config.GatewayMapping;
import com.acgist.rest.service.SignatureService;

/**
 * 请求数据
 * 
 * <p>session：发生异常、请求转发均不会丢失数据（重定向时不丢失数据）</p>
 * <p>request：发生异常、请求转发均不会丢失数据（重定向时会丢失数据）</p>
 * 
 * @author acgist
 */
@Component
@Scope("request")
public class GatewaySession implements Serializable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySession.class);

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SignatureService signatureService;
	
	/**
	 * 是否正在处理
	 */
	private boolean process = false;
	/**
	 * 请求标识
	 */
	private Long queryId;
	/**
	 * 请求数据
	 */
	private Gateway request;
	/**
	 * 网关映射
	 */
	private GatewayMapping gatewayMapping;
	/**
	 * 请求数据
	 */
	private Map<String, Object> requestData;
	/**
	 * 响应数据
	 */
	private final Map<String, Object> responseData = new HashMap<>();;

	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}

	/**
	 * 创建新请求
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
			this.request = null;
			this.requestData = null;
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
	 * 数据格式校验
	 * 
	 * @return 错误信息
	 */
	public String validator() {
		return ValidatorUtils.verify(this.request);
	}
	
	/**
	 * 验签
	 * 
	 * @return 是否验签成功
	 */
	public boolean verifySignature() {
		return this.signatureService.verify(this.requestData);
	}
	
	/**
	 * 创建失败响应
	 * 
	 * @param code 状态码
	 * 
	 * @return this
	 */
	public GatewaySession buildFail(GatewayCode code) {
		this.buildFail(code, code.getMessage());
		return this;
	}
	
	/**
	 * 创建失败响应
	 * 
	 * @param code 状态编码
	 * @param message 状态描述
	 * 
	 * @return this
	 */
	public GatewaySession buildFail(GatewayCode code, String message) {
		this.responseData.put(Gateway.GATEWAY_CODE, code.getCode());
		this.responseData.put(Gateway.GATEWAY_MESSAGE, message == null ? code.getMessage() : message);
		return this;
	}

	/**
	 * 创建成功响应
	 * 
	 * @return this
	 */
	public GatewaySession buildSuccess() {
		return this.buildSuccess(null);
	}
	
	/**
	 * 创建成功响应
	 * 
	 * @param response 响应数据
	 * 
	 * @return this
	 */
	public GatewaySession buildSuccess(Map<String, Object> response) {
		if(response != null) {
			this.responseData.putAll(response);
		}
		this.responseData.put(Gateway.GATEWAY_CODE, GatewayCode.CODE_0000.getCode());
		this.responseData.put(Gateway.GATEWAY_MESSAGE, GatewayCode.CODE_0000.getMessage());
		return this;
	}

	/**
	 * 获取请求内容
	 * 
	 * @param key key
	 * 
	 * @return 请求内容
	 */
	public Object getRequest(String key) {
		return this.requestData.get(key);
	}
	
	/**
	 * 获取响应内容
	 * 
	 * @param key key
	 * 
	 * @return 响应内容
	 */
	public Object getResponse(String key) {
		return this.responseData.get(key);
	}
	
	/**
	 * 获取请求JSON
	 * 
	 * @return 请求JSON
	 */
	public String getRequestJSON() {
		return JSONUtils.toJSON(this.requestData);
	}
	
	/**
	 * 获取响应JSON
	 * 
	 * @return 响应JSON
	 */
	public String getResponseJSON() {
		return JSONUtils.toJSON(this.responseData);
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
	 * 
	 * @return 响应数据
	 */
	public Map<String, Object> buildResponse() {
		this.responseData.put(Gateway.GATEWAY_QUERY_ID, this.queryId);
		this.responseData.put(Gateway.GATEWAY_RESPONSE_TIME, DateUtils.buildTime());
		// 请求数据原样返回
		if(this.requestData != null) {
			this.requestData.forEach((key, value) -> this.responseData.computeIfAbsent(key, x -> value));
		}
		this.signatureService.signature(this.responseData);
		return this.responseData;
	}
	
	/**
	 * 写出响应数据
	 * 
	 * @param response 响应
	 */
	public void response(HttpServletResponse response) {
		this.buildResponse();
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getOutputStream().write(JSONUtils.toJSON(this.responseData).getBytes());
		} catch (IOException e) {
			LOGGER.error("写出响应数据异常", e);
		}
	}
	
	public Long getQueryId() {
		return queryId;
	}

	public Gateway getRequest() {
		return this.request;
	}
	
	public GatewayMapping getGatewayMapping() {
		return this.gatewayMapping;
	}

	public Map<String, Object> getRequestData() {
		return this.requestData;
	}

	public void setRequest(Gateway request) {
		this.request = request;
	}
	
	public void setGateway(GatewayMapping gatewayMapping) {
		this.gatewayMapping = gatewayMapping;
	}

	public void setRequestData(Map<String, Object> requestData) {
		this.requestData = requestData;
	}
	
}
