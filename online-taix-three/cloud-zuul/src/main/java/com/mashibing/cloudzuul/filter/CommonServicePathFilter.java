package com.mashibing.cloudzuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.scenario.effect.FilterContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 49178
 * @create 2022/3/10
 */
@Component
public class CommonServicePathFilter extends ZuulFilter {
    @Override
    public String filterType() {//这里很重要，必须是route
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    //知道对应的服务名和uri,原来请求的url不想改，但是要把旧的url映射成新的url
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        System.out.println("uri:" + requestURI);
        System.out.println("url:" + requestURL);
        if (requestURL.contains("service-sms/send/test1")) {
            //1.设置目标service的Controller的路径 ctx.put(FilterConstants.REQUEST_URI_KEY,requestUrl);
            ctx.put(FilterConstants.REQUEST_URI_KEY, "/send/test");
           // ctx.set(FilterConstants.REQUEST_URI_KEY, "/send/test");
            //2.设置目标service的serviceId 现在为啥不用设置serviceId 也可以进行请求映射了？？
            //但是我使用的时候，一直报404，后来跟踪服务之后，发现其实ctx里面还有一个serviceId的属性，它是跟当前请求的这个原始url映射的serviceId保持一致的，但是如果你要转发的serviceId并不是这个的话，那就会报404，所以必须要在这里重新定义serviceId,
           // ctx.put(FilterConstants.SERVICE_ID_KEY,"service-sms");
            System.out.println(String.format("对请求路径[%s]进行映射啦[%s] ", requestURL, "/send/test"));
        }

        return null;
    }
}
