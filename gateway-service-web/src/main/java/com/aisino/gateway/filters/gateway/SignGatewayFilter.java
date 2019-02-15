package com.aisino.gateway.filters.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: xiajun003
 * @Date: 2019/2/15 15:26
 * @Description:
 */
public class SignGatewayFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.err.println("signGatewayFilter start");
        return chain.filter(exchange).then(Mono.fromRunnable(() ->
                System.err.println("signGatewayFilter end")
        ));
    }
}
