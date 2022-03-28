package com.mashibing.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CalculatorProxy {
/*

        public static Calculator getProxy(final Calculator calculator){
            ClassLoader classLoader =calculator.getClass().getClassLoader();
            Class<?>[] interfaces = calculator.getClass().getInterfaces();
            InvocationHandler handler =new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object invoke = method.invoke(calculator, args);
                    return invoke;
                }
            };
            Object o = Proxy.newProxyInstance(classLoader, interfaces, handler);
            return (Calculator) o;
        }
*/
 public static Calculator getProxy(final Calculator calculator){
     Class<?>[] interfaces = calculator.getClass().getInterfaces();
     ClassLoader classLoader = calculator.getClass().getClassLoader();
     InvocationHandler handler =new InvocationHandler() {
         @Override
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
             Object invoke = method.invoke(calculator, args);
             return invoke;
         }
     };
     Object o = Proxy.newProxyInstance(classLoader, interfaces, handler);
     return (Calculator)o;
 }

    public static void main(String[] args) {
     //将JDK动态代理生成的class文件保存到本地
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        Calculator proxy = CalculatorProxy.getProxy(new MyCalculator());
        System.out.println(proxy.add(1, 1));
        System.out.println(proxy);
    }
}
