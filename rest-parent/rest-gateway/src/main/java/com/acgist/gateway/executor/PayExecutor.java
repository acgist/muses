package com.acgist.gateway.executor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.boot.MessageCode;
import com.acgist.rest.executor.GatewayExecutor;
import com.acgist.rest.request.pay.PayRequest;

/**
 * 交易执行器
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
		if ("fail".equals(orderId)) {
			this.session.buildFail(MessageCode.CODE_9999);
		} else if ("exception".equals(orderId)) {
			throw new RuntimeException();
		} else {
			this.session.putResponse("status", "success").buildSuccess();
		}
	}

}
