package com.mashibing.system.io.rpcdemo;

import com.mashibing.system.io.rpcdemo.proxy.MyProxy;
import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHttpRPCHandler;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ServerRequestHander;
import com.mashibing.system.io.rpcdemo.rpc.services.Car;
import com.mashibing.system.io.rpcdemo.rpc.services.Fly;
import com.mashibing.system.io.rpcdemo.rpc.services.MyCar;
import com.mashibing.system.io.rpcdemo.rpc.services.MyFly;
import com.mashibing.system.io.rpcdemo.rpc.services.Person;
import com.mashibing.system.io.rpcdemo.rpc.transport.ServerDecode;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 49178
 * @create 2021/12/6
 */
public class MyRPC_HTTPTest {      //基于nettyy实现RPC远程调用
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
                Car car = MyProxy.proxyGet(Car.class);  //动态代理实现接口
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
        dispatcher.register(Car.class.getName(), myCar);
        dispatcher.register(Fly.class.getName(), myFly);
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
                    /*    //1. 自定义的rpc
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ServerRequestHander(dispatcher));
*/
                        //2.基于http传输协议
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 512));
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                FullHttpRequest request = (FullHttpRequest) msg;   //客户端基于HTTP请求发送过来的
                                System.out.println("request:" + request.toString());
                                ByteBuf content = request.content();
                                byte[] data = new byte[content.readableBytes()];
                                content.readBytes(data);
                                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                MyContent reqContent = (MyContent) ois.readObject();
                                String methodName = reqContent.getMethodName();
                                String serviceName = reqContent.getName();
                                Class<?>[] parameterTypes = reqContent.getParameterTypes();
                                Object[] args = reqContent.getArgs();
                                Object o = dispatcher.get(serviceName);
                                Class<?> clazz = o.getClass();
                                Method method = clazz.getMethod(methodName, parameterTypes);
                                Object res = method.invoke(o, args);
                                MyContent resContent = new MyContent();
                                resContent.setRes(res);
                                byte[] resContentBytes = SerDecodeUtil.ser(resContent);

                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,
                                        HttpResponseStatus.OK,
                                        Unpooled.copiedBuffer(resContentBytes));
                                //http协议，header+body
                                response.headers().set(HttpHeaderNames.CONTENT_LENGTH,resContentBytes.length);
                                ctx.channel().writeAndFlush(response);
                            }
                        });

                    }
                }).bind(new InetSocketAddress("localhost", 9099));
        try {
            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    @Test
    public void startHttpServer(){
        MyCar car = new MyCar();
        MyFly fly = new MyFly();

        Dispatcher dis = Dispatcher.getDis();

        dis.register(Car.class.getName(), car);
        dis.register(Fly.class.getName(), fly);

        Server server = new Server(new InetSocketAddress("localhost", 9099));
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);
        handler.addServlet(MyHttpRPCHandler.class,"/*");
        try {
            server.start();
            server.join();  //服务不停止
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRPCObj() {
        Fly fly = MyProxy.proxyGet(Fly.class);
        Person per = fly.getPerson("sdf");
        System.out.println("per:" + per);


    }


}















