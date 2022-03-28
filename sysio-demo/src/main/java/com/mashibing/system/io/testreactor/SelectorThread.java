package com.mashibing.system.io.testreactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.ServerError;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 49178
 * @create 2021/12/2
 */
public class SelectorThread extends ThreadLocal<LinkedBlockingQueue<Channel>> implements Runnable {
    //每个线程对应一个Selector，并发情况下，多个连接上的客户端被分配到不同的Selector上，每个客户端只绑定到一个Selector上

    Selector selector = null;
    SelectorThreadGroup group =null;
   // LinkedBlockingQueue<Channel>queue =new LinkedBlockingQueue<>();
    LinkedBlockingQueue<Channel>queue =initialValue();

    @Override
    protected LinkedBlockingQueue<Channel> initialValue() {
        return new LinkedBlockingQueue<Channel>();
    }

    SelectorThread(SelectorThreadGroup g) {
        try {
            selector = Selector.open();
            group =g;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //一直循环
        while (true) {
            try {
                //1.select ()
                /* //select ()不传参数，是阻塞的 如果在对一些文件描述符调起select()时，
                如果其它线程在这个selector里加入了一些文件描述符，但这里是阻塞的，selectKeys()方法还是原来的那些keys
                可能永远阻塞下去，不能往下走
                 */
                //System.out.println(Thread.currentThread().getName() +"：before selec()："+selector.keys().size());
                int nums = selector.select(); //返回有事件的fd个数  将有事件的fd集合放到jVm的直接空间  是内核到jvm直接空间 fd集合进行增量的过程
              //  Thread.sleep(1000);
              //  System.out.println(Thread.currentThread().getName() +"：after selec()："+selector.keys().size());

                //2.selectedKeys();   //返回注册在selector中等待IO操作(及有事件发生)channel的selectionKey。
                if (nums > 0) { //如果selecto里来事件了，进行处理
                    Set<SelectionKey> keys = selector.selectedKeys();  //把jVm的直接空间里fds拷贝到堆空间，在jvm堆空间给你一个事件集
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {  //线程内部拿到keys是线性处理
                        SelectionKey key = iterator.next();
                        //为啥要remove，是下一次循环 selector.selectedKeys()时，还会再次把这些有状态的keys取出来，处理就重复了，这些keys应该是在链表里保存？？？？
                        //2.selector不会自己删除selectedKeys()集合中的selectionKey，那么如果不人工remove()，将导致下次select()的时候selectedKeys()中仍有上次轮询留下来的信息，这样必然会出现错误。
                        iterator.remove();  //将处理完事件的fd从jVm的直接空间移除，不移除那下一次调selector.selectedKeys()，再从jvm直接空间拷贝时，还会重复处理
                        if (key.isAcceptable()) {  //接收客户端
                            acceptHander(key);
                        }
                        if (key.isReadable()) {
                            readHandler(key);
                        }
                        if (key.isWritable()) {

                        }
                    }

                }else {
                    //3. 处理task 从队列里取出事件进行注册
                if (!queue.isEmpty()){ // //队列是个啥东西啊？ 堆里的对象，线程的栈是独立，堆是共享的
                    //只有方法的逻辑，本地变量是线程隔离的
                    Channel ch = queue.take();
                    if (ch instanceof  ServerSocketChannel){
                        ServerSocketChannel server = (ServerSocketChannel) ch;
                        server.register(selector,SelectionKey.OP_ACCEPT);
                        System.out.println(Thread.currentThread().getName()+ "register OP_ACCEPT");
                    }else  if(ch instanceof  SocketChannel){
                        SocketChannel client = (SocketChannel) ch;
                        ByteBuffer buffer =ByteBuffer.allocateDirect(4096);
                       client.register(selector,SelectionKey.OP_READ,buffer);
                        System.out.println(Thread.currentThread().getName()+ "register OP_READ ");
                    }
                }
                }


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName()+" hander read....");
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true) {

            try {
                int num = client.read(buffer);
                if (num > 0) {
                    buffer.flip(); //将读到的内容翻转，然后写出
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }else if(num==0){  //没读到
                        break;
                    }
                else if (num<0){
                    //客户端断开了
                    System.out.println("client :"+client.getRemoteAddress() +"closed");
                    key.cancel();  //从多路复用器里取消
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void acceptHander(SelectionKey key) {
        System.out.println(Thread.currentThread().getName()+ "  accept hander..");
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            ///choose  a selector and Register;
           group.nextSelectV4(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStg(SelectorThreadGroup stg) {
        this.group=stg;
    }
}
