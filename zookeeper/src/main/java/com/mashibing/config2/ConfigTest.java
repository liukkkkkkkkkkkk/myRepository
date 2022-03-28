package com.mashibing.config2;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.StringUtils;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ConfigTest {
    @Test
    public void testConfig(){
        ZooKeeper zk = ZKUtils.getZk();
        WatchCallback watchCallback = new WatchCallback();
        watchCallback.setZk(zk);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        watchCallback.setCc(countDownLatch);
        MyConfig myConfig = new MyConfig();
        watchCallback.setMyConfig(myConfig);
        watchCallback.await();
        try {
            countDownLatch.await();
            while (true){
                String config = myConfig.getConfig();
                if (!StringUtils.isBlank(config)){
                    System.out.println("config:"+config);

                }else{
                    //尝试重新加载配置
                    System.out.println("配置没有了");
                    watchCallback.await();
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
