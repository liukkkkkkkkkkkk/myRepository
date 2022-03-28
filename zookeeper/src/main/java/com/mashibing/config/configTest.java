package com.mashibing.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class configTest {

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
    public void testConfig() throws InterruptedException {  //path的根目录为创建连接的testconf下
         WatcherCallback watcherCallback = new WatcherCallback();
         watcherCallback.setZooKeeper(zooKeeper);
         MyConfig myConfig = new MyConfig();
         watcherCallback.setMyConfig(myConfig);

         watcherCallback.await();
         //1.节点不存在的时候
         //2.节点存在

         while (true){
             if(!StringUtils.isBlank(myConfig.getConfig())){

                 Thread.sleep(1000);
                 System.out.println("myconf:"+myConfig.getConfig());
             }else {
                 System.out.println("配置信息被删除了");
                 watcherCallback.await();   //尝试重新去拿数据
             }
         }
     /*    zooKeeper.exists("AppConf",watcherCallback,watcherCallback,"sss");
         while (true){
             System.out.println(myConfig.getConfig());
         }*/
       /*  zooKeeper.exists("/AppConf", new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        }, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
             if (stat!=null){//节点存在
                 //zooKeeper.get
             }
            }
        },"ssss");*/
     }
}
