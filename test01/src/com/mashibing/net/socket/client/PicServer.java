package com.mashibing.net.socket.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PicServer {
    public static void main(String[] args) throws Exception {
        //创建服务端对象，开放端口
        ServerSocket serverSocket = new ServerSocket(10086);
        //创建服务端socket
        Socket server = serverSocket.accept();
        InputStream inputStream = server.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("tupian.jpg");
        int temp =0;
        while ((temp =inputStream.read())!=-1){
            fileOutputStream.write(temp);
        }
        server.shutdownInput();
        //响应客户端
        OutputStream os = server.getOutputStream();
        os.write("图片上传成功".getBytes());
        server.shutdownOutput();
        //关闭流
        os.close();
        fileOutputStream.close();
        inputStream.close();
        server.close();
        serverSocket.close();

    }
}
