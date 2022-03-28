package com.mashibing.system.io.netty;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.security.ntlm.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import jdk.nashorn.internal.ir.ReturnNode;
import org.junit.Test;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
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

        new Thread(() -> {
            startServer();
        }).start();
        System.out.println("server started...");

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                Car car = proxyGet(Car.class);  //动态代理实现接口
                String arg = "hello " + atomicInteger.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println(" client over message :" + res + "  request arg :" + arg);
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        Car car = proxyGet(Car.class);  //动态代理实现接口
        car.ooxx("hello world");
        Fly fly = proxyGet(Fly.class);
        fly.xxoo("hello");
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
        Dispatcher dispatcher = new Dispatcher();
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
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ServerRequestHander(dispatcher));
                    }
                }).bind(new InetSocketAddress("localhost", 9099));
        try {
            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //动态代理返回对象
    public static <T> T proxyGet(Class<T> interfaceInfo) {  //先使用jdk自带的实现，基于接口
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};
        T t = (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override  //调用代理对象的方法时触发
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = interfaceInfo.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                MyContent myContent = new MyContent(name, methodName, parameterTypes, args);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(myContent);
                byte[] contentBytes = os.toByteArray();
                MyHeader myHeader = createHeader(contentBytes);
                long reqUid = myHeader.getReqUid();
                os.reset();
                oos = new ObjectOutputStream(os);
                oos.writeObject(myHeader);
                byte[] headBytes = os.toByteArray();
                System.out.println("client send header length：" + headBytes.length);
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(contentBytes.length + headBytes.length);
                byteBuf.writeBytes(headBytes);
                byteBuf.writeBytes(contentBytes);

                ClientFactory factory = ClientFactory.getFactory();
                NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9099));
                CompletableFuture<String> res = new CompletableFuture<>(); //
//                CountDownLatch latch = new CountDownLatch(1);
//                ResponseMappingCallback.addCallBack(reqUid, latch);  //在请求结果回来后再把latch减1
                System.out.println("ResponseMappingCallback.addCallBack2 reqUid:"+reqUid);
                ResponseMappingCallback.addCallBack2(reqUid, res);
                ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
                channelFuture.sync();  //仅代表写出去时同步，得另想办法让调用回来后再往下执行
//                latch.await();
                String result = res.get();//阻塞，等待消息返回，回调方法执行拿到结果了，再往下执行
            //    System.out.println("往下走了");
                return result;
            }
        });

        return t;
    }


    private static MyHeader createHeader(byte[] contentBytes) {
        int f = 0x14141414;
        MyHeader myHeader = new MyHeader();
        myHeader.setFlag(f);
        myHeader.setLength(contentBytes.length);
        myHeader.setReqUid(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        return myHeader;
    }


}

class  Dispatcher{
    static   ConcurrentHashMap<String ,Object> invokeMap=new ConcurrentHashMap<>();
    public  void register(String key ,Object value  ){
        invokeMap.put(key,value);
    }
    public  Object get(String key){
        return invokeMap.get(key);
    }
}
//server端的处理
class ServerRequestHander extends ChannelInboundHandlerAdapter {
    Dispatcher dis;
    public ServerRequestHander(Dispatcher dispatcher) {
        this.dis= dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //  System.out.println("server  channelRead");
        Package requeskPkg = (Package) msg;
        //  System.out.println("server hander:"+buf.content.methodName);
        //  System.out.println("server hander:"+requeskPkg.content.getArgs()[0]);
        String ioThreadName = Thread.currentThread().getName();
        System.out.println("ioThreadName:" + ioThreadName);
        //直接用netty自己的eventExecutor处理业务及返回    exeThreadname  和 ioThreadName发现是同一个线程
   /*     ctx.executor().execute(new Runnable(){
            @Override
            public void run() {
                String exeThreadname = Thread.currentThread().getName();
                MyContent returnContent = new MyContent();
                System.out.println("exeThreadname:"+exeThreadname);
               String ss= "io thread :"+ioThreadName+ "exec thread:"+exeThreadname+ " from args:" +requeskPkg.getContent().getArgs()[0] ;
                System.out.println("SS:"+ss);
                returnContent.setRes(ss);
                byte[] contentBytes = SerDecodeUtil.ser(returnContent);
                //组装 新的header content返回给客户端
                MyHeader respoondHeader = new MyHeader();
                respoondHeader.setReqUid(requeskPkg.getHeader().getReqUid());
                respoondHeader.setFlag(0x141424);
                respoondHeader.setLength(contentBytes.length);
                byte[] headerBytes = SerDecodeUtil.ser(respoondHeader);
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + contentBytes.length);
                byteBuf.writeBytes(contentBytes).writeBytes(headerBytes);
                ctx.writeAndFlush(byteBuf);

            }
        });*/
        ctx.executor().parent().next().execute(new Runnable() {
            /*   从线程池的父亲EventExecutorGroup 去挑选下一个去执行任务。好处是在netty已定义好的NioEventGroup线程数上，不增加新的资源消耗，
                将业务任务，打散到已有的其它的线程里去处理*/
            @Override
            public void run() {
                String exeThreadname = Thread.currentThread().getName();
                MyContent returnContent = new MyContent();
                System.out.println("exeThreadname:" + exeThreadname);
                System.out.println("requeskPkg.getContent().getArgs():"+requeskPkg.getContent().getArgs());
             //   String ss = "io thread :" + ioThreadName + " exec thread:" + exeThreadname + " from args:" + requeskPkg.getContent().getArgs()[0];
                String ss = "io thread :" + ioThreadName + " exec thread:" + exeThreadname + " from args:" + requeskPkg.getContent().getArgs()[0];
                System.out.println("SS:" + ss);


                String serviceName = requeskPkg.getContent().getName();
                String methodName = requeskPkg.getContent().getMethodName();
                Class<?>[] parameterTypes = requeskPkg.getContent().getParameterTypes();
                Object[] args = requeskPkg.getContent().getArgs();
                Object obj = dis.get(serviceName);
                Class<?> clazz= obj.getClass();
                Object res =null;
                try {
                    Method method = clazz.getMethod(methodName, parameterTypes);
                    res= method.invoke(obj, args);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
//                returnContent.setRes(ss);
                returnContent.setRes((String)res);
                byte[] contentBytes = SerDecodeUtil.ser(returnContent);
                //组装 新的header content返回给客户端
                MyHeader respoondHeader = new MyHeader();
                respoondHeader.setReqUid(requeskPkg.getHeader().getReqUid());
                System.out.println("server return reqUid:"+requeskPkg.getHeader().getReqUid());
                respoondHeader.setFlag(0x14141424);
                respoondHeader.setLength(contentBytes.length);
                byte[] headerBytes = SerDecodeUtil.ser(respoondHeader);
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + contentBytes.length);
               byteBuf.writeBytes(headerBytes);
               byteBuf.writeBytes(contentBytes);
                ctx.writeAndFlush(byteBuf);

            }
        });


        //
    }
}

