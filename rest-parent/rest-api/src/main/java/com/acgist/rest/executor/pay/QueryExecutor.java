package com.acgist.rest.executor.pay;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.rest.executor.GatewayExecutor;
import com.acgist.rest.request.pay.QueryRequest;

/**
 * <p>执行器 - 交易查询</p>
 * 
 * @author acgist
 */
@Component
@Scope("prototype")
public class QueryExecutor extends GatewayExecutor<QueryRequest> {

	public static final QueryExecutor newInstance(ApplicationContext context) {
		return context.getBean(QueryExecutor.class);
	}
	
	@Override
	protected void execute() {
		this.session.putResponse("status", "success").buildSuccess();
	}

}
