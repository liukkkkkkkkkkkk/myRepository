package com.mashibing.system.io.rpcdemo.rpc;

import java.util.concurrent.ConcurrentHashMap;

public class  Dispatcher{
    //单例
    private  Dispatcher(){

    }
    static Dispatcher dis =null;
    static {
        dis =new Dispatcher();
    }
    public  static  Dispatcher getDis(){
        return dis;
    }
    static ConcurrentHashMap<String ,Object> invokeMap=new ConcurrentHashMap<>();
    public  void register(String key ,Object value  ){
        invokeMap.put(key,value);
    }
    public  Object get(String key){
        return invokeMap.get(key);
    }
}