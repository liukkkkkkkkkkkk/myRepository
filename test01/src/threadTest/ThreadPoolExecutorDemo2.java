package threadTest;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo2 {

    private static class MyThreadPool extends ThreadPoolExecutor {
        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue ) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        public  void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            int i = 1 / 0;
        }

    }

    @Test
    public void testAfterExecute() {
        MyThreadPool myThreadPool = new MyThreadPool(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});

    }

    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});
        myThreadPool.execute(() -> { System.out.println(1);});
    }

}
