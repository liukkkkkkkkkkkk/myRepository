package com.mashibing.system.io.rpcdemo;

import com.mashibing.system.io.rpcdemo.rpc.util.Package;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 49178
 * @create 2021/12/14
 */
public class ResponseMappingCallback {
    static ConcurrentHashMap<Long, CompletableFuture<Object>> callBackMap = new ConcurrentHashMap<Long, CompletableFuture<Object>>();


    public static void addCallBack(long reqUid, CompletableFuture res) {
        callBackMap.putIfAbsent(reqUid, res);
    }



    public static void exeCallBack(Long reqId, Package pack) {
        CompletableFuture<Object> res = callBackMap.get(reqId);
        // System.out.println("res:"+res);
        //  System.out.println("pack.getContent().getRes():"+pack.getContent().getRes());
        res.complete(pack.getContent().getRes());
        DelMap(reqId);
    }


    public static void DelMap(Long reqUid) {
        callBackMap.remove(reqUid);
    }
}

