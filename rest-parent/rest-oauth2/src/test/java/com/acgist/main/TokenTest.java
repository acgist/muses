//package com.acgist.main;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.Map;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//
//public class TokenTest {
//
//	@Autowired
//	private JwtAccessTokenConverter jwtAccessTokenConverter;
//	@Autowired
//	private ResourceServerTokenServices resourceServerTokenServices;
//	
//	@Test
//	public void testToken() {
//		final String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290Iiwic2NvcGUiOlsiYWxsIl0sImlkIjoxLCJleHAiOjE2NDI3NDYwOTgsImF1dGhvcml0aWVzIjpbInJvb3QiXSwianRpIjoiMTQ3NzgxMGUtZDc1ZC00ZjEyLWIyZGUtZTQ3MjcxZDAzOGMzIiwiY2xpZW50X2lkIjoiY2xpZW50RnJvbnRFbmQifQ.Zh7qTLisyxZ1bzJV4c8IEyXQgWkj5oIpmNEhaA8F4EOWaxG3X98Q0cIgK3LzkKbbRFGmYS363h1PLxLnNAYp6pUaQG2ys476AyIwAwDbjRbhFSBto139xpigOj_qkdudW3JpnQxrWtm3vXBIi4nZRH4F8mgg7SauYDYfq_rcDUpzKVVjfftiku7dF5ePZDtzPmLATC2zSVyCJt79pkwvwsJCk1NszQ9IXKga9MX5Wo65_7jJYWUUosu5vgkfRO6WIfoCOPGIjIhJZ7KBI50d4GGNIpumG7Xpw2-r-x7CU7MBsvzk7EvR0tT_7qx8Mpu8dtj_qYEilum_vG25X0cekQ";
//		final OAuth2AccessToken accessToken = this.resourceServerTokenServices.readAccessToken(token);
//		final OAuth2Authentication authentication = this.resourceServerTokenServices.loadAuthentication(token);
//		final Map<String, ?> convertAccessToken = this.jwtAccessTokenConverter.convertAccessToken(accessToken, authentication);
//		assertNotNull(convertAccessToken);
//		assertTrue(accessToken.isExpired());
//	}
//	
//}
