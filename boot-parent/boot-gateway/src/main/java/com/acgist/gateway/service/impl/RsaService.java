package com.acgist.gateway.service.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.acgist.boot.RsaUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * RSA
 * 
 * @author acgist
 */
@Slf4j
public class RsaService {

	/**
	 * 签名
	 */
	private static final String SIGNATURE = "signature";
	
	/**
	 * 文本公钥
	 */
	@Value("${system.public.key:}")
	private String publicKeyValue;
	/**
	 * 文本私钥
	 */
	@Value("${system.private.key:}")
	private String privateKeyValue;
	/**
	 * 公钥
	 */
	private PublicKey publicKey;
	/**
	 * 私钥
	 */
	private PrivateKey privateKey;

	public RsaService() {
	}

	public RsaService(String publicKeyValue, String privateKeyValue) {
		this.publicKeyValue = publicKeyValue;
		this.privateKeyValue = privateKeyValue;
	}

	@PostConstruct
	public void init() {
		log.info("加载公钥私钥");
		this.publicKey = RsaUtils.loadPublicKey(this.publicKeyValue);
		this.privateKey = RsaUtils.loadPrivateKey(this.privateKeyValue);
	}

	/**
	 * 内容加密
	 * 
	 * @param content 原始内容
	 * 
	 * @return 加密内容
	 */
	public String encrypt(String content) {
		return RsaUtils.encrypt(content, this.publicKey);
	}
	
	/**
	 * 内容解密
	 * 
	 * @param content 加密内容
	 * 
	 * @return 原始内容
	 */
	public String decrypt(String content) {
		return RsaUtils.decrypt(content, this.privateKey);
	}
	
	/**
	 * 计算签名
	 * 
	 * @param map 签名数据
	 * 
	 * @return 签名内容
	 */
	public String signature(Map<String, Object> map) {
		final String content = this.join(map);
		return RsaUtils.signature(content, this.privateKey);
	}
	
	/**
	 * 验签
	 * 
	 * @param map 数据
	 * @param signature 签名
	 * 
	 * @return 是否验证成功
	 */
	public boolean verify(Map<String, Object> map, String signature) {
		final String content = this.join(map);
		return RsaUtils.verify(content, signature, this.publicKey);
	}
	
	/**
	 * 数据拼接
	 * 
	 * @param map 数据
	 * 
	 * @return 拼接结果
	 */
	private String join(Map<String, Object> map) {
		final Map<String, Object> sortMap = new TreeMap<>((source, target) -> source.compareTo(target));
		sortMap.putAll(map);
		return sortMap.entrySet().stream()
			.filter(entity -> !SIGNATURE.equals(entity.getKey()))
			.map(entity -> entity.getKey() + entity.getValue())
			.collect(Collectors.joining());
	}
	
}
