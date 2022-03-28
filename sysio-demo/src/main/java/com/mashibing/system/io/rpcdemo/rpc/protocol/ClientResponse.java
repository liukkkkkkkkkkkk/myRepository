package com.mashibing.system.io.rpcdemo.rpc.protocol;

import com.mashibing.system.io.rpcdemo.ResponseMappingCallback;
import com.mashibing.system.io.rpcdemo.rpc.util.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class ClientResponse extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
           System.out.println("client  channelRead ");


        Package respPackage = (Package) msg;
        MyHeader header = respPackage.getHeader();
        //  MyContent content = respPackage.getContent();
        long reqUid = header.getReqUid();
        //  System.out.println("client receive reqUid:"+reqUid);
        // String arg = (String) content.getArgs()[0];
        ResponseMappingCallback.exeCallBack(reqUid, respPackage);
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

