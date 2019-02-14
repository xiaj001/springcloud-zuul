/*
package com.aisino.gateway.filters;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.net.URI;

*/
/**
 * @author: xiajun003
 * @Date: 2019/1/17 11:28
 * @Description:
 *//*

@Component
public class SignFilter2 extends AbstractGlobalFilter {
    @Override
    public boolean shouldFilter(ServerWebExchange exchange) {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public void preHandler(ServerWebExchange exchange) {
        //获取requestBody
        String requestBody = getRequstBody(exchange.getRequest());

    }

        */
/**
         * 获取requestBody
         * 这个方法可能需要优化
         *
         * @param serverRequest
         * @return
         *//*

        private String getRequstBody(ServerHttpRequest serverRequest) {
            StringBuilder stringBuilder = new StringBuilder();
            serverRequest.bodyToMono(String.class).subscribe(s -> {
                stringBuilder.append(s);
            });
            return stringBuilder.toString();
        }

        */
/**
         * 重新封装reqeust
         *
         * @param serverHttpRequest
         * @param requestBody
         * @return
         *//*

        private ServerHttpRequest getRequest(ServerHttpRequest serverHttpRequest, String requestBody) {
            //下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
            URI uri = serverHttpRequest.getURI();
            ServerHttpRequest request = serverHttpRequest.mutate().uri(uri).build();
            DataBuffer bodyDataBuffer = stringToDataBuffer(requestBody);
            Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

            request = new ServerHttpRequestDecorator(request) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return bodyFlux;
                }
            };
            return request;
        }




}
*/
