package com.mashibing.fiber;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {

         AtomicInteger idx = new AtomicInteger();

        System.out.println(idx.getAndIncrement() % 2);
        System.out.println(idx.getAndIncrement() % 2);

        System.out.println("main start");
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+ "start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName()+ "over");

        }).start();
        Thread.sleep(500);
        System.out.println("main over");

    }



}
