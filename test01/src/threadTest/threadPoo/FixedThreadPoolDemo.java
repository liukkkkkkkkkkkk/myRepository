package threadTest.threadPoo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i =0;i<50;i++){
            executorService.submit(new Task());
        }
        executorService.shutdown();
    }
}
