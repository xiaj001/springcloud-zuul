/*

package com.aisino.gateway.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lianjia.matrix.gateway.model.ErrorCode;
import com.lianjia.matrix.gateway.model.Result;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;



@Component
public class ResponseCodeFilter extends AbstractGlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCodeFilter.class);

    private static DynamicBooleanProperty INCLUDE_DEBUG_HEADER = DynamicPropertyFactory
            .getInstance()
            .getBooleanProperty(ZuulConstants.ZUUL_INCLUDE_DEBUG_HEADER, false);

    private static DynamicIntProperty INITIAL_STREAM_BUFFER_SIZE = DynamicPropertyFactory
            .getInstance()
            .getIntProperty(ZuulConstants.ZUUL_INITIAL_STREAM_BUFFER_SIZE, 8192);

    private static DynamicBooleanProperty SET_CONTENT_LENGTH = DynamicPropertyFactory
            .getInstance()
            .getBooleanProperty(ZuulConstants.ZUUL_SET_CONTENT_LENGTH, false);

    private ThreadLocal<byte[]> buffers = ThreadLocal.withInitial(() -> new byte[INITIAL_STREAM_BUFFER_SIZE.get()]);

    private static final int ORDER = 300;



    private MediaType[] supportedMediaTypes = new MediaType[]{MediaType.APPLICATION_JSON, new MediaType("application", "*+json")};



    @Override
    public int filterOrder() {
        return ORDER;
    }

    @Override
    public boolean shouldFilter(ServerWebExchange exchange) {
        return true;
    }

    @Override
    public void postHandler(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful()){

        }else if (statusCode.is1xxInformational()){

        }

        RequestContext context = RequestContext.getCurrentContext();
        int status = context.getResponseStatusCode();
        HttpServletResponse response = context.getResponse();
        response.setStatus(HttpServletResponse.SC_OK);
        context.setResponseStatusCode(HttpServletResponse.SC_OK);
        String body = context.getResponseBody();
        InputStream responseDataStream = context.getResponseDataStream();
        if (status >= 200 && status < 300) {
            if (StringUtils.isEmpty(body) && (context.getResponseDataStream() == null)) {
                context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.Ok.getCode())));
                return null;
            } else {
                try {
                    List<com.netflix.util.Pair<String, String>> pairs = context.getOriginResponseHeaders();
                    String contentType = getPairHttpHeader(pairs, HttpHeaders.CONTENT_TYPE);
                    MediaType mediaType = MediaType.parseMediaType(contentType);
                    if (canJSON(mediaType)) {
                        Result result = new Result(ErrorCode.Ok.getCode());
                        Object data = parseObject(responseDataStream);
                        if (null == data) {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        }
                        result.setData(data);
                        context.setResponseBody(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
                    } else {
                        context.setResponseDataStream(responseDataStream);
                    }

                } catch (Exception e) {
                    LOGGER.info("[Parse Target Service Response Body Error]", e);
                    context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.InternalError.getCode(), "未知错误")));
                }
            }
        } else if (status == HttpServletResponse.SC_BAD_REQUEST) {
            Result result = new Result(ErrorCode.ParameterError.getCode(), "参数错误");
            if (StringUtils.isEmpty(body) && context.getResponseDataStream() == null) {
            } else {
                try {
                    parseProxiedServiceErrorMsg(context, result);
                } catch (Exception e) {
                }
            }
            context.setResponseBody(JSON.toJSONString(result));
        } else if (status == HttpServletResponse.SC_FORBIDDEN || HttpServletResponse.SC_UNAUTHORIZED == status || HttpServletResponse.SC_METHOD_NOT_ALLOWED == status) {
            context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.NoPermission.getCode(), "访问受限")));
        } else if (status == HttpServletResponse.SC_NOT_FOUND) {
            context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.ServiceNotFound.getCode(), "服务不存在")));
        } else if (status == HttpServletResponse.SC_BAD_GATEWAY) {
            context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.RouterError.getCode(), "服务已下线")));
        } else if (status == HttpServletResponse.SC_GATEWAY_TIMEOUT) {
            context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.ServiceCallTimeout.getCode(), "服务超时")));
        } else {
            try {
                Map<String, Object> data = (Map<String, Object>) parseObject(context.getResponseDataStream());
                String exception = (String) data.get("exception");
                String msg = null;
                if (exception != null && exception.startsWith("com.lianjia")) {
                    msg = (String) data.get("message");
                }
                if (msg == null) {
                    msg = "未知错误";
                }
                context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.InternalError.getCode(), msg)));
            } catch (Exception e) {
                LOGGER.info("[Parse Target Service Response Body Error]", e);
                context.setResponseBody(JSON.toJSONString(new Result(ErrorCode.InternalError.getCode(), "未知错误")));
            }
        }

        return null;
    }



    private boolean canJSON(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : supportedMediaTypes) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    private void parseProxiedServiceErrorMsg(RequestContext context, Result result) throws IOException {
        Object data = parseObject(context.getResponseDataStream());
        Map<String, Object> map = (Map<String, Object>) data;
        List errors = (List) map.get("errors");
        if (errors != null && errors.size() > 0) {
            Map<String, Object> error = (Map<String, Object>) errors.get(0);
            String errMsg = (String) error.get("defaultMessage");
            if (errMsg != null) {
                result.setMsg(errMsg);
            }
        }
    }



    private String getPairHttpHeader(List<com.netflix.util.Pair<String, String>> pairs, String httpHeaders) {
        for (com.netflix.util.Pair<String, String> pair : pairs) {
            String first = pair.first();
            String second = pair.second();
            if (first.equals(httpHeaders)) {
                return second;
            }
        }
        return MediaType.APPLICATION_JSON_VALUE;
    }

    private Object parseObject(InputStream in) throws IOException {
        byte[] bytes = buffers.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead = -1;
        while ((bytesRead = in.read(bytes)) != -1) {
            out.write(bytes, 0, bytesRead);
        }
        byte[] t = out.toByteArray();
        try {
            return JSON.parse(t);
        } catch (Exception e) {
            LOGGER.warn("[Not Json String ,use String instead!!]", e);
            return new String(t);
        }
    }
}


*/
