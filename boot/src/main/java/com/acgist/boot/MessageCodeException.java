package com.acgist.boot;

import com.acgist.boot.pojo.bean.MessageCode;

/**
 * 状态编码异常
 * 
 * @author acgist
 */
public class MessageCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final MessageCode code;

	public static final MessageCodeException of(Object ... messages) {
		return of(null, MessageCode.CODE_9999, messages);
	}
	
	public static final MessageCodeException of(Throwable t, Object ... messages) {
		return of(t, MessageCode.CODE_9999, messages);
	}
	
	public static final MessageCodeException of(Throwable t, MessageCode code, Object ... messages) {
		final StringBuilder builder = new StringBuilder();
		for (Object message : messages) {
			builder.append(message);
		}
		if(t == null) {
			return new MessageCodeException(code, builder.toString());
		} else {
			return new MessageCodeException(code, builder.toString(), t);
		}
	}
	
	public MessageCodeException(MessageCode code) {
		this(code, code.getMessage());
	}

	public MessageCodeException(MessageCode code, String message) {
		super(message);
		this.code = code;
	}

	public MessageCodeException(MessageCode code, Throwable throwable) {
		this(code, code.getMessage(), throwable);
	}

	public MessageCodeException(MessageCode code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public MessageCode getCode() {
		return this.code;
	}

	public String getMessage() {
		if (StringUtils.isEmpty(super.getMessage())) {
			return this.code.getMessage();
		} else {
			return super.getMessage();
		}
	}

}
