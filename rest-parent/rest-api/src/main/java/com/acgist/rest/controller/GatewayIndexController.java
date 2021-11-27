package com.acgist.rest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.rest.GatewaySession;
import com.acgist.rest.service.GatewayService;

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