class SerDecodeUtil {
    static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public static synchronized byte[] ser(Object msg) {
        out.reset();
        ObjectOutputStream oout = null;
        byte[] msBody = null;
        try {
            oout = new ObjectOutputStream(out);
            oout.writeObject(msg);
            msBody = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msBody;
    }
}

class ResponseMappingCallback {
    static ConcurrentHashMap<Long, CountDownLatch> callBackMap = new ConcurrentHashMap<Long, CountDownLatch>();
    static ConcurrentHashMap<Long, CompletableFuture<String>> callBackMap2 = new ConcurrentHashMap<Long, CompletableFuture<String>>();

    public static void addCallBack(long reqUid, CountDownLatch latch) {
        callBackMap.putIfAbsent(reqUid, latch);
    }

    public static void addCallBack2(long reqUid, CompletableFuture res) {
        callBackMap2.putIfAbsent(reqUid, res);
    }

    public static void exeCallBack(Long reqId) {
        CountDownLatch countDownLatch = callBackMap.get(reqId);
        countDownLatch.countDown();
        DelMap(reqId);
    }

    public static void exeCallBack2(Long reqId, Package pack) {
        CompletableFuture<String> res = callBackMap2.get(reqId);
       // System.out.println("res:"+res);
      //  System.out.println("pack.getContent().getRes():"+pack.getContent().getRes());
        res.complete(pack.getContent().getRes());
        DelMap2(reqId);
    }

    public static void DelMap(Long reqUid) {
        callBackMap.remove(reqUid);
    }

    public static void DelMap2(Long reqUid) {
        callBackMap2.remove(reqUid);
    }
}

//连接池
class ClientFactory {
    private static final ClientFactory factory;
    private int poolSize = 10;
    Random random = new Random();

    static {
        factory = new ClientFactory();
    }

    public static ClientFactory getFactory() {
        return factory;
    }

    ConcurrentHashMap<InetSocketAddress, ClientPool> outBoxes = new ConcurrentHashMap();

    public synchronized NioSocketChannel getClient(InetSocketAddress socketAddress) {
        ClientPool clientPool = outBoxes.get(socketAddress);
        if (clientPool == null) {
            outBoxes.putIfAbsent(socketAddress, new ClientPool(poolSize));
            clientPool = outBoxes.get(socketAddress);
        }
        NioSocketChannel[] clients = clientPool.getClients();
        int index = random.nextInt(poolSize);
        NioSocketChannel client = clients[index];
        if (client != null && client.isActive()) {
            return clients[index];
        }
        synchronized (clientPool.locks[index]) {
            return clients[index] = createSocketChannel(socketAddress);
        }

    }

    private NioSocketChannel createSocketChannel(InetSocketAddress socketAddress) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ClientResponse());
                    }
                })
                .connect(socketAddress);
        try {
            NioSocketChannel channel = (NioSocketChannel) connect.sync().channel();
            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}


