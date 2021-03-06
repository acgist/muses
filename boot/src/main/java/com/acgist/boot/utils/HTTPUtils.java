package com.acgist.boot.utils;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.model.MessageCodeException;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP??????
 * 
 * @author acgist
 */
@Slf4j
public final class HTTPUtils {

	/**
	 * ????????????
	 * 
	 * ????????????Client???????????????????????????TCP??????
	 */
	private static final CloseableHttpClient CLIENT;

	static {
		final List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "ACGIST/1.0.0 +(https://www.acgist.com)"));
		headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MusesConfig.APPLICATION_JSON_UTF8));
		// SSL???????????????????????????HTTPClient????????????
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", createSSLConnSocketFactory())
//			.register("https", SSLConnectionSocketFactory.getSocketFactory())
			.build();
		final SocketConfig socketConfig = SocketConfig.custom()
//			.setSoLinger(1)
			.setSoTimeout(MusesConfig.TIMEOUT)
			.setTcpNoDelay(true)
			.setSoKeepAlive(true)
			.setSoReuseAddress(true)
			.build();
//		final ConnectionConfig connectionConfig = ConnectionConfig.custom()
//			.setCharset(MusesConfig.CHARSET)
//			.build();
		// ??????????????????????????????????????????
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(256);
		connectionManager.setDefaultMaxPerRoute(8);
		connectionManager.setDefaultSocketConfig(socketConfig);
//		connectionManager.setDefaultConnectionConfig(connectionConfig);
		// ???????????????????????????
//		connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
//		connectionManager.closeExpiredConnections();
		final RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(MusesConfig.TIMEOUT)
			.setConnectTimeout(MusesConfig.TIMEOUT)
			.setConnectionRequestTimeout(MusesConfig.TIMEOUT)
			.build();
		CLIENT = HttpClients.custom()
//			.setProxy(null)
//			.setRetryHandler(null)
			.setDefaultHeaders(headers)
//			.setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
//			.setRedirectStrategy(DefaultRedirectStrategy.INSTANCE)
//			.setKeepAliveStrategy(null)
			.setConnectionManager(connectionManager)
			// ????????????????????????
//			.setConnectionManagerShared(true)
//			.setConnectionReuseStrategy(null)
//			.setDefaultConnectionConfig(null)
//			.setDefaultCookieStore(null)
//			.setDefaultCookieSpecRegistry(null)
//			.setDefaultSocketConfig(socketConfig)
			.setDefaultRequestConfig(requestConfig)
			// ????????????
			.evictIdleConnections(30, TimeUnit.SECONDS)
