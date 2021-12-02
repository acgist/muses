package com.acgist.boot;

/**
 * 响应消息
 * 
 * @author acgist
 */
public class Message<T> {

	/**
	 * 响应编码
	 */
	private MessageCode code;
	/**
	 * 响应描述
	 */
	private String message;
	/**
	 * 消息内容
	 */
	private T body;

	public MessageCode getCode() {
		return code;
	}

	public void setCode(MessageCode code) {
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

}
