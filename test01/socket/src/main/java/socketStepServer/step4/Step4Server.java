package socketStepServer.step4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 49178
 * @create 2022/2/4
 */
public class Step4Server {
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    public void listen(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        //  reactor / reactive
        serverSocketChannel.configureBlocking(false);
         selector = Selector.open();
        serverSocketChannel.register(selector,serverSocketChannel.validOps()); //把这个channel所有的操作注册上去
        ByteBuffer buffer =ByteBuffer.allocate(1024*16);
       // ByteBuffer buffer =ByteBuffer.allocateDirect(1024*16);
        for (;;){  //相比于while(true)少一条判断true的指令？
            int numOfKeys = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){

                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){  //来客户端连接了
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel socketChannel = server.accept();
                    // Kernel ->mmap(buffer 内存映射 ) ->Channel -->User (使用buffer去接收数据)
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);

                }else if(key.isReadable()){  //可读事件
                  SocketChannel socketChannel =  (SocketChannel)key.channel();

                    buffer.clear();
                    socketChannel.read(buffer);
                    String request = new String(buffer.array());
                    System.out.println("request:"+request);
                    buffer.clear();
                    buffer.put("HTTP/1.1 200 ok \n\n test selector!!!!".getBytes());
                    // HTTP/1.1......!_ _
                    //                Pos(Limit)
                    // Pos            Limit
                    buffer.flip();
                    // Kernel <--mmap(buffer 内存映射) <--Channel <--User(使用buffer去接收数据)
                    socketChannel.write(buffer);
                    socketChannel.close();

                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Step4Server step4Server = new Step4Server();
        step4Server.listen(8088);

    }
}
