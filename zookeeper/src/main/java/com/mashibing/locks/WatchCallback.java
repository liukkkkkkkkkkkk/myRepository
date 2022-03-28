package com.mashibing.locks;

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

public class WatchCallback implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    String threadName;
    CountDownLatch cc =new CountDownLatch(1);
    ZooKeeper zooKeeper;

    String pathNmae;

    public CountDownLatch getCc() {
        return cc;
    }

    public void setCc(CountDownLatch cc) {
        this.cc = cc;
    }

    public String getPathNmae() {
        return pathNmae;
    }

    public void setPathNmae(String pathNmae) {
        this.pathNmae = pathNmae;
    }

    public CountDownLatch getLatch() {
        return cc;
    }

    public void setLatch(CountDownLatch cc) {
        this.cc = cc;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void unLock(){
        try {
            zooKeeper.delete(pathNmae,-1) ;  //-1忽略版本判定
            System.out.println("threadName:"+threadName+ "over work");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void tryLock(){
        try {
            System.out.println("threadName;"+threadName +" create...");
            zooKeeper.create("/lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,this,"abc");
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void process(WatchedEvent event) {
        //如果某个线程点完活了，释放锁，节点被删除了，只有watch紧跟某后的那个节点的线程收到回调事件。
        //如果不是第一个哥们挂了，某一个挂了，也能造成他后面的收到这个通知，从而使他后面的哥们watch挂掉这个哥们前面的节点
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zooKeeper.getChildren("/",false,this,"dsssss");
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

    @Override      //        zooKeeper.create（）的回调  StringCallback cb,
    public void processResult(int rc, String path, Object ctx, String name) {
         if(!StringUtils.isBlank(name)){ //创建节点成功
             pathNmae=name;
             System.out.println("threadName："+threadName +" create pathname:"+pathNmae);
             //不需要关注锁目录testLock的变化，所有不用watch,只需要关注getChildren的回调，找出自己是第一个，或者去监控自己的前面一个节点
             //"/"表示的是zk连接的根目录testLock
         zooKeeper.getChildren("/",false,this,"dsssss");
         }
    }

    @Override   ///get children Children2Callback
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
     //创建节点完成后，可以拿到lock目录下的在自己之前创建的所有的节点

      //  System.out.println("threadName;"+threadName +"see these nodes");
        Collections.sort(children);
      /*  for (String child : children) {
            System.out.println(child);
        }*/
        int index = children.indexOf(pathNmae.substring(1));   //children没有带'/',需要截取下
        if (index==0){
            System.out.println("threadName;"+threadName+ "是最小的节点");
            cc.countDown();  //它是目录下最小的节点（等于获取锁，减1 ，可以开始干活)
        }else { //非最小节点去监控自己前面的一个节点，前面的节点发生删除事件时，进行回调
            zooKeeper.exists("/"+children.get(index-1),this,this,"ccccc");
        }

    }

    @Override // zooKeeper.exists() 的 StatCallback cb,
    public void processResult(int rc, String path, Object ctx, Stat stat) {
    //先不写
    }
}
