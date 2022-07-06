package com.acgist.boot.model;

import lombok.Getter;
import lombok.Setter;

/**
 * WebSocket消息
 * 
 * @author acgist
 * 
 * @param <T> 消息类型
 */
@Getter
@Setter
public class WebSocketMessage<T> extends Model {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 消息类型
	 * 
	 * @author acgist
	 */
	public enum Type {
		
	}

	/**
	 * 消息类型
	 */
	private Type type;
	/**
	 * 消息内容
	 */
	private T message;
	
}
