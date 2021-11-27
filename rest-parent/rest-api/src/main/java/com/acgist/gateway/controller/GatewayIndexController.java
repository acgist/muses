package com.acgist.gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.service.GatewayService;

/**
 * <p>Controller - 网关</p>
 * 
 * @author acgist
 */
@RestController
public class GatewayIndexController {

	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/")
	public Map<String, Object> index() {
		return GatewaySession.getInstance(this.context).buildSuccess().getResponseData();
	}

	@RequestMapping(value = GatewayService.GATEWAY, method = RequestMethod.POST)
	public Map<String, Object> gateway() {
		return GatewaySession.getInstance(this.context).response();
	}
	
}
