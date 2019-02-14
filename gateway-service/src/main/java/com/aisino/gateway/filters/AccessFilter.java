/*
package com.aisino.gateway.filters;

import com.lianjia.matrix.openapi.gateway.constants.ErrorCode;
import com.lianjia.matrix.openapi.gateway.core.CacheManager;
import com.lianjia.matrix.openapi.gateway.exception.NoPermissionException;
import com.lianjia.matrix.openapi.gateway.exception.ServerException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

*/
/**
 * 权鉴过滤器
 *//*

public class AccessFilter extends AbstractGlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

    private boolean skipAccess;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private List<String> ignorePaths;

    public void setSkipAccess(boolean skipAccess) {
        this.skipAccess = skipAccess;
    }

    public void setIgnorePaths(List<String> ignorePaths) {
        this.ignorePaths = ignorePaths;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    @Autowired
    private CacheManager cacheManager;

    @Override
    public String filterType() {
        return FilterType.Pre.getType();
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        if (skipAccess || !(boolean)context.get("isSuccess")) {
            return false;
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        for (String ignorePath : ignorePaths) {
            if (pathMatcher.match(ignorePath, path)) {
                return false;
            }
        }
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
            String accessToken = request.getParameter("accessToken");
            if (org.apache.commons.lang3.StringUtils.isBlank(accessToken)) {
                throw new NoPermissionException("AccessToken不能为空", ErrorCode.NoPermission.getCode());
            }
            String userInfo = cacheManager.getUserAccessCache(accessToken);
            if (StringUtils.isBlank(userInfo)) {
                LOGGER.info("[AccessToken:{} 过期]", accessToken);
                throw new NoPermissionException("AccessToken 已过期，请重新登录", ErrorCode.NoPermission.getCode());
            }
        }catch (Exception e){
            throwable = new ServerException(e.getMessage(), ErrorCode.ParameterError.getCode());
        }finally {
            if (throwable != null) {
                context.getResponse().setContentType("text/html; charset=UTF-8");//.setCharacterEncoding("UTF-8");
                // 过滤该请求，不对其进行路由
                context.setSendZuulResponse(false);
                // 返回错误码
                context.setResponseStatusCode(401);
                // 返回错误内容
                context.setResponseBody("{\"errorCode\":" + throwable.getErrorCode() + ", \"msg\":\"" + throwable.getMessage() +"\"}");
                context.set("isSuccess", false);
            }else{
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
