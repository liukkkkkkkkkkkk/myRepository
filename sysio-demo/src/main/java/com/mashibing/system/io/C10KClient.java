package com.mashibing.system.io;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class C10KClient {
    public static void main(String[] args) {
        LinkedList<SocketChannel>clients =new LinkedList<>();
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.33.104", 9090);
        for (int i = 10000; i < 65000; i++) {
            try {
                SocketChannel client1 = SocketChannel.open();
                SocketChannel client2 = SocketChannel.open();
                client1.bind(new InetSocketAddress("192.168.0.103", i));
                client2.bind(new InetSocketAddress("192.168.33.1", i));
                client1.connect(serverAddr);
                boolean open = client1.isOpen();
                   client2.connect(serverAddr);
                clients.add(client1);
             //   clients.add(client2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("cliets size:"+clients.size());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Test
    public void test1() {
        for (int i = 10000; i < 65000; i++) {

            try {
                Socket client = new Socket("192.168.33.104", 9090);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
