package com.mashibing.net.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LogInThread implements  Runnable  {
  private Socket socket;

    public LogInThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream=null;
        DataOutputStream  dataOutputStream =null;
        try {
             objectInputStream = new ObjectInputStream(socket.getInputStream());
            User user = (User)objectInputStream.readObject();
            String str ="";
            if("zhangsan".equals(user.getUsername())&&"123456".equals(user.getPassword())){
                str ="登录成功";
                System.out.println("欢迎你," + user.getUsername());
            }else{
                str ="登录失败";
            }
            socket.shutdownInput();
             dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(user.getUsername()+str);
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                dataOutputStream.close();
                objectInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