class ClientResponse extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //   System.out.println("client  channelRead ");


        Package respPackage = (Package) msg;
        MyHeader header = respPackage.getHeader();
      //  MyContent content = respPackage.getContent();
        long reqUid = header.getReqUid();
      //  System.out.println("client receive reqUid:"+reqUid);
       // String arg = (String) content.getArgs()[0];
        ResponseMappingCallback.exeCallBack2(reqUid, respPackage);
       /* ByteBuf buf = (ByteBuf) msg;
        ByteBuf copy = buf.copy();
        if (buf.readableBytes() >= 160) {
            byte[] bytes = new byte[160];
            buf.readBytes(bytes);
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(is);
            MyHeader header = (MyHeader) ois.readObject();
            System.out.println("client  read header length:" + header.getLength());
            long reqUid = header.getReqUid();
            System.out.println("requestId" + reqUid); //TODO:拿到requestId，让前面等待请求返回的线程继续执行
            ResponseMappingCallback.exeCallBack(reqUid);

           // copy
            //buf偏移了，如果剩余可读的长读大于header.getLength()
        *//*    if (buf.readableBytes()>=header.getLength()){
                byte[]data =new byte[header.getLength()];
                buf.readBytes(data);
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                MyContent content = (MyContent)objectInputStream.readObject();
                String name = content.getName();
                ResponseMappingCallback.exeCallBack(reqUid);

            }*/
    }
}


class ServerDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

        while(buf.readableBytes() >= 100) {
            byte[] bytes = new byte[100];
            buf.getBytes(buf.readerIndex(),bytes);  //从哪里读取，读多少，但是readindex不变
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            MyHeader header = (MyHeader) oin.readObject();


            //DECODE在2个方向都使用
            //通信的协议
            if(buf.readableBytes() >= header.getLength()+100){
                //处理指针
                buf.readBytes(100);  //移动指针到body开始的位置
                byte[] data = new byte[(int)header.getLength()];
                buf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);

                if(header.getFlag() == 0x14141414){
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new Package(header,content));

                }else if(header.getFlag() == 0x14141424){
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new Package(header,content));
                }


            }else{
                break;
            }


        }

    }
}
  /*  @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        ByteBuf sendBuf = buf.copy();
        while (buf.readableBytes() >= 100) {
            byte[] bytes = new byte[100];
            buf.getBytes(buf.readerIndex(), bytes);  //不移动指针
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(is);
            MyHeader header = (MyHeader) ois.readObject();
          //  System.out.println("server  read header length:" + header.getLength());
            long reqUid = header.getReqUid();
          //  System.out.println("server read requestId:" + reqUid); //TODO:拿到requestId，让前面等待请求返回的线程继续执行


            //decode在客户端和服务端2 个端都使用 根据通信协议判断
            //buf偏移了，如果剩余可读的长读大于header.getLength()
            if (buf.readableBytes() >= header.getLength()) {
                //处理指针
                buf.readBytes(100);  //移动指针到body开始的位置
                byte[] data = new byte[header.getLength()];
                buf.readBytes(data);
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                MyContent content = (MyContent) objectInputStream.readObject();
                String name = content.getName();
                // System.out.println("serverr method name:"+name);
                out.add(new Package(header, content));
              //  ResponseMappingCallback.exeCallBack(reqUid);

                if (header.getFlag() == 0x14141414) {
                    out.add(new Package(header, content));

                } else if (header.getFlag() == 0x14141424) {
                    out.add(new Package(header, content));

                }
            } else {
                break;
            }
        }

    }*/


class Package {
    MyHeader header;
    MyContent content;

    public Package(MyHeader header, MyContent content) {
        this.header = header;
        this.content = content;
    }

    public MyHeader getHeader() {
        return header;
    }

    public void setHeader(MyHeader header) {
        this.header = header;
    }

    public MyContent getContent() {
        return content;
    }

    public void setContent(MyContent content) {
        this.content = content;
    }
}

class ClientPool {
    NioSocketChannel[] clients;
    Object[] locks;

    ClientPool(int size) {
        clients = new NioSocketChannel[size];  //初始出来的数组里面都是null
        locks = new Object[size];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }

    public NioSocketChannel[] getClients() {
        return clients;
    }

    public void setClients(NioSocketChannel[] clients) {
        this.clients = clients;
    }

    public Object[] getLocks() {
        return locks;
    }

    public void setLocks(Object[] locks) {
        this.locks = locks;
    }
}

class MyHeader implements Serializable {
    /*
    通信上的协议
    1，ooxx值
    2，UUID:requestID
    3，DATA_LEN
    */
    int length;
    long reqUid;
    int flag;

    public MyHeader() {

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}

interface Car {
    public String ooxx(String msg);
}

class  MyCar implements Car{
    @Override
    public String ooxx(String msg) {
        System.out.println( "server recevie client arg:"+msg);
        return "server res arg:"+msg;
    }
}

class  MyFly implements Fly{
    @Override
    public void xxoo(String msg) {
        System.out.println( "server recevie client arg:"+msg);
    }
}
interface Fly {
    public void xxoo(String msg);
}

class MyContent implements Serializable {
    String name;
    String methodName;
    Class<?>[] parameterTypes;
    Object[] args;
    String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public MyContent() {
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MyContent(String name, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.name = name;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public MyContent(String name, String methodName, Class<?>[] parameterTypes) {
        this.name = name;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}