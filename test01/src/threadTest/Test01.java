package threadTest;

import javafx.scene.control.Skin;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test01 {
    class impRunnable implements Runnable {
        @Override
        public void run() {

        }
    }


    @Test
    public void test01() {
        new Thread(() -> {
            System.out.println("Thread的匿名类对象");
        }).start();

    }


    class MyCall implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("hell mycall");
            return "success";
        }
    }

    @Test
    public void testCallable() throws ExecutionException, InterruptedException { //实现Callable接口
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            System.out.println("hello threadpool");
        });
        Future<String> future = service.submit(new MyCall()); //使用线程池异步执行
        String s = future.get(); // 阻塞直到拿到值 success
        System.out.println("s:" + s); // success
        service.shutdown();

       /* FutureTask<String> task = new FutureTask<>(new MyCall());
        String s1 = task.get();       //如果不new 一个Thread，则这行会一直处于阻塞状态
        System.out.println("s1:"+s1);*/
        FutureTask<String> task = new FutureTask<>(new MyCall()); // FutureTask 实现了Runnable和Future接口
        Thread thread = new Thread(task);
        thread.start();
        String s1 = task.get();
        System.out.println("s1:" + s1);

    }


    @Test
    public void testThreadStatus() throws InterruptedException {
        long count = 10_0000_00000L;
        final Lock lock = new ReentrantLock();  //ReentrantLock 使用JUC的锁，会进入盲等待，waiting,不会block，如果是synchronized则会是blocked状态
        Thread t4 = new Thread(() -> {
            lock.lock();
            System.out.println("t4得到了这把锁");

        });
        new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }).start();

        Thread.sleep(1000);
        t4.start();
        Thread.sleep(1000);
        System.out.println("t4的状态：" + t4.getState());  //WAITING
    }

    @Test
    public void testLable() {
        int count = 0;
        retry:
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 5; j++) {
                    count++;
                    if (count == 4) {
                        continue retry;
                    }
                    System.out.print(count + " "); //1 2 3 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
                }
            }
        }

    }


    @Test
    //break label直接跳出到标签处，不再执行循环代码；continue label，只是结束本轮循环，跳转到标签处，继续下一轮循环（本质上与单层循环的break和continue类似）。
    public void testBreakRetry() {
        int count = 0;
        retry:
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 5; j++) {
                    count++;
                    if (count == 4) {
                        break retry;
                    }
                    System.out.print(count + " "); /// 1 2 3
                }
            }
        }


    }


    @Test
    public void testLable02() {
        String strSearch = "This is the string in which you have to search for a substring.";
        String substring = "substring";
        boolean found = false;
        int max = strSearch.length() - substring.length();
        testlbl:
        for (int i = 0; i <= max; i++) {
            int length = substring.length();
            int j = i;
            int k = 0;
            while (length-- != 0) {
                if (strSearch.charAt(j++) != substring.charAt(k++)) {
                    continue testlbl;
                }
            }
            found = true;
            break testlbl;
        }
        if (found) {
            System.out.println("发现子字符串。");
        } else {
            System.out.println("字符串中没有发现子字符串。");
        }
    }


    @Test
    public void testTrycatch() {
        try {
            int x = 1;
            int y = 0;
            int a = x / y;
        } finally {
            System.out.println(11111);
        }
    }

    public static volatile int count = 0;

    @Test
    public void testVolatile() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {

                count++;
            }).start();
        }

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {

                count++;
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(count);
    }


    @Test
    public void testFile() throws IOException {
        String path = "C:\\Users\\49178\\OneDrive\\桌面\\马士兵\\jvm" + File.separator;
        File file = new File(path + "aa.txt");
        boolean newFile = file.createNewFile();
        System.out.println("aaa");

    }

    class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return Thread.currentThread().getName() + " is running";
        }
    }

    @Test
    public void test08() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //  ExecutorService executorService =Executors.newCachedThreadPool();
        try {
            for (int i = 0; i < 20; i++) {
                Future<String> submit = executorService.submit(new Task());
                System.out.println(submit.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test09() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(111);
            }
        });
        threadPoolExecutor.shutdown();


    }


}
