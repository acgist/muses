package com.acgist.web;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.HTTPUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebTest {

	@Test
	public void testException() {
		log.info("{}", HTTPUtils.get("http://localhost:8888/404"));
		log.info("{}", HTTPUtils.get("http://localhost:8888/exception/know"));
		log.info("{}", HTTPUtils.get("http://localhost:8888/exception/unknow"));
		log.info("{}", HTTPUtils.get("http://localhost:8888/exception/post"));
		log.info("{}", HTTPUtils.post("http://localhost:8888/exception/post", ""));
		log.info("{}", HTTPUtils.post("http://localhost:8888/exception/know", ""));
		log.info("{}", HTTPUtils.post("http://localhost:8888/exception/know", Map.of()));
	}
	
}
