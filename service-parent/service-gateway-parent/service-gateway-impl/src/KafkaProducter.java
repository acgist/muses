package com.example.demo;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducter {

	@Autowired
	Consumer<Message> gatewayPush;
	
	@PostConstruct
	public void init() {
		new Thread(() -> {
			while (true) {
				if (this.gatewayPush != null) {
					this.gatewayPush.accept(new Message());
				}
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}).start();
	}

	public static class Message implements Serializable {

		private static final long serialVersionUID = 1L;

		private String name = System.currentTimeMillis() + "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
