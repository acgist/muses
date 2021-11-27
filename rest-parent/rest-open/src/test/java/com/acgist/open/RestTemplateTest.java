package com.acgist.open;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.acgist.main.OpenApplication;

@SpringBootTest(classes = OpenApplication.class)
public class RestTemplateTest {

	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	public void testHttp() {
		assertNotNull(this.restTemplate);
		System.out.println(this.restTemplate.getForEntity("https://www.acgist.com", String.class));
	}
	
}
