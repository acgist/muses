package com.acgist.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class RetryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryApplication.class, args);
	}

	@Slf4j
	@org.springframework.stereotype.Service
	public static class Service {
		
		@Retryable(value = Exception.class, maxAttempts = 10, backoff = @Backoff(delay = 1000, maxDelay = 5000, multiplier = 2))
		public void retry() {
			log.info("{}", 1 / 0);
		}
		
	}
	
}
