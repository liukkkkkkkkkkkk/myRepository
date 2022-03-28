package socketStepServer.step3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author 49178
 * @create 2022/2/3
 */
public class Response {
     Socket socket;
    private int status =200;
    private HashMap<Integer,String> codeMap;


    public Response(Socket socket) {
        this.socket =socket;
        if (codeMap ==null){
            codeMap =new HashMap<Integer,String>();
            codeMap.put(200,"ok");
        }
    }
    public void sendMsg(String msg) throws IOException {
      String response ="HTTP/1.1 "+this.status +" " +this.codeMap.get(this.status)+"\n";
      response +="\n";
      response+=msg;
        System.out.println("respponse："+response);
      this.sentRawMsg(response);
    }
    public void sentRawMsg(String msg) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(msg);
        writer.flush();
        socket.close();
    }



}
