package com.mashibing.locks2;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class WatchCallback implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback , AsyncCallback.StatCallback {
    String threadName;
    ZooKeeper zk;
    String pathName;


    CountDownLatch cc =new CountDownLatch(1);
    public void setThreadName(String threadName) {
        this.threadName =threadName;
    }

    public CountDownLatch getCc() {
        return cc;
    }

    public void setCc(CountDownLatch cc) {
        this.cc = cc;
    }
    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"dsafscx");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }

    public void tryLock() {
        System.out.println(String.format("threadName [%s] creating...", threadName));
        zk.create("/lock2",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,this,"dsafsadf");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void unLock() {
        try {
            zk.delete(pathName,-1);
            System.out.println(String.format("threadname[%s]释放锁了", threadName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override // zk.create 的StringCallback回调
    public void processResult(int rc, String path, Object ctx, String name) {
        if (!StringUtils.isBlank(name)){ //创建节点成功
            System.out.println(String.format("threadName [%s] create node [%s]", threadName,name));
            pathName =name;
            //不需要关注锁目录testLock2的变化，所有不用watch,只需要关注getChildren的回调，找出自己是第一个，或者去监控自己的前面一个节点
            zk.getChildren("/",false,this,"dsafscx");
        }
    }

    @Override  //zk.getChildren  的回调Children2Callback cb
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        Collections.sort(children);   //childn里的节点没有带/
      //  System.out.println("aaaaaaaa");
        children.forEach(e-> System.out.println(String.format("threadName:[%s] 看见了如下节点[%s]", threadName,e)));
        int index = children.indexOf(pathName.substring(1));

        if (index==0){
            cc.countDown();
        }else {    //非最小节点去监控自己前面的一个节点，前面的节点发生删除事件时，进行回调
            zk.exists("/"+children.get(index-1),this,this,"saf");
        }
    }

    @Override  ///zk.exists 的StatCallback cb
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        //
    }
}
