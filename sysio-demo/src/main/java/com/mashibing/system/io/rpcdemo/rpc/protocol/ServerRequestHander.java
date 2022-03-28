package com.mashibing.system.io.rpcdemo.rpc.protocol;
import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
import com.mashibing.system.io.rpcdemo.rpc.util.Package;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 49178
 * @create 2021/12/14
 */
public//server端的处理
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
                returnContent.setRes(res);
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
