package com.mashibing.apiconsumer.gray;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author 49178
 * @create 2022/3/7
 */
@Aspect
@Component
public class RequestAspact {

    // execution(* com.sample.service.impl..*.*(..))
//符号 含义
//execution（）
// 表达式的主体；
//第一个”*“符号
// 表示返回值的类型任意；
//com.sample.service.impl AOP所切的服务的包名，即，我们的业务部分
//包名后面的”..“ 表示当前包及子包
//第二个”*“ 表示类名，*即所有类。此处可以自定义，下文有举例
//.*(..) 表示任何方法名，括号表示参数，两个点表示任何参数类型
    @Pointcut("execution(* com.mashibing.apiconsumer.controller..*Controller*.*(..))")
    private void anyMethod(){
    }

/*<dependency>
            <groupId>io.jmnarloch</groupId>
            <artifactId>ribbon-discovery-filter-spring-cloud-starter</artifactId>
            <version>2.1.0</version>
        </dependency>
        */
    //使用和网关路由同样的方式进行灰度路由
    @Before("anyMethod()")
    public  void before(JoinPoint joinPoint){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String version = request.getHeader("version");
        System.out.println("请求参数version:"+version);
        // 此处的逻辑---》灰度规则 匹配的地方 查db，redis ====匹配上规则后，加入元数据属性，走metadata里设置的属性的对应服务
        RibbonFilterContextHolder.getCurrentContext().add("version",version);

    }



}
