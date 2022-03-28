package com.mashibing.net.socket.server;

import com.mashibing.net.socket.LogInThread;
import com.mashibing.net.socket.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(10002);
        while(true){ //多线程实现接收多个登录请求
            Socket server = serverSocket.accept();
            LogInThread logInThread = new LogInThread(server);
            new Thread(logInThread).start();
        }

        //serverSocket.close();
    }
}
