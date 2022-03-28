package com.mashibing.system.io.rpcdemo.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;

/**
 * @author 49178
 * @create 2021/12/20
 */
public class ClientPool2 {
    NioSocketChannel clients[];
    Object[]locks ;
    Random random ;
    public NioSocketChannel[] getClients() {
        return clients;
    }

    public void setClients(NioSocketChannel[] clients) {
        this.clients = clients;
    }

    public Object[] getLocks() {
        return locks;
    }

    public void setLocks(Object[] locks) {
        this.locks = locks;
    }

    public ClientPool2(int poolSize){
        this.clients =new NioSocketChannel[poolSize];
        this.locks =new Object[poolSize];
        for (int i = 0; i < locks.length; i++) {
            locks[i]=new Object();
        }
    }

}
