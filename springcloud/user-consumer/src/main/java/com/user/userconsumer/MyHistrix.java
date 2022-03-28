package com.user.userconsumer;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
@Component
public class MyHistrix implements ConsumerApi{


    @Override
    public Map getMap1(String name, int age) {
        return null;
    }

    @Override
    public Map getMap2(Map map) {
        return null;
    }

    @Override
    public Map getMap3(Map map) {
        System.out.println("服务降级了");
        return null;
    }

    @Override
    public Person getPerson(Person person) {
        System.out.println("服务降级了");
        return null;
    }

    @Override
    public String testHystrix() {
        System.out.println("服务降级了");
        return "服务走的降级方法";
    }

    @Override
    public String alive() {
        return null;
    }

    @Override
    public String getById(Integer id) {
        return null;
    }

    @Override
    public Map getMap4(Map map) {
        return null;
    }
}
