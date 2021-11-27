package com.acgist.gateway.request.pay;

import javax.validation.constraints.NotBlank;

import com.acgist.gateway.executor.pay.DrawbackExecutor;
import com.acgist.gateway.request.GatewayRequest;

/**
 * <p>请求 - 交易退款</p>
 * 
 * @author acgist
 */
public class DrawbackRequest extends GatewayRequest<DrawbackExecutor> {

	private static final long serialVersionUID = 1L;
	
	public DrawbackRequest() {
		super(DrawbackExecutor.class);
	}

	/**
	 * <p>交易订单编号</p>
	 */
	@NotBlank(message = "交易订单编号不能为空")
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
