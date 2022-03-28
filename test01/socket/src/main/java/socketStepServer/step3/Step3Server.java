package socketStepServer.step3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 49178
 * @create 2022/2/3
 */
public class Step3Server {

    private final IHanderInterface httpHandler;

    public  Step3Server(IHanderInterface handerInterface){
        this.httpHandler =handerInterface;
    }
    ServerSocket serverSocket ;
    public  void  listen (int port) throws IOException {
         serverSocket =new ServerSocket(8088);
        while (true){
            this.accept();
        }
    }

    private void accept() throws IOException {
        Socket socket = serverSocket.accept();
        new Thread(()->{
            try {
                this.handler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handler(Socket socket) throws IOException {
        Request request =new Request(socket);
        Response response =new Response(socket);
        this.httpHandler.handler(request,response);
    }

    public static void main(String[] args) throws IOException {
        Step3Server step3Server = new Step3Server((req,resp)->{
            System.out.println("requestHeaders:"+req.getHeaders());
            System.out.println("requestBody:"+req.getBody());
            System.out.println("requestMethod:"+req.getMethod());
            resp.sendMsg("hello world!!!");
        });
        step3Server.listen(8088);
    }
}
