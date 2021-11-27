package com.acgist.gateway;

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

import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayService;
import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.DateUtils;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.ValidatorUtils;

/**
 * <p>请求数据</p>
 * <p>session：发生异常、请求转发均不会丢失数据（重定向时不丢失数据）</p>
 * <p>request：发生异常、请求转发均不会丢失数据（重定向时会丢失数据）</p>
 * 
 * @author acgist
 */
@Component
@Scope("request")
public class GatewaySession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySession.class);

	@Autowired
	private ApplicationContext context;
	@Autowired
	private SignatureService signatureService;
	
	/**
	 * <p>是否正在处理</p>
	 */
	private boolean process = false;
	/**
	 * <p>请求标识</p>
	 */
	private String queryId;
	/**
	 * <p>请求数据</p>
	 */
	private Gateway request;
	/**
	 * <p>网关映射</p>
	 */
	private GatewayMapping gatewayMapping;
	/**
	 * <p>请求数据</p>
	 */
	private Map<String, Object> requestData;
	/**
	 * <p>响应数据</p>
	 */
	private final Map<String, Object> responseData = new HashMap<>();;

	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}

	/**
	 * <p>创建新请求</p>
	 * 
	 * @param queryId 请求标识
	 * 
	 * @return 是否创建成功
	 */
	public boolean buildProcess(String queryId) {
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
	 * <p>完成请求</p>
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
	 * <p>判断是否记录网关</p>
	 * 
	 * @return 是否记录
	 */
	public boolean record() {
		return this.gatewayMapping != null && this.gatewayMapping.getRecord();
	}
	
	/**
	 * <p>数据格式校验</p>
	 * 
	 * @return 错误信息
	 */
	public String validator() {
		return ValidatorUtils.verify(this.request);
	}
	
	/**
	 * <p>验签</p>
	 * 
	 * @return 是否验签成功
	 */
	public boolean verifySignature() {
		return this.signatureService.verify(this.requestData);
	}
	
	/**
	 * <p>创建失败响应</p>
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
	 * <p>创建失败响应</p>
	 * 
	 * @param code 状态码
	 * @param message 状态描述
	 * 
	 * @return this
	 */
	public GatewaySession buildFail(GatewayCode code, String message) {
		this.responseData.put(GatewayService.GATEWAY_CODE, code.getCode());
		this.responseData.put(GatewayService.GATEWAY_MESSAGE, message == null ? code.getMessage() : message);
		return this;
	}

	/**
	 * <p>创建成功响应</p>
	 * 
	 * @return this
	 */
	public GatewaySession buildSuccess() {
		return this.buildSuccess(null);
	}
	
	/**
	 * <p>创建成功响应</p>
	 * 
	 * @param response 响应数据
	 * 
	 * @return this
	 */
	public GatewaySession buildSuccess(Map<String, Object> response) {
		if(response != null) {
			this.responseData.putAll(response);
		}
		this.responseData.put(GatewayService.GATEWAY_CODE, GatewayCode.CODE_0000.getCode());
		this.responseData.put(GatewayService.GATEWAY_MESSAGE, GatewayCode.CODE_0000.getMessage());
		return this;
	}

	/**
	 * <p>写出响应数据</p>
	 * 
	 * @return 响应
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> response() {
		final GatewayRequest<Executor> request = (GatewayRequest<Executor>) this.request;
		final Executor executor = (Executor) this.context.getBean(request.getExecutorClass());
		return executor.response();
	}
	
	/**
	 * <p>写出响应数据</p>
	 * 
	 * @param response 响应
	 */
	public void response(HttpServletResponse response) {
		this.buildResponse();
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getOutputStream().write(JSONUtils.serialize(this.responseData).getBytes());
		} catch (IOException e) {
			LOGGER.error("写出响应数据异常", e);
		}
	}
	
	/**
	 * <p>获取请求内容</p>
	 * 
	 * @param key key
	 * 
	 * @return 请求内容
	 */
	public Object getRequest(String key) {
		return this.requestData.get(key);
	}
	
	/**
	 * <p>获取响应内容</p>
	 * 
	 * @param key key
	 * 
	 * @return 响应内容
	 */
	public Object getResponse(String key) {
		return this.responseData.get(key);
	}
	
	/**
	 * <p>获取请求JSON</p>
	 * 
	 * @return 请求JSON
	 */
	public String getRequestJSON() {
		return JSONUtils.serialize(this.requestData);
	}
	
	/**
	 * <p>获取响应JSON</p>
	 * 
	 * @return 响应JSON
	 */
	public String getResponseJSON() {
		return JSONUtils.serialize(this.responseData);
	}
	
	/**
	 * <p>设置请求内容</p>
	 * <p>请求内容不建议修改</p>
	 * 
	 * @param key key
	 * @param value value
	 * 
	 * @return this
	 */
	@Deprecated
	public GatewaySession putRequest(String key, Object value) {
		this.requestData.put(key, value);
		return this;
	}
	
	/**
	 * <p>设置响应内容</p>
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
	 * <p>创建响应数据</p>
	 * <p>最终返回响应数据时调用，调用后不允许在设置响应数据。</p>
	 * 
	 * @return this
	 */
	public GatewaySession buildResponse() {
		this.responseData.put(GatewayService.GATEWAY_QUERY_ID, this.queryId);
		this.responseData.put(GatewayService.GATEWAY_RESPONSE_TIME, DateUtils.buildTime());
		this.convertRequestToResponse();
		this.signatureService.signature(this.responseData);
		return this;
	}
	
	/**
	 * <p>请求数据原样返回响应数据</p>
	 */
	private void convertRequestToResponse() {
		if(this.requestData != null) {
			this.requestData.forEach((key, value) -> this.responseData.computeIfAbsent(key, x -> value));
		}
	}
	
	public String getQueryId() {
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

	public Map<String, Object> getResponseData() {
		return this.responseData;
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
