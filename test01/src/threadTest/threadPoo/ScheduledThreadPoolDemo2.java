package threadTest.threadPoo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolDemo2 {
    public static void main(String[] args) {
        //执行定时调度的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("11111延迟1秒执行，每3秒执行一次");
            }
        },1,3, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("22222延迟1秒执行，每3秒执行一次");
            }
        },1,3, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("3333延迟1秒执行，每3秒执行一次");
            }
        },1,3, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("4444延迟1秒执行，每3秒执行一次");
            }
        },1,3, TimeUnit.SECONDS);
        //scheduledExecutorService.shutdown();
    }
}
