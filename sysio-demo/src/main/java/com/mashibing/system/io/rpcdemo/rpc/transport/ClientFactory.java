package com.mashibing.system.io.rpcdemo.rpc.transport;

import com.mashibing.system.io.rpcdemo.ResponseMappingCallback;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ClientResponse;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHeader;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

//连接池
public  class ClientFactory {
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

    public NioSocketChannel getClient(InetSocketAddress socketAddress) {
        ClientPool clientPool = outBoxes.get(socketAddress);

        if (clientPool == null) {
            synchronized (outBoxes) {
                if (clientPool ==null){ //两个线程一个抢到锁，一个没抢到，一直卡在这里，第一个线程往下走后，
                    // 为防止第二个线程重复添加，需要再次判断lientPool ==null为空
                    outBoxes.putIfAbsent(socketAddress, new ClientPool(poolSize));
                    clientPool = outBoxes.get(socketAddress);
                }
            }
        }


        NioSocketChannel[] clients = clientPool.getClients();
        int index = random.nextInt(poolSize);
        NioSocketChannel client = clients[index];
        if (client != null && client.isActive()) {
            return clients[index];
        }else {
            //如果 没创建，或断开了
            synchronized (clientPool.locks[index]) {
                if (client==null || !client.isActive()){  //和上面一样，两个线程一个抢到锁，一个没抢到，一直卡在这里，第一个线程执行完后，
                    // 第二个线程往下执行时要再次判断条件
                    clients[index] = createSocketChannel(socketAddress);
                }

            }
        }
        return clients[index] ;
    }

    public static CompletableFuture transport(MyContent content){
//        String type ="PRC";
        String type ="HTTP";
        CompletableFuture<Object> res = new CompletableFuture<>(); //

        if ("RPC".equals(type)){
        // TODO 未来协议可能会变
        byte[] contentBytes = SerDecodeUtil.ser(content);
        MyHeader myHeader = MyHeader.createHeader(contentBytes);
        long reqUid = myHeader.getReqUid();
        byte[] headBytes = SerDecodeUtil.ser(myHeader);
        System.out.println("client send header length：" + headBytes.length);
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(contentBytes.length + headBytes.length);
        byteBuf.writeBytes(headBytes);
        byteBuf.writeBytes(contentBytes);

        NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9099));
        System.out.println("ResponseMappingCallback.addCallBack reqUid:"+reqUid);
        ResponseMappingCallback.addCallBack(reqUid, res);
           ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
       }else {
           //使用http协议作为载体
           //1. 使用URL现成的工具（已经包含了编解码，发送和socket连接的建立
          // urlTs(content,res);

            //2，自己操心：on netty  （io 框架）+ 已经提供的http相关的编解码
            nettyTs(content,res);
        }

        return res;

    }

    private static void nettyTs(MyContent content, CompletableFuture<Object> res) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        Bootstrap client = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        FullHttpResponse response = (FullHttpResponse) msg;
                                        System.out.println("response:"+response);
                                        ByteBuf resBytes = response.content();
                                        byte[] data = new byte[resBytes.readableBytes()];
                                        resBytes.readBytes(data);
                                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent resContent = (MyContent) ois.readObject();
                                        Object result = resContent.getRes();
                                        res.complete(result);
                                    }
                                });
                    }
                });
        try {
            ChannelFuture future = client.connect("localhost", 9099).sync();
            Channel channel = future.channel();
            byte[] data = SerDecodeUtil.ser(content);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
                    HttpMethod.POST,
                    "/sdfaddd",  // 可随便给
                    Unpooled.copiedBuffer(data)
            );
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,data.length);
            channel.writeAndFlush(request).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void urlTs(MyContent content, CompletableFuture<Object> res) {
        try {  ///每次请求占用一个连接 ，因为使用的http协议
            URL url = new URL("http://localhost:9099/");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpCon = (HttpURLConnection) urlConnection;
            //post 方式
            httpCon.setRequestMethod("POST");
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            OutputStream out = httpCon.getOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(content);   //还没真的发出去
            if (httpCon.getResponseCode()==200){ //BIO 发出去后就一直阻塞着
                InputStream in =httpCon.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(in);
                MyContent resContent = (MyContent)ois.readObject();
                res.complete(resContent.getRes());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
