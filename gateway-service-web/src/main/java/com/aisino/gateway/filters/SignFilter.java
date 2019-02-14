/*
package com.aisino.gateway.filters;

import com.lianjia.matrix.openapi.gateway.constants.Constants;
import com.lianjia.matrix.openapi.gateway.constants.ErrorCode;
import com.lianjia.matrix.openapi.gateway.core.CacheManager;
import com.lianjia.matrix.openapi.gateway.dto.AppIdKeyInfo;
import com.lianjia.matrix.openapi.gateway.exception.*;
import com.lianjia.matrix.openapi.gateway.service.AppIdKeyService;
import com.lianjia.matrix.openapi.gateway.util.SignHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

*/
/**
 * 签名校验,按照指定规则签名，不符合规定者返回
 *//*

public class SignFilter extends AbstractGlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignFilter.class);

    private boolean skipSign;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private List<String> ignorePaths;

    public void setSkipSign(boolean skipSign) {
        this.skipSign = skipSign;
    }

    public void setIgnorePaths(List<String> ignorePaths) {
        this.ignorePaths = ignorePaths;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    @Autowired
    private AppIdKeyService appIdKeyService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public int filterOrder() {
        return FilterOrder.SignFilterOrder.getOrder();
    }

    @Override
    public boolean shouldFilter(ServerWebExchange exchange) {
        Boolean isSuccess = exchange.getRequiredAttribute("isSuccess");
        if (skipSign || !isSuccess) {
            return false;
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        for (String ignorePath : ignorePaths) {
            if (pathMatcher.match(ignorePath, path)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void preHandler(ServerWebExchange exchange) {
        HttpRequest request1 = exchange.getRequest();
        ServerHttpRequest request = exchange.getRequest();
        ServerException throwable = null;
        try {
            String appId = request.getParameter("appId");
            if (StringUtils.isBlank(appId)) {
                throw new ParameterErrorException("AppId缺失");
            }
            String mt = request.getParameter("mt");
            if (StringUtils.isBlank(mt)) {
                throw new ParameterErrorException("mt丢失");
            }
            String signCode = request.getParameter("signCode");
            if (StringUtils.isBlank(signCode)) {
                throw new ParameterErrorException("签名丢失");
            }

            Long now = System.currentTimeMillis();
            if (Math.abs(now - Long.parseLong(mt)) > Constants.REDIS_KEY_REQ_REP_TIME * 1000) {
                LOGGER.info("[Mt 非法，误差大于5分钟]");
                throw new ParameterErrorException("Mt非法，签名错误");
            }
            AppIdKeyInfo appIdKeyInfo = appIdKeyService.getAppIdKey(appId);
            if (appIdKeyInfo == null) {
                LOGGER.info("[appId 未注册，appId={}]", appId);
                throw new NoPermissionException("appId未注册，非法访问");
            }
            String targetSignCode = SignHelper.sign(request, appIdKeyInfo.getAppKey());
            if (!targetSignCode.equals(signCode)) {
                LOGGER.info("{}签名错误，原值:{},计算值:{} appId:{} appkey:{}", request.getRequestURI(), signCode, targetSignCode, appId, appIdKeyInfo.getAppKey());
                throw new SignException("签名错误");
            }
            if (cacheManager.contentReqKey(signCode)) {
                throw new DupSubmitException("重复提交");
            }
            cacheManager.setReqCache(signCode);
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
    }
}
*/
