package socketStepServer.step2;

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
public class Step2Server {

    Function<String,String> hander;
    public Step2Server(Function<String,String> hander){
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

    void  accept() throws IOException {
        Socket socket = serverSocket.accept();
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  handerSocket(socket);
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
    }
    void handerSocket(Socket socket) throws IOException {
        try {
           // Blocking..
            // Thread -->sleep  --->other threads
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
                sb.append(line).append('\n');

            }
           /* while (!(line=reader.readLine()).isEmpty()){
                sb.append(line).append('\n');
            }*/
            String request = sb.toString();
            System.out.println(request);
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
        Step2Server step2Server = new Step2Server(req -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "HTTP/1.1 201 ok \n\n very gooddd!\n";
        });
      step2Server.listen(8089);

    }

}
