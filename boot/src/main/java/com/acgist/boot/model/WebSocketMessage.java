package com.acgist.boot.model;

import lombok.Setter;

import lombok.Getter;

/**
 * WebSocket消息
 * 
 * @author acgist
 */
@Getter
@Setter
public class WebSocketMessage {

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
	 * 消息
	 */
	private Object message;
	
}
