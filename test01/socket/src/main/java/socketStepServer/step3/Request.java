package socketStepServer.step3;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 49178
 * @create 2022/2/3
 */
public class Request {
    static  Pattern methodRegex = Pattern.compile("(GET|PUT|POST|DELETE|OPTIONS|TRACE|HEAD)");
    private final String method;
    private final HashMap<String, String> headers;
    private final String body;

    public static Pattern getMethodRegex() {
        return methodRegex;
    }

    public String getMethod() {
        return method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Request(Socket socket) throws IOException {
        //DataInputStream -> primitives (Char,Float) 可读基本数据类型
        // InputStream  -> bytes 二进制
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        // GET /path HTTP/1.1
        String methodLine  = HttpParser.readLine(dataInputStream,"UTF-8");
        Matcher matcher = methodRegex.matcher(methodLine);
        matcher.find();
        String method = matcher.group();  //http请求方法
        // Content-Type:xxxx
        // Length:xxx
        HashMap<String, String> headMap = new HashMap<>();
        Header[] headers = HttpParser.parseHeaders(dataInputStream, "UTF-8");
        for (int i = 0; i < headers.length; i++) {
            headMap.put(  headers[i].getName(),headers[i].getValue());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream));
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        while (dataInputStream.available()>0){
            reader.read(buffer);
            sb.append(buffer);
        }
        this.body=sb.toString();
        this.method =method;
        this.headers =headMap;

        //


    }
}
