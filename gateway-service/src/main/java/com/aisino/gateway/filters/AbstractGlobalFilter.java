package com.aisino.gateway.filters;

import com.aisino.gateway.dec.PartnerServerWebExchangeDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: xiajun003
 * @Date: 2019/1/2 20:15
 * @Description:
 */
public abstract  class AbstractGlobalFilter implements GlobalFilter,Ordered {

    private static final Logger log = LoggerFactory.getLogger(AbstractGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (!shouldFilter(exchange)){
            return chain.filter(exchange);
        }

        PartnerServerWebExchangeDecorator exchangeDecorator = new PartnerServerWebExchangeDecorator(exchange);


            preHandler(exchangeDecorator);


        return chain.filter(exchangeDecorator).then(Mono.fromRunnable(() ->
            postHandler(exchange)
        ));

    }


    public void preHandler(ServerWebExchange exchange){

    }


    public void postHandler(ServerWebExchange exchange){

    }


    public abstract boolean shouldFilter(ServerWebExchange exchange);

    public abstract int filterOrder();

    @Override
    public int getOrder(){
        return filterOrder();
    }

}
