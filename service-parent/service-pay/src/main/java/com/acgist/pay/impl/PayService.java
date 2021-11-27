package com.acgist.pay.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.acgist.pay.IPayService;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class PayService implements IPayService {

	@Override
	public boolean pay() {
		return false;
	}

}
