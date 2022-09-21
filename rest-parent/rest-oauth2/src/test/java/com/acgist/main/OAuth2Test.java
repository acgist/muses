package com.acgist.main;

import java.util.Base64;
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

import com.acgist.boot.utils.HTTPUtils;

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
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final ResponseEntity<String> body = restTemplate.getForEntity(OAUTH2_URL + "/oauth2/jwks", String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testToken() {
		// authorization_code页面获取Code
//		"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state",
//		"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=https://www.acgist.com",
		// 通过code获取token
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		// authorization_code
//		params.add("code", "LpuB4K9Jf8a1vIp4mbhiZ_Qq-_lrUxCvqLu3sJDNOIiAp9GBOccLRxRMsVLTh8J8Tcr1DS9ydENOytYugXYTfXxmks-SHfgAp5YKGWznKhVCcBocDec4Ez4H4K3E3r0S");
//		params.add("grant_type", "authorization_code");
		// sms
//		params.add("scope", "all");
//		params.add("mobile", "root");
//		params.add("smsCode", "000000");
//		params.add("grant_type", "sms");
		// password
		params.add("scope", "all");
		params.add("username", "root");
		params.add("password", "123456");
//		params.add("password", "000000");
		params.add("grant_type", "password");
		// 如果没有指定redirect_uri可以填写也可以不填写
//		params.add("redirect_uri", "https://www.acgist.com");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testRefreshToken() {
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "refresh_token");
		params.add("refresh_token", "pLO-VYC6axxe4AUc2nDTLkD59zd3x127H3quHTAknk7tQRmHoLbMZWZRlLdI0jM_Q2U2QEKVwqIdpxyWMXty9h1OZngXt19-tVaPAc-7hfKHsNrFOMKuU17yLJsMTh5n");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjYzNzIzMTAyLCJyb2xlIjoi6LaF57qn55So5oi3Iiwic2NvcGUiOlsiYWxsIl0sImlzcyI6Imh0dHBzOlwvXC93d3cuYWNnaXN0LmNvbSIsIm5hbWUiOiJyb290IiwiaWQiOjEsImV4cCI6MTY2MzczMDMwMiwiaWF0IjoxNjYzNzIzMTAyfQ.kOykmVNe8A-rfKHuUCzW_i730bmrSY3ZK89LHZ7610scsm0gJHH3eUcSDPw3ssVDfkq6xLPFyANPCklnpNvAqdhAu3pgvEEibt6Q4oqXDF5ZmLHKjJc9-OWvp5F70q7DPKfnZ9gPJJvK28Mns7I5IIx085TEv1CCKH4MYkmBC6lkL3NyaWFFlJeP5vAF8DCornb2MU8IGNO3-8tWOXO-ATro9tqwlWEVCTvJIaWmxtf4gEKDcojnnwKJjmI3EI392--RrYsT6rKB8xmoWfEXYWqlOLHFPY5VauG9gHG-VI7FQWPqKPsZwIJZsLOlz6QNz0LN9fiPybbxVrRxF9JNPw");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testRevoke() {
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjYzNzIzMTAyLCJyb2xlIjoi6LaF57qn55So5oi3Iiwic2NvcGUiOlsiYWxsIl0sImlzcyI6Imh0dHBzOlwvXC93d3cuYWNnaXN0LmNvbSIsIm5hbWUiOiJyb290IiwiaWQiOjEsImV4cCI6MTY2MzczMDMwMiwiaWF0IjoxNjYzNzIzMTAyfQ.kOykmVNe8A-rfKHuUCzW_i730bmrSY3ZK89LHZ7610scsm0gJHH3eUcSDPw3ssVDfkq6xLPFyANPCklnpNvAqdhAu3pgvEEibt6Q4oqXDF5ZmLHKjJc9-OWvp5F70q7DPKfnZ9gPJJvK28Mns7I5IIx085TEv1CCKH4MYkmBC6lkL3NyaWFFlJeP5vAF8DCornb2MU8IGNO3-8tWOXO-ATro9tqwlWEVCTvJIaWmxtf4gEKDcojnnwKJjmI3EI392--RrYsT6rKB8xmoWfEXYWqlOLHFPY5VauG9gHG-VI7FQWPqKPsZwIJZsLOlz6QNz0LN9fiPybbxVrRxF9JNPw");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/revoke", requestEntity, String.class);
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
