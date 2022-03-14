package com.acgist.gateway.model.entity;

import com.acgist.model.entity.BootEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 网关报文
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_gateway")
public class GatewayEntity extends BootEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * QueryId
	 */
	@TableField(value = "query_id")
	private Long queryId;
	/**
	 * 请求
	 */
	@TableField(value = "request")
	private String request;
	/**
	 * 响应
	 */
	@TableField(value = "response")
	private String response;
	
}
