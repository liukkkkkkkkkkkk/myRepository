package com.mashibing.system.io.rpcdemo.rpc.services;

public class  MyCar implements Car {
    @Override
    public String ooxx(String msg) {
        System.out.println( "server recevie client arg:"+msg);
        return "server res arg:"+msg;
    }



}
