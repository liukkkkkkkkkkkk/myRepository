package com.mashibing.locks;

import com.mashibing.config.DefaultWatcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {
    private static ZooKeeper zooKeeper;

    private static String address= "192.168.33.104:2181,192.168.33.105:2181,192.168.33.106:2181,192.168.33.107:2181/testLock";

    private static DefaultWatcher deWatcher=new DefaultWatcher();

    static  CountDownLatch latch =new CountDownLatch(1);
    public static ZooKeeper getZk(){
        try {
             zooKeeper =new ZooKeeper(address,1000,deWatcher);
            deWatcher.setLatch(latch);
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }
}
