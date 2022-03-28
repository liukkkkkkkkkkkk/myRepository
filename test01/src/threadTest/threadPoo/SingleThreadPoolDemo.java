package threadTest.threadPoo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPoolDemo {
    public static void main(String[] args) {
        //只有一个线程来执行所有任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i =0;i<20;i++){
            executorService.submit(new Task());
        }
        executorService.shutdown();
    }
}
