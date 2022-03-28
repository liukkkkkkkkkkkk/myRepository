package socketStepServer.step1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Function;

/**
 * @author 49178
 * @create 2022/2/3
 */
public class Step1Server {

    Function<String,String> hander;
    public  Step1Server(Function<String,String> hander){
        this.hander=hander;
    }
    ServerSocket serverSocket;

    //Pending Queue
    public void listen(int port) throws IOException {
         serverSocket = new ServerSocket(port);
        while (true){
        this.accept();
        }
    }

    void accept() throws IOException {
        try {
           // Blocking..
            // Thread -->sleep  --->other threads
            Socket socket = serverSocket.accept();
            System.out.println("a socket is created");
            DataInputStream dataInputstream = new DataInputStream(socket.getInputStream());
            BufferedReader reader =new BufferedReader(new InputStreamReader(dataInputstream));
            StringBuffer sb =new StringBuffer();
            String line ="";

            //Radline -> line end  '\n'
            while (true){
                line=reader.readLine();
                if (line==null||line.isEmpty()){
                    break;
                }
                sb.append(line);
                        //.append('\n');

            }
           /* while (!(line=reader.readLine()).isEmpty()){
                sb.append(line).append('\n');
            }*/
            String request = sb.toString();
            System.out.println("request:"+request);  //   GET / HTTP/1.1User-Agent: curl/7.29.0Host: 192.168.0.105:8088Accept: */*
            String response = hander.apply(request);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write(response);
            writer.flush();
            socket.close();

        }catch (SocketException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        Step1Server step1Server = new Step1Server(req -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "HTTP/1.1 201 ok \n\n very good!\n";
        });
      step1Server.listen(8088);

    }

}
