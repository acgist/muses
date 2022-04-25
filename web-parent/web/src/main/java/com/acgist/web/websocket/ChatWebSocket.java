package com.acgist.web.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * 聊天室
 * 
 * @author acgist
 */
@Component
@ServerEndpoint("/chat.socket")
public class ChatWebSocket {

	/**
	 * 聊天室
	 */
	private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
	
	@OnOpen
	public void open(Session session) {
		// 长连接
		session.setMaxIdleTimeout(0);
		SESSIONS.put(session.getId(), session);
	}

	@OnMessage
	public void message(Session session, String message) {
		SESSIONS.values().forEach(client -> {
			if (client.getId().equals(session.getId())) {
				return;
			}
			client.getAsyncRemote().sendText(message);
		});
	}

	@OnClose
	public void close(Session session) {
		SESSIONS.remove(session.getId());
	}

	@OnError
	public void error(Session session, Throwable e) {
		SESSIONS.remove(session.getId());
	}

}
