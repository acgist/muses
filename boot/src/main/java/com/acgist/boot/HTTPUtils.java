package com.acgist.boot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP工具 
 * 
 * @author acgist
 */
public final class HTTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);
	
	/**
	 * 超时时间
	 */
	private static final int DEFAULT_TIMEOUT = 5 * 1000;
	/**
	 * 浏览器参数
	 */
	private static final Map<String, String> BROWSER_HEADERS = new HashMap<>();
	/**
	 * 复用TCP连接
	 * 不能关闭Client：工具自动管理，不同的域名会使用不同的TCP连接。
	 */
	private static final CloseableHttpClient REUSE_CLIENT;
	/**
	 * 复用连接管理
	 */
	private static final PoolingHttpClientConnectionManager MANAGER;
	
	static {
		BROWSER_HEADERS.put("User-Agent", "ACGIST/1.0.0 +(https://www.acgist.com)");
		BROWSER_HEADERS.put("Content-Type", "application/json");
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", createSSLConnSocketFactory()).build();
		MANAGER = new PoolingHttpClientConnectionManager(registry);
		REUSE_CLIENT = HttpClients.custom().setRedirectStrategy(DefaultRedirectStrategy.INSTANCE).setConnectionManager(MANAGER).build();
	}
	
	public static final String get(String url) {
		return get(url, null, null, DEFAULT_TIMEOUT);
	}

	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}

	public static final String get(String url, String body) {
		return get(url, body, null, DEFAULT_TIMEOUT);
	}

	public static final String get(String url, String body, Map<String, String> headers, int timeout) {
		final HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig(timeout));
		addHeaders(get, headers);
		if(StringUtils.isNotEmpty(body)) {
			get.setEntity(new StringEntity(body, SystemConfig.DEFAULT_CHARSET));
		}
		return execute(get);
	}
	
	private static final class HttpGet extends HttpEntityEnclosingRequestBase {

		public final static String METHOD_NAME = "GET";

		@Override
		public String getMethod() {
			return METHOD_NAME;
		}

		HttpGet(final String uri) {
			super();
			setURI(URI.create(uri));
		}
		
	}
	
	public static final String post(String url, Map<String, Object> body) {
		return post(url, body, null, DEFAULT_TIMEOUT);
	}
	
	public static final String post(String url, Map<String, Object> body, int timeout) {
		return post(url, body, null, timeout);
	}
	
	public static final String post(String url, Map<String, Object> body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig(timeout));
		addHeaders(post, headers);
		post.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE + ";charset=" + SystemConfig.DEFAULT_CHARSET);
		if(MapUtils.isNotEmpty(body)) {
			try {
				post.setEntity(new UrlEncodedFormEntity(generateParams(body), SystemConfig.DEFAULT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("设置请求参数异常", e);
			}
		}
		return execute(post);
	}
	
	public static final String post(String url, String body) {
		return post(url, body, null, DEFAULT_TIMEOUT);
	}
	
	public static final String post(String url, String body, int timeout) {
		return post(url, body, null, timeout);
	}
	
	public static final String post(String url, String body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig(timeout));
		addHeaders(post, headers);
		if(StringUtils.isNotEmpty(body)) {
			post.setEntity(new StringEntity(body, SystemConfig.DEFAULT_CHARSET));
		}
		return execute(post);
	}
	
	private static final RequestConfig requestConfig(int timeout) {
		return RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
	}
	
	private static final void addHeaders(HttpRequestBase base, Map<String, String> headers) {
		BROWSER_HEADERS.forEach(base::addHeader);
		if(MapUtils.isNotEmpty(headers)) {
			headers.forEach(base::addHeader);
		}
	}

	/**
	 * <p>生成参数集合</p>
	 * 
	 * @param body 数据
	 * 
	 * @return 参数集合
	 */
	private static final List<NameValuePair> generateParams(Map<String, Object> body) {
		if(MapUtils.isNotEmpty(body)) {
			return body.entrySet().stream()
				.map(entity -> new BasicNameValuePair(entity.getKey(), String.valueOf(entity.getValue())))
				.collect(Collectors.toList());
		}
		return List.of();
	}
	
	/**
	 * <p>执行请求</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 返回内容
	 */
	private static final String execute(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		try {
			response = REUSE_CLIENT.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK) {
				LOGGER.error("HTTP返回错误状态：{}", statusCode);
			}
			return EntityUtils.toString(response.getEntity(), SystemConfig.DEFAULT_CHARSET);
		} catch(Exception e) {
			LOGGER.error("发送请求异常", e);
		} finally {
			close(response);
		}
		return null;
	}
	
	/**
	 * <p>SSL工厂</p>
	 */
	private static final SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslFactory = null;
		try {
			sslContext = SSLContextBuilder.create().setProtocol("TLSv1.2").loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有证书
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOGGER.error("创建SSL工程异常", e);
		}
		sslFactory = new SSLConnectionSocketFactory(sslContext);
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

	/**
	 * <p>关闭资源</p>
	 * 
	 * @param response 请求响应
	 */
	private static final void close(CloseableHttpResponse response) {
		if(response != null) {
			try {
				response.close();
			} catch (IOException e) {
				response = null;
				LOGGER.error("关闭响应异常", e);
			}
		}
	}
	
	/**
	 * <p>工具关闭</p>
	 */
	public static final void shutdown() {
		if(REUSE_CLIENT != null) {
			try {
				REUSE_CLIENT.close();
			} catch (IOException e) {
				LOGGER.error("关闭连接异常", e);
			}
		}
		if(MANAGER != null) {
			MANAGER.shutdown();
		}
	}
	
}

/**
 * <p>请求异常</p>
 */
class RequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequestException(String content) {
		super(content);
	}
	
}