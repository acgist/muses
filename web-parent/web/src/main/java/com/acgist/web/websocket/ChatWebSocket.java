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

	@OnMessage
	public void message(String message, Session session) {
		for (Session client : SESSIONS.values()) {
			if (client.getId().equals(session.getId())) {
				continue;
			}
			client.getAsyncRemote().sendText(message);
		}
	}

	@OnOpen
	public void open(Session session) {
		SESSIONS.put(session.getId(), session);
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
