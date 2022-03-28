package com.mashibing.net.socket.client;

import com.mashibing.net.socket.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class LoginClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 10002);
        OutputStream os = socket.getOutputStream();
      //  User user = new User("zhangsan", "123456");
        User user =getUser();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(user);
        //接收服务端响应数据
        DataInputStream dataInputStream =new DataInputStream(socket.getInputStream());
        String str = dataInputStream.readUTF();
        System.out.println(str);
        //调用shoutdown方法告诉对方已经传输完成了
        socket.shutdownOutput();
        //关流
        dataInputStream.close();
        objectOutputStream.close();
        os.close();
        socket.close();
    }

    private static User getUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.nextLine();
        System.out.println("请输入密码:");
        String password = scanner.nextLine();
        return new User(username,password);


    }
}
