
package com.aisino.gateway.utils;

import com.aisino.gateway.req.ApiLocator1;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: xiajun003
 * @Date: 2019/1/8 21:58
 * @Description:
 */

public class RequestUtil {

    private static final Pattern QUERY_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();


    public static MultiValueMap<String, String> getQueryParams(ServerWebExchange exchange){
        HttpMethod method = exchange.getRequest().getMethod();
        if ( method == HttpMethod.POST){
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
            String s = ApiLocator1.threadLocal.get();
            if (StringUtils.isEmpty(s)){
                return queryParams;
            }

            /**
             * 参照实现:   org.springframework.http.server.reactive.AbstractServerHttpRequest#initQueryParams()
             */

            Matcher matcher = QUERY_PATTERN.matcher(s);
            while(matcher.find()) {
                String name = decodeQueryParam(matcher.group(1));
                String eq = matcher.group(2);
                String value = matcher.group(3);
                value = value != null ? decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null);
                queryParams.add(name, value);
            }

            return CollectionUtils.unmodifiableMultiValueMap(queryParams);
        }else if (method == HttpMethod.GET){

        return exchange.getRequest().getQueryParams();

    }else {
        throw new RuntimeException("request method not support :"+method.name());
    }
    }

    public static String getRequestParam(ServerWebExchange exchange, String key){

       return getQueryParams(exchange).getFirst(key);

    }


    private static String decodeQueryParam(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException var3) {

            return URLDecoder.decode(value);
        }
    }



}

