package com.mashibing.servicesms.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author 49178
 * @create 2022/3/22
 * 服务端限流，使用过滤器
 */
@Component
public class LimitFilter implements Filter {
    private static  final RateLimiter RATE_LIMITER =RateLimiter.create(2);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (RATE_LIMITER.tryAcquire()){
            System.out.println("LimitFilter 令牌拿到了，请求往下走");
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            System.out.println("限流了。。。");

            servletResponse.setCharacterEncoding("utf-8");
            servletResponse.setContentType("text/html;charset=utf-8");
            PrintWriter writer = servletResponse.getWriter();
            writer.write("请求被限流啦");
        }
    }
}
