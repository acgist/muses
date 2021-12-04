package com.acgist.main;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class OauthTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OauthTest.class);
	
	@Test
	public void testTokenKey() {
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<String> body = restTemplate.getForEntity("http://localhost:8080/oauth2/jwks", String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testAuthorize() {
		// 直接访问
//		http://localhost:9090/oauth2/authorize?response_type=code&client_id=web-client&scope=all&state=state&redirect_uri=http://www.acgist.com
		// 代码获取
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", "user");
		params.add("password", "123456");
//		params.add("grant_type", "password");
		params.add("grant_type", "code");
		params.add("client_id", "web-client");
		params.add("client_secret", "123456");
		params.add("state", System.currentTimeMillis() + "");
		params.add("scope", "all");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/authorize", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
//		{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTA4NjIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.doZvWxNp3BuGCCzeZ8GpY7tTPyCWn0ahrw4bs4kJGlqw092q12v53XajLNC5dfF88XBC1BIOfGnGV5zjLBt8WTo8VTvfAIySESvJxx4ZgvtJ2bH-oQYzcCyaKz5MvuELtizZBjf3YY2GNHbdu8APHgpWyImEJtrmYMFyjRPX4OQ","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImV4cCI6MTYzODEwODg2MiwiYXV0aG9yaXRpZXMiOlsicm9vdCJdLCJqdGkiOiJiYTVjNWQ5MS04ZjZkLTRhZWItYjg4OC1hNzFlMzlkZmVkY2YiLCJjbGllbnRfaWQiOiJjbGllbnQtcmVzdCJ9.Gk7dqt_5vFdgQkN0GGDi7_kqGHme7ymJujcynPV8jq-vSS_feWXVBBDwUmru-vsfXNdMoMFHIUEF3B5Fi8yKfFNpEgDu0imIVjI7J8XyUMGJAWm91cvGKdTXZcVSY7YrrMokaQHtKJYU7CVA282Ov-kVKQ6hTkk1TsZrVHO8rck","expires_in":6066,"scope":"all","jti":"354f02eb-d26e-4cfe-9fe9-b2eb27f28b67"}
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
		params.add("username", "user");
		params.add("password", "123456");
		params.add("grant_type", "password");
//		params.add("grant_type", "client_credentials");
		params.add("client_id", "rest-client");
		params.add("client_secret", "123456");
		params.add("scope", "all");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
//		{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzgwOTA4NjIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.doZvWxNp3BuGCCzeZ8GpY7tTPyCWn0ahrw4bs4kJGlqw092q12v53XajLNC5dfF88XBC1BIOfGnGV5zjLBt8WTo8VTvfAIySESvJxx4ZgvtJ2bH-oQYzcCyaKz5MvuELtizZBjf3YY2GNHbdu8APHgpWyImEJtrmYMFyjRPX4OQ","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjM1NGYwMmViLWQyNmUtNGNmZS05ZmU5LWIyZWIyN2YyOGI2NyIsImV4cCI6MTYzODEwODg2MiwiYXV0aG9yaXRpZXMiOlsicm9vdCJdLCJqdGkiOiJiYTVjNWQ5MS04ZjZkLTRhZWItYjg4OC1hNzFlMzlkZmVkY2YiLCJjbGllbnRfaWQiOiJjbGllbnQtcmVzdCJ9.Gk7dqt_5vFdgQkN0GGDi7_kqGHme7ymJujcynPV8jq-vSS_feWXVBBDwUmru-vsfXNdMoMFHIUEF3B5Fi8yKfFNpEgDu0imIVjI7J8XyUMGJAWm91cvGKdTXZcVSY7YrrMokaQHtKJYU7CVA282Ov-kVKQ6hTkk1TsZrVHO8rck","expires_in":6066,"scope":"all","jti":"354f02eb-d26e-4cfe-9fe9-b2eb27f28b67"}
	}
		
	@Test
	public void getTokenAuthorizationCode() {
		// 通过code获取token
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic user=123456");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "qtTifnEGLC8OuRKykW5ENB0Q1lOyvNuciVjmQIMPmUNSk7XhBrflBPL3OXTuUB_PF2uJZ9rD_djXNtXkNbqXjXtAKIGvGTmSHBTRJlUjoH2pcfROH23RcApYCMB8yOO1");
		params.add("grant_type", "authorization_code");
		params.add("client_id", "web-client");
		params.add("client_secret", "123456");
		params.add("username", "user");
		params.add("password", "123456");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testCheckToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzg1OTY0NDIsInVzZXJfbmFtZSI6InJvb3QiLCJhdXRob3JpdGllcyI6WyJyb290Il0sImp0aSI6IjFmMmMzNzQ1LTljMDItNDFhMi1iMzRiLTYzY2JhMzEzMWRlMyIsImNsaWVudF9pZCI6ImNsaWVudC1yZXN0Iiwic2NvcGUiOlsiYWxsIl19.D001xWqJlf0ykBXa88e_oQP6DyrDg2GzWcbcBYg9gBLtFeLGoZU-Hu3QE8qFG5syxjmffglWChEzvnifVIiZWk_BZhgV607rmnr8KGq6t4rbt6y-ft_SgKC8_w7bGnazcqaIx7BxBnqJJXN9dswjHqBM_zni2ea7uaehZEgOg_w");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth/check_token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		headers.set("Authorization", "Bearer eyJraWQiOiI4MmQ2OWQ4OS1hZmE0LTRkY2ItYTVlZS03M2IxZTYyYTgwMmMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXN0LWNsaWVudCIsImF1ZCI6InJlc3QtY2xpZW50IiwibmJmIjoxNjM4NjAzOTc0LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2Mzg2MDQyNzQsImlhdCI6MTYzODYwMzk3NH0.gEEQK0iFjuR-pm5-X-gew5gub_CdLYkRaoVc_VqxcQwf_lHphNzqHQENC9xxVHtZLUoTca-d3JPU3tofLweC8GHQrOAFmnWKPlQwAwmX3Ozh4j1E3d54Fco9yGdjW9TvD3CQwUrrSplFA3FOZEk2fB8xoCRuuDT8pMRpqHbZs-1FLT20tLHnuxnQhSaRWytXrHVU389zDky8de0LNVNz0b2FgkZnr61FVAFTX8fBKKQcaZbczph7-v-_-fXbzbnuYqkA7ticu-SpSWk_8ood4FZiUI_xhVvusjK4y8kUIu6cbfwm0nkjPCTKmHmINpriuHInglFMNvZqSxVGgzkuew");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI4MmQ2OWQ4OS1hZmE0LTRkY2ItYTVlZS03M2IxZTYyYTgwMmMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXN0LWNsaWVudCIsImF1ZCI6InJlc3QtY2xpZW50IiwibmJmIjoxNjM4NjAzOTc0LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2Mzg2MDQyNzQsImlhdCI6MTYzODYwMzk3NH0.gEEQK0iFjuR-pm5-X-gew5gub_CdLYkRaoVc_VqxcQwf_lHphNzqHQENC9xxVHtZLUoTca-d3JPU3tofLweC8GHQrOAFmnWKPlQwAwmX3Ozh4j1E3d54Fco9yGdjW9TvD3CQwUrrSplFA3FOZEk2fB8xoCRuuDT8pMRpqHbZs-1FLT20tLHnuxnQhSaRWytXrHVU389zDky8de0LNVNz0b2FgkZnr61FVAFTX8fBKKQcaZbczph7-v-_-fXbzbnuYqkA7ticu-SpSWk_8ood4FZiUI_xhVvusjK4y8kUIu6cbfwm0nkjPCTKmHmINpriuHInglFMNvZqSxVGgzkuew");
		params.add("client_id", "rest-client");
		params.add("client_secret", "123456");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/introspect", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
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
		LOGGER.info("{}", body.getBody());
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
		LOGGER.info("{}", userBody.getBody());
		final ResponseEntity<String> userNameBody = restTemplate.postForEntity("http://localhost:9091/user/all", requestEntity, String.class);
		LOGGER.info("{}", userNameBody.getBody());
	}
	
}
