package com.leyou.gateway.filter;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties(value={JwtProperties.class,FilterProperties.class})

public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties props;
    @Autowired
    private FilterProperties filterProperties;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;  //前置过滤器
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        for (String allowPath : filterProperties.getAllowPaths()) {
            if (path.startsWith(allowPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, props.getPublicKey());
            //TODO 此处做权限判断调用
        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
        }

        return null;
    }
}
