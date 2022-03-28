package com.mashibing.net.socket.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketeServer {
    public static void main(String[] args) throws IOException {
        //服务端要使用serversocket开放本地端口号
        ServerSocket serverSocket = new ServerSocket(10086);
        //需要接受客户端传过来的数据，需要定义socket对象
        Socket socket = serverSocket.accept();
        //通过socket获得输入流
        InputStream inputStream = socket.getInputStream();
        //对流进行包装，可以读字符串
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String str = dataInputStream.readUTF();
        System.out.println(str);
        dataInputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();


    }
}
