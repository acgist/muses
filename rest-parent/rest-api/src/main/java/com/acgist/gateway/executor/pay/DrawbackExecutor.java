package com.acgist.gateway.executor.pay;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.pay.DrawbackRequest;

/**
 * <p>执行器 - 交易退款</p>
 * 
 * @author acgist
 */
@Component
@Scope("prototype")
public class DrawbackExecutor extends GatewayExecutor<DrawbackRequest> {

	public static final DrawbackExecutor newInstance(ApplicationContext context) {
		return context.getBean(DrawbackExecutor.class);
	}
	
	@Override
	protected void execute() {
		this.session.putResponse("status", "success").buildSuccess();
	}

}
