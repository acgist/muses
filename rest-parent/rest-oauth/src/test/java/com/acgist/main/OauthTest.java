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
//		{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTA4NjIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.doZvWxNp3BuGCCzeZ8GpY7tTPyCWn0ahrw4bs4kJGlqw092q12v53XajLNC5dfF88XBC1BIOfGnGV5zjLBt8WTo8VTvfAIySESvJxx4ZgvtJ2bH-oQYzcCyaKz5MvuELtizZBjf3YY2GNHbdu8APHgpWyImEJtrmYMFyjRPX4OQ","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImV4cCI6MTYzODEwODg2MiwiYXV0aG9yaXRpZXMiOlsicm9vdCJdLCJqdGkiOiJiYTVjNWQ5MS04ZjZkLTRhZWItYjg4OC1hNzFlMzlkZmVkY2YiLCJjbGllbnRfaWQiOiJjbGllbnQtcmVzdCJ9.Gk7dqt_5vFdgQkN0GGDi7_kqGHme7ymJujcynPV8jq-vSS_feWXVBBDwUmru-vsfXNdMoMFHIUEF3B5Fi8yKfFNpEgDu0imIVjI7J8XyUMGJAWm91cvGKdTXZcVSY7YrrMokaQHtKJYU7CVA282Ov-kVKQ6hTkk1TsZrVHO8rck","expires_in":6066,"scope":"all","jti":"354f02eb-d26e-4cfe-9fe9-b2eb27f28b67"}
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
		params.add("code", "RIg2f0");
		params.add("grant_type", "authorization_code");
		params.add("client_id", "client-web");
		params.add("client_secret", "acgist");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/token", requestEntity, String.class);
		System.out.println(body.getBody());
//		{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTIxMjksInVzZXJfbmFtZSI6ImRkIiwiYXV0aG9yaXRpZXMiOlsiZGQiXSwianRpIjoiOGVkMTM1NmEtMDBiMC00ZTNiLWJiODItMzY3Y2VlNjhlN2EwIiwiY2xpZW50X2lkIjoiY2xpZW50LXdlYiIsInNjb3BlIjpbImFsbCJdfQ.MkzF22E7cXXrpsZ3R3wxkRUDoqfHqxNHGpIIBkG7LijEtduK_OcLrr55y_lMdtPmSkKYSPGbX6rc11wYB0ae_iOJTj0QoWt_O-WwWQWrn8R72RcbA-HiV86fYlgjgZxJEyTz2XhkoTpAGt9WlUpWAVuCAIy0IQYMJduLnpTe8Ac","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkZCIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiI4ZWQxMzU2YS0wMGIwLTRlM2ItYmI4Mi0zNjdjZWU2OGU3YTAiLCJleHAiOjE2MzgxMTAxMjksImF1dGhvcml0aWVzIjpbImRkIl0sImp0aSI6ImI0YWYwZjNjLTBhYzItNDRiNC1hYjNlLTE2NzY4NDI0ZTFiOCIsImNsaWVudF9pZCI6ImNsaWVudC13ZWIifQ.UwzwDyt7hLUJc8g8csG34AuAMCTDzFH3B5saftYF5hG1NJ0SLuKl_2kV72DovVGbhEg3NkNStRTvbTDiOGsKx2KEKaHLZv-8TpgiWVU3LXn6GPkvmfDcQUjOzAHVOtlts5H-GlpjJZNZU1mLchN5M2vvcky7F6O5_TEtg1EeipM","expires_in":7199,"scope":"all","jti":"8ed1356a-00b0-4e3b-bb82-367cee68e7a0"}
	}
	
	@Test
	public void testCheckToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTIwNjYsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjhjNTM3MWM3LTU1NjQtNGM2ZS04NzE1LTY5N2E0ZDNmMzU1MyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.ZATk6t1fSJhXqL40WY7f9nJPhHNWMUQvJjSXBR4ep5TC4fNR4FSmLZSXYQJWk8Y66Wgut_gD6iAvyVJ2MlOhr-GcTi-NvNstxUOZeZ4zEUE-kVAxluockBPbLlsA2ORMDuPDr05K-e7unrGB_J0uLOoGOgedQCDRKpQ74a41LcY");
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
		params.add("refresh_token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImV4cCI6MTYzODEwODg2MiwiYXV0aG9yaXRpZXMiOlsicm9vdCJdLCJqdGkiOiJiYTVjNWQ5MS04ZjZkLTRhZWItYjg4OC1hNzFlMzlkZmVkY2YiLCJjbGllbnRfaWQiOiJjbGllbnQtcmVzdCJ9.Gk7dqt_5vFdgQkN0GGDi7_kqGHme7ymJujcynPV8jq-vSS_feWXVBBDwUmru-vsfXNdMoMFHIUEF3B5Fi8yKfFNpEgDu0imIVjI7J8XyUMGJAWm91cvGKdTXZcVSY7YrrMokaQHtKJYU7CVA282Ov-kVKQ6hTkk1TsZrVHO8rck");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/token", requestEntity, String.class);
		System.out.println(body.getBody());
	}
	
	@Test
	public void testCheckResource() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTIwNjYsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjhjNTM3MWM3LTU1NjQtNGM2ZS04NzE1LTY5N2E0ZDNmMzU1MyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.ZATk6t1fSJhXqL40WY7f9nJPhHNWMUQvJjSXBR4ep5TC4fNR4FSmLZSXYQJWk8Y66Wgut_gD6iAvyVJ2MlOhr-GcTi-NvNstxUOZeZ4zEUE-kVAxluockBPbLlsA2ORMDuPDr05K-e7unrGB_J0uLOoGOgedQCDRKpQ74a41LcY");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:9091/user", requestEntity, String.class);
		System.out.println(userBody.getBody());
		final ResponseEntity<String> userNameBody = restTemplate.postForEntity("http://localhost:9091/user/all", requestEntity, String.class);
		System.out.println(userNameBody.getBody());
	}

}
