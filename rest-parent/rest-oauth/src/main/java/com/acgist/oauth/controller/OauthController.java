package com.acgist.oauth.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.common.RsaUtils;

@RestController
@RequestMapping("/oauth")
public class OauthController {

	@Autowired
	private KeyPair jwtKeyPair;
	
	@GetMapping("/code")
	public String code(String code) {
		return code;
	}
	
	@GetMapping("/public/key")
	public String publicKey() {
		final RSAPublicKey publicKey = (RSAPublicKey) this.jwtKeyPair.getPublic();
		return RsaUtils.toString(publicKey);
	}
	
}
