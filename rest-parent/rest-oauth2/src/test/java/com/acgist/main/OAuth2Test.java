package com.acgist.main;

import java.util.Map;

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

public class OAuth2Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Test.class);
	
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
			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=https://www.acgist.com",
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
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "wZZdhoTHnaWov9vtwsKrOqQa18CXVMs8aMVtg4IcTjE5tw8qJxHiLNzjUdMpTi94pSwlC2oAwGS465dmK01xqjXhc5FxTitbnYQVesUmASxNirTPxit-BDEENgdiwBz5");
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "https://www.acgist.com");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testRefreshToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "refresh_token");
		params.add("refresh_token", "YCtFi2UIR2-B8wKBMbICPJBNLYzTGO1xxmUNdPQblD-38mphlE56dryfdIQ96L1Pz2h7tz64K_tCGTAY5rubBaiZUp8NrYWaTQx7d4LtwocvxWoJV8gbXyj3Ti2tvozQ");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiItMTIwMzIwOTE1OCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjM5ODk1MjE3LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjM5OTAyNDE3LCJpYXQiOjE2Mzk4OTUyMTd9.mtCAVPxVh0MAkOHDlHzAYA0uDd33BwMkksQcgbK8uiDwrkr_n1wxRs8F33SUO_2JZnXgWIG_K1iaDV0BdvUBFdOn6GJxqpURu6fS2hss2cYgFkUSpm4POvESIJq71nGrjEBBIGMMl5CInd2MIQhbVwOgDIvL1u3trnnDZDL0iIX2hGTBn8eAMTd5v2q1HkaJ5K_l1lkcb-N2KYE3S3hVdW_OEcHMzeq9SfStnBD-RP0MDWnKH5y0hi6I2UkBrQF2-EQkzVbW0RdGEGELx1QgWP-BamR-1I20i9z2ZIdtzaU_3fLETcE9y-v-fyzJmpaNmh4LEDa7T9tdsAB2hS_mYA");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9999/oauth2/introspect", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testRest() {
		final String body = HTTPUtils.post(
//			"http://localhost:9090/user",
			"http://localhost:8888/rest/user",
			Map.of(),
			Map.of(
//				User.HEADER_CURRENT_USER, "{\"id\":1,\"name\":\"root\"}",
				"Authorization", "Bearer eyJraWQiOiItMTIwMzIwOTE1OCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjM5ODk1MjE3LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjM5OTAyNDE3LCJpYXQiOjE2Mzk4OTUyMTd9.mtCAVPxVh0MAkOHDlHzAYA0uDd33BwMkksQcgbK8uiDwrkr_n1wxRs8F33SUO_2JZnXgWIG_K1iaDV0BdvUBFdOn6GJxqpURu6fS2hss2cYgFkUSpm4POvESIJq71nGrjEBBIGMMl5CInd2MIQhbVwOgDIvL1u3trnnDZDL0iIX2hGTBn8eAMTd5v2q1HkaJ5K_l1lkcb-N2KYE3S3hVdW_OEcHMzeq9SfStnBD-RP0MDWnKH5y0hi6I2UkBrQF2-EQkzVbW0RdGEGELx1QgWP-BamR-1I20i9z2ZIdtzaU_3fLETcE9y-v-fyzJmpaNmh4LEDa7T9tdsAB2hS_mYA"
			),
			1000
		);
		LOGGER.info("{}", body);
	}
	
}
