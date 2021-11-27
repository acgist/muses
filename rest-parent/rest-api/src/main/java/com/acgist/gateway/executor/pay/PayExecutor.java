package com.acgist.gateway.executor.pay;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.pay.PayRequest;

/**
 * <p>执行器 - 交易</p>
 * 
 * @author acgist
 */
@Component
@Scope("prototype")
public class PayExecutor extends GatewayExecutor<PayRequest> {

	public static final PayExecutor newInstance(ApplicationContext context) {
		return context.getBean(PayExecutor.class);
	}
	
	@Override
	public void execute() {
		final String orderId = this.request.getOrderId();
		if("fail".equals(orderId)) {
			this.session.buildFail(GatewayCode.CODE_9999);
		} else if ("exception".equals(orderId)) {
			throw new RuntimeException();
		} else {
			this.session.putResponse("status", "success").buildSuccess();
		}
	}

}
