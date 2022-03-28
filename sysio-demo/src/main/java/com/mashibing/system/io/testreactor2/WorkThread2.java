package com.mashibing.system.io.testreactor2;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 49178
 * @create 2021/12/4
 */
public class WorkThread2 extends ThreadLocal<LinkedBlockingQueue<Channel>> implements Runnable {

    LinkedBlockingQueue<Channel> queue = get();
    Selector selector;
    ThreadGroup2 group2;

    @Override
    protected LinkedBlockingQueue<Channel> initialValue() {
        return new LinkedBlockingQueue<Channel>();
    }

    public WorkThread2(ThreadGroup2 group2) {
        try {
            selector = Selector.open();
            this.group2 = group2;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!queue.isEmpty()) {
                    Channel ch = queue.take();
                    if (ch instanceof ServerSocketChannel) {
                        System.out.println(Thread.currentThread().getName()+ " register accept");
                        ((ServerSocketChannel) ch).register(selector, SelectionKey.OP_ACCEPT);
                    }
                    if (ch instanceof SocketChannel) {
                        System.out.println(Thread.currentThread().getName()+ " register read");
                        SocketChannel client = (SocketChannel) ch;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                    }

                }


                int select = selector.select();
                if (select > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHander(key);
                        }
                        if (key.isReadable()) {
                            readHander(key);
                        }
                        if (key.isWritable()) {

                        }
                    }
                }
       /*     if (ch instanceof  ServerSocketChannel){
                acceptHander(ch);
            }
            if (ch instanceof SocketChannel){

            }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readHander(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " readHander..");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        while (true) {
            try {
                int read = client.read(buffer);
                if (read > 0) {
                    buffer.flip();  //写出
                    client.write(buffer);
                } else if (read == 0) {
                    break;
                } else if (read < 0) { //对方断开了
                    key.cancel();
                    System.out.println(client.getRemoteAddress() + " 断开连接了");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void acceptHander(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " acceptHander..");
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            WorkThread2 workThread2 = group2.next();
            workThread2.queue.put(client);
            workThread2.selector.wakeup();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setThreadGroup(ThreadGroup2 workgroup) {
        this.group2 = workgroup;
    }
}
