package com.acgist.boot;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA工具
 * 
 * 注意：Cipher非线程安全
 * 
 * @author acgist
 */
public final class RsaUtils {

	/**
	 * 密钥长度
	 */
	private static final int KEY_LENGTH = 2048;
	/**
	 * RSA最大加密明文大小：密钥长度 / 8 - 11
	 * 11：填充长度
	 */
	private static final int MAX_ENCRYPT_BLOCK = KEY_LENGTH / 8 - 11;
	/**
	 * RSA最大解密密文大小：密钥长度 / 8
	 */
	private static final int MAX_DECRYPT_BLOCK = KEY_LENGTH / 8;
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
	 */
	public static final Map<String, String> buildKey() {
		KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw MessageCodeException.of(e, "未知算法");
		}
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
		final byte[] bytes = StringUtils.base64Decode(content);
		final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		try {
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw MessageCodeException.of(e, "加载公钥失败");
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
		} catch (IOException | CertificateException e) {
			throw MessageCodeException.of(e, "加载公钥失败");
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
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw MessageCodeException.of(e, "加载私钥失败");
		}
	}

	/**
	 * 加载私钥文件
	 * 
	 * @param path 文件路径
	 * @param password 密码
	 * 
	 * @return 私钥
	 */
	public static final PrivateKey loadFilePrivateKey(String path, String password) {
		try (final InputStream input = new FileInputStream(path)) {
			String aliase = null;
			final KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
			keyStore.load(input, password.toCharArray());
			final Enumeration<String> aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				aliase = aliases.nextElement();
			}
			return (PrivateKey) keyStore.getKey(aliase, password.toCharArray());
		} catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | UnrecoverableKeyException e) {
			throw MessageCodeException.of(e, "加载私钥失败");
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data 原始数据
	 * @param publicKey 公钥
	 * 
	 * @return Base64编码加密数据
	 */
	public static final String encrypt(String data, PublicKey publicKey) {
		return StringUtils.base64Encode(encrypt(data.getBytes(), publicKey));
	}

	/**
	 * 公钥加密
	 * 
	 * @param data 原始数据
	 * @param publicKey 公钥
	 * 
	 * @return Base64编码加密数据
	 */
	private static final byte[] encrypt(byte[] data, PublicKey publicKey) {
		if (Objects.isNull(data) || Objects.isNull(publicKey)) {
			return null;
		}
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int index = 0;
			final int length = data.length;
			final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			while (index < length) {
				// 注意：这里不是使用update方法
				if (length - index > MAX_ENCRYPT_BLOCK) {
					out.write(cipher.doFinal(data, index, MAX_ENCRYPT_BLOCK));
				} else {
					out.write(cipher.doFinal(data, index, length - index));
				}
				index += MAX_ENCRYPT_BLOCK;
			}
			return out.toByteArray();
		} catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw MessageCodeException.of(e, "公钥加密失败");
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param data Base64编码加密数据
	 * @param privateKey 私钥
	 * 
	 * @return 原始数据
	 */
	public static final String decrypt(String data, PrivateKey privateKey) {
		return new String(decrypt(StringUtils.base64Decode(data), privateKey));
	}

	/**
	 * 私钥解密
	 * 
	 * @param data 加密数据
	 * @param privateKey 私钥
	 * 
	 * @return 原始数据
	 */
	private static final byte[] decrypt(byte[] data, PrivateKey privateKey) {
		if (Objects.isNull(data) || Objects.isNull(privateKey)) {
			return null;
		}
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int index = 0;
			final int length = data.length;
			final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			while (index < length) {
				// 注意：这里不是使用update方法
				if (length - index > MAX_DECRYPT_BLOCK) {
					out.write(cipher.doFinal(data, index, MAX_DECRYPT_BLOCK));
				} else {
					out.write(cipher.doFinal(data, index, length - index));
				}
				index += MAX_DECRYPT_BLOCK;
			}
			return out.toByteArray();
		} catch (IOException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			throw MessageCodeException.of(e, "私钥解密失败");
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
		if (Objects.isNull(data) || Objects.isNull(privateKey)) {
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
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw MessageCodeException.of(e, "签名失败");
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
		if (Objects.isNull(data) || Objects.isNull(signature) || Objects.isNull(publicKey)) {
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
	 * 
	 * @return 是否成功
	 */
	private static final boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
		try {
			final Signature signatureTool = Signature.getInstance(SIGNATURE_ALGORITHM);
			signatureTool.initVerify(publicKey);
			signatureTool.update(data);
			return signatureTool.verify(signature);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw MessageCodeException.of(e, "验签失败");
		}
	}

	/**
	 * 读取公钥文件序列号
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
		} catch (IOException | CertificateException e) {
			throw MessageCodeException.of(e, "读取公钥序列号失败");
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