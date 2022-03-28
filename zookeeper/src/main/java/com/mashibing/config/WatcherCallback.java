package com.mashibing.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatcherCallback implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {


    private ZooKeeper zooKeeper;
    private  MyConfig myConfig;

    private CountDownLatch latch =new CountDownLatch(1);
    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public MyConfig getMyConfig() {
        return myConfig;
    }

    public void setMyConfig(MyConfig myConfig) {
        this.myConfig = myConfig;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }


    public void  await(){
        zooKeeper.exists("/AppConf",this,this,"sss");
        try {
            latch.await();   //执行完成，拿到数据，latch减了，再往下执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                zooKeeper.getData("/AppConf",this,this,"sdafas");
                break;
            case NodeDeleted:
                System.out.println("节点删除了");
                myConfig.setConfig(""); //清除配置信息
                latch=new CountDownLatch(1);  //重新计数阻塞
                break;
            case NodeDataChanged:
                //重新更新配置myConfig
                zooKeeper.getData("/AppConf",this,this,"sdafas");

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

    // zooKeeper.getData 的callback回调
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      if (data!=null){
          String str = new String(data);
          myConfig.setConfig(str);
          latch.countDown();  //执行完成，拿到数据，再减
      }

    }


    // zooKeeper.exists 的callback回调
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(stat!=null){
            zooKeeper.getData("/AppConf",this,this,"sdafas");
        }
    }
}
