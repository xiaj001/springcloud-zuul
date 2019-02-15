package com.aisino.gateway.req;

import com.aisino.gateway.define.ResponseBodyRewriteFunction;
import com.aisino.gateway.filters.gateway.SignGatewayFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

/**
 * @author lili
 * @description ${DESCRIPTION}
 * @create 2018-12-13 13:20
 * @since
 **/
@Configuration
@Log4j2
public class ApiLocator {


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        RouteLocatorBuilder.Builder routes = builder.routes();

        //租务 filters
        Function<GatewayFilterSpec, UriSpec>  zuwuFilters = f -> {
            //签名验证
            f = f.filter(new SignGatewayFilter(),-100000);
            //登录验证

            //限流验证

            //header添加user信息
           // f.addRequestHeader("","");

            //统一异常处理
            f = f.modifyResponseBody(String.class, String.class, new ResponseBodyRewriteFunction());
            return f;
        };

        //租务服务路由
        Function<PredicateSpec, Route.AsyncBuilder> orderServiceRoute  = predicateSpec -> {
            predicateSpec = predicateSpec.order(-100000);
            BooleanSpec op = predicateSpec.path("/user-service/*");
            UriSpec filters = op.filters(zuwuFilters);
            Route.AsyncBuilder result = filters.uri("lb://user-service");
            return result;
        };
        routes = routes.route(orderServiceRoute);


        RouteLocator routeLocator = routes.build();
        log.info("custom RouteLocator is loading ... {}", routeLocator);
        return routeLocator;
    }
}