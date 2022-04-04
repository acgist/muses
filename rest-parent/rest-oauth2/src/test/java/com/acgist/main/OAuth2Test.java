package com.acgist.main;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.acgist.boot.HTTPUtils;
import com.acgist.boot.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Test {

	public static final String OAUTH2_URL = "http://localhost:9999";
	public static final String GATEWAY_URL = "http://localhost:8888";
	
	@Test
	public void testPassword() {
		log.info("{}", new BCryptPasswordEncoder().encode("123456"));
	}
	
	@Test
	public void testTokenKey() {
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<String> body = restTemplate.getForEntity(OAUTH2_URL + "/oauth2/jwks", String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testCode() {
		final String code = HTTPUtils.get(
			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state",
//			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=https://www.acgist.com",
			null,
			null,
			1000
		);
		log.info(code);
	}
	
	@Test
	public void getTokenAuthorizationCode() {
		// 通过code获取token
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "Jj2btQNaf-MCqLY0-Gjd-EAi-b8wOSu0bGBfHUkqRZiEdD1DL3eaXoK80ufeNwc4EEw1Ko-Wywft3wg-Hu6Kto19Ux3v-6qsRxR-U3PjjEt8dwW1v056tZA8erp6GjlG");
		params.add("grant_type", "authorization_code");
		// 如果没有指定redirect_uri可以填写也可以不填写
//		params.add("redirect_uri", "https://www.acgist.com");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testRefreshToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "refresh_token");
		params.add("refresh_token", "CJqLgBRbrbMSN4_LWHIWITJAv4NfTz8SIkgzVApLcdCYzRKrVauN0zED_94MtM3ajFBEZjrYBsQP_juI-SGrZUrRYPoa-0aDEhXnBEeS1B9vrvBybzbMuqZRxoo_9b5d");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQ5MDc2ODk4LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojk5OTkiLCJleHAiOjE2NDkwODQwOTgsImlhdCI6MTY0OTA3Njg5OH0.cEWJ8u4xUO80qojev6y91W0AXm6PgJeeQUBe0TTph4BPFz7ofrPwL8HEcLPFvYAC-Af6LHuVBF2P8E3md2qBevSlubUti3XoYj6SebzJgsI-f3uXHUePx0Vdh8xbZykwA7x_qwfte7-yB7DczQ3D-eTdzYcAF5MrKq2YTVtTdg4MzpEb7ZGHXErCQht_vJidJld7S14By9pPPXTupr7YPp-t1joDusk2IEEYQhR6cJeZMCeEG4BHRHQd9-ugWLJ59cfuZh07jHfA0Gqq1KN_J73o4VYVqTrHgzAP7koaNiiKxee63WYHRE5QsgbrilJpGk6yNvGAwDh33r_-CPnyJQ");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testRest() {
		final String body = HTTPUtils.post(
//			"http://localhost:9090/user",
			"http://localhost:8888/rest/user",
			Map.of(),
			Map.of(
//				User.HEADER_CURRENT_USER, "{\"id\":1,\"name\":\"root\"}",
				"Authorization", "Bearer eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQxNzA3MTc3LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjQxNzE0Mzc3LCJpYXQiOjE2NDE3MDcxNzd9.MCe2HiztEhGvGfG5l7X65mvULF-PcSh25i6eO2SjKuuAGtyf14CwcREzyTx7lyr-jSdmAnaO438VyvuER7_03EJ0vdXGXlTlClb-jY3iFYbxk3fH7u2lR6sutb1qy_O4sK4ApgL-dENmYxoFbOmE5WhFI88IWHt5NLv4mbUhSP6yDLOh3SLE7rrvjcQMVtqysqTTkC5Mx_qyhR1MjeAELt9pIwDK19g39fr7kvwPjgQOnvlRGYAJ15mTIN7Jz5n37PoiGiEcXZxKegePerjHBZfuH8ZVFJHnLPqh64p-Gzi1GYq0Egzg045zslFDv9U8RFpvqbgYxO5oAtv-O3Gipg"
			),
			1000
		);
		log.info("{}", body);
	}
	
}
