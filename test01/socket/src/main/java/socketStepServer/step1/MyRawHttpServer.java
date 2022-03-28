package socketStepServer.step1;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 49178
 * @create 2022/2/2
 */
public class MyRawHttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("a socket is created");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str =null;
          /* while (!(str =reader.readLine()).isEmpty() ){
                System.out.println("服务端收到了消息:"+str);
            }*/

            while ((str =reader.readLine())!=null&& str.trim().length()!=0){
                System.out.println("服务端收到了消息:"+str);
            }

         /*   while ((str =reader.readLine())!=null){   //??为啥就是一直阻塞不能往下走了？？？ 阻塞了
                System.out.println("服务端收到了消息:"+str);
            }*/
            System.out.println(1111);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("HTTP/1.1 200 ok\n\nhello world!\n");
            writer.flush();
            socket.close();
        }

    }

    @Test
    public void test1(){
        String str ="  ";
        String str2 ="";

        System.out.println(str.isEmpty());
        System.out.println(str2.isEmpty());
        System.out.println(str.trim().length());
    }
}
