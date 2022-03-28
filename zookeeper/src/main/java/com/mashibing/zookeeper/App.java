package com.mashibing.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App


{
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException {
       System.out.println( "Hello World!" );
        final CountDownLatch downLatch =new CountDownLatch(1);
        //zk是有session概念的，没有连接池的概念
        //watch:观察，回调
        //watch的注册值发生在 读类型调用，get，exites。。。
        //第一类：new zk 时候，传入的watch，这个watch，session级别的，跟path 、node没有关系。 4000 表示，连接的session断开4秒内，EPHEMERAL节点可以存活，超时节点消失
        final ZooKeeper zooKeeper = new ZooKeeper("192.168.33.104:2181,192.168.33.105:2181,192.168.33.106:2181,192.168.33.107:2181", 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {   //Watch 的回调方法！
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                System.out.println("new Zookeeper watcher event:" + event);
                System.out.println("new Zookeeper watcher status:" + state);
                System.out.println("new Zookeeper watcher type:" + type);
                System.out.println("new Zookeeper watcher path:" + path);

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        System.out.println("disconnected........");
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connetced........");
                        downLatch.countDown();

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
                        System.out.println("closed...");
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
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
        });


        downLatch.await();
        //状态 获得zookeeper对象后，还没有完成连接 connecting...  使用countdownlatch保证ZooKeeper对象创建完成再执行
        ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("connecting........");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("zk已经连接上了........");

                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }
        //同步阻塞的创建节点方式，返回创建节点名称
      /*  String pathName= zooKeeper.create("/aaa", "olddata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Stat stat =new Stat();
        byte[] olddata = zooKeeper.getData("/aaa", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("callback getData watch" + event.toString());
            }
        }, stat);
        System.out.println("olddata:"+new String(olddata));
        zooKeeper.setData("/aaa","newdata".getBytes(),0);  //修改节点信息，会触发上面的Watcher回调函数
        zooKeeper.setData("/aaa","newdata02".getBytes(),1);  //watcher回调只会发生一次，再次修改节点信息，不会被2次触发
        */





        String pathName= zooKeeper.create("/aaa", "olddata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        final Stat stat =new Stat();
        byte[] olddata = zooKeeper.getData("/aaa", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("callback getData watch--" + event.toString());
                try {
                    //true表示， default watcher 被重新注册（new Zookeeeper时的那个Watcher）
                  //  byte[] data = zooKeeper.getData("/aaa", true, stat);

                    //this表示，仍然继续使用刚刚的watcher来进行二次回调，可以被多次使用
                    byte[] data = zooKeeper.getData("/aaa", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println("olddata:"+new String(olddata));
        Stat stat1 = zooKeeper.setData("/aaa", "newdata".getBytes(), 0);//修改节点信息，会触发上面的Watcher回调函数
        Stat stat2 = zooKeeper.setData("/aaa", "newdata02".getBytes(), 1);//修改节点信息，会触发上面的Watcher回调函数
        Stat stat3= zooKeeper.setData("/aaa", "newdata03".getBytes(), 2);//修改节点信息，会触发上面的Watcher回调函数
        Stat stat4= zooKeeper.setData("/aaa", "newdata04".getBytes(), 3);//修改节点信息，会触发上面的Watcher回调函数
        Stat stat5= zooKeeper.setData("/aaa", "newdata04".getBytes(), 4);//修改节点信息，会触发上面的Watcher回调函数



        //异步回调 ,请求结果一旦回来，会调用processResult 回调方法来取数据，不会阻塞下面的代码执行
        System.out.println("-------async start---------");
        zooKeeper.getData("/aaa", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, java.lang.String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("-------async call back---------");
                System.out.println(new String(data));
                System.out.println("ctx:"+ctx);
            }

        },"abcd");
        System.out.println("-------async over---------");





      //让主线程先别结束，以免jvm退出，看不到结果
        Thread.sleep(2222222);


    }
}
