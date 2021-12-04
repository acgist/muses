package com.acgist.boot;

import java.io.Serializable;

/**
 * 响应消息
 * 
 * @author acgist
 */
public class Message<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 响应编码
	 */
	private String code;
	/**
	 * 响应描述
	 */
	private String message;
	/**
	 * 消息内容
	 */
	private T body;
	/**
	 * 数据签名
	 */
	private String signature;

	public static final <T> Message<T> success() {
		return success(null);
	}
	
	/**
	 * 成功消息
	 * 
	 * @param <T> 消息类型
	 * @param body 消息内容
	 * 
	 * @return 成功消息
	 */
	public static final <T> Message<T> success(T body) {
		final Message<T> message = new Message<>();
		message.code = MessageCode.CODE_0000.getCode();
		message.message = MessageCode.CODE_0000.getMessage();
		message.body = body;
		return message;
	}

	public static final <T> Message<T> fail(MessageCode code) {
		return fail(code, null);
	}
	
	/**
	 * 错误消息
	 * 
	 * @param <T> 消息类型
	 * @param code 错误编码
	 * @param message 错误描述
	 * 
	 * @return 错误消息
	 */
	public static final <T> Message<T> fail(MessageCode code, String message) {
		final Message<T> failMessage = new Message<>();
		failMessage.code = code.getCode();
		if(StringUtils.isEmpty(message)) {
			failMessage.message = code.getMessage();
		} else {
			failMessage.message = message;
		}
		return failMessage;
	}
	
	/**
	 * 错误消息
	 * 
	 * @param <T> 消息类型
	 * @param code 错误编码
	 * @param body 消息内容
	 * 
	 * @return 错误消息
	 */
	public static final <T> Message<T> fail(MessageCode code, T body) {
		final Message<T> message = new Message<>();
		message.code = code.getCode();
		message.message = code.getMessage();
		message.body = body;
		return message;
	}
	
	/**
	 * 错误消息
	 * 
	 * @param <T> 消息类型
	 * @param code 错误编码
	 * @param message 响应描述
	 * @param body 消息内容
	 * 
	 * @return 错误消息
	 */
	public static final <T> Message<T> fail(MessageCode code, String message, T body) {
		final Message<T> failMessage = new Message<>();
		failMessage.code = code.getCode();
		failMessage.message = message;
		failMessage.body = body;
		return failMessage;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
