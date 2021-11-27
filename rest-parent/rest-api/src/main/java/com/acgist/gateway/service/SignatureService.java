package com.acgist.gateway.service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>Service - 签名</p>
 * <p>公钥私钥生成：http://web.chacuo.net/netrsakeypair</p>
 * 
 * @author acgist
 */
@Service
public class SignatureService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignatureService.class);
	
	/**
	 * <p>密钥算法</p>
	 */
	private static final String RSA_ALGORITHM = "RSA";
	/**
	 * <p>签名算法</p>
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

	/**
	 * <p>文本公钥</p>
	 */
	@Value("${system.public.key:}")
	private String publicKeyValue;
	/**
	 * <p>文本私钥</p>
	 */
	@Value("${system.private.key:}")
	private String privateKeyValue;
	/**
	 * <p>公钥</p>
	 */
	private PublicKey publicKey;
	/**
	 * <p>私钥</p>
	 */
	private PrivateKey privateKey;

	public SignatureService() {
	}

	public SignatureService(String publicKeyValue, String privateKeyValue) {
		this.publicKeyValue = publicKeyValue;
		this.privateKeyValue = privateKeyValue;
	}

	@PostConstruct
	public void init() {
		LOGGER.info("初始化公钥");
		this.publicKey = stringToPublicKey(this.publicKeyValue);
		LOGGER.info("初始化私钥");
		this.privateKey = stringToPrivateKey(this.privateKeyValue);
	}

	/**
	 * <p>验签</p>
	 * 
	 * @param data 验签数据
	 * 
	 * @return 是否验证成功
	 */
	public boolean verify(Map<String, Object> data) {
		if (data == null) {
			return false;
		}
		final String digest = dataToDigest(data);
		final String signature = (String) data.get(GatewayService.GATEWAY_SIGNATURE);
		try {
			return verify(digest, signature, this.publicKey);
		} catch (Exception e) {
			LOGGER.error("验签异常", e);
		}
		return false;
	}

	/**
	 * <p>签名</p>
	 * 
	 * @param data 签名数据
	 */
	public void signature(final Map<String, Object> data) {
		final String digest = dataToDigest(data);
		final String signature = signature(digest, this.privateKey);
		data.put(GatewayService.GATEWAY_SIGNATURE, signature);
	}

	/**
	 * <p>获取散列值</p>
	 * 
	 * @param data 数据
	 * 
	 * @return 散列值
	 */
	private static final String dataToDigest(final Map<String, Object> data) {
		final StringBuilder builder = new StringBuilder();
		final TreeMap<String, String> sortData = new TreeMap<>();
		data.entrySet().stream()
			.filter(entry -> !GatewayService.GATEWAY_SIGNATURE.equals(entry.getKey()))
			.forEach(entry -> sortData.put(entry.getKey(), String.valueOf(entry.getValue())));
		sortData.entrySet().forEach(entry -> builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));
		if (builder.length() != 0) {
			builder.setLength(builder.length() - 1);
		}
		return builder.toString();
	}

	/**
	 * <p>签名</p>
	 * 
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * 
	 * @return 签名
	 */
	private static final String signature(String data, PrivateKey privateKey) {
		if (data == null) {
			return null;
		}
		return Base64.getMimeEncoder().encodeToString(signature(data.getBytes(), privateKey));
	}

	/**
	 * <p>签名</p>
	 * 
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * 
	 * @return 签名
	 */
	private static final byte[] signature(byte[] data, PrivateKey privateKey) {
		try {
			final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			LOGGER.error("签名异常", e);
		}
		return null;
	}

	/**
	 * <p>验签</p>
	 * 
	 * @param data 待验证数据
	 * @param signature 签名数据
	 * @param publicKey 公钥
	 * 
	 * @return 是否验证成功
	 */
	private static final boolean verify(String data, String signature, PublicKey publicKey) {
		if (data == null || signature == null) {
			return false;
		}
		return verify(data.getBytes(), Base64.getMimeDecoder().decode(signature), publicKey);
	}

	/**
	 * <p>验签</p>
	 * 
	 * @param data 待验签数据
	 * @param signature 签名数据
	 * @param publicKey 公钥
	 * 
	 * @return 是否验证成功
	 */
	private static final boolean verify(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
		try {
			final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(signatureBytes);
		} catch (Exception e) {
			LOGGER.error("验签异常", e);
		}
		return false;
	}

	/**
	 * <p>字符串转公钥</p>
	 * 
	 * @param key 字符串
	 * 
	 * @return 公钥
	 */
	private static final PublicKey stringToPublicKey(String key) {
		final byte[] bytes = Base64.getMimeDecoder().decode(key);
		final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		try {
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("字符串转公钥异常：{}", key, e);
		}
		return null;
	}

	/**
	 * <p>字符串转私钥</p>
	 * 
	 * @param key 字符串
	 * 
	 * @return 私钥
	 */
	private static final PrivateKey stringToPrivateKey(String key) {
		final byte[] bytes = Base64.getMimeDecoder().decode(key);
		final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		try {
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("字符串转私钥异常：{}", key, e);
		}
		return null;
	}

}
