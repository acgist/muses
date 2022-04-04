package com.acgist.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 慢请求统计
 * 
 * @author acgist
 */
@Slf4j
@Component
public class SlowRequestFilter implements Ordered, GlobalFilter {

	@Value("${system.gateway.slow.request.duration:1000}")
	private long duration;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		final long time = System.currentTimeMillis();
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			final long duration = System.currentTimeMillis() - time;
			if(duration > this.duration) {
				log.info("请求执行过慢：{}-{}", duration, exchange.getRequest().getPath());
			}
		}));
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
