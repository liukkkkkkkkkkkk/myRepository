package com.mashibing.cloudzuul.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author 49178
 * @create 2022/3/23
 */

/**
 * 使用阿里的sentinel实现限流
 */
@Component
public class SentinelFIlter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -11;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //限流的业务逻辑
        Entry entry =null;
        try {
             entry = SphU.entry("my_resource1");
            System.out.println("拿到令牌,执行下面的逻辑");
        } catch (BlockException e) {
            System.out.println("限流，请求被阻塞住了。。");
            e.printStackTrace();
        }finally {
            if (entry !=null){
                entry.exit();
            }
        }
        return null;
    }
}
