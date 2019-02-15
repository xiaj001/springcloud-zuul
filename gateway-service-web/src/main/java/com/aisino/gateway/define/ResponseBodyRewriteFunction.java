package com.aisino.gateway.define;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: xiajun003
 * @Date: 2019/1/29 16:33
 * @Description:
 */
@Component("responseBodyRewriteFunction")
public class ResponseBodyRewriteFunction implements RewriteFunction<String,String> {
    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String s) {
        System.err.println(s);
        return Mono.just("test body ......"+s);
    }




    /*public Object apply(Object object, Object object2) {
        System.err.println("ResponseBodyRewriteFunction ========== ||||||||||"  + object);
        return object;
    }*/
}
