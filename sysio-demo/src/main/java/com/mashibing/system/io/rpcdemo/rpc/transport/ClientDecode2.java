package com.mashibing.system.io.rpcdemo.rpc.transport;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHeader;
import com.mashibing.system.io.rpcdemo.rpc.util.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class ClientDecode2 extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("客户端返回...");
        while (byteBuf.readableBytes()>115){
       byte[] bytes = new byte[115];
      byteBuf.getBytes(byteBuf.readerIndex(), bytes);
       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
       ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
       MyHeader myHeader = (MyHeader) objectInputStream.readObject();
       int length = myHeader.getLength();
       if (byteBuf.readableBytes()>=115+length){
           byteBuf.readBytes(115);
           byte[] data = new byte[length];
           byteBuf.readBytes(data);
           ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(data);
           ObjectInputStream objectInputStream1 = new ObjectInputStream(byteArrayInputStream1);
           MyContent myContent= (MyContent)objectInputStream1.readObject();
           list.add( new Package(myHeader, myContent));
       }
   }
    }
}

