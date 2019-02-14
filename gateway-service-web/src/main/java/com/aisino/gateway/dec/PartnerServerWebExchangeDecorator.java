package com.aisino.gateway.dec;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

/**
 * @author: xiajun003
 * @Date: 2019/1/8 15:44
 * @Description:
 */
public class PartnerServerWebExchangeDecorator extends ServerWebExchangeDecorator {

    private final ServerHttpRequestDecorator requestDecorator;

    public PartnerServerWebExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);
        this.requestDecorator = new PartnerServerHttpRequestDecorator(delegate.getRequest());
    }

    @Override
    public ServerHttpRequest getRequest() {
        return requestDecorator;
    }
}
