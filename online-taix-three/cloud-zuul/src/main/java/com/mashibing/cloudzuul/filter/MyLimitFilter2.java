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


@Component
public class MyLimitFilter2 extends ZuulFilter {

    // 5 qps(1秒  5个 请求 Query Per Second 每秒查询量)

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(5);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 限流应该在网关第一步做
     */
    @Override
    public int filterOrder() {
        return -9;
    }

    @Override
    public boolean shouldFilter() {
  /*      Object limit = RequestContext.getCurrentContext().get("limit");
        if (limit!=null){
            return (Boolean)limit;
        }
        return true;*/
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("MyLimit2往下执行了");
        return null;
    }
}
