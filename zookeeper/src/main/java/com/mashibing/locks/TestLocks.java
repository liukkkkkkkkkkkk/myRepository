package com.mashibing.locks;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLocks {

    ZooKeeper zooKeeper;

    @Before
    public void conn() {
        zooKeeper = ZKUtils.getZk();
    }

    @After
    public void close() throws InterruptedException {
        zooKeeper.close();
    }



    @Test
    public void lock(){
        for (int i=0;i<10;i++){
            new Thread(){
                //每个线程抢锁、干活、释放锁
                @Override
                public void run() {
                    String threadname = Thread.currentThread().getName();
                    WatchCallback watchCallback = new WatchCallback();
                    watchCallback.setZooKeeper(zooKeeper);
                    watchCallback.setThreadName(threadname);
                    //抢锁
                    watchCallback.tryLock();
                    System.out.println("threadName:"+threadname+" 干活啦");
                /*    try {   //如果把睡眠去掉，业务执行太快就结束，可能其它线程监控watcher上这个节点时，节点在这以前已经被删掉了，就无法监控上节点消失的事件
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    //释放锁
                    watchCallback.unLock();
                    //  zooKeeper.create
                }
            }.start();
        }

        while (true){

        }
    }
}
