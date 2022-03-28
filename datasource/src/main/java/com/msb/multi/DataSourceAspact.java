package com.msb.multi;

import com.msb.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class DataSourceAspact {
//切入点，对带有DataSource注解的地方进行拦截
    @Pointcut("@annotation(com.msb.annotation.DataSource)")
    public void myPointCut(){
        System.out.println("自定义的切入点");
    }
    @Around(" myPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DataSource annotation = method.getAnnotation(DataSource.class);
        if(annotation !=null){
          DynamicDataSourceContextHolder.setDataSourceType(annotation.value().name());
        }
        try{
           return  joinPoint.proceed();
        }
        finally {
            //销毁数据源，在执行方法之后
            DynamicDataSourceContextHolder.clearDataSource();
        }
    }
}
