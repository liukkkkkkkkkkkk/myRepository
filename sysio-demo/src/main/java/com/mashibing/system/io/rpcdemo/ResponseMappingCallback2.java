package com.mashibing.system.io.rpcdemo;

import com.mashibing.system.io.rpcdemo.rpc.util.Package;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class ResponseMappingCallback2 {
    static ConcurrentHashMap<Long,CompletableFuture> callBackMap  =new ConcurrentHashMap<Long,CompletableFuture>();
    public static void addCallBack(long reqId,CompletableFuture future){
        callBackMap.putIfAbsent(reqId,future);
    }
    public static  void removCallBackMap(long reqId){
        callBackMap.remove(reqId);
    }

    public static void exeCallback(long reqId,Package pack){
        CompletableFuture future = callBackMap.get(reqId);
        future.complete(pack.getContent().getRes());
        removCallBackMap(reqId);
    }


}

