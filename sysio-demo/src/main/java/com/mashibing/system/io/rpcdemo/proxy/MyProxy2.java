package com.mashibing.system.io.rpcdemo.proxy;


import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHeader;
import com.mashibing.system.io.rpcdemo.rpc.transport.ClientFactory;
import com.mashibing.system.io.rpcdemo.rpc.transport.ClientFactory2;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class MyProxy2 {

    public  static <T> T proxyGet2(Class<T>interfaceInfo){
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[]interfaces =new Class[]{interfaceInfo};
        T t = (T)Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                MyHeader myHeader = new MyHeader();
                MyContent content = new MyContent();
                content.setMethodName(method.getName());
                content.setName(interfaceInfo.getName());
                content.setArgs(args);
                content.setParameterTypes(method.getParameterTypes());
                CompletableFuture future = ClientFactory2.transport(content);
                Object res = future.get();
                return res;
            }
        });
        return t;
    }

    @Test
    public void test1(){
        InetSocketAddress localhost = new InetSocketAddress("localhost", 9099);
        InetSocketAddress localhost2 = new InetSocketAddress("localhost", 9099);
        System.out.println(localhost.equals(localhost2));
    }
}