//			.evictExpiredConnections()
//			.setConnectionTimeToLive(1, TimeUnit.MINUTES)
//			.setMaxConnTotal(256)
//			.setMaxConnPerRoute(4)
//			.disableAuthCaching()
//			.disableCookieManagement()
			// ??????????????????
			.disableAutomaticRetries()
			.build();
	}

	/**
	 * ??????GET??????
	 * 
	 * @param url ????????????
	 * 
	 * @return ????????????
	 */
	public static final String get(String url) {
		return get(url, null, null, MusesConfig.TIMEOUT);
	}

	/**
	 * ??????GET??????
	 * 
	 * @param url ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}

	/**
	 * ??????GET??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * 
	 * @return ????????????
	 */
	public static final String get(String url, String body) {
		return get(url, body, null, MusesConfig.TIMEOUT);
	}

	/**
	 * ??????GET??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * @param headers ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String get(String url, String body, Map<String, String> headers, int timeout) {
		final HttpGet get = new HttpGet(url);
		addHeaders(get, headers);
		requestConfig(get, timeout);
		if (StringUtils.isNotEmpty(body)) {
			get.setEntity(new StringEntity(body, MusesConfig.CHARSET_VALUE));
		}
		return execute(get);
	}

	/**
	 * ????????????????????????GET??????
	 * 
	 * @author acgist
	 */
	private static final class HttpGet extends HttpEntityEnclosingRequestBase {

		public final static String METHOD_NAME = "GET";

		@Override
		public String getMethod() {
			return METHOD_NAME;
		}

		HttpGet(final String uri) {
			this.setURI(URI.create(uri));
		}

	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, Map<String, Object> body) {
		return post(url, body, null, MusesConfig.TIMEOUT);
	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, Map<String, Object> body, int timeout) {
		return post(url, body, null, timeout);
	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * @param headers ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, Map<String, Object> body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		addHeaders(post, headers);
		requestConfig(post, timeout);
		post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		if (MapUtils.isNotEmpty(body)) {
			post.setEntity(new UrlEncodedFormEntity(buildFormParams(body), MusesConfig.CHARSET));
		}
		return execute(post);
	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, String body) {
		return post(url, body, null, MusesConfig.TIMEOUT);
	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, String body, int timeout) {
		return post(url, body, null, timeout);
	}

	/**
	 * ??????POST??????
	 * 
	 * @param url ????????????
	 * @param body ????????????
	 * @param headers ????????????
	 * @param timeout ????????????
	 * 
	 * @return ????????????
	 */
	public static final String post(String url, String body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		addHeaders(post, headers);
		requestConfig(post, timeout);
		if (StringUtils.isNotEmpty(body)) {
			post.setEntity(new StringEntity(body, MusesConfig.CHARSET_VALUE));
		}
		return execute(post);
	}

	/**
	 * ??????????????????
	 * 
	 * @param request ??????
	 * @param headers ??????
	 */
	private static final void addHeaders(HttpRequestBase request, Map<String, String> headers) {
		if (MapUtils.isNotEmpty(headers)) {
			headers.forEach(request::addHeader);
		}
	}

	/**
	 * ??????????????????
	 * 
	 * @param request ??????
	 * @param timeout ????????????
	 */
	private static final void requestConfig(HttpRequestBase request, int timeout) {
		if (timeout <= 0 || timeout == MusesConfig.TIMEOUT) {
			return;
		}
		final RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(timeout)
			.setConnectTimeout(timeout)
			.setConnectionRequestTimeout(timeout)
			.build();
		request.setConfig(requestConfig);
	}

	/**
	 * ??????????????????
	 * 
	 * @param body ????????????
	 * 
	 * @return ????????????
	 */
	private static final List<NameValuePair> buildFormParams(Map<String, Object> body) {
		if (MapUtils.isNotEmpty(body)) {
			return body.entrySet().stream()
				.map(entity -> new BasicNameValuePair(entity.getKey(), String.valueOf(entity.getValue())))
				.collect(Collectors.toList());
		}
		return List.of();
	}

	/**
	 * ????????????
	 * 
	 * @param request ??????
	 * 
	 * @return ????????????
	 */
	private static final String execute(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		try {
			response = CLIENT.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.warn("HTTP?????????????????????{}-{}-{}", statusCode, request, response);
			}
			return EntityUtils.toString(response.getEntity(), MusesConfig.CHARSET_VALUE);
		} catch (ParseException | IOException e) {
			throw MessageCodeException.of(e);
		} finally {
			close(response);
		}
	}

	/**
	 * ????????????
	 * 
	 * @param response ??????
	 */
	private static final void close(CloseableHttpResponse response) {
		if (response != null) {
			try {
				// ????????????
				response.close();
			} catch (IOException e) {
				log.error("??????????????????", e);
			}
		}
	}

	/**
	 * ????????????
	 * 
	 * ?????????????????????????????????
	 */
	public static final void shutdown() {
		if (CLIENT != null) {
			try {
				CLIENT.close();
			} catch (IOException e) {
				log.error("??????????????????", e);
			}
		}
	}
	
	/**
	 * ??????RestTemplate
	 * 
	 * @return RestTemplate
	 */
	public static final RestTemplate buildRestTemplate() {
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(CLIENT);
		final BufferingClientHttpRequestFactory bufferingRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
		final RestTemplate restTemplate = new RestTemplate(bufferingRequestFactory);
		return restTemplate;
	}

	/**
	 * ??????SSL??????
	 * 
	 * @return SSL??????
	 */
	private static final SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslFactory = null;
		final TrustStrategy trustStrategy = new TrustStrategy() {
			// ??????????????????
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		};
		try {
			sslContext = SSLContextBuilder
				.create()
				.setProtocol("TLSv1.2")
				.setSecureRandom(new SecureRandom())
				.loadTrustMaterial(null, trustStrategy)
				.build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("??????SSL????????????", e);
		}
		sslFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
//		sslFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
//		sslFactory = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1.2", "TLSv1.3"}, new String[] {
//			"TLS_AES_128_GCM_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
//			"TLS_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256"
//		}, new HostnameVerifier() {
//			@Override
//			public boolean verify(String host, SSLSession session) {
//				return true;
//			}
//		});
		return sslFactory;
	}

}