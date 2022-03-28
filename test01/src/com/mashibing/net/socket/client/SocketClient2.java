package com.mashibing.net.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient2 {
    public static void main(String[] args) throws IOException {
        //创建客户端的套接字
        Socket client = new Socket("127.0.0.1", 30000);
        OutputStream outputStream = client.getOutputStream();
        //向外输出数据
        outputStream.write("hello,java".getBytes());
        ///////////接收服务端返回的数据
        InputStream inputStream = client.getInputStream();
        byte[] bytes = new byte[1024];
        int lenth = inputStream.read(bytes);
        String str = "服务端的响应数据：" + new String(bytes, 0, lenth);
        System.out.println(str);
        inputStream.close();
        outputStream.close();
        client.close();
    }
}
