package com.mashibing.system.io.testreactor2;

/**
 * @author 49178
 * @create 2021/12/4
 */
public class MainThread2 {
    public static void main(String[] args) {
        ThreadGroup2 bossGroup = new ThreadGroup2(2);
        ThreadGroup2 workerGroup =new ThreadGroup2(3);
        bossGroup.setWorkGroup(workerGroup);
        bossGroup.bind(8887);
        bossGroup.bind(8888);
        bossGroup.bind(8889);

    }
}
