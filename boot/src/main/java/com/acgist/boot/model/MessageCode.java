package com.acgist.boot.model;

import lombok.Getter;

/**
 * 状态编码
 * 
 * 1xxx=前置错误：拦截器的错误（数据校验）
 * 2xxx=服务错误：服务调用错误（本地服务调用、远程服务调用）
 * 3xxx=HTTP错误：系统异常
 * 4xxx=数据库的错误（唯一约束、外键约束）
 * 9999=未知错误：没有适配异常
 * 
 * @author acgist
 */
@Getter
public enum MessageCode {
	
	CODE_0000("0000", 200, "成功"),
	CODE_1000("1000", 404, "未知接口"),
	CODE_1001("1001", 400, "上次请求没有完成"),
	CODE_1002("1002", 400, "数据格式错误"),
	CODE_1003("1003", 400, "验签失败"),
	CODE_3400("3400", 400, "请求错误"),
	CODE_3401("3401", 401, "没有授权"),
	CODE_3403("3403", 403, "请求拒绝"),
	CODE_3404("3404", 404, "资源失效"),
	CODE_3405("3405", 405, "请求方法错误"),
	CODE_3406("3406", 406, "请求不可接受"),
	CODE_3415("3415", 415, "请求资源类型错误"),
	CODE_3500("3500", 500, "系统异常"),
	CODE_3502("3502", 502, "服务无效"),
	CODE_3503("3503", 503, "服务正在维护"),
	CODE_3504("3504", 504, "服务超时"),
	CODE_4001("4001", 400, "重复标识"),
	CODE_9999("9999", 500, "未知错误");
	
	/**
	 * HTTP状态编码头部
	 */
	public static final String HTTP_STATUS = "3";

	/**
	 * 状态编码
	 */
	private final String code;
	/**
	 * 状态数值
	 */
	private final Integer status;
	/**
	 * 状态信息
	 */
	private final String message;

	private MessageCode(String code, Integer status, String message) {
		this.code = code;
		this.status = status;
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
		final MessageCode[] values = MessageCode.values();
		for (MessageCode value : values) {
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
		final MessageCode[] values = MessageCode.values();
		for (MessageCode value : values) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return of(String.valueOf(status));
	}
	
}
