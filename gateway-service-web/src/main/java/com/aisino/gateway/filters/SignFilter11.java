package com.aisino.gateway.filters;

import com.aisino.gateway.filters.global.AbstractGlobalFilter;
import com.aisino.gateway.utils.RequestUtil;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * @author: xiajun003
 * @Date: 2019/1/7 16:53
 * @Description:
 */
@Component
public class SignFilter11 extends AbstractGlobalFilter {

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public boolean shouldFilter(ServerWebExchange exchange) {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public void preHandler(ServerWebExchange exchange){

        System.err.println("signFilter11 pre handler ");
        System.err.println(RequestUtil.getRequestParam(exchange,"a"));
        System.err.println(RequestUtil.getRequestParam(exchange,"b"));
        System.err.println(RequestUtil.getRequestParam(exchange,"a"));
        System.err.println(RequestUtil.getRequestParam(exchange,"b"));


    }


    @Override
    public void postHandler(ServerWebExchange exchange){
        System.err.println("signFilter11 post handler ");
       // throw new RuntimeException("test exception ");
    }


}
