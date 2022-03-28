package com.mashibing.apipassenger.test.ThreadLocalTest;

/**
 * @author 49178
 * @create 2022/3/7
 */
public class SequenceNumber {
//ThreadLocal是为解决多线程程序的并发问题而提出的，可以称之为线程局部变量。与一般的变量的区别在于，生命周期是在线程范围内的。
//static变量是的生命周期与类的使用周期相同，即只要类存在，那么static变量也就存在。
    private static ThreadLocal<Integer> seqNoLocal =new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int getNextSeq(){
        int seq = seqNoLocal.get();
        seqNoLocal.set(seq+1);
        return seq;
    }


}
