package CRCTest;

import org.junit.Test;

public class Test02 {
    class impRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    @Test
    public void test01() {
        new Thread(() -> {
            System.out.println("Thread的匿名实现类对象");
        }).start();


    }
}
