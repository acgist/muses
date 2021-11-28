package com.acgist.main;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class OauthTest {

	@Test
	public void testTokenKey() {
//		http://localhost:9090/oauth/token_key
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<String> body = restTemplate.getForEntity("http://localhost:9090/oauth/token_key", String.class);
		System.out.println(body.getBody());
	}
	
	@Test
	public void getTokenPassword() {
		// 直接访问
//		http://localhost:9090/oauth/token?username=root&password=root&grant_type=password&client_id=client-rest&client_secret=acgist
		// 代码获取
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", "root");
		params.add("password", "root");
		params.add("grant_type", "password");
		params.add("client_id", "client-rest");
		params.add("client_secret", "acgist");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/token", requestEntity, String.class);
		System.out.println(body.getBody());
	}
		
	@Test
	public void getTokenAuthorizationCode() {
		// 获取code
//		http://localhost:9090/oauth/authorize?client_id=client-web&response_type=code&scope=all
		// 通过code获取token
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "gOWD60");
		params.add("grant_type", "authorization_code");
		params.add("client_id", "client-web");
		params.add("client_secret", "acgist");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/token", requestEntity, String.class);
		System.out.println(body.getBody());
	}
	
	@Test
	public void testCheckToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTA4NjIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.doZvWxNp3BuGCCzeZ8GpY7tTPyCWn0ahrw4bs4kJGlqw092q12v53XajLNC5dfF88XBC1BIOfGnGV5zjLBt8WTo8VTvfAIySESvJxx4ZgvtJ2bH-oQYzcCyaKz5MvuELtizZBjf3YY2GNHbdu8APHgpWyImEJtrmYMFyjRPX4OQ");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/check_token", requestEntity, String.class);
		System.out.println(body.getBody());
	}
	
	@Test
	public void testRefreshToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "refresh_token");
		params.add("client_id", "client-rest");
		params.add("client_secret", "acgist");
		params.add("refresh_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjRkNmRjOTE2LWJhZGEtNDZkOC04ZWE1LTY0OWYxYjBhNGNiMiIsImV4cCI6MTYzODA5Mjk3NCwiYXV0aG9yaXRpZXMiOlsicm9vdCJdLCJqdGkiOiIxZDY2NzRmYy0yMWQzLTQ2NWYtOGY4MC03ZDIwNTQ1YzUyY2QiLCJjbGllbnRfaWQiOiJjbGllbnQtcmVzdCJ9.BiGkITOoLdvhsmz-jZNin8EFD8joHzpm_r0GZ1aGRcA");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/token", requestEntity, String.class);
		System.out.println(body.getBody());
	}
	
	@Test
	public void testCheckResource() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTA4NjIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.doZvWxNp3BuGCCzeZ8GpY7tTPyCWn0ahrw4bs4kJGlqw092q12v53XajLNC5dfF88XBC1BIOfGnGV5zjLBt8WTo8VTvfAIySESvJxx4ZgvtJ2bH-oQYzcCyaKz5MvuELtizZBjf3YY2GNHbdu8APHgpWyImEJtrmYMFyjRPX4OQ");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:9091/user", requestEntity, String.class);
		System.out.println(userBody.getBody());
		final ResponseEntity<String> userNameBody = restTemplate.postForEntity("http://localhost:9091/user/all", requestEntity, String.class);
		System.out.println(userNameBody.getBody());
	}

}
