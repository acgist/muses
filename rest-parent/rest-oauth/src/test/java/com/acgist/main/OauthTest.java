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
	
	public static final String OAUTH2_URL = "http://localhost:9999";
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
			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web-client&client_secret=123456&scope=all&state=state&redirect_uri=http://www.acgist.com",
			null,
			null,
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
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "6A-C0uDRshUuCpN1jB44tdyKw7zGmhmwQ_93va1iPVRsKgV4XlqF1iQsfSlS56JPutpKNvxwAZVOPFqs2VywiO4zodyicFWUTsC41ekYfxAZti5lLaYVIuMDQFufVRXW");
		params.add("grant_type", "authorization_code");
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
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI1Y2VlMDIxNS0zZjhkLTRkYWQtYjM1NS1jYmJhOTk1ZjA4MzIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTIwNDMxMSwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTIxNTExMSwiaWF0IjoxNjM5MjA0MzExfQ.IZP0o6osmH1s2hvHFwzDU7eEDS7B-4UpeGgt7BfDXem90VUE-8089T1w9gk-65FtGaS37hrPfsceJ6ylcVQP6Uzns_59ev6MuuXiGeTnHJlaVFW-kJc-kd5yw6f9tnVMGAOjLQMXo6YqsZgD8PNuksh_-CYOZap7hpwrypeRcHS0gxKAyhw7Tq2rRUnq7KDpyFoA447vqKdwDfFnKQVkI_CLUlqc04ynPlRLKyyG2yB-dUtPDqY93XYiRsVGIqYZXMzQ4uUfQm_zIk0BRsp6J0Wjofy2s1rTo7GoOaiJwKeNbxsh7vYC4QhCexmHyDFVg-y2DpCZ0IlvkXAYDhsRrw");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testUser() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web-client:123456".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI4NTg0N2E2YS00OTEyLTQ2YzAtYmY3OS0yYTczNzBlOTBjN2QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTIwMjE5Mywic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTIwMjQ5MywiaWF0IjoxNjM5MjAyMTkzfQ.JeZy3g3zLqvpTQcWs2nSAGhcKRfgGPtU0CJZE5OMU4g7IrwpzhAqA_CAmkzp7VytTwLYusXGJccpD0yUJOH9ODZjbNz4PbZuWruC5gyzWiK4eMqd0O7jWF0CnA8YRZfZrBFEQKLRsGOYzVeFl99OvZxxqsKgQLSN3B6iS7uzaCwMjuIwZowCFzQPvTvzmz6J3NK506TYAQXVg9WkemkqjjISC8DvEMGp-dnjLZkF3RMRDfGW1DNOSLz_K-s27-lZUlzRb_VXSrVrwwe66BqVyWKxFMOz9sBGGgjTUb_IAeVAqtq_MsKwS0KEOIZiF7fhESb-IPCwh0V1EQgHQCNB-g");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/user", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testRest() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer eyJraWQiOiI1Y2VlMDIxNS0zZjhkLTRkYWQtYjM1NS1jYmJhOTk1ZjA4MzIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTIwNDMxMSwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTIxNTExMSwiaWF0IjoxNjM5MjA0MzExfQ.IZP0o6osmH1s2hvHFwzDU7eEDS7B-4UpeGgt7BfDXem90VUE-8089T1w9gk-65FtGaS37hrPfsceJ6ylcVQP6Uzns_59ev6MuuXiGeTnHJlaVFW-kJc-kd5yw6f9tnVMGAOjLQMXo6YqsZgD8PNuksh_-CYOZap7hpwrypeRcHS0gxKAyhw7Tq2rRUnq7KDpyFoA447vqKdwDfFnKQVkI_CLUlqc04ynPlRLKyyG2yB-dUtPDqY93XYiRsVGIqYZXMzQ4uUfQm_zIk0BRsp6J0Wjofy2s1rTo7GoOaiJwKeNbxsh7vYC4QhCexmHyDFVg-y2DpCZ0IlvkXAYDhsRrw");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:8888/rest/user", requestEntity, String.class);
		LOGGER.info("{}", userBody.getBody());
		final ResponseEntity<String> userNameBody = restTemplate.postForEntity("http://localhost:8888/rest/user/all", requestEntity, String.class);
		LOGGER.info("{}", userNameBody.getBody());
	}
	
}
