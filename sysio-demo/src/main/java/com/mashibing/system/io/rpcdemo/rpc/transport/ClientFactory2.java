package com.mashibing.system.io.rpcdemo.rpc.transport;

import com.mashibing.system.io.rpcdemo.ResponseMappingCallback;
import com.mashibing.system.io.rpcdemo.ResponseMappingCallback2;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ClientResponse;
import com.mashibing.system.io.rpcdemo.rpc.protocol.ClientResponse2;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;
import com.mashibing.system.io.rpcdemo.rpc.protocol.MyHeader;
import com.mashibing.system.io.rpcdemo.rpc.util.SerDecodeUtil;
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
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.HttpConnection;

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
public class ClientFactory2 {
    private static final ClientFactory2 factory;
    private int poolSize = 10;
    Random random = new Random();

    static {
        factory = new ClientFactory2();
    }

    public static ClientFactory2 getFactory() {
        return factory;
    }

    ConcurrentHashMap<InetSocketAddress, ClientPool2> outBoxes = new ConcurrentHashMap();


    public NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool2 clientPool2 = outBoxes.get(address);
        if ( outBoxes.get(address) == null) {
            synchronized (outBoxes) {
                if (clientPool2 ==null){ //两个线程一个抢到锁，一个没抢到，一直卡在这里，第一个线程往下走后，
                    // 为防止第二个线程重复添加，需要再次判断lientPool ==null为空
                    System.out.println("outBoxes.putIfAbsent(address, new ClientPool2(poolSize))");
                     clientPool2 = new ClientPool2(poolSize);
                    outBoxes.putIfAbsent(address, clientPool2);
                }
            }
        }
        NioSocketChannel[] clients = clientPool2.getClients();
        int num = random.nextInt(poolSize);
        NioSocketChannel client = clients[num];
        if (client != null && client.isActive()) {
            return clients[num];
        } else {
            synchronized (clientPool2.getLocks()[num]) {
                if (client == null || !client.isActive()) {
                    client = createSocketChannel2(address);
                }
            }
        }
        return client;

    }


    
/*    public NioSocketChannel getClient(InetSocketAddress socketAddress) {
        ClientPool2 ClientPool2 = outBoxes.get(socketAddress);

        if (ClientPool2 == null) {
            synchronized (outBoxes) {
                if (ClientPool2 ==null){ //两个线程一个抢到锁，一个没抢到，一直卡在这里，第一个线程往下走后，
                    // 为防止第二个线程重复添加，需要再次判断lientPool ==null为空
                    outBoxes.putIfAbsent(socketAddress, new ClientPool2(poolSize));
                    ClientPool2 = outBoxes.get(socketAddress);
                }
            }
        }


        NioSocketChannel[] clients = ClientPool2.getClients();
        int index = random.nextInt(poolSize);
        NioSocketChannel client = clients[index];
        if (client != null && client.isActive()) {
            return clients[index];
        }else {
            //如果 没创建，或断开了
            synchronized (ClientPool2.locks[index]) {
                if (client==null || !client.isActive()){  //和上面一样，两个线程一个抢到锁，一个没抢到，一直卡在这里，第一个线程执行完后，
                    // 第二个线程往下执行时要再次判断条件
                    clients[index] = createSocketChannel(socketAddress);
                }

            }
        }
        return clients[index] ;
    }*/

    enum TransType {
        HTTP,
        RPC,
        OTHER;
    }


    public static CompletableFuture transport(MyContent myContent) {
        TransType type = TransType.HTTP;
        CompletableFuture<Object> res = new CompletableFuture<>();
        switch (type) {
            case HTTP:
               // urlTs(myContent,res);
                nettyTs(myContent,res);
                break;
            case RPC:
                byte[] contentBytes = SerDecodeUtil.ser2(myContent);
                MyHeader header = MyHeader.createHeader2(contentBytes);
                byte[] headerBytes = SerDecodeUtil.ser2(header);
                System.out.println("client send length:" + headerBytes.length);
                long reqUid = header.getReqUid();
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + contentBytes.length);
                byteBuf.writeBytes(headerBytes).writeBytes(contentBytes);
                NioSocketChannel channel = factory.getClient(new InetSocketAddress("localhost", 9099));
                System.out.println("ResponseMappingCallback.addCallBack reqUid:"+reqUid);
                ResponseMappingCallback2.addCallBack(reqUid, res);
                ChannelFuture future = channel.writeAndFlush(byteBuf);
                break;
            case OTHER:
                break;
        }


        return res;

    }



/*    public static CompletableFuture transport(MyContent content){
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
         //  urlTs(content,res);

            //2，自己操心：on netty  （io 框架）+ 已经提供的http相关的编解码
            nettyTs(content,res);
        }

        return res;

    }*/

private  static  void nettyTs(MyContent content,CompletableFuture<Object> res){
    NioEventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bs = new Bootstrap();
    ChannelFuture connect = bs.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new HttpClientCodec())
                            .addLast(new HttpObjectAggregator(1024*512))
                            .addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    FullHttpResponse response = (FullHttpResponse) msg;
                                    ByteBuf buf = response.content();
                                    System.out.println("response:"+response.toString());
                                    byte[] data = new byte[buf.readableBytes()];
                                    buf.readBytes(data);
                                    ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                    MyContent resContent = (MyContent) oin.readObject();
                                    System.out.println("client receive:"+resContent.getRes());
                                    res.complete(resContent.getRes());
                                }
                            });
                }
            })
            .connect(new InetSocketAddress("localhost", 9099));
    try {
        Channel channel = connect.sync().channel();
        byte[] sentContnets = SerDecodeUtil.ser2(content);   //
        /*HTTP_1_1报的这个问题是啥？？ response:HttpObjectAggregator$AggregatedFullHttpResponse(decodeResult: success, version: HTTP/1.1, content: CompositeByteBuf(ridx: 0, widx: 50, cap: 50, components=1))
HTTP/1.1 400 No Host*/
        DefaultHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST,
                "/*",
                Unpooled.copiedBuffer(sentContnets));
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,sentContnets.length);
        System.out.println("reqest:"+request.toString());
        channel.writeAndFlush(request).sync();
//        channel.closeFuture().sync();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

}
  /*  private static void nettyTs(MyContent content, CompletableFuture<Object> res) {
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
                                        System.out.println("response:" + response);
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
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.length);
            channel.writeAndFlush(request).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
*/
    private static void urlTs(MyContent myContent, CompletableFuture<Object> res) {
        try {
            URL url = new URL("http://localhost:9099/");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpCon =(HttpURLConnection)urlConnection;
            httpCon.setDoInput(true);
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            OutputStream os = httpCon.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(myContent);
            if ( 200==(httpCon.getResponseCode())){
                InputStream is = httpCon.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                MyContent resContent = (MyContent) ois.readObject();
                res.complete(resContent.getRes());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    private NioSocketChannel createSocketChannel2(InetSocketAddress address) {
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        Bootstrap bs = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(5);
        ChannelFuture future = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ClientDecode2());
                        pipeline.addLast(new ClientResponse2());

                    }
                })
                .connect(address);
        NioSocketChannel channel = null;
        try {
            channel = (NioSocketChannel) future.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }
   /* private NioSocketChannel createSocketChannel(InetSocketAddress socketAddress) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ServerDecode2());
                        pipeline.addLast(new ClientResponse2());
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
    }*/
}
