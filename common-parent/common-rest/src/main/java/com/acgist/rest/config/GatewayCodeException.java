package com.acgist.rest.config;

public class GatewayCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GatewayCodeException(GatewayCode code) {
		this(code, code.getMessage());
	}

	public GatewayCodeException(GatewayCode code, String message) {
		super(message);
		this.code = code;
	}

	public GatewayCodeException(GatewayCode code, Throwable throwable) {
		this(code, code.getMessage(), throwable);
	}

	public GatewayCodeException(GatewayCode code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	private final GatewayCode code;

	public GatewayCode getCode() {
		return code;
	}

}
