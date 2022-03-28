package com.mashibing.system.io.rpcdemo.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

public  class ClientPool {
    NioSocketChannel[] clients;
    Object[] locks;

    ClientPool(int size) {
        clients = new NioSocketChannel[size];  //初始出来的数组里面都是null
        locks = new Object[size];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }

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
}
