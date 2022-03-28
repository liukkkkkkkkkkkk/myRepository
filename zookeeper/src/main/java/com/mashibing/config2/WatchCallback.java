package com.mashibing.config2;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallback implements Watcher , AsyncCallback.StatCallback, AsyncCallback.DataCallback {


    private ZooKeeper zk;
    private CountDownLatch cc;
    private MyConfig myConfig;

    public MyConfig getMyConfig() {
        return myConfig;
    }

    public void setMyConfig(MyConfig myConfig) {
        this.myConfig = myConfig;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public CountDownLatch getCc() {
        return cc;
    }

    public void setCc(CountDownLatch cc) {
        this.cc = cc;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                System.out.println("节点创建了");
                zk.getData("/aaa",this,this,"safasdf");
                break;
            case NodeDeleted:
                System.out.println("节点删除了");
                myConfig.setConfig("");
                cc=new CountDownLatch(1);
                break;
            case NodeDataChanged:
                cc=new CountDownLatch(1);
                zk.getData("/aaa",this,this,"safasdf");
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



    //exists 的StatCallback 实现
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
       if (stat!=null){
           zk.getData("/aaa",this,this,"safasdf");
       }
    }

    @Override   //getData 的callback
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if(data!=null){
            String str =new String(data);
            myConfig.setConfig(str);
            cc.countDown(); //减1，开始取配置
        }
    }
/*    public void exists(String path, boolean watch, StatCallback cb, Object ctx) {
 */
    public void await() {
        zk.exists("/aaa",this,this,"sadfs");

    }
}
