package com.aisino.gateway.filters;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author: xiajun003
 * @Date: 2019/1/28 17:20
 * @Description:
 */
//@Component
public class Test extends AbstractGlobalFilter {


    @Override
    public  Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {



        ServerHttpResponse originalResponse = exchange.getResponse();

        System.err.println("test filter");
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if ( body instanceof Flux) {

                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        StringBuffer sb = new StringBuffer();

                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);

                            try {
                                sb.append(new String(content, "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        });
                        String s = "body test"+sb.toString();

                        System.out.println(s);



                        //如果不重新设置长度则收不到消息。
                        originalResponse.getHeaders().setContentLength(s.length());

                        return bufferFactory().wrap(s.getBytes());
                    }));


                }

                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public boolean shouldFilter(ServerWebExchange exchange) {
        return true;
    }

    @Override
    public int filterOrder() {
        return -1;
    }
}
