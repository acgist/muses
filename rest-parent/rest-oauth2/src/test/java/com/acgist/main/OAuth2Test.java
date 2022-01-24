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
			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state",
//			"http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=https://www.acgist.com",
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
		params.add("code", "psrSpXXFPp0Y2nlQ8QcZNAGe_QG4u1jkYlF4CYCplAxM0F7lHFOnM_72jRTPLA2578Jh08CQmUjxmuMC62ai3qD3Pfe3ch4XT9ZWK8ry0ZJ2HTFytSsZIjjGULlExp_d");
		params.add("grant_type", "authorization_code");
		// 如果没有指定redirect_uri可以填写也可以不填写
//		params.add("redirect_uri", "https://www.acgist.com");
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
		params.add("refresh_token", "CJqLgBRbrbMSN4_LWHIWITJAv4NfTz8SIkgzVApLcdCYzRKrVauN0zED_94MtM3ajFBEZjrYBsQP_juI-SGrZUrRYPoa-0aDEhXnBEeS1B9vrvBybzbMuqZRxoo_9b5d");
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
		params.add("token", "eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQxNzA2OTkwLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjQxNzE0MTkwLCJpYXQiOjE2NDE3MDY5OTB9.TbOOBjT5R-3M7TIV6gm6obzb5CzvWVJW34Ndo6lDki18CLhkDMBvgPSoUyNT29YVbZvBSB2VTcSDWZB1v-uidriJ1kDi2rsVRyCBxCyb1__HY_EDouAX04kwpr1u7gMP1qNKG07-u-TzcUzAV6tEsu9IIxpHTWBtReD3ZerYvWgbVvXgVEbCWoDKCfphTguxKXIZv9CtNljaV0AY8T04vaypQ0xmLqOgIJIl3mfcswwb4szbEGw0aDX7C9BL0cPHptVt67fNHV6Noa44ZXKhVxX-Mu8lqCb9ywZJHQSc8_hbKsuQENieL75nKxBj3cUibq1V-xUsyxpmKadRyTCqIg");
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
				"Authorization", "Bearer eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjQxNzA3MTc3LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjQxNzE0Mzc3LCJpYXQiOjE2NDE3MDcxNzd9.MCe2HiztEhGvGfG5l7X65mvULF-PcSh25i6eO2SjKuuAGtyf14CwcREzyTx7lyr-jSdmAnaO438VyvuER7_03EJ0vdXGXlTlClb-jY3iFYbxk3fH7u2lR6sutb1qy_O4sK4ApgL-dENmYxoFbOmE5WhFI88IWHt5NLv4mbUhSP6yDLOh3SLE7rrvjcQMVtqysqTTkC5Mx_qyhR1MjeAELt9pIwDK19g39fr7kvwPjgQOnvlRGYAJ15mTIN7Jz5n37PoiGiEcXZxKegePerjHBZfuH8ZVFJHnLPqh64p-Gzi1GYq0Egzg045zslFDv9U8RFpvqbgYxO5oAtv-O3Gipg"
			),
			1000
		);
		LOGGER.info("{}", body);
	}
	
}
