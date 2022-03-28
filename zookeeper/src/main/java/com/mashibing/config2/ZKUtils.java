package com.mashibing.config2;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {

    private static DefaultWatcher watch =new DefaultWatcher();
    private static CountDownLatch cc =new CountDownLatch(1);

    private static String address ="192.168.33.104:2181,192.168.33.105:2181,192.168.33.106:2181,192.168.33.107:2181/testconf2";
    public  static ZooKeeper getZk(){
        ZooKeeper zk =null;
        try {
             zk =new ZooKeeper(address,1000,watch);
            watch.setCc(cc);
            cc.await();
            System.out.println("往下走了");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    public static void main(String[] args) throws InterruptedException {
        getZk();
        Thread.sleep(100000);
    }
}
