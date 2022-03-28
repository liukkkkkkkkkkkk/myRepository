package com.mashibing.system.io.testreactor;

/**
 * @author 49178
 * @create 2021/12/2
 */
public class MainThread {
    //创建IO Thread 一个或多个，
    public static void main(String[] args) {
      /*  SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(3);
       // SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(3);

        //把监听 9999 的server 注册到某一个selector上
        selectorThreadGroup.bind(9999);
        */

      /*boss组持有work工作组的引用*/
        /**
         * boss里选一个线程注册listen ， 触发bind，从而，这个被选中的线程得持有 workerGroup的引用
         * 因为未来 listen 一旦accept得到client后得去worker组中 next出一个线程分配
         */
      SelectorThreadGroup boss =new SelectorThreadGroup(2);
      SelectorThreadGroup work =new SelectorThreadGroup(3);
      boss.setWork(work);

        boss.bind(9999);
        boss.bind(9998);
        boss.bind(9997);
        boss.bind(9996);


    }
}
