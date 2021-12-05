package com.acgist.main;

import java.util.Map;

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

import com.acgist.boot.HTTPUtils;
import com.acgist.boot.StringUtils;

public class OauthTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OauthTest.class);
	
	public static final String OAUTH2_URL = "http://localhost:9090";
	public static final String GATEWAY_URL = "http://localhost:8888";
	
	@Test
	public void testTokenKey() {
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<String> body = restTemplate.getForEntity(OAUTH2_URL + "/oauth2/jwks", String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testCode() {
		final String code = HTTPUtils.get(
			"http://localhost:9090/oauth2/authorize?response_type=code&client_id=web-client&client_secret=123456&scope=all&state=state&redirect_uri=http://www.acgist.com",
			null,
			Map.of("Authorization", "Basic " + StringUtils.base64Encode("user:123456".getBytes())),
			1000
		);
		LOGGER.info(code);
	}
	
	@Test
	public void getTokenAuthorizationCode() {
		// 通过code获取token
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web-client:123456".getBytes()));
//		headers.set("Authorization", "Basic user:123456");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "TF3UgO9bUV3FjmF_5fUCNMbxyko5mj9DhByptrbwoq81L0RNU1F1ly_n2rxwMZJ206NuruUvzMU_ITKsKeb7zV1-bECyvoaKSVpRVa8UGTB1FmKVG6_QcAJnYf4S8phQ");
		params.add("grant_type", "authorization_code");
//		params.add("client_id", "web-client");
		params.add("redirect_uri", "http://www.acgist.com");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testRefreshToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web-client:123456".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "refresh_token");
		params.add("refresh_token", "L_Ga8y0qyGPWiVDlcoV-wSGlSV3A5pH2XWX7NYrubOa8sMjNNir94IMr4ge8nU6jIDO0wc2EkD9p-8kHKMqArJHZzR5pYcSkb5ShcWRJfK509j7EnhFPN-PalViHDdBV");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web-client:123456".getBytes()));
//		headers.set("Authorization", "Bearer eyJraWQiOiI4MmQ2OWQ4OS1hZmE0LTRkY2ItYTVlZS03M2IxZTYyYTgwMmMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXN0LWNsaWVudCIsImF1ZCI6InJlc3QtY2xpZW50IiwibmJmIjoxNjM4NjAzOTc0LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2Mzg2MDQyNzQsImlhdCI6MTYzODYwMzk3NH0.gEEQK0iFjuR-pm5-X-gew5gub_CdLYkRaoVc_VqxcQwf_lHphNzqHQENC9xxVHtZLUoTca-d3JPU3tofLweC8GHQrOAFmnWKPlQwAwmX3Ozh4j1E3d54Fco9yGdjW9TvD3CQwUrrSplFA3FOZEk2fB8xoCRuuDT8pMRpqHbZs-1FLT20tLHnuxnQhSaRWytXrHVU389zDky8de0LNVNz0b2FgkZnr61FVAFTX8fBKKQcaZbczph7-v-_-fXbzbnuYqkA7ticu-SpSWk_8ood4FZiUI_xhVvusjK4y8kUIu6cbfwm0nkjPCTKmHmINpriuHInglFMNvZqSxVGgzkuew");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2ZDczZmVjOS05N2FjLTQ3N2MtYjgyYi0xMjNlNGM2ZTMwNmUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzODYzMzA3Niwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzODYzMzM3NiwiaWF0IjoxNjM4NjMzMDc2fQ.0bHQOk23x0O9j0pp-gpkHcJvEVJhfG6rRe3MBWmAEJNJcm-6NwKqYeFb0_MPsm6vuWqIgg6sWyw0VrsE7tiPvMyHVcI-nWSJ1F1K_eTNZ7b2mlqEP4bFWFv6Zh6InROrPsRiQ2wiMFY_BEtuvRNfRcIT6w2_E-xhz72l6hpVjWO7oHm1vjAVlNAQufn0lqAvJHhMG6Bn4u7rMYpSsiE58nGOUMgLFt3RBxIj5jcmUEdCwOY_zjAVX97SHLtRovElp-p8SdPdSWaNsCW6N6SjB8Z9voKlOrVBZYUUSlB7_XbC6oJ6IIuemi-fKs2J_9dzwc2d2FPswd7xJMQG2vuuIg");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/introspect", requestEntity, String.class);
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
	
	@Test
	public void testRest() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer eyJraWQiOiJlZTNiZTkwNS03ZWEzLTQ0ZWYtYWI5OS05ZDJmN2RiNGM1NzkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzODY3MzQ0MSwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzODY3Mzc0MSwiaWF0IjoxNjM4NjczNDQxfQ.Dk1HJUVmmW30AnZCo08Bc-zFx8mnpb8T05xGE9jujJbDmpXTTSgrFGROFoZHacWXPVaMsuKCS8kluStmvtqJGKtjUlqdA30aUlqsTLKzCuhqWQUjCd8p8XuFRAXAKxA-mlm0C49t0cCw3gFFuzJN_eoEY8ETGKtOOTced1n1flLVGsw-YLVOJyXz8u4oW73g8ifjh47iKbhkbi5SCS6BMjTUZPxbqgWG6VXuw-CA-S_-6rfMidxDUNsKI1v1RG0IntZMrhRUyw042U15hWDQFZ0NuenJxaPYCrlYdfmnehTddeLUkJEwme0MKSWzrrxqiBRb-5QNZGmV9b4VBcB-Bg");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:8888/rest/user", requestEntity, String.class);
		LOGGER.info("{}", userBody.getBody());
		final ResponseEntity<String> userNameBody = restTemplate.postForEntity("http://localhost:8888/rest/user/all", requestEntity, String.class);
		LOGGER.info("{}", userNameBody.getBody());
	}
	
}
