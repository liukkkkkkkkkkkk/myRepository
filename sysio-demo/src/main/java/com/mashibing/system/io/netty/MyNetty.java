package com.mashibing.system.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author 49178
 * @create 2021/12/5
 */
public class MyNetty {
    @Test
    public void myByteBuf() {
        //动态分配大小
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(8, 20);
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
  /*      buffer.writeBytes(new byte[]{1,2,3,4});
        System.out.println("________________________");
        print(buffer);*/
    }


    @Test
    public void myByteBuf2() {
        //pool
        ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("________________________");
        print(buffer);
  /*      buffer.writeBytes(new byte[]{1,2,3,4});
        System.out.println("________________________");
        print(buffer);*/
    }

    public static void print(ByteBuf buf) {
        System.out.println("isReadable: " + buf.isReadable()); //是否可读
        System.out.println("readerIndex: " + buf.readerIndex());//从哪里读
        System.out.println("readableBytes: " + buf.readableBytes());//可以读多少个字节
        System.out.println("isWritable: " + buf.isWritable());
        System.out.println("writerIndex: " + buf.writerIndex());
        System.out.println("writableBytes: " + buf.writableBytes());
        System.out.println("capacity: " + buf.capacity());
        System.out.println("maxCapacity: " + buf.maxCapacity());
        System.out.println("isDirect: " + buf.isDirect()); //true 堆外分配 false 堆内
    }



    @Test
    public void myBufTest3(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(8, 20);
        System.out.println("before getBytes readableBytes length:"+buffer.readableBytes() );
        byte[] bytes = new byte[2];
        buffer.getBytes(buffer.readerIndex(), bytes);  //不移动指针
        System.out.println("after getBytes  readableBytes length:"+buffer.readableBytes() );
        buffer.readBytes(bytes);
        System.out.println("after readBytes  readableBytes length:"+buffer.readableBytes() );


    }

    @Test
    public void loopExecutors() throws IOException {
        //group  是一个线程池，定义线池数
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        eventExecutors.execute(() -> {
            try {
                for (; ; ) {
                    System.out.println("hello world01");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        eventExecutors.execute(() -> {
            try {
                for (; ; ) {
                    System.out.println("hello world02");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.in.read();
    }

    @Test //客户端，连接别人 主动发送数据
    public void clientMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);
        //客户端
        NioSocketChannel client = new NioSocketChannel();
        thread.register(client); //epoll_ctl(5,ADD,3)
        //reactor异步的特性 连接异步
        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.33.105", 9999));
        ChannelFuture channelFuture = connect.sync();  //等待连接上了才往下走
        ByteBuf byteBuf = Unpooled.copiedBuffer("hellowhorld".getBytes());
        ChannelFuture send = client.writeAndFlush(byteBuf);  //这里也是异步的
        send.sync(); //发成功才能往下走

        //响应式
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new MyInHandler());  //有事件到达以后，通过管道触发handler处理
        channelFuture.channel().closeFuture().sync();   //等待对方关闭连接，只有关闭连接，连接断开了才会往下走
        System.out.println("client is over");
    }

    //
    // @ChannelHandler.Sharable
    class MyInHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client Registered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client Active");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            //read、write的方法会移动指针，到内容的最后，再往外写时，就读不到内容了
//            CharSequence charSequence = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
            //get、set不会改变指针，需要给出读取的区间
            CharSequence charSequence = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
            System.out.println(charSequence);
            ctx.writeAndFlush(buf);


        }
    }

    @Test
    public void serverMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();
        thread.register(server); //什么时候来客户端不知道，响应式
        ChannelPipeline pipeline = server.pipeline();
//        pipeline.addLast(new MyAcceptHandler(thread, new MyInHandler()));  //accept接受客户端，并且还要注册到selector上
        pipeline.addLast(new MyAcceptHandler(thread, new InitHandler()));
        ChannelFuture bind = server.bind(new InetSocketAddress("192.168.43.169", 9090));
        bind.sync().channel().closeFuture().sync();   // 异步绑定，返回异步的future,给它同步，连接成功了，才能往下走， 并且拿到channel,等它关闭，同步了再往下走

    }


    class MyAcceptHandler extends ChannelInboundHandlerAdapter {
        private NioEventLoopGroup selector;
        private ChannelHandler handler;

        public MyAcceptHandler(NioEventLoopGroup thread, ChannelHandler myInHandler) {
            this.selector = thread;
            this.handler = myInHandler;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("server registered..");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // listen socket  accpet 我们没有调用accept，是框架做的
            SocketChannel client = (SocketChannel) msg;
            //1。注册
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(handler);  //1,client::pipeline[ChannelInit,]
            //上面把initHandler加进client的pipeline了，这里进行注册时，会触发initHandler的channelRegistered方法，
            // 把myInhandler也加到client的pipelinee里，对client的具体处理业务，都写在MyInHandler里
            selector.register(client);
        }
    }

    //为啥要有一个inithandler，可以没有，但是MyInHandler就得设计成单例 Sharable供所有的client使用
    @ChannelHandler.Sharable
    class InitHandler extends ChannelInboundHandlerAdapter {
        @Override   //在client注册到多路复用器时（selector.register(client)），channelRegistered被调用
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client registered....");
            Channel client = ctx.channel();
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(new MyInHandler()); // 2,client::pipeline[ChannelInit,MyInHandler]
            pipeline.remove(this);  //client::pipeline[MyInHandler]  ChannelInit已经没啥用了，把它移除，遍历pipeline时就不用再遍历它了
        }
    }


    @Test
    public void nettyClient() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(group).channel(NioSocketChannel.class)
        //        .handler(new InitHandler())
                  .handler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      protected void initChannel(SocketChannel channel) throws Exception {
                          ChannelPipeline pipeline = channel.pipeline();
                          pipeline.addLast(new MyInHandler());
                      }
                  })
                .connect(new InetSocketAddress("192.168.33.105", 9090));
        Channel client = connect.sync().channel();
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world".getBytes());
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();

    }


    @Test
    public void nettyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap bs = new ServerBootstrap();
        ChannelFuture bind = bs.group(group, group)
                .channel(NioServerSocketChannel.class)
             //   .childHandler(new InitHandler())
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MyInHandler());

                    }
                })
                .bind(8888);


        bind.sync().channel().closeFuture().sync();
    }


    @Test
    public void testThread(){
        while (!flag){//可能永远也停不下来了
            System.out.println("11111111");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag =true;
            }
        }).start();
    }
    static  boolean flag =false;

    @Test
    public void testThread2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
            System.out.println("11111111");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               countDownLatch.countDown();
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        System.out.println("0000");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println("减1 了");
            }
        }).start();

        countDownLatch.await();
        System.out.println("11111111");

    }
}

