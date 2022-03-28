package com.mashibing.system.io.rpcdemo.rpc.services;

public class  MyFly implements Fly {
    @Override
    public void xxoo(String msg) {
        System.out.println( "server recevie client arg:"+msg);
    }

    @Override
    public Person getPerson(String name) {
        return new Person(20,"zhangsan");
    }
}