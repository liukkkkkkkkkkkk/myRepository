package com.mashibing.system.io.rpcdemo;

import com.mashibing.system.io.rpcdemo.proxy.MyProxy;
import com.mashibing.system.io.rpcdemo.proxy.MyProxy2;
import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyRPCHanderServlet;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ServerRequestHander;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ServerRequestHander2;
import com.mashibing.system.io.rpcdemo.rpc.services.Car;
import com.mashibing.system.io.rpcdemo.rpc.services.Fly;
import com.mashibing.system.io.rpcdemo.rpc.services.MyCar;
import com.mashibing.system.io.rpcdemo.rpc.services.MyFly;
import com.mashibing.system.io.rpcdemo.rpc.transport.ServerDecode2;
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
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 49178
 * @create 2021/12/20
 */
public class MyRPCTest2 {

    @Test
    public void get() {
     /*   AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                Car car = MyProxy2.proxyGet2(Car.class);  //动态代理实现接口
                String arg = "hello " + atomicInteger.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println(" client over message :" + res + "  request arg :" + arg);
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }*/
        Fly fly = MyProxy2.proxyGet2(Fly.class);
        Car myCar = MyProxy2.proxyGet2(Car.class);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 20; i++) {
            new Thread() {
                @Override
                public void run() {
                    String ss = "hello" + atomicInteger.incrementAndGet();
                    String ooxx = myCar.ooxx(ss);
                    System.out.println("client send :" + ss + "  receive:" + ooxx);
                }
            }.start();

        }
    /*    try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("main thread over..");
    }


    @Test
    public void startServer() {
        Car myCar = new MyCar();
        Fly myfly = new MyFly();
        Dispatcher dispatcher = Dispatcher.getDis();
        dispatcher.register(Car.class.getName(), myCar);
        dispatcher.register(Fly.class.getName(), myfly);

        NioEventLoopGroup group = new NioEventLoopGroup(10);
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture future = sbs.group(group, group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ServerDecode2());
                        pipeline.addLast(new ServerRequestHander2(dispatcher));
                    }
                })
                .bind(new InetSocketAddress("localhost", 9099));

        try {
            future.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void startHttpServerWithNetty() {
        Car myCar = new MyCar();
        Fly myfly = new MyFly();
        Dispatcher dispatcher = Dispatcher.getDis();
        dispatcher.register(Car.class.getName(), myCar);
        dispatcher.register(Fly.class.getName(), myfly);
        NioEventLoopGroup group = new NioEventLoopGroup(20);
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture channelFuture = sbs.group(group, group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 512));
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                FullHttpRequest request = (FullHttpRequest) msg;
                                System.out.println("request:" + request.toString());

                                ByteBuf buf = request.content();
                                byte[] data = new byte[buf.readableBytes()];
                                buf.readBytes(data);
                                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                MyContent content = (MyContent)ois.readObject();
                                String serviceName = content.getName();
                                Object o = dispatcher.get(serviceName);
                                Class<?> clazz = o.getClass();
                                Method method = clazz.getMethod(content.getMethodName(), content.getParameterTypes());
                                Object res = method.invoke(o, content.getArgs());
                                MyContent resContent = new MyContent();
                                resContent.setRes(res);
                                byte[] resContentnBytes = SerDecodeUtil.ser2(resContent);
                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(resContentnBytes));
                               response.headers().set(HttpHeaderNames.CONTENT_LENGTH,resContentnBytes.length);
                               ctx.writeAndFlush(response);
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
    public void startHttpServerWithJetty(){
        MyCar car = new MyCar();
        MyFly fly = new MyFly();

        Dispatcher dis = Dispatcher.getDis();

        dis.register(Car.class.getName(), car);
        dis.register(Fly.class.getName(), fly);

        Server server = new Server(new InetSocketAddress("localhost",9099));
        ServletContextHandler handler = new ServletContextHandler(server, "/*");
        server.setHandler(handler);
        handler.addServlet(MyRPCHanderServlet.class,"/");
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
