package com.acgist.gateway.request.pay;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.acgist.gateway.executor.pay.PayExecutor;
import com.acgist.gateway.request.GatewayRequest;

/**
 * <p>请求 - 交易</p>
 * 
 * @author acgist
 */
public class PayRequest extends GatewayRequest<PayExecutor> {

	private static final long serialVersionUID = 1L;
	
	public PayRequest() {
		super(PayExecutor.class);
	}

	/**
	 * <p>交易金额</p>
	 */
	@NotNull(message = "交易金额不能为空")
	@Min(value = 1, message = "交易金额错误")
	private Long amount;
	/**
	 * <p>交易订单编号</p>
	 */
	@NotBlank(message = "交易订单编号不能为空")
	private String orderId;
	/**
	 * <p>通知地址</p>
	 */
	private String noticeURL;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNoticeURL() {
		return noticeURL;
	}

	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}

}
