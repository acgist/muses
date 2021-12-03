package com.acgist.boot;

/**
 * 错误编码异常
 * 
 * @author acgist
 */
public class MessageCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final MessageCode code;

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
		if(StringUtils.isEmpty(super.getMessage())) {
			return this.code.getMessage();
		} else {
			return super.getMessage();
		}
	}

}
