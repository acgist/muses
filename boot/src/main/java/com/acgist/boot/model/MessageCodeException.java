package com.acgist.boot.model;

import java.util.Objects;

import com.acgist.boot.utils.ArrayUtils;
import com.acgist.boot.utils.StringUtils;

/**
 * 状态编码异常
 * 
 * @author acgist
 */
public class MessageCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态编码
	 */
	private final MessageCode code;

	public static final MessageCodeException of(Object ... messages) {
		return of(null, MessageCode.CODE_9999, messages);
	}
	
	public static final MessageCodeException of(Throwable t, Object ... messages) {
		return of(t, MessageCode.CODE_9999, messages);
	}
	
	public static final MessageCodeException of(MessageCode code, Object ... messages) {
		return of(null, code, messages);
	}
	
	public static final MessageCodeException of(Throwable t, MessageCode code, Object ... messages) {
		final String message;
		if(ArrayUtils.isEmpty(messages)) {
			message = Objects.isNull(t) ? code.getMessage() : t.getMessage();
		} else {
			final StringBuilder builder = new StringBuilder();
			for (Object value : messages) {
				builder.append(value);
			}
			message = builder.toString();
		}
		if(Objects.isNull(t)) {
			return new MessageCodeException(code, message);
		} else {
			return new MessageCodeException(code, message, t);
		}
	}

	/**
	 * 获取底层异常
	 * 
	 * @param t 异常
	 * 
	 * @return 底层异常
	 */
	public static final Throwable root(Throwable t) {
		Throwable cause = t;
		do {
			// 属于状态编码异常
			if(cause instanceof MessageCodeException) {
				return cause;
			}
		} while(cause != null && (cause = cause.getCause()) != null);
		// 返回原始异常
		return t;
	}
	
	public MessageCodeException(MessageCode code) {
		this(code, code.getMessage());
	}

	public MessageCodeException(MessageCode code, String message) {
		super(message);
		this.code = code;
	}

	public MessageCodeException(MessageCode code, Throwable t) {
		this(code, Objects.isNull(t) ? code.getMessage() : t.getMessage(), t);
	}

	public MessageCodeException(MessageCode code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}

	public MessageCode getCode() {
		return this.code;
	}

	public String getMessage() {
		final String message = super.getMessage();
		if (StringUtils.isEmpty(message)) {
			return this.code.getMessage();
		} else {
			return message;
		}
	}

}
