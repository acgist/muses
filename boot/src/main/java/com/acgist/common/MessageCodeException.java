package com.acgist.common;

public class MessageCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

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

	private final MessageCode code;

	public MessageCode getCode() {
		return code;
	}

}
