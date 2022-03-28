package com.mashibing.system.io.rpcdemo.proxy;


import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHeader;
import com.mashibing.system.io.rpcdemo.rpc.transport.ClientFactory;
import com.mashibing.system.io.rpcdemo.rpc.transport.ClientFactory2;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
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
public class MyProxy {

    //动态代理返回对象
    public static <T> T proxyGet(Class<T> interfaceInfo) {  //先使用jdk自带的实现，基于接口
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};
        T t = (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override  //调用代理对象的方法时触发
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //service方法执行的时候确定是调用本地方法还是远程方法 用dispatch区分
                Dispatcher dis = Dispatcher.getDis();
                String serviceName = interfaceInfo.getName();
                Object obj = dis.get(serviceName);
                Object res =null; //定义返回值
                if (obj ==null){
                    //走RPC远程调用
                    //TODO  rpc  就像小火车拉货  content是service的具体数据，但是还需要header层完成IO传输的控制

                    String name = interfaceInfo.getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    MyContent myContent = new MyContent(name, methodName, parameterTypes, args);
               /*     // TODO 未来协议可能会变
                    byte[] contentBytes = SerDecodeUtil.ser(myContent);
                    MyHeader myHeader = MyHeader.createHeader(contentBytes);

                    long reqUid = myHeader.getReqUid();
                    byte[] headBytes = SerDecodeUtil.ser(myHeader);
                    System.out.println("client send header length：" + headBytes.length);
                    ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(contentBytes.length + headBytes.length);
                    byteBuf.writeBytes(headBytes);
                    byteBuf.writeBytes(contentBytes);*/

                     CompletableFuture future = ClientFactory.transport(myContent);
                   res = future.get();//阻塞，等待消息返回，回调方法执行拿到结果了，再往下执行
                    //    System.out.println("往下走了");
                }else {
                    System.out.println("local  FC....");
                    //local调用
                   /* String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] args = requeskPkg.getContent().getArgs();
                    Object obj = dis.get(serviceName);
                    Class<?> clazz= obj.getClass();*/
                    try {
                     //   Method method = clazz.getMethod(methodName, parameterTypes);
                        res= method.invoke(obj, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
              return  res;
            }
        });

        return t;
    }



    @Test
    public void test1(){
        System.out.println("200".equals(200)); //false
        InetSocketAddress localhost = new InetSocketAddress("localhost", 9099);
        InetSocketAddress localhost2 = new InetSocketAddress("localhost", 9099);
        System.out.println(localhost.equals(localhost2)); //true
    }
}
