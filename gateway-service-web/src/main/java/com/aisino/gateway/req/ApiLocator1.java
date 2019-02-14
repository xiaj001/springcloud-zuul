package com.aisino.gateway.req;

import com.aisino.gateway.define.ResponseBodyRewriteFunction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.AbstractServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lili
 * @description ${DESCRIPTION}
 * @create 2018-12-13 13:20
 * @since
 **/
@Configuration
@Log4j2
public class ApiLocator1 {

    public final static ThreadLocal<String> threadLocal = new ThreadLocal();
    private static final Pattern QUERY_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

    private static final String SERVICE = "/path/**";
    private static final String URI = "http://www.baidu.com";

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {

        /*
        route1 是get请求，get请求使用readBody会报错
        route2 是post请求，Content-Type是application/x-www-form-urlencoded，readbody为String.class
        route3 是post请求，Content-Type是application/json,readbody为Object.class
         */
        RouteLocatorBuilder.Builder routes = builder.routes();
        RouteLocatorBuilder.Builder serviceProvider = routes
                .route( r -> r.method(HttpMethod.POST).and()
                                .readBody(String.class, readBody -> {
                                    threadLocal.set(readBody);
                                    return true;
                                }) .and().order(Ordered.HIGHEST_PRECEDENCE)
                                .path(SERVICE).filters(f -> {
                                    return f.modifyRequestBody(String.class,String.class,new ResponseBodyRewriteFunction());

                                })
                                .uri(URI));


        RouteLocator routeLocator = serviceProvider.build();
        log.info("custom RouteLocator is loading ... {}", routeLocator);
        return routeLocator;
    }


    @Configuration
    class RemoveBodyFilter  implements GlobalFilter,Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {



            String s = threadLocal.get();
            if (!StringUtils.isEmpty(s)){
                /**
                 * 参照实现:   org.springframework.http.server.reactive.AbstractServerHttpRequest#initQueryParams()
                 */
                MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
                Matcher matcher = QUERY_PATTERN.matcher(s);
                while(matcher.find()) {
                    String name = decodeQueryParam(matcher.group(1));
                    String eq = matcher.group(2);
                    String value = matcher.group(3);
                    value = value != null ? decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null);
                    queryParams.add(name, value);
                }
                CollectionUtils.unmodifiableMultiValueMap(queryParams);
                AbstractServerHttpRequest request = (AbstractServerHttpRequest) exchange.getRequest();
            }


            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                        System.err.println(threadLocal.get());
                threadLocal.remove() ;

            }

            ));
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }



    private static String decodeQueryParam(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException var3) {

            return URLDecoder.decode(value);
        }
    }
}