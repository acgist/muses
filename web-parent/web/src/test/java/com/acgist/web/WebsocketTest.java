package com.acgist.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketTest {

	@Test
	public void testJdk() {
		final WebSocket webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.buildAsync(URI.create(""), new Listener() {
				@Override
				public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
					// 注意：即是处理也要调用上级方法
					return Listener.super.onText(webSocket, data, last);
				}
			}).join();
		assertNotNull(webSocket);
	}

	@Test
	public void testClient() throws DeploymentException, IOException {
		final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		final Client client = new Client();
		container.connectToServer(client, URI.create(""));
	}

	@ClientEndpoint
	class Client {

		private Session session;

		@OnOpen
		public void open(Session session) {
			this.session = session;
		}

		@OnMessage
		public void onMessage(Session session, String message) {
			log.info("消息：{}", message);
		}

		@OnClose
		public void onClose(Session session) {
		}
		
		@OnError
		public void onError(Session session, Throwable throwable) {
		}
		
		public void send(String message) {
			this.session.getAsyncRemote().sendText(message);
//			this.session.getBasicRemote().sendText(message);
		}
		
	}

}
