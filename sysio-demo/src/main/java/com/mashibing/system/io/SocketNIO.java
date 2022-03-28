package com.mashibing.system.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SocketNIO {
    public static void main(String[] args) throws IOException {
        LinkedList<SocketChannel> list = new LinkedList<>();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(9090));
        channel.configureBlocking(false); //设置非阻塞

        while (true){
            try {
                Thread.sleep(1000);
                SocketChannel client = channel.accept();
                if (client!=null){
                    client.configureBlocking(false); //设置非阻塞
                    System.out.println("来客户端连接了");
                    int port = client.socket().getPort();
                    System.out.println("port:"+port);
                    list.add(client);
                }

                ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                for (SocketChannel ss :list){
                    int len = ss.read(buffer);
                    if (len>0){
                        buffer.flip();
                        byte[] aaa = new byte[buffer.limit()];
                        buffer.get(aaa);
                        String b = new String(aaa);
                        System.out.println(ss.socket().getPort() + " : " + b);
                        buffer.clear();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
