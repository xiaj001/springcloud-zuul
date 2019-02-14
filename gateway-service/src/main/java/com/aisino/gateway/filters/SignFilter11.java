package com.aisino.gateway.filters;

import com.aisino.gateway.utils.RequestUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: xiajun003
 * @Date: 2019/1/7 16:53
 * @Description:
 */
@Component
public class SignFilter11 extends AbstractGlobalFilter{

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
