package com.aisino.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


/**
 * @author: xiajun003
 * @Date: 2019/1/3 17:57
 * @Description:
 */
@Configuration
public class TestConfig {


/*    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }*/

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {

        return new HiddenHttpMethodFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {

                HttpServletRequest requestToUse = request;
                filterChain.doFilter(requestToUse, response);
            }

        };
    }
}
