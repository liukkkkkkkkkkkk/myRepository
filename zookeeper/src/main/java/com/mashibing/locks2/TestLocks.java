package com.mashibing.locks2;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class TestLocks {
    @Test
    public void testLocks(){
        final ZooKeeper zk = ZKUtils.getZk();
        for (int i=0;i<10;i++){
            new Thread(){
                @Override
                public void run(){
                    String threadName =this.getName();
                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    WatchCallback watchCallback = new WatchCallback();
                    watchCallback.setThreadName(threadName);
                    watchCallback.setCc(countDownLatch);
                    watchCallback.setZk(zk);
                    watchCallback.tryLock();   //抢锁
                    System.out.println(String.format("threadname[%s]抢到锁了，开始干活...", threadName));
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    watchCallback.unLock();//释放锁

                }
            }.start();
        }

        while (true){

        }
    }
}
