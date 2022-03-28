package com.mashibing.system.io.rpcdemo.rpc.protocol;

import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;
import com.mashibing.system.io.rpcdemo.rpc.util.Package;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
import com.sun.xml.internal.stream.util.BufferAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 49178
 * @create 2021/12/20
 */
public class ServerRequestHander2 extends ChannelInboundHandlerAdapter {
    Dispatcher dis;

    public ServerRequestHander2(Dispatcher dis) {
        this.dis = dis;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Package recvPackage = (Package) msg;
        String ioThreadName = Thread.currentThread().getName();
        EventExecutorGroup exeGroup = ctx.executor().parent();
        EventExecutor next = exeGroup.next();
        next.execute(new Runnable() {
            @Override
            public void run() {
          /*      String serviceName = recvPackage.getContent().getName();
                String methodName = recvPackage.getContent().getMethodName();
                Class<?>[] parameterTypes = recvPackage.getContent().getParameterTypes();
                Object[] args = recvPackage.getContent().getArgs();
                Object obj = dis.get(serviceName);
                Class<?> clazz= obj.getClass();
                Object res =null;
                try {
                    Method method = clazz.getMethod(methodName, parameterTypes);
                    res= method.invoke(obj, args);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                MyContent returnContent = new MyContent();
                returnContent.setRes(res);
                byte[] contentBytes = SerDecodeUtil.ser(returnContent);
                //组装 新的header content返回给客户端
                MyHeader respoondHeader = new MyHeader();
                respoondHeader.setReqUid(recvPackage.getHeader().getReqUid());
                System.out.println("server return reqUid:"+recvPackage.getHeader().getReqUid());
                respoondHeader.setFlag(0x14141424);
                respoondHeader.setLength(contentBytes.length);
                byte[] headerBytes = SerDecodeUtil.ser(respoondHeader);
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + contentBytes.length);
                byteBuf.writeBytes(headerBytes);
                byteBuf.writeBytes(contentBytes);
                ctx.writeAndFlush(byteBuf);*/

                MyContent content = recvPackage.getContent();
                MyContent returnContent =new MyContent();
                MyHeader respHeader =new MyHeader();
                String service = content.getName();
                String methodName = content.getMethodName();
                Class<?>[] parameterTypes = content.getParameterTypes();
                Object[] args = content.getArgs();
                Object o = dis.get(service);
                Class<?> aClass = o.getClass();

                Method method = null;
                Object res =null;
                try {
                    method = aClass.getMethod(methodName, parameterTypes);
                     res = method.invoke(o, args);
                    System.out.println("retrun res:"+res.toString());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                    returnContent.setRes(res);
                    byte[] returnContentBytes= SerDecodeUtil.ser2(returnContent);
                    respHeader.setLength(returnContentBytes.length);
                    respHeader.setFlag(0x14141424);
                    respHeader.setReqUid(recvPackage.getHeader().getReqUid());
                    System.out.println("server return reqId11:"+recvPackage.getHeader().getReqUid());
                    byte[] headerBytes = SerDecodeUtil.ser2(respHeader);
                    ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(returnContentBytes.length + headerBytes.length);
                    byteBuf.writeBytes(headerBytes).writeBytes(returnContentBytes);
                    ctx.writeAndFlush(byteBuf);


            }
        });

    }
}
