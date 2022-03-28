package threadTest;

import org.junit.Test;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {

    @Test
    public void test1() {
        int cps = 1;
        int mps = 2;
        int c = 5;
        int size = mps + c;
        size++; //值大了报错
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(cps, mps, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(c));
        for (int i = 0; i < size; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("wwwwwwwww");
    }

    @Test
    public void test2() {
        int cps = 1;
        int mps = 2;
        int c = 5;
        int size = mps + c;
        size++;
        //线程池满了，CallerRunsPolicy 让调用者线程自己去执行
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(cps, mps, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(c), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < size; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("wwwwwwwww");
    }

    @Test
    public void test3() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<?> submit = executorService.submit(() -> {
            try {
                Thread.sleep(20000); //模拟业务耗时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        boolean b = executorService.awaitTermination(5, TimeUnit.SECONDS);//主线程等待线程结束，等5秒钟 会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
    }

    @Test
    public void test4() {
        ExecutorService executorService = new ThreadPoolExecutor(10, 15, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(8), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                return thread;
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                if (!e.isShutdown()) {
                    r.run();
                    System.out.println("线程池坚持不住了，别往里面放任务了。。。");
                }
            }
        });



        for (int i = 0; i < 15 + 8 + 1; i++) {  //如果提交任务大于最大线程数+阻塞列队长度，则会调用拒绝策略
            executorService.submit(() -> {
                try {
                    System.out.println(111);
                    Thread.sleep(2000); //模拟业务耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void test5() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            System.out.println("helllo word");
        });

    }

    private static class MyThreadPool extends ThreadPoolExecutor {


        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            int i = 1 / 0;
        }

    }

    @Test  //重写 uncaughtException 方法捕获线程池异常，每次执行任务都会走它
    public void testAfterExecute() {
        MyThreadPool myThreadPool = new MyThreadPool(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
                @Override
                public void uncaughtException (Thread t, Throwable e){
                    System.out.println("糟糕，出异常了");
                };
                });
                return t;
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());

        myThreadPool.execute(() -> { System.out.println(1); });
        myThreadPool.execute(() -> { System.out.println(1); });
        myThreadPool.execute(() -> { System.out.println(1); });
        myThreadPool.execute(() -> { System.out.println(1);});
    }

}
