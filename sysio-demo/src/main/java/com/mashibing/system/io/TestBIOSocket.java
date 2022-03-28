package com.mashibing.system.io;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class TestBIOSocket {
    public static void main(String[] args) throws IOException {
        System.out.println(1111);
        ServerSocket serverSocket = new ServerSocket(9090);
        while (true){
             Socket client = serverSocket.accept();  //阻塞
            System.out.println("client port:"+client.getPort());
            new Thread(){
                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = client.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        char[] chars = new char[1024];
                        int len = reader.read(chars);
                        if (len>0){
                            String msg =new String(chars,0,len);
                            System.out.println("从客户端读到了数据："+msg);
                        }

                        // System.out.println(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }
}
