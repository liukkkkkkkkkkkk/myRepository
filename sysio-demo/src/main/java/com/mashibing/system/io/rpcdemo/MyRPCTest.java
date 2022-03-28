package com.mashibing.system.io.rpcdemo;

import com.mashibing.system.io.rpcdemo.proxy.MyProxy;
import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ServerRequestHander;
import com.mashibing.system.io.rpcdemo.rpc.services.Car;
import com.mashibing.system.io.rpcdemo.rpc.services.Fly;
import com.mashibing.system.io.rpcdemo.rpc.services.MyCar;
import com.mashibing.system.io.rpcdemo.rpc.services.MyFly;
import com.mashibing.system.io.rpcdemo.rpc.services.Person;
import com.mashibing.system.io.rpcdemo.rpc.transport.ServerDecode;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 49178
 * @create 2021/12/6
 */
public class MyRPCTest {      //基于nettyy实现RPC远程调用
    @Test  //模拟consumer
    public void get() {

    /*    new Thread(() -> {
            startServer();
        }).start();
        System.out.println("server started...");*/

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                Car car =MyProxy.proxyGet(Car.class);  //动态代理实现接口
                String arg = "hello " + atomicInteger.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println(" client over message :" + res + "  request arg :" + arg);
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
  /*      Car car = MyProxy.proxyGet(Car.class);  //动态代理实现接口
        car.ooxx("hello world");
        Fly fly = MyProxy.proxyGet(Fly.class);
        fly.xxoo("hello");*/
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startServer() {
        MyCar myCar = new MyCar();
        MyFly myFly = new MyFly();
        Dispatcher dispatcher = Dispatcher.getDis();
        dispatcher.register(Car.class.getName(),myCar);
        dispatcher.register(Fly.class.getName(),myFly);
        NioEventLoopGroup boss = new NioEventLoopGroup(10);
        NioEventLoopGroup worker = boss;
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture channelFuture = sbs.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        System.out.println("server accpet client port:" + channel.remoteAddress().getPort());
                        ChannelPipeline pipeline = channel.pipeline();
                        //1. 自定义的rpc
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ServerRequestHander(dispatcher));

                        //2.基于http传输协议
                    }
                }).bind(new InetSocketAddress("localhost", 9099));
        try {
            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }


    @Test
    public void testRPCObj(){
        Fly fly =MyProxy.proxyGet(Fly.class);
        Person per = fly.getPerson("sdf");
        System.out.println("per:"+per);


    }


}















