package threadTest;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockTest {
    static Object o1 =new Object();
    static Object o2 =new Object();
    public static class A implements  Runnable{
        @Override
        public void run() {
            synchronized (o1){
                try {
                    System.out.println(Thread.currentThread().getName() + " get o1");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " get o2");
                    e.printStackTrace();
                }
                synchronized (o2){
                }
            }
        }
    }

    static class B implements  Runnable{
        @Override
        public void run() {
            synchronized (o2){
                try {
                    System.out.println(Thread.currentThread().getName() + " get o2");
                    Thread.sleep(1000);
                    while (true){  //模拟内存溢出
                        Object o = new Object();
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " get o1");
                    e.printStackTrace();
                }
                synchronized (o1){
                }
            }
        }
    }

    public static void main(String[] args) {
       /*  Thread t1 = new Thread(new A(),"thread1");
         Thread t2 = new Thread(new B(),"thread2");
         t1.start();
         t2.start();*/
    }



    final static Lock lock1= new ReentrantLock();
    final static Lock lock2 =new ReentrantLock();






    @Test
    public void testDeadLock(){
        new Thread(()->{
            lock1.lock();
            System.out.println("线程1得到lock1这把锁了");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1想得到lock2这把锁");
            lock2.lock();
      /*      lock1.unlock();
            lock2.unlock();*/
        }).start();

        new Thread(()->{
            lock2.lock();
            System.out.println("线程2得到lock2这把锁了");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2想得到lock1这把锁");
            lock1.lock();
      /*      lock1.unlock();
            lock2.unlock();*/
        }).start();

    }


    @Test
    public void testDeadLock2(){
        new Thread(()->{
            lock1.lock();
            try{
                Thread.sleep(3000);
                lock2.lock();
                System.out.println("Thread1");
                lock2.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.unlock();
        },"Thread1").start();

        new Thread(()->{
            lock2.lock();
            try{
                Thread.sleep(3000);
                lock1.lock();
                System.out.println("Thread2");
                lock1.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.unlock();
        },"Thread2").start();
    }

}
