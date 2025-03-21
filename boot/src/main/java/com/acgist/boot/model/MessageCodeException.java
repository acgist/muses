package com.acgist.boot.model;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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

	/**
	 * @param messages 错误消息
	 * 
	 * @return 状态编码异常
	 */
	public static final MessageCodeException of(Object ... messages) {
		return of(null, MessageCode.CODE_9999, messages);
	}
	
	/**
	 * @param t 异常
	 * @param messages 错误消息
	 * 
	 * @return 状态编码异常
	 */
	public static final MessageCodeException of(Throwable t, Object ... messages) {
		return of(t, MessageCode.CODE_9999, messages);
	}
	
	/**
	 * @param code 状态编码
	 * @param messages 错误消息
	 * 
	 * @return 状态编码异常
	 */
	public static final MessageCodeException of(MessageCode code, Object ... messages) {
		return of(null, code, messages);
	}
	
	/**
	 * @param t 异常
	 * @param code 状态编码
	 * @param messages 错误消息
	 * 
	 * @return 状态编码异常
	 */
	public static final MessageCodeException of(Throwable t, MessageCode code, Object ... messages) {
		final String message;
		if(ArrayUtils.isEmpty(messages)) {
			message = Objects.isNull(t) ? code.getMessage() : t.getMessage();
		} else {
			// 拼接错误描述
			final StringBuilder builder = new StringBuilder();
			for (Object value : messages) {
				builder.append(value);
			}
			message = builder.toString();
		}
		return new MessageCodeException(code, message, t);
	}

	/**
	 * @param code 状态编码
	 */
	public MessageCodeException(MessageCode code) {
		this(code, code.getMessage());
	}

	/**
	 * @param code 状态编码
	 * @param message 错误消息
	 */
	public MessageCodeException(MessageCode code, String message) {
		this(code, message, null);
	}

	/**
	 * @param code 状态编码
	 * @param t 异常
	 */
	public MessageCodeException(MessageCode code, Throwable t) {
		this(code, Objects.isNull(t) ? code.getMessage() : t.getMessage(), t);
	}

	/**
	 * @param code 状态编码
	 * @param message 错误消息
	 * @param t 异常
	 */
	public MessageCodeException(MessageCode code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}

	/**
	 * @return 状态编码
	 */
	public MessageCode getCode() {
		return this.code;
	}
	
    /**
     * @param result  是否抛出异常
     * @param message 异常描述信息
     */
    public static final void check(boolean result, String message) {
        if(result) {
            throw MessageCodeException.of(message);
        }
    }
    
    /**
     * 空值抛出异常
     * 
     * @param object  数据对象
     * @param message 异常描述信息
     */
    public static final void nullThrow(Object object, String message) {
        MessageCodeException.check(object == null, message);
    }
    
    /**
     * 非空抛出异常
     * 
     * @param object  数据对象
     * @param message 异常描述信息
     */
    public static final void nonNullThrow(Object object, String message) {
        MessageCodeException.check(object != null, message);
    }
    
    /**
     * 相等抛出异常
     * 
     * @param source  原始数据
     * @param target  比较数据
     * @param message 异常描述信息
     */
    public static final void equalThrow(Object source, Object target, String message) {
        MessageCodeException.check(Objects.equals(source, target), message);
    }
    
    /**
     * 不等抛出异常
     * 
     * @param source  原始数据
     * @param target  比较数据
     * @param message 异常描述信息
     */
    public static final void notEqualThrow(Object source, Object target, String message) {
        MessageCodeException.check(!Objects.equals(source, target), message);
    }

	@Override
	public String getMessage() {
		final String message = super.getMessage();
		if (StringUtils.isEmpty(message)) {
			return this.code.getMessage();
		} else {
			return message;
		}
	}

}
