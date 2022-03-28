package com.mashibing.apipassenger.test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author 49178
 * @create 2022/1/27
 */
@RestController
public class Test1 {

    @Value("${zone.name}")
    private String zoneName;


    @Value("${server.port}")
    private String port;

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @GetMapping
    public  String test(){
        return "aaaa";
    }

    @GetMapping("/zoneName")
    public  String getZoneName(){
        return "zoneName:"+zoneName+" port:"+port;
    }

    LoadingCache<String,String> cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "load:"+new Random().nextInt(100);
                }
            });

    public static void main(String[] args) {


    }

    @PostMapping("/test-set/{id}")
    public  String testSet(@PathVariable("id") String id){
        cache.put(id,"set:"+new Random().nextInt(100));
       return "success";
    }


    @GetMapping("/test-get/{id}")
    public  String testGet(@PathVariable("id") String id){
        String str =null;
        try {
             str = cache.get(id).toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return str;
    }
}
