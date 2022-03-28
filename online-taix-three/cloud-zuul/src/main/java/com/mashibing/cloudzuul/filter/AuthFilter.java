package com.mashibing.cloudzuul.filter;

import com.mashibing.internalcommon.constant.RedisKeyPrefixConstant;
import com.mashibing.internalcommon.util.JwtInfo;
import com.mashibing.internalcommon.util.JwtUtil;
import com.mashibing.internalcommon.util.RedisKeyUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权filter
 * @author 49178
 * @create 2022/2/23
 */
@Slf4j
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 拦截类型，4种类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
    /**
     * 	值越小，越在前执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 该过滤器是否生效
     */
    @Override
    public boolean shouldFilter() {
        return false; //便于测试其它的，注释掉不走
     /*   RequestContext requestContext = RequestContext.getCurrentContext();
        StringBuffer requestURL = requestContext.getRequest().getRequestURL();
        String requestURI = requestContext.getRequest().getRequestURI();
        System.out.println("请求url是:"+requestURL+" 请求uri是:"+requestURI);
        if (requestURI.contains("/auth/login")){
            log.info("请求为登录接口，authFilter不需要拦截验证..");
            return false;
        }
        return true;*/
    }

    /**
     * 拦截后的具体业务逻辑
     */
    @Override
    public Object run() {
        System.out.println("AuthFilter拦截了。。");
        //获取上下文（重要，贯穿 所有filter，包含所有参数）
        RequestContext currentReqContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentReqContext.getRequest();
        String token = request.getHeader("Authorization");
        System.out.println("token:"+token);
        if (StringUtils.isNotBlank(token)){
            JwtInfo jwtInfo = JwtUtil.parseToken(token);
            if (jwtInfo !=null){
                String tokenUserId = jwtInfo.getSubject();
                Long tokenIssueDate = jwtInfo.getIssueDate();
                BoundValueOperations<String, String> stringStringBoundValueOperations = redisTemplate.boundValueOps(RedisKeyPrefixConstant.PASSENGER_LOGIN_TOKEN_APP_KEY_PRE + tokenUserId);
                String redisToken = stringStringBoundValueOperations.get();
                if (token.equals(redisToken)){
                    System.out.println("用户的token验证通过啦。。");
                    return null;  //方法结束，请求继续往下一个ZuulFilter 过滤器走
                }
            }

        }

        // setSendZuulResponse(false) 请会将不会向后面的服务转发，但还走剩下的过滤器。不向后面的RibbonRoutingFilter过滤器转发 /*	@Override
        //	public boolean shouldFilter() {
        //		RequestContext ctx = RequestContext.getCurrentContext();
        //		return (ctx.getRouteHost() == null && ctx.get(SERVICE_ID_KEY) != null
        //				&& ctx.sendZuulResponse());
        //	}*/
        System.out.println("用户的token验证失败了！不往服务转发，还走下一个过滤器");
        currentReqContext.setSendZuulResponse(false);
        currentReqContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        currentReqContext.setResponseBody("auth fail......");




        return null;
    }
}
