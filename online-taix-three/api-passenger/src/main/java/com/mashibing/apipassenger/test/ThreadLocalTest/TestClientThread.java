package com.mashibing.apipassenger.test.ThreadLocalTest;

import lombok.Data;
import sun.security.krb5.internal.SeqNumber;

/**
 * @author 49178
 * @create 2022/3/7
 */
@Data
public class TestClientThread extends Thread {
    private SequenceNumber sn;

    public TestClientThread(SequenceNumber sn) {
        this.sn = sn;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println("ThreadName:" + Thread.currentThread().getName() + " nextSeq:" + sn.getNextSeq());
        }
    }
    public   void printSeq(){
        for (int i = 0; i < 50; i++) {
            System.out.println("ThreadName:" + Thread.currentThread().getName() + " nextSeq:" + sn.getNextSeq());
        }
    }
    public static void main(String[] args) {
        SequenceNumber sequenceNumber = new SequenceNumber();
        TestClientThread t1 = new TestClientThread(sequenceNumber);
        TestClientThread t2 = new TestClientThread(sequenceNumber);
        TestClientThread t3= new TestClientThread(sequenceNumber);
        t1.start();
        t2.start();
        t3.start();
        System.out.println("--------------------");
        t1.printSeq();
        t2.printSeq();
        t3.printSeq();
    }
}
