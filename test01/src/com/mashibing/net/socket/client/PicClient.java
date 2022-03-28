package com.mashibing.net.socket.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PicClient {
    public static void main(String[] args) throws Exception {
        //创建图片的输入流对象
        FileInputStream fileInputStream = new FileInputStream("jnt.jpg");
        //创建socket
        Socket client =new Socket("localhost",10086);
        //获得输出流对象
        OutputStream outputStream = client.getOutputStream();
        int temp =0;
        while ((temp =  fileInputStream.read())!=-1){
            outputStream.write(temp);
        }
        client.shutdownOutput();

        //接收服务端的响应
        InputStream inputStream = client.getInputStream();
        byte[] buf = new byte[1024];
        int length = inputStream.read(buf);
        String str = new String(buf, 0, length);
        System.out.println(str);
        client.shutdownInput();
        outputStream.close();
        client.close();
        fileInputStream.close();

    }
}
