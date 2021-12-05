package com.acgist.boot;

/**
 * 响应状态编码
 * 
 * 1xxx=请求错误
 * 2xxx=服务错误
 * 3xxx=HTTP错误
 * 9999=未知错误
 * 
 * @author acgist
 */
public enum MessageCode {

	CODE_0000("0000", "成功"),
	CODE_1000("1000", "未知接口"),
	CODE_1001("1001", "上次请求没有完成"),
	CODE_1002("1002", "数据格式错误"),
	CODE_1003("1003", "验签失败"),
	CODE_1004("1004", "没有登陆"),
	CODE_3404("3404", "资源失效"),
	CODE_9999("9999", "未知错误");
	
	/**
	 * 状态编码
	 */
	private final String code;
	/**
	 * 状态信息
	 */
	private final String message;

	private MessageCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static final MessageCode of(String code) {
		final MessageCode[] codes = MessageCode.values();
		for (MessageCode value : codes) {
			if(value.code.equals(code)) {
				return value;
			}
		}
		return CODE_9999;
	}
	
	public static final MessageCode of(int code) {
		final String httpCode = "3" + code;
		final MessageCode[] codes = MessageCode.values();
		for (MessageCode value : codes) {
			if(value.code.equals(httpCode)) {
				return value;
			}
		}
		return CODE_9999;
	}
	
	public static final MessageCode of(String code, int status) {
		MessageCode messageCode = of(code);
		if(messageCode != null && messageCode != CODE_9999) {
			return messageCode;
		}
		messageCode = of(status);
		if(messageCode != null && messageCode != CODE_9999) {
			return messageCode;
		}
		return CODE_9999;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
