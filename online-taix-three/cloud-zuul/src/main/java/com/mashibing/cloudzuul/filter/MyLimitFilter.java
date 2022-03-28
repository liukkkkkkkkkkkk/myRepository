package com.mashibing.cloudzuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author 49178
 * @create 2022/3/22
 */

//网关限流，使用 google.common.util.concurrent.RateLimiter
@Component
public class MyLimitFilter extends ZuulFilter {

    // 5 qps(1秒  5个 请求 Query Per Second 每秒查询量)

    private static final RateLimiter RATE_LIMITER =RateLimiter.create(2);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 网关限流应该在网关第一步做
     */
    @Override
    public int filterOrder() {
        return -10;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        if (RATE_LIMITER.tryAcquire()){
            System.out.println("令牌桶生成的令牌拿到啦，请求被允许往下执行了");
            return null;
        }
        System.out.println("令牌拿不到");
        context.set("limit",false); //设置key为false,后面再进行判断
        return null;
    }
}
