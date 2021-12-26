package com.acgist.boot.pojo.bean;

/**
 * 状态编码
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
	CODE_3400("3400", "请求错误"),
	CODE_3401("3401", "没有授权"),
	CODE_3403("3403", "请求拒绝"),
	CODE_3404("3404", "资源失效"),
	CODE_3405("3405", "请求方法错误"),
	CODE_3500("3500", "系统异常"),
	CODE_3502("3502", "服务无效"),
	CODE_3503("3503", "服务正在维护"),
	CODE_3504("3504", "服务超时"),
	CODE_9999("9999", "未知错误");
	
	/**
	 * HTTP状态编码头部
	 */
	public static final String HTTP_STATUS = "3";

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

	/**
	 * 通过code获取状态编码
	 * 
	 * @param code code
	 * 
	 * @return 状态编码
	 */
	public static final MessageCode of(String code) {
		final MessageCode[] codes = MessageCode.values();
		for (MessageCode value : codes) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return CODE_9999;
	}

	/**
	 * 通过HTTP Status获取状态编码
	 * 
	 * @param status HTTP Sttus
	 * 
	 * @return 状态编码
	 */
	public static final MessageCode of(int status) {
		final String code = HTTP_STATUS + status;
		final MessageCode[] codes = MessageCode.values();
		for (MessageCode value : codes) {
			if (value.code.equals(code)) {
				return value;
			}
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
