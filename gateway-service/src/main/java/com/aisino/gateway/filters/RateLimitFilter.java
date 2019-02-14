/*
package com.aisino.gateway.filters;

import com.lianjia.matrix.openapi.gateway.constants.ErrorCode;
import com.lianjia.matrix.openapi.gateway.exception.ServerException;
import com.lianjia.matrix.openapi.gateway.model.SysRateConfig;
import com.lianjia.matrix.openapi.gateway.service.RedisRateLimiter;
import com.lianjia.matrix.openapi.gateway.service.SysRateConfigService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

*/
/**
 * 限流
 *//*

public class RateLimitFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private SysRateConfigService sysRateConfigService;

    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Override
    public String filterType() {
        return FilterType.Pre.getType();
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        if (request instanceof HttpServletRequestWrapper) {
            request = ((HttpServletRequestWrapper) request).getRequest();
        }
        ServerException throwable = null;
        try {
            //根据接口限流
            String url = request.getRequestURI();

            //根据appId限流
            String appId = request.getParameter("appId");

            //根据用户IP限流

            //查找该接口的配置文件
            List<SysRateConfig> configs = sysRateConfigService.getSysRateConfigByUrl(url);

            if (!CollectionUtils.isEmpty(configs)) {
                //整个接口的配置
                SysRateConfig interfaceConfig = configs.stream().filter(config -> config.getStatus() == 1 && config.getAppId().equals("-")).findFirst().orElse(null);

                //每个app的默认配置
                SysRateConfig defaultConfig = configs.stream().filter(config -> config.getStatus() == 1 && config.getAppId().equals("*")).findFirst().orElse(null);

                //app的配置
                SysRateConfig appIdConfig = configs.stream().filter(config -> config.getStatus() == 1 && config.getAppId().equals(appId)).findFirst().orElse(null);

                if (interfaceConfig != null) {
                    redisRateLimiter.consume(interfaceConfig, url);
                }
                if (appIdConfig != null) {
                    redisRateLimiter.consume(appIdConfig, url + "_" + appId);
                } else if (defaultConfig != null) {
                    redisRateLimiter.consume(defaultConfig, url + "_" + appId);
                }
            }

        } catch (Exception e) {
            throwable = new ServerException(e.getMessage(), ErrorCode.ParameterError.getCode());
        } finally {
            if (throwable != null) {
                context.getResponse().setContentType("text/html; charset=UTF-8");//.setCharacterEncoding("UTF-8");
                // 过滤该请求，不对其进行路由
                context.setSendZuulResponse(false);
                // 返回错误码
                context.setResponseStatusCode(403);
                // 返回错误内容
                context.setResponseBody("{\"errorCode\":" + throwable.getErrorCode() + ", \"msg\":\"" + throwable.getMessage() + "\"}");
                context.set("isSuccess", false);
            } else {
                //对该请求进行路由
                context.setSendZuulResponse(true);
                context.setResponseStatusCode(200);
                // 设值，可以在多个过滤器时使用
                context.set("isSuccess", true);
            }
        }
        return null;
    }
}
*/
