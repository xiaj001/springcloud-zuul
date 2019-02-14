package com.aisino.gateway.filters;

import com.aisino.gateway.dec.PartnerServerWebExchangeDecorator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: xiajun003
 * @Date: 2019/1/17 11:39
 * @Description:
 */
//@Component
public class SignFilter3 extends AbstractGlobalFilter {
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

        System.err.println(getParamFromRequest(exchange));
        System.err.println(getParamFromRequest(exchange));

        System.err.println(666);
    }


    private String getParamFromRequest(ServerWebExchange exchange){
        Flux<DataBuffer> body = exchange.getRequest().getBody();

        //缓存读取的request body信息
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            //释放buffer资源
            DataBufferUtils.release(dataBuffer);
            bodyRef.set(new String(bytes));
        });//读取request body到缓存

        return bodyRef.get();
    }




}
