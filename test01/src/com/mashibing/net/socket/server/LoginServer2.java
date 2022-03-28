package com.mashibing.net.socket.server;

import com.mashibing.net.socket.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(10002);
        while(true){ //while实现接收多个登录请求
            Socket server = serverSocket.accept();
            // 获取输入流
            InputStream inputStream = server.getInputStream();
            //需要使用ObjectInputStream对象获取user
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user =(User) objectInputStream.readObject();
            String str ="";
            if("zhangsan".equals(user.getUsername())&&"123456".equals(user.getPassword())){
                str ="登录成功";
                System.out.println("欢迎你," + user.getUsername());
            }else{
                str ="登录失败";
            }
            //截断输入流
            server.shutdownInput();
            OutputStream outputStream = server.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(str);
            server.shutdownOutput();
            //关闭流
            dataOutputStream.close();
            outputStream.close();
            objectInputStream.close();
            inputStream.close();
        }

        //serverSocket.close();
    }
}
