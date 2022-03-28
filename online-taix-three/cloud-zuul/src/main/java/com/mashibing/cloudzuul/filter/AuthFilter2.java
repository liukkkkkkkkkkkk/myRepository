package com.mashibing.cloudzuul.filter;

import com.mashibing.internalcommon.constant.RedisKeyPrefixConstant;
import com.mashibing.internalcommon.util.JwtInfo;
import com.mashibing.internalcommon.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权filter
 * @author 49178
 * @create 2022/2/23
 */
@Component
public class AuthFilter2 extends ZuulFilter {

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
        return 1;
    }

    /**
     * 该过滤器是否生效
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        StringBuffer requestURL = requestContext.getRequest().getRequestURL();
      //  System.out.println("url是:"+requestURL);
        return false;
    }

    /**
     * 拦截后的具体业务逻辑
     */
    @Override
    public Object run() {
        System.out.println("AuthFilter2拦截了。。");
        //获取上下文（重要，贯穿 所有filter，包含所有参数）
        RequestContext currentReqContext = RequestContext.getCurrentContext();
        boolean flag = currentReqContext.sendZuulResponse();
        if (!flag){
            System.out.println("sendZuulResponse:"+flag+"  拦截方法结束");
        }
        return null;
    }
}
