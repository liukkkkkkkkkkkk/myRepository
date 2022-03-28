package com.mashibing.system.io.testreactor;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 49178
 * @create 2021/12/2
 */
public class SelectorThreadGroup {
    SelectorThread[] sts;
    ServerSocketChannel server;
    AtomicInteger xid = new AtomicInteger(0);
    SelectorThreadGroup stg =this;
    public void setWork(SelectorThreadGroup workGroup){
       this.stg =workGroup;
    }

    SelectorThreadGroup(int num) {
        //num线程数
        sts = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread(this);
            new Thread(sts[i]).start();
        }
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            //注册到哪一个selector上？
            nextSelectV4(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextSelectV4(Channel channel) {
        SelectorThread selectorThread = nextV4(channel);
        selectorThread.queue.add(channel);  //通过对列传递数据
        if (channel instanceof  ServerSocketChannel){
            selectorThread.setStg(this.stg);
        }
        selectorThread.selector.wakeup(); //通过打断阻塞，让对应的线程自己去完成selector的注册
        //重点，channel有可能是server,或者client
   /*     ServerSocketChannel server= (ServerSocketChannel) channel;
        try {

            //int nums = selector.select(); 另一个线程跑起来后，select()方法阻塞，所以下面这行代码server.register也会被阻塞，使用wakeup解决
            server.register(selectorThread.selector, SelectionKey.OP_ACCEPT);  //会被阻塞！
            System.out.println("accept...");
            selectorThread.selector.wakeup(); //wakeup方法是 让另一个线程里的 selector.select()立刻返回不阻塞
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/
    }

    //无论是serversocketchannel还是socketchannel，都是用这个方法选择注册到哪个selector上
    private SelectorThread nextV4(Channel channel) {
            int index = xid.incrementAndGet() % (sts.length );
            SelectorThread st = sts[index];
            return st;
    }





    public void nextSelectV3(Channel channel) {
        SelectorThread selectorThread = nextV3(channel);
        selectorThread.queue.add(channel);  //通过对列传递数据
        selectorThread.selector.wakeup(); //通过打断阻塞，让对应的线程自己去完成selector的注册
        //重点，channel有可能是server,或者client
   /*     ServerSocketChannel server= (ServerSocketChannel) channel;
        try {

            //int nums = selector.select(); 另一个线程跑起来后，select()方法阻塞，所以下面这行代码server.register也会被阻塞，使用wakeup解决
            server.register(selectorThread.selector, SelectionKey.OP_ACCEPT);  //会被阻塞！
            System.out.println("accept...");
            selectorThread.selector.wakeup(); //wakeup方法是 让另一个线程里的 selector.select()立刻返回不阻塞
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/
    }

    //无论是serversocketchannel还是socketchannel，都是用这个方法选择注册到哪个selector上
    private SelectorThread nextV3(Channel channel) {
        if (channel instanceof ServerSocketChannel) {
            return sts[0];   //监听的固定绑定一个线程
        } else {  //连接的socket轮询绑定
            int index = xid.incrementAndGet() % (sts.length - 1);
            SelectorThread st = sts[index + 1];
            return st;
        }
    }


    public void nextSelectV2(Channel channel) {
        SelectorThread selectorThread = nextV2(channel);
        selectorThread.queue.add(channel);  //通过对列传递数据
        selectorThread.selector.wakeup(); //通过打断阻塞，让对应的线程自己去完成selector的注册
        //重点，channel有可能是server,或者client
   /*     ServerSocketChannel server= (ServerSocketChannel) channel;
        try {

            //int nums = selector.select(); 另一个线程跑起来后，select()方法阻塞，所以下面这行代码server.register也会被阻塞，使用wakeup解决
            server.register(selectorThread.selector, SelectionKey.OP_ACCEPT);  //会被阻塞！
            System.out.println("accept...");
            selectorThread.selector.wakeup(); //wakeup方法是 让另一个线程里的 selector.select()立刻返回不阻塞
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/
    }

    //无论是serversocketchannel还是socketchannel，都是用这个方法选择注册到哪个selector上
    private SelectorThread nextV2(Channel channel) {
        int index = xid.incrementAndGet() % sts.length;
        SelectorThread st = sts[index];
        return st;
    }


    private void nextSelectV1(Channel channel) {
        SelectorThread selectorThread = nextV1(channel);
        //重点，channel有可能是server,或者client
        ServerSocketChannel server = (ServerSocketChannel) channel;
        try {

            //int nums = selector.select(); 另一个线程跑起来后，select()方法阻塞，所以下面这行代码server.register也会被阻塞，使用wakeup解决
            server.register(selectorThread.selector, SelectionKey.OP_ACCEPT);  //会被阻塞！
            System.out.println("accept...");
            selectorThread.selector.wakeup(); //wakeup方法是 让另一个线程里的 selector.select()立刻返回不阻塞
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    //无论是serversocketchannel还是socketchannel，都是用这个方法选择注册到哪个selector上
    private SelectorThread nextV1(Channel channel) {
        int index = xid.incrementAndGet() % sts.length;
        SelectorThread st = sts[index];
        return st;
    }
}
