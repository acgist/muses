package com.acgist.boot.service;

import java.util.Map;

/**
 * RSA
 * 
 * @author acgist
 */
public interface RsaService {

	/**
	 * 签名
	 */
	String SIGNATURE = "signature";
	
	/**
	 * 内容加密
	 * 
	 * @param content 原始内容
	 * 
	 * @return 加密内容
	 */
	String encrypt(String content);
	
	/**
	 * 内容解密
	 * 
	 * @param content 加密内容
	 * 
	 * @return 原始内容
	 */
	String decrypt(String content);
	
	/**
	 * 签名
	 * 
	 * @param map 签名数据
	 * 
	 * @return 签名内容
	 */
	String signature(Map<String, Object> map);
	
	/**
	 * 验签
	 * 
	 * @param map 签名数据
	 * @param signature 签名
	 * 
	 * @return 是否验证成功
	 */
	boolean verify(Map<String, Object> map, String signature);
	
}
