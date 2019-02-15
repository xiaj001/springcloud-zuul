package com.aisino.gateway.req;

import com.aisino.gateway.define.ResponseBodyRewriteFunction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

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
            //登录验证

            //header添加user信息
           // f.addRequestHeader("","");
            //统一异常处理
            f.modifyResponseBody(String.class,String.class,new ResponseBodyRewriteFunction());
            return f;
        };

        //租务服务路由
        Function<PredicateSpec, Route.AsyncBuilder> orderServiceRoute  = predicateSpec -> {
            BooleanSpec op = predicateSpec.path("/user-service/*");
            UriSpec filters = op.filters(zuwuFilters);
            Route.AsyncBuilder result = filters.uri("lb://user-service");
            return result;
        };
        routes = routes.route(orderServiceRoute);


        //房源服务路由
        /*Function<PredicateSpec, Route.AsyncBuilder> houseServiceRoute  = predicateSpec -> {
            BooleanSpec.BooleanOpSpec op = predicateSpec.path("/baidu").and();
            BooleanSpec path = op.path("");
            UriSpec filters1 = path.filters(zuwuFilters);
            Route.AsyncBuilder result = filters1.uri("http://www.baidu.com");
            return result;
        };
        routes = routes.route(houseServiceRoute);*/

        RouteLocator routeLocator = routes.build();
        log.info("custom RouteLocator is loading ... {}", routeLocator);
        return routeLocator;
    }
}