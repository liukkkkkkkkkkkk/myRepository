package com.mashibing.system.io.testreactor2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 49178
 * @create 2021/12/4
 */
public class ThreadGroup2{
    WorkThread2[] workThreads;
    ThreadGroup2 workgroup =this;
    AtomicInteger id =new AtomicInteger(0);
    public ThreadGroup2(int i) {
        workThreads =new WorkThread2[i];
        for (int j = 0; j <i ; j++) {
            workThreads[j]  = new WorkThread2(workgroup);
           // workThreads[j] = workThread2;
            new Thread( workThreads[j]).start();
        }
    }

    public void bind(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            WorkThread2 workerThread = next();
            workerThread.queue.add(serverSocketChannel);
            workerThread.setThreadGroup(workgroup);
            workerThread.selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public WorkThread2 next(){
        int i = id.getAndIncrement() % workThreads.length;
        return workThreads[i];
    }


    public void setWorkGroup(ThreadGroup2 workerGroup) {
        this.workgroup=workerGroup;
    }
}
