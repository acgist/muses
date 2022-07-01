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

import com.acgist.boot.utils.HTTPUtils;
import com.acgist.boot.utils.StringUtils;

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
	public void testCode() {
		final String code = HTTPUtils.get(
			// 注意：环回地址这里redirect_uri不要配置：配置中心配死（域名可以配置）
			// POST需要设置重定向测量
			"http://localhost:9999/oauth2/login/password?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&username=root&password=123456",
//			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state",
//			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=https://www.acgist.com",
//			Map.of()
			1000
		);
		log.info(code);
	}
	
	@Test
	public void getTokenAuthorizationCode() {
		// 通过code获取token
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "-8Ejdfnw6W9WNwYsZvAtK4I0NL-Dk7tyQELsJu5ipxBmKx5yreD8D1mNChbtaEWCAEmGo7k-dvZJ2EXT-mY4wy9Som78RONmEFgeVeJ8tOyk78I81w7JSaLUPaqykjpI");
		params.add("grant_type", "authorization_code");
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
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQ5MTIwNDEyLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6OTk5OSIsImV4cCI6MTY0OTEyNzYxMiwiaWF0IjoxNjQ5MTIwNDEyfQ.ltQPiXI0BLcn0crn6sGXBANzaXX3-D48wkxHHTeErHWU5e0vRsdzOFCipT3XPgW0pJE0kL0Zb_mK35umAXWcotUrHxxqgXrQiqhLfsY-IHRJec-V42gEuQdo_h5_-AMlFzrVK0uv6tO68FYsZt8snwUAzsKnXmQYhReG_IFGee0ilXL3hYXSUnYeFIVmYOkEWK8EFkDdSMCDAO0KbVL_xywEnK6zBEAUWsLg4pTACe4N_XbyyFknYEZzPG13_0WLAin4yS91IMXASb-toPbvPc8dMVZiloyouSnBOYXQkTH9hhgucsHVn-d7ZQ95MWu8ngKiMRD--H5qZmX4nvLU3w");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		log.info("{}", body.getBody());
	}
	
	@Test
	public void testRevoke() {
		final RestTemplate restTemplate = HTTPUtils.buildRestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQ5MDgwNjk2LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojk5OTkiLCJleHAiOjE2NDkwODc4OTYsImlhdCI6MTY0OTA4MDY5Nn0.pA1CwjZGh9pVWf4bX1WVvAfdYpzD0BOXYbC40sNJDHTQ2cffd6jEZcT0s-H0crkO5K4hFTM1YYYbyfaydzQ3uRSzkBnEaQ7SYyabMLGSvCDr6k_6dDZX_NSH-KuUcasuo_NqEwkU3B2-F4jEXnSa_qAIXWHqW9yPznzz-GfPOQt8__BwRqvceIbkH6Uvo1q3JYaJVMzD510tEOSiMZHftqjwIB2Kf8aWS2xK6ZA5YCm6Tg23gIvqpUawuWQJTwP0a3qdpD8ZtuQpFftj1I0ugA_jJfoj_YM2Yp5Jrg2wfs2KZY5-g3_SyF0pEE-o8KIi89e5QTMqdF3jcKucZPmKsw");
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
