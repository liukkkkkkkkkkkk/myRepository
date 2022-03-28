package com.mashibing.userprovider.controller;

import com.mashibing.userprovider.Person;
import com.userapi.UserApi;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController implements  UserApi {

    @Value("${server.port}")
    private String port;

//实现接口会自动继承注解，不用再写requestMapping
    @Override
    public String alive() {
        return "from prot:"+port;
    }

    @Override
    public String getById(Integer id) {
        return id.toString();
    }

    @GetMapping("getMap1")
    public Map getMap1(@RequestParam String name, @RequestParam int age){
        Map<String, Integer> map = Collections.singletonMap(name, age);
        System.out.println(map);
        return map;
    }

    @GetMapping("getMap2")
    public Map getMap2(@RequestParam Map map){
        System.out.println(map);
        return map;
    }

    //post方式远程调用
    @PostMapping("getMap3")
    public Map getMap3(@RequestBody Map map){
        System.out.println(map);
        return map;
    }

    //post方式远程调用,实现的接口里已经有注解了，不用加requestMapping
    @Override
    public Map getMap4( Map map){
        System.out.println(map);
        return map;
    }

    //post方式远程调用
    @PostMapping("getPerson")
    public Person getPerson(@RequestBody Person person){
        System.out.println("person:"+person);
        System.out.println(ToStringBuilder.reflectionToString(person));
        return person;
    }


    @GetMapping("testHystrix")
    public String testHystrix(){
        System.out.println("testHystrix");
        int x =0;
        int i = 10 / x;
        return null;   
    }
}
