package com.mashibing.net.socket.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketeServer2 {
    public static void main(String[] args) throws IOException {
        //服务端要使用serversocket开放本地端口号
        ServerSocket serverSocket = new ServerSocket(30000);
        //获取服务端的套接字对象
        Socket socket = serverSocket.accept(); //阻塞
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int lenth = inputStream.read(bytes);
        System.out.println("服务端接受到的数据是：" + new String(bytes, 0, lenth));

        /////////////////////////响应给客户端
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("你好，客户端".getBytes());
        //////////关闭流
        outputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();

    }
}
