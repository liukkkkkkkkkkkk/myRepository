package com.mashibing.net.socket.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
/**
 * 客户端向服务端发数据
 * 发送时先启动服务端
 * */
    public static void main(String[] args) throws IOException {
        //创建socket对象，开启实现io的虚拟接口 （类似于网线插槽）
        Socket client = new Socket("localhost", 10086);
        //获取输出流对象，向服务端发数据
        OutputStream os = client.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(os);
        //将流进行包装
        dataOutputStream.writeUTF("你好！");
        //关闭流
        dataOutputStream.close();
        os.close();
        client.close();


    }
}
