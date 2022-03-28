package com.mashibing.cloudzuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URL;

/**
 * @author 49178
 * @create 2022/3/12
 */
@Component
public class MYHostFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext cxt = RequestContext.getCurrentContext();
        HttpServletRequest request = cxt.getRequest();
        String url = request.getRequestURL().toString();
        System.out.println("myhostFilter.....");
        if (url.contains("/xxoo-ooxx")) {
            cxt.setRouteHost(new URL("http://localhost:8041/send/test"));
          //  cxt.setRouteHost(new URL("http://localhost:8041/test/sms-test3").toURL());

        }

        return null;
    }

}
