package com.acgist.gateway.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 更新
 * 
 * @author acgist
 */
public class SetMemoRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户备注
	 */
	@Length(max = 64, message = "用户备注最长64字符")
	@NotNull(message = "用户备注不能为空")
	private String memo;
	/**
	 * 通知地址
	 */
	private String notice;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
