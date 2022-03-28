package com.mashibing.locks2;

import com.mashibing.config.DefaultWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {
    private static ZooKeeper zooKeeper;
    private static String address= "192.168.33.104:2181,192.168.33.105:2181,192.168.33.106:2181,192.168.33.107:2181/testLock2";
    private static CountDownLatch cc =new CountDownLatch(1) ;
    private static  ZooKeeper zk;


    private static Watcher watcher =new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            switch (event.getState()) {
                case Unknown:
                    break;
                case Disconnected:
                    break;
                case NoSyncConnected:
                    break;
                case SyncConnected:
                    System.out.println("连接上zk了");
                    cc.countDown();
                    break;
                case AuthFailed:
                    break;
                case ConnectedReadOnly:
                    break;
                case SaslAuthenticated:
                    break;
                case Expired:
                    break;
                case Closed:
                    break;
            }
        }
    };
    public static  ZooKeeper getZk(){
        try {
            if (zk==null)
             zk = new ZooKeeper(address,1000,watcher);
            cc.await();
            System.out.println("zk初始化好了");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
