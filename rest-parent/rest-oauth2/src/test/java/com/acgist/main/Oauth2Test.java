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

public class Oauth2Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oauth2Test.class);
	
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
		params.add("code", "VTJaqJh79HwdYj_tqv6DfV77POh91km3sHeMgtSgUxMzzDs1tvGxWMuACRW-MxOQl6CPb7vFT_4wSHUlJXoRj_8T4oXPfWC9mP1AHfsyxH5WhKZKeKZkL4LvRKgtKMir");
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
		params.add("refresh_token", "eyJraWQiOiJmZGI1ZjQ4YS0wMjJiLTQ3ZWEtOTgzNy00NWI3MTE3MWYyMjEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjM5ODg3MjI0LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjM5ODk0NDI0LCJpYXQiOjE2Mzk4ODcyMjR9.SzcvBlEnLaSCwU8JL8G8kYXEoqWMAGbE7h59IwpxE4ipo3hsuQOrhEkw-pDBNcwsMfdhI72CsISftfEfipZchwEnYh75wmFGIl-mOfbdK_0Fqi-U2BzFDWTe6SD-fDnNTCuBG3pGt49tD5N_ptdtQGmONQY8JmoYmcedvA8cbTw-RqGgqXLKodQfBVNigYvDMl5IyrsAwFjhv1l3P2JJrpUEQ4u6BD-ctGs_mdMOGjTRbAPqOO9qYpflEBVFM0zOZa-gfSm3bkERn-R8jf1bz6sSd0vJjPjrbsTK7tYn_YoDDBDg0z-fbdIEjeQlSsdGOhEp0iJnsyzb3IT0ec8SUQ");
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		final ResponseEntity<String> body = restTemplate.postForEntity("http://localhost:9090/oauth2/token", requestEntity, String.class);
		LOGGER.info("{}", body.getBody());
	}
	
	@Test
	public void testIntrospect() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic " + StringUtils.base64Encode("web:acgist".getBytes()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", "eyJraWQiOiJmZGI1ZjQ4YS0wMjJiLTQ3ZWEtOTgzNy00NWI3MTE3MWYyMjEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjM5ODg3MjI0LCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjM5ODk0NDI0LCJpYXQiOjE2Mzk4ODcyMjR9.SzcvBlEnLaSCwU8JL8G8kYXEoqWMAGbE7h59IwpxE4ipo3hsuQOrhEkw-pDBNcwsMfdhI72CsISftfEfipZchwEnYh75wmFGIl-mOfbdK_0Fqi-U2BzFDWTe6SD-fDnNTCuBG3pGt49tD5N_ptdtQGmONQY8JmoYmcedvA8cbTw-RqGgqXLKodQfBVNigYvDMl5IyrsAwFjhv1l3P2JJrpUEQ4u6BD-ctGs_mdMOGjTRbAPqOO9qYpflEBVFM0zOZa-gfSm3bkERn-R8jf1bz6sSd0vJjPjrbsTK7tYn_YoDDBDg0z-fbdIEjeQlSsdGOhEp0iJnsyzb3IT0ec8SUQ");
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
		headers.set("Authorization", "Bearer eyJraWQiOiJmNDdhNzVmYy02MjQ0LTRlNzgtOGIzOC1iZWZhODZjZGFkNDkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTg3NzUwMiwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTg4ODMwMiwiaWF0IjoxNjM5ODc3NTAyfQ.CTAszAMyeGRRIsapBLMs1Btvn3rdUMJmVlKSnu4gLyAaDJ7PaTKGR3Gd5gYfMzmJ9pMYb4kgpTPzcjx8IEJeShXsDu_zhC0RbDoF6csz7dk_GhpFtGgBEeKVBZA-ki7IewWW4BcKocodCZavvHsvlcNG9yzIS4YbAUzSyAoht799DgtlCxclxEPNnNiZ5ipYmzhUIQUd7IgnV9dfhFW59-CwLLauI16Nvzh5moA-tNgxyjEdBSrMluxo_-uVsZzMLrJPXIFDNsrHfxZkFTeauLRnTJbj14iCd8m9gD5-VxuzmKAHFeWqpqFDnKcsd-KbC8Haz1Dfjtcl-K2WjaZf9w");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:9090/user", requestEntity, String.class);
		final ResponseEntity<String> userBody = restTemplate.postForEntity("http://localhost:8888/rest/user", requestEntity, String.class);
		LOGGER.info("{}", userBody.getBody());
	}
	
}
