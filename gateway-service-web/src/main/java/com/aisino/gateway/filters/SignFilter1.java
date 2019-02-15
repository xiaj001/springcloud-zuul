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
public class SignFilter1 extends AbstractGlobalFilter{

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


        System.err.println("signFilter1 pre handler ");
        System.err.println(RequestUtil.getRequestParam(exchange,"a"));
        System.err.println(RequestUtil.getRequestParam(exchange,"b"));
        System.err.println(RequestUtil.getRequestParam(exchange,"a"));
        System.err.println(RequestUtil.getRequestParam(exchange,"b"));


        //throw new RuntimeException("test exception");
/*        System.err.println(getRequestParam(exchange));

        System.err.println(getRequestParam(exchange));

        System.err.println(getRequestParam2(exchange));

        System.err.println(getRequestParam2(exchange));

        System.err.println(resolveBodyFromRequest(exchange.getRequest().getBody()));

        System.err.println(getRequestParam(exchange));*/


    }




    private String resolveBodyFromRequest(Flux<DataBuffer> body) {
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });

        String s = bodyRef.get();
        System.err.println(s);
        return s;
    }

    @Override
    public void postHandler(ServerWebExchange exchange){
        System.err.println("signFilter1 post handler ");
        //throw new RuntimeException("test exception");
    }



    private String getRequestParam2(ServerWebExchange exchange){

              /*  fluxBody.map(dateBuffer ->{
            System.err.println(66666666);
             return "";
                }
        ).subscribe();*/


        AtomicReference<String> s = new AtomicReference<>();
        exchange.getRequest().getBody().map(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };
            return ServerRequest.create(exchange.mutate().request(mutatedRequest).build(), messageReaders)
                    .bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        s.set(resolveBodyFromRequest(cachedFlux));
                    });
        }).subscribe();
        return s == null ? null : s.get();
    }





    private String getRequestParam(ServerWebExchange exchange){


        HttpMethod method = exchange.getRequest().getMethod();
        if ( method == HttpMethod.POST){
            AtomicReference<String> s = new AtomicReference<>();
            AtomicReference<Flux<DataBuffer>> data = new AtomicReference<>();
            exchange.getRequest().getBody().flatMap(dataBuffer -> {
                DataBufferUtils.retain(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        data.set(cachedFlux);
                        return cachedFlux;
                    }
                };
                return ServerRequest.create(exchange.mutate().request(mutatedRequest).build(), messageReaders)
                        .bodyToMono(String.class).doOnNext((value) -> s.set(value));
            }).subscribe();
            data.get().subscribe(
                    buffer -> {
                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                        DataBufferUtils.release(buffer);
                        s.set(charBuffer.toString());
                    });
            return s == null ? null : s.get();

        }else if (method == HttpMethod.GET){

            return exchange.getRequest().getPath().toString();

        }

        return null;
    }


}
