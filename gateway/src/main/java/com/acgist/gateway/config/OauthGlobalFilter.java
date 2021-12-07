package com.acgist.gateway.config;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.acgist.boot.StringUtils;
import com.acgist.boot.pojo.bean.User;
import com.nimbusds.jose.JWSObject;

import reactor.core.publisher.Mono;

/**
 * 设置授权登陆信息
 * 
 * @author acgist
 * 
 * TODO：只拦截/rest：不使用golbalfilter gatewayfilter
 */
@Component
public class OauthGlobalFilter implements GlobalFilter, Ordered {

    private static Logger LOGGER = LoggerFactory.getLogger(OauthGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)) {
            return chain.filter(exchange);
        }
        try {
            final String message = token.replace("Bearer ", "");
            final JWSObject jws = JWSObject.parse(message);
            final String user = jws.getPayload().getOrigin().name();
            final ServerHttpRequest request = exchange.getRequest().mutate().header(User.HEADER_NAME, user).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
        	LOGGER.error("Token解析异常：{}", token, e);
		}
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
    
}
