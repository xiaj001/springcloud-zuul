package com.aisino.gateway.req;

import com.aisino.gateway.filters.gateway.SignGatewayFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
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

    @Resource(name = "responseBodyRewriteFunction")
    private RewriteFunction rewriteFunction;

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
            f = f.modifyResponseBody(String.class, String.class, rewriteFunction);
            return f;
        };

        //租务服务路由
        Function<PredicateSpec, Route.AsyncBuilder> zuwuServiceRoute  = predicateSpec -> {
            //注意:此处的匹配，如果只有一个 * ,则不能全部匹配
            BooleanSpec op = predicateSpec.path("/user-service/**");
            UriSpec filters = op.filters(zuwuFilters);
            Route.AsyncBuilder result = filters.uri("lb://user-service");
            return result;
        };
        routes = routes.route(zuwuServiceRoute);


        RouteLocator routeLocator = routes.build();
        return routeLocator;
    }
}