package com.mashibing.system.io.rpcdemo.rpc.protocol;

import com.mashibing.system.io.rpcdemo.ResponseMappingCallback;
import com.mashibing.system.io.rpcdemo.ResponseMappingCallback2;
import com.mashibing.system.io.rpcdemo.rpc.util.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class ClientResponse2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ClientResponse2,,,,");
        Package resPackage = (Package) msg;
        MyHeader header = resPackage.getHeader();
        ResponseMappingCallback2.exeCallback(header.getReqUid(),resPackage);

    }
}

