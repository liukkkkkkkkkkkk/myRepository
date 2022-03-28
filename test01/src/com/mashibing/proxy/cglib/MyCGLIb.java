package com.mashibing.proxy.cglib;

import com.sun.org.apache.xpath.internal.Arg;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyCGLIb implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("方法执行前");
        Object o = methodProxy.invokeSuper(obj, args);
        System.out.println("方法执行后");
        return o;
    }


    public static void main(String[] args) {
        Enhancer enhancer =new Enhancer();
        enhancer.setSuperclass(MyCalculator.class);
        enhancer.setCallback(new MyCGLIb());
        MyCalculator myCalculator = (MyCalculator)enhancer.create();
        myCalculator.add(1,1);
        System.out.println(myCalculator.getClass());
    }
}
