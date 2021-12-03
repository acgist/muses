package com.acgist.boot;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA工具
 * 
 * 注意：Cipher非线程安全
 * 
 * @author acgist
 */
public class RsaUtils {
	
	/**
	 * 密钥长度
	 */
	private static final int KEY_LENGTH = 1024;
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	/**
	 * CER文件
	 */
	private static final String CER_TYPE = "X.509";
	/**
	 * PFX文件
	 */
	private static final String PFX_TYPE = "PKCS12";
	/**
	 * 加密算法：RSA = RSA/ECB/PKCS1Padding
	 */
	private static final String RSA_ALGORITHM = "RSA";
	/**
	 * 签名算法
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY = "publicKey";
	/**
	 * 私钥
	 */
	public static final String PRIVATE_KEY = "privateKey";
	
	/**
	 * 生成公钥私钥
	 * 
	 * @return 公钥私钥
	 * 
	 * @throws NoSuchAlgorithmException 未知算法
	 */
	public static final Map<String, String> buildKey() throws NoSuchAlgorithmException {
		final KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		generator.initialize(KEY_LENGTH);
		final KeyPair keyPair = generator.generateKeyPair();
		final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return Map.of(
			PUBLIC_KEY, StringUtils.base64Encode(publicKey.getEncoded()),
			PRIVATE_KEY, StringUtils.base64Encode(privateKey.getEncoded())
		);
	}
	
	/**
	 * 加载Base64编码公钥
	 * 
	 * @param content Base64编码公钥
	 * 
	 * @return 公钥
	 */
	public static final PublicKey loadPublicKey(String content) {
		try {
			final byte[] bytes = StringUtils.base64Decode(content);
			final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 加载公钥文件
	 * 
	 * @param path 文件路径
	 * 
	 * @return 公钥
	 */
	public static final PublicKey loadFilePublicKey(String path) {
		try (final InputStream input = new FileInputStream(path)) {
			final CertificateFactory certificateFactory = CertificateFactory.getInstance(CER_TYPE);
			final Certificate certificate = certificateFactory.generateCertificate(input);
			return certificate.getPublicKey();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 加载Base64编码私钥
	 * 
	 * @param content Base64编码私钥
	 * 
	 * @return 私钥
	 */
	public static final PrivateKey loadPrivateKey(String content) {
		try {
			final byte[] bytes = StringUtils.base64Decode(content);
			final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 加载密钥文件
	 * 
	 * @param path 文件路径
	 * @param password 密码
	 * 
	 * @return 密钥
	 */
	public static final PrivateKey loadFilePrivateKey(String path, String password) {
		try (final InputStream input = new FileInputStream(path)) {
			String aliase = null;
			final KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
			keyStore.load(input, password.toCharArray());
			final Enumeration<String> aliases = keyStore.aliases();
			while(aliases.hasMoreElements()) {
				aliase = aliases.nextElement();
			}
			return (PrivateKey) keyStore.getKey(aliase, password.toCharArray());
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param publicKey 公钥
	 * @param data 原始数据
	 * 
	 * @return Base64编码加密数据
	 */
	public static final String encrypt(PublicKey publicKey, String data) {
		return StringUtils.base64Encode(encrypt(publicKey, data.getBytes()));
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param publicKey 公钥
	 * @param data 原始数据
	 * 
	 * @return Base64编码加密数据
	 */
	private static final byte[] encrypt(PublicKey publicKey, byte[] data) {
		if(publicKey == null || data == null) {
			return null;
		}
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int index = 0;
			final int length = data.length;
			final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			while(index < length) {
				if(length - index > MAX_ENCRYPT_BLOCK) {
					out.write(cipher.update(data, index, MAX_ENCRYPT_BLOCK));
				} else {
					out.write(cipher.doFinal(data, index, length - index));
				}
				index += MAX_ENCRYPT_BLOCK;
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param data Base64编码加密数据
	 * 
	 * @return 原始数据
	 */
	public static final String decrypt(PrivateKey privateKey, String data) {
		return new String(decrypt(privateKey, StringUtils.base64Decode(data)));
	}
	
	/**
	 * 私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param data 加密数据
	 * 
	 * @return 原始数据
	 */
	private static final byte[] decrypt(PrivateKey privateKey, byte[] data) {
		if (privateKey == null || data == null) {
			return null;
		}
		try(final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int index = 0;
			final int length = data.length;
			final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			while(index < length) {
				if(length - index > MAX_DECRYPT_BLOCK) {
					out.write(cipher.update(data, index, MAX_DECRYPT_BLOCK));
				} else {
					out.write(cipher.doFinal(data, index, length - index));
				}
				index += MAX_DECRYPT_BLOCK;
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 签名
	 * 
	 * @param data 签名数据
	 * @param privateKey 私钥
	 * 
	 * @return Base64编码签名
	 */
	public static final String signature(String data, PrivateKey privateKey) {
		if (data == null || privateKey == null) {
			return null;
		}
		return StringUtils.base64Encode(signature(data.getBytes(), privateKey));
	}
	
	/**
	 * 签名
	 * 
	 * @param data 签名数据
	 * @param privateKey 私钥
	 * 
	 * @return 签名
	 */
	private static final byte[] signature(byte[] data, PrivateKey privateKey) {
		try {
			final Signature signatureTool = Signature.getInstance(SIGNATURE_ALGORITHM);
			signatureTool.initSign(privateKey);
			signatureTool.update(data);
			return signatureTool.sign();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 验签
	 * 
	 * @param data 验签数据
	 * @param signature 签名数据
	 * @param publicKey 公钥
	 * 
	 * @return 验签结果
	 */
	public static final boolean verify(String data, String signature, PublicKey publicKey) {
		if (data == null || signature == null || publicKey == null) {
			return false;
		}
		return verify(data.getBytes(), StringUtils.base64Decode(signature), publicKey);
	}
	
	/**
	 * 验签
	 * 
	 * @param data 验签数据
	 * @param signature 签名
	 * @param publicKey 公钥
	 * @return true：通过验证；false：验证失败
	 */
	private static final boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
		try {
			final Signature signatureTool = Signature.getInstance(SIGNATURE_ALGORITHM);
			signatureTool.initVerify(publicKey);
			signatureTool.update(data);
			return signatureTool.verify(signature);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * <p>读取公钥文件序列号</p>
	 * 
	 * @param path 证书路径
	 * 
	 * @return 序列号
	 */
	public static final BigInteger readSerialNumber(String path) {
		try (final InputStream input = new FileInputStream(path)) {
			final CertificateFactory certificateFactory = CertificateFactory.getInstance(CER_TYPE);
			final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);
			return certificate.getSerialNumber();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 公钥转字符串
	 * 
	 * @param publicKey 公钥
	 * 
	 * @return 公钥字符串
	 */
	public static final String toString(PublicKey publicKey) {
		return StringUtils.base64Encode(publicKey.getEncoded());
	}
	
	/**
	 * 私钥转字符串
	 * 
	 * @param publicKey 私钥
	 * 
	 * @return 私钥字符串
	 */
	public static final String toString(PrivateKey privateKey) {
		return StringUtils.base64Encode(privateKey.getEncoded());
	}
	
}