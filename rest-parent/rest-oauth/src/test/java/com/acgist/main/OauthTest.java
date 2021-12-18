package com.acgist.main;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class OauthTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OauthTest.class);
	
	public static final String OAUTH2_URL = "http://localhost:9999";
	public static final String GATEWAY_URL = "http://localhost:8888";
	
	@Test
	public void testPassword() {
		LOGGER.info("{}", new BCryptPasswordEncoder().encode("123456"));
	}
	
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
		params.add("code", "l0iJeplhPcSb6fuG28am2SYUuXYIKgYw46uSJVuMN7GVQLUdnNREdgaamrP_tbglQ208EOmEoNUAxJAswEXdCY_6ditp_WK2wSsoqblOTUndAOIaB9gqETdQXJNV9Y1I");
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
		params.add("refresh_token", "eyJraWQiOiJhZjRiYmQ3Yi0wMDA5LTQ3MmEtOWFkZC00MjkyNGNhYTMyNzciLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTgxMjMzOSwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTgyMzEzOSwiaWF0IjoxNjM5ODEyMzM5fQ.i66KU86qAyZcEj6qKlpjerS0pIakFLMTvZeIEGvOjjH5SXgZH7HOeCQQeWsLErhgewotdlMz9DyeRrVMQBDRWvdhsKEurbX8-_JmlbrxSwPYcgsFGe9vFBVaKOrD999qhNz-j9_gSkGK2ovGncZfkZS1lKStzEXsjwaBYyd4SxnN0JN92bo8egg4aDoNfnVlTEL46fbZfiOG4Tnqa09ZDmETfxlT81UJ5Hx5vxTRC394F84gp3ei0ZP9t3sHyFTOvQ1JkpBgKkC2lCbvz0dqHxKkxFf-iXmpNveSKfHneW6ockemGlb4fA1KvI5x-_b50Ooai3reIoZ0tvqBufOHMg");
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
		params.add("token", "eyJraWQiOiI3NDQ0ZjU4Ny1hMTIzLTQwNjktYmM1Ni0xZGIwNGNiZTE1MGYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTgxOTkyMywic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTgzMDcyMywiaWF0IjoxNjM5ODE5OTIzfQ.AXVR5oO5z0HpYORJBOiMUt8ykp2MawjfwUF3flUlePP0NNPVVn-g4CAre1Pc1W7wmLC-uqMnfctj9J96PW_3pZ67IiaDz7t_z78E9gg_Nc4Dn-gG2hLEtY2djwPmLyCSfnBKyRw8PRLw5V-tLihWNxWcmXrw8-LY0cd9xpI4UFc6w0eTBD5-OyqXdWbEzsaxDjaUtwLYHgubcjv7z3hvr0sgnFlPbWT4w0ZjQT_WCdkGKT3bJP6Zz6QQrNOTrhTAiVT766pMJoTNAETFLWSaETFFqngnBX6ELccyHekRaWc48xhx6o6p299ayjSN9OlHGpEA-8Fq2Synm4b1YfaUfQ");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testRest() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// 没有通过网关需要注入用户信息
//		headers.set(User.HEADER_NAME, "root");
		headers.set("Authorization", "Bearer eyJraWQiOiI3NDQ0ZjU4Ny1hMTIzLTQwNjktYmM1Ni0xZGIwNGNiZTE1MGYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTgxOTkyMywic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTgzMDcyMywiaWF0IjoxNjM5ODE5OTIzfQ.AXVR5oO5z0HpYORJBOiMUt8ykp2MawjfwUF3flUlePP0NNPVVn-g4CAre1Pc1W7wmLC-uqMnfctj9J96PW_3pZ67IiaDz7t_z78E9gg_Nc4Dn-gG2hLEtY2djwPmLyCSfnBKyRw8PRLw5V-tLihWNxWcmXrw8-LY0cd9xpI4UFc6w0eTBD5-OyqXdWbEzsaxDjaUtwLYHgubcjv7z3hvr0sgnFlPbWT4w0ZjQT_WCdkGKT3bJP6Zz6QQrNOTrhTAiVT766pMJoTNAETFLWSaETFFqngnBX6ELccyHekRaWc48xhx6o6p299ayjSN9OlHGpEA-8Fq2Synm4b1YfaUfQ");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:9090/user", requestEntity, String.class);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:8888/rest/user", requestEntity, String.class);
		LOGGER.info("{}", userBody.getBody());
	}
	
}
