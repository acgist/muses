package com.acgist.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 慢请求统计
 * 
 * @author acgist
 */
@Component
public class SlowRequestFilter implements Ordered, GlobalFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlowRequestFilter.class);
	
	@Value("${system.gateway.slow.request.duration:1000}")
	private long duration;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		final long time = System.currentTimeMillis();
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			final long duration = System.currentTimeMillis() - time;
			if(duration > this.duration) {
				LOGGER.info("请求执行过慢：{}-{}", duration, exchange.getRequest().getPath());
			}
		}));
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
